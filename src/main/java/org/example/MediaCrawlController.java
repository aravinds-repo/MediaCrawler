package org.example;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.io.File;
import java.util.List;

public class MediaCrawlController {

    private final CrawlConfig config;

    public MediaCrawlController() {
        config = new CrawlConfig();
        config.setCrawlStorageFolder(System.getProperty("user.dir") + "/crawl_history");
        config.setMaxDepthOfCrawling(1);
        config.setIncludeBinaryContentInCrawling(true);
    }

    public void startCrawling(List < String > crawlDomains) throws Exception {
        for (String domain: crawlDomains) {
            String domainName = domain.replaceAll("https?://", "").replaceAll("[^a-zA-Z0-9]", "_");
            String extractPath = System.getProperty("user.dir") + "/extract/" + domainName;

            PageFetcher pageFetcher = new PageFetcher(config);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
            controller.addSeed(domain);

            File storageFolder = new File(extractPath);
            if (!storageFolder.exists() && !storageFolder.mkdirs()) {
                throw new IllegalStateException("Failed to create storage folder: " + storageFolder.getAbsolutePath());
            }

            CrawlController.WebCrawlerFactory < MediaCrawler > factory = () -> new MediaCrawler(storageFolder, domain);
            controller.start(factory, 8);

            Thread.sleep(30 * 1000);
            controller.shutdown();
            controller.waitUntilFinish();
        }
    }
}
