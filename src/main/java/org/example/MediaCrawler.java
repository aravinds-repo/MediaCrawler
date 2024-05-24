package org.example;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;

import static com.google.common.io.Files.getFileExtension;

public class MediaCrawler extends WebCrawler {

    private static final Pattern filters = Pattern.compile(".*(\\.(css|js|pdf|zip|rar|gz))$");
    private static final Pattern imgPatterns = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");
    private static final Pattern audioPatterns = Pattern.compile(".*(\\.(mid|mp2|mp3|wav|wma))$");
    private static final Pattern videoPatterns = Pattern.compile(".*(\\.(mp4|avi|mov|mpeg|ram|m4v|rm|smil|wmv|swf|webm))$");
    
    private static final String imageFolderName = "Image";
    private static final String audioFolderName = "Audio";
    private static final String videoFolderName = "Video";
    
    private final File imageFolder;
    private final File audioFolder;
    private final File videoFolder;
    private final List < String > crawlDomains;
    private final File csvFile;

    private static final int MAX_FILES = 10;
    private static final Set < String > imageUrls = new HashSet < > ();
    private static final Set < String > audioUrls = new HashSet < > ();
    private static final Set < String > videoUrls = new HashSet < > ();
    private static final Set < String > mediaUrls = new HashSet < > ();

    private final Set < String > existingRecords = new HashSet < > ();

    public MediaCrawler(File storageFolder, List < String > crawlDomains) {
        this.crawlDomains = ImmutableList.copyOf(crawlDomains);

        this.imageFolder = new File(storageFolder, imageFolderName);
        this.audioFolder = new File(storageFolder, audioFolderName);
        this.videoFolder = new File(storageFolder, videoFolderName);

        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }
        if (!audioFolder.exists()) {
            audioFolder.mkdirs();
        }
        if (!videoFolder.exists()) {
            videoFolder.mkdirs();
        }

        this.csvFile = new File(storageFolder, "ExportDetails.csv");
        if (!csvFile.exists()) {
            try (FileWriter writer = new FileWriter(csvFile); CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("File Type", "File Extension", "File Name", "URL", "Time of Extraction"))) {
                csvPrinter.flush();
            } catch (IOException e) {
                WebCrawler.logger.error("Failed to create CSV file: {}", csvFile, e);
            }
        } else {
            loadExistingRecords();
        }
    }

    private void loadExistingRecords() {
        try (FileReader reader = new FileReader(csvFile)) {
            Iterable < CSVRecord > records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record: records) {
                String fileName = record.get("File Name");
                String fileType = record.get("File Type");
                String url = record.get("URL");
                String recordKey = fileName + fileType + url;
                existingRecords.add(recordKey);
            }
        } catch (IOException e) {
            WebCrawler.logger.error("Failed to read existing CSV file: {}", csvFile, e);
        }
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        if (imageUrls.size() > MAX_FILES && audioUrls.size() > MAX_FILES && videoUrls.size() > MAX_FILES) {
            return false;
        }

        String href = url.getURL().toLowerCase();
        if (filters.matcher(href).matches()) {
            return false;
        }

        for (String domain: crawlDomains) {
            if (href.startsWith(domain)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void visit(Page page) {
        if (imageUrls.size() > MAX_FILES && audioUrls.size() > MAX_FILES && videoUrls.size() > MAX_FILES) {
            return;
        }

        String url = page.getWebURL().getURL();
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            Document doc = Jsoup.parse(htmlParseData.getHtml());

            Elements mediaElements = doc.select("img[src], img[data-src], video source[src], video[src], audio source[src], audio[src], a[href]");

            for (Element mediaElement: mediaElements) {
                String mediaUrl = resolveMediaUrl(mediaElement);
                if (mediaUrl != null) {
                    if (imgPatterns.matcher(mediaUrl).matches()) {
                        downloadAndSaveFile(mediaUrl, imageFolder, imageFolderName);
                    } else if (audioPatterns.matcher(mediaUrl).matches()) {
                        downloadAndSaveFile(mediaUrl, audioFolder, audioFolderName);
                    } else if (videoPatterns.matcher(mediaUrl).matches()) {
                        downloadAndSaveFile(mediaUrl, videoFolder, videoFolderName);
                    }
                }
            }
        } else if (page.getParseData() instanceof BinaryParseData) {
            if (imgPatterns.matcher(url).matches()) {
                downloadAndSaveFile(url, imageFolder, imageFolderName);
            } else if (audioPatterns.matcher(url).matches()) {
                downloadAndSaveFile(url, audioFolder, audioFolderName);
            } else if (videoPatterns.matcher(url).matches()) {
                downloadAndSaveFile(url, videoFolder, videoFolderName);
            }
        }
    }

    private String resolveMediaUrl(Element element) {
        String mediaUrl = element.hasAttr("abs:src") ? element.attr("abs:src") : element.hasAttr("abs:data-src") ? element.attr("abs:data-src") : element.attr("abs:href");
        return mediaUrl.isEmpty() ? null : mediaUrl;
    }

    private void downloadAndSaveFile(String url, File targetFolder, String fileType) {
        if (imageUrls.size() > MAX_FILES && audioUrls.size() > MAX_FILES && videoUrls.size() > MAX_FILES) {
            return;
        }
        if (mediaUrls.contains(url)) {
            WebCrawler.logger.warn("Already downloaded URL - Skipping URL from download: {}", url);
        } else if (imageFolderName.equals(fileType) && imageUrls.size() > MAX_FILES - 1) {
            WebCrawler.logger.warn("Skipping Image URL from download: {}", url);
        } else if (audioFolderName.equals(fileType) && audioUrls.size() > MAX_FILES - 1) {
            WebCrawler.logger.warn("Skipping Audio URL from download: {}", url);
        } else if (videoFolderName.equals(fileType) && videoUrls.size() > MAX_FILES - 1) {
            WebCrawler.logger.warn("Skipping Video URL from download: {}", url);
        } else {
            String fileName = getFileName(url);
            String filePath = targetFolder.getAbsolutePath() + '/' + fileName;

            try {
                HttpURLConnection connection = createConnection(url);
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    byte[] contentData = connection.getInputStream().readAllBytes();
                    Files.write(contentData, new File(filePath));
                    WebCrawler.logger.info("Stored: {} in {}", url, filePath);
                    writeCsvRecord(fileName, fileType, url);
                } else {
                    WebCrawler.logger.warn("Failed to download file: {} with response code: {}", url, responseCode);
                }
            } catch (IOException e) {
                WebCrawler.logger.error("Error downloading file: {}", url, e);
            }

            switch (fileType) {
                case imageFolderName:
                    imageUrls.add(url);
                    mediaUrls.add(url);
                    break;

                case audioFolderName:
                    audioUrls.add(url);
                    mediaUrls.add(url);
                    break;

                case videoFolderName:
                    videoUrls.add(url);
                    mediaUrls.add(url);
                    break;

                default:
                    break;
            }
        }
    }

    private HttpURLConnection createConnection(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Referer", url);
        return connection;
    }

    private String getFileName(String url) {
        try {
            URL urlObj = new URL(url);
            String path = urlObj.getPath();
            return path.substring(path.lastIndexOf('/') + 1);
        } catch (MalformedURLException e) {
            WebCrawler.logger.error("Malformed URL: {}", url, e);
            return UUID.randomUUID().toString();
        }
    }

    private void writeCsvRecord(String fileName, String fileType, String url) {
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        String recordKey = fileName + fileType + url;
        String extension = getFileExtension(url);
        if (existingRecords.contains(recordKey)) {
            WebCrawler.logger.info("Skipping duplicate record: {}", recordKey);
            return;
        }

        try (FileWriter writer = new FileWriter(csvFile, true); CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord(fileType, extension, fileName, url, formattedTime);
            csvPrinter.flush();
            existingRecords.add(recordKey);
        } catch (IOException e) {
            WebCrawler.logger.error("Failed to write to CSV file: {}", csvFile, e);
        }
    }
}
