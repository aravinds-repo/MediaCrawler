package org.example;

import java.util.List;

public class RunCrawler {

    public static void main(String[] args) throws Exception {
        MediaCrawlController controller = new MediaCrawlController();

        List < String > urls = List.of(
                "https://www.freepik.com/videos",
                "https://www.shutterstock.com/",
                "https://www.chosic.com/download-audio/25858/"
        );

        for (String url: urls) {
            System.out.println("Starting crawl for URL: " + url);
            controller.startCrawling(List.of(url));
            System.out.println("Completed crawl for URL: " + url);
        }
    }
}