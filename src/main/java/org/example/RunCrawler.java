package org.example;

import java.util.List;

public class RunCrawler {

    public static void main(String[] args) throws Exception {
        MediaCrawlController controller = new MediaCrawlController();

        List < String > urls = List.of(
                "https://www.freepik.com/videos",
                "https://www.shutterstock.com/",
                "https://www.chosic.com/free-music/all/"
        );

        for (String url: urls) {
            System.out.println("Starting crawl for URL: " + url);
            Thread.sleep(5 * 1000);
            controller.startCrawling(List.of(url));
            System.out.println("Completed crawl for URL: " + url);
        }
    }
}