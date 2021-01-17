import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    private static String SEED_URL = "https://www.bbc.com/future";

    public static void main(String[] args) throws IOException {

        // Add crawl storage
        File crawlData = new File("crawler");
        String crawlDataPath = crawlData.getAbsolutePath();
        CrawlConfig crawlConfig = new CrawlConfig();
        crawlConfig.setCrawlStorageFolder(crawlDataPath);

        // Add page fetcher
        PageFetcher pageFetcher = new PageFetcher(crawlConfig);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotsTxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        // Add crawler statistics
        Statistics statistics = new Statistics();
        String dataPath = crawlDataPath + File.separator + "bbc-future.xml";
        File file = new File(dataPath);
        if (file.createNewFile()) {
            System.out.println("File created: " + file.getName());
        } else {
            System.out.println("File already exists.");
        }

        FileWriter w = new FileWriter(dataPath);
        CrawlController.WebCrawlerFactory<HtmlCrawler> factory = () -> new HtmlCrawler(SEED_URL, statistics, w);

        try {
            // Add crawl controller
            CrawlController controller = new CrawlController(crawlConfig, pageFetcher, robotsTxtServer);
            controller.addSeed(SEED_URL);
            controller.start(factory, 12);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            w.close();
            System.out.println("Finish writing the file " + "bbc-future.xml" + ".");
        }

    }

}
