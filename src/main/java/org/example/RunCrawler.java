package org.example;

import java.util.Arrays;
import java.util.List;

public class RunCrawler {

    public static void main(String[] args) throws Exception {
        List<String> domains = Arrays.asList(
                "https://www.freepik.com/videos",
                "https://www.shutterstock.com/"
        );
        for (String domain: domains) {
            MediaCrawlController controller = new MediaCrawlController();
            controller.startCrawling(domain);
        }
    }
}
