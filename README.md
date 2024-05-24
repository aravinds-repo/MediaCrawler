# MediaCrawler

MediaCrawler is a Java application designed to crawl web pages, identify and download media files (images, audio, and video), and store them in a structured format. The application also records details of downloaded files in a CSV file.

## Project Structure

The project consists of three main classes:

1. **MediaCrawler**: The web crawler that extends `WebCrawler` from `crawler4j`. It filters URLs, downloads media files, and writes their details to a CSV file.
2. **MediaCrawlController**: Sets up and starts the crawling process using configurations defined for the crawler.
3. **RunCrawler**: Contains the `main` method to initiate the crawling process.

## Prerequisites

- Java 8 or higher
- Maven

## Libraries Used

The project uses several libraries to facilitate web crawling, HTML parsing, and file operations:

1. **crawler4j**: A powerful and flexible web crawler for Java.
    - Maven Dependency:
      ```xml
      <dependency>
        <groupId>edu.uci.ics</groupId>
        <artifactId>crawler4j</artifactId>
        <version>4.4.0</version>
      </dependency>
      ```

2. **jsoup**: A Java library for working with real-world HTML.
    - Maven Dependency:
      ```xml
      <dependency>
        <groupId>org.jsoup</groupId>
        <artifactId>jsoup</artifactId>
        <version>1.14.3</version>
      </dependency>
      ```

3. **Apache Commons CSV**: A library for reading and writing CSV files.
    - Maven Dependency:
      ```xml
      <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-csv</artifactId>
        <version>1.8</version>
      </dependency>
      ```

4. **Google Guava**: A set of core libraries that includes new collection types and utilities.
    - Maven Dependency:
      ```xml
      <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
        <version>30.1.1-jre</version>
      </dependency>
      ```

## Setup and Installation
**Clone the repository**:
```sh
git clone https://github.com/aravinds-repo/web_crawling.git
cd WebCrawling
```

**Install dependencies**:
```sh
mvn clean install
```

**Install dependencies**:
```sh
mvn clean install
```

## Configuration
The crawling configuration is defined in the MediaCrawlController class. By default, it is set to:

- Store crawl history in **./crawl_history**
- Crawl with a depth of 8.
- Include binary content in crawling. 

**Running the Crawler**
- To run the crawler, use the RunCrawler class. You can specify the list of domains to crawl by modifying the main method in RunCrawler.

**Example domains in RunCrawler:**
```txt
List.of(
    "https://www.shutterstock.com/image-photo/two-women-having-conversation-while-cohosting-2269365599",
            "https://www.shutterstock.com/image-photo/erp-document-management-concept-working-laptop-1970907203"
);
```

## Execute the Crawler
Run the main method in the RunCrawler class using your IDE or from the command line:

```sh
mvn exec:java -Dexec.mainClass="org.example.RunCrawler"
```

## Output
The media files will be stored in the extract directory within the project root, organized by domain. For example:
```bash
/extract/www_shutterstock_com_image_photo_two_women_having_conversation_while_cohosting_2269365599/images
/extract/www_shutterstock_com_image_photo_two_women_having_conversation_while_cohosting_2269365599/audios
/extract/www_shutterstock_com_image_photo_two_women_having_conversation_while_cohosting_2269365599/videos
```

A CSV file ExportDetails.csv will be created in the respective domain's directory, recording the details of each downloaded file.

**Example ExportDetails.csv:**
```arduino
File Name,File Type,URL
example_image.jpg,Image,https://example.com/example_image.jpg
example_audio.mp3,Audio,https://example.com/example_audio.mp3
example_video.mp4,Video,https://example.com/example_video.mp4
```

## Extending the Project
To extend the project with additional features or different file types:

1. Modify the patterns in MediaCrawler to include new file extensions.
2. Update the visit method in MediaCrawler to handle new types of media or data processing requirements.


This updated `README.md` file includes all the specified sections and provides comprehensive instructions on setting up, configuring, running, and extending the project.
