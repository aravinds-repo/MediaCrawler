package org.example;

import java.util.List;

public class RunCrawler {

    public static void main(String[] args) throws Exception {
        MediaCrawlController controller = new MediaCrawlController();
        controller.startCrawling(List.of("https://www.freepik.com/videos", "https://www.shutterstock.com/"));
    }
}
