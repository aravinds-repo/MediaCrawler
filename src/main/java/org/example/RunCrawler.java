package org.example;

import java.util.Arrays;
import java.util.List;

public class RunCrawler {

    public static void main(String[] args) throws Exception {
        MediaCrawlController controller = new MediaCrawlController();

        // Example Image Domain
        controller.startCrawling("https://www.shutterstock.com/");

        // Example Audio Domain
//        controller.startCrawling("https://www.chosic.com/download-audio/27966/");

        // Example Video Domain
//        controller.startCrawling("https://www.freepik.com/videos");
//        controller.startCrawling("https://mixkit.co/free-stock-video/");
    }
}
