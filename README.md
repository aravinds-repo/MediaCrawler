# MediaCrawler

MediaCrawler is a Java application designed to crawl web pages, identify and download media files (images, audio, and video), and store them in a structured format. The application also records details of downloaded files in a CSV file.

## Project Structure

The project consists of three main classes:

1. **MediaCrawler**: The web crawler that extends `WebCrawler` from `crawler4j`. It filters URLs, downloads media files, and writes their details to a CSV file.
2. **MediaCrawlController**: Sets up and starts the crawling process using configurations defined for the crawler.
3. **RunCrawler**: Contains the `main` method to initiate the crawling process.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Libraries Used](#libraries-used)
   - [crawler4j](#crawler4j)
   - [jsoup](#jsoup)
   - [Apache Commons CSV](#apache-commons-csv)
   - [Google Guava](#google-guava)
3. [Setup and Installation](#setup-and-installation)
4. [Configuration](#configuration)
5. [Running the Crawler](#running-the-crawler)
6. [Execute the Crawler](#execute-the-crawler)
7. [Output](#output)
   - [Example ExportDetails.csv](#example-exportdetailscsv)
8. [Extending the Project](#extending-the-project)
9. [Class created](#class-created)


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
git clone https://github.com/aravinds-repo/MediaCrawler.git
```

**Install dependencies**:
```sh
mvn clean package install
```


## Configuration
The crawling configuration is defined in the MediaCrawlController class. By default, it is set to:

- Store crawl history in **./crawl_history**
- Crawl with a depth of 8.
- Include binary content in crawling. 

**Running the Crawler**
- To run the crawler, use the RunCrawler class. You can specify the list of domains to crawl by modifying the main method in RunCrawler.


## Execute the Crawler
Run the main method in the RunCrawler class using your IDE or from the command line:

```sh
mvn exec:java
```

## Output
The media files will be stored in the extract directory within the project root, organized by domain. For example:
```txt
/extract/www_example_com/Image
/extract/www_example_com/Audio
/extract/www_example_com/Video
```

A CSV file ExportDetails.csv will be created in the respective domain's directory, recording the details of each downloaded file.

**Example ExportDetails.csv:**

| File Type | File Extension | File Name         | File Path                     | URL                                    | Time of Extract      |
|-----------|----------------|-------------------|-------------------------------|----------------------------------------|----------------------|
| Image     | jpg            | example_image.jpg | /extract/www_example_com/Image | https://example.com/example_image.jpg | 2024-05-25 00:00:00 |
| Audio     | mp3            | example_video.mp3 | /extract/www_example_com/Audio | https://example.com/example_audio.mp3 | 2024-05-25 00:00:00 |
| Video     | mp4            | example_video.mp4 | /extract/www_example_com/Video | https://example.com/example_video.mp4 | 2024-05-25 00:00:00 |



## Extending the Project
To extend the project with additional features or different file types:

1. Modify the patterns in MediaCrawler to include new file extensions.
2. Update the visit method in MediaCrawler to handle new types of media or data processing requirements.

----
## Class created
### Table of Contents

1. [MediaCrawler Class](#mediacrawler-class)
   - [Overview](#overview)
   - [Dependencies](#dependencies)
   - [Class Structure](#class-structure)
      - [Member Variables](#member-variables)
      - [Constructor](#constructor)
      - [Methods](#methods)
   - [Usage](#usage)

2. [RunCrawler Class](#runcrawler-class)
   - [Overview](#overview-1)
   - [Dependencies](#dependencies-1)
   - [Class Structure](#class-structure-1)
      - [Main Method](#main-method)
   - [Usage](#usage-1)

3. [MediaCrawlController Class](#mediacrawlcontroller-class)
   - [Overview](#overview-2)
   - [Dependencies](#dependencies-2)
   - [Class Structure](#class-structure-2)
      - [Member Variables](#member-variables-1)
      - [Constructor](#constructor-1)
      - [Methods](#methods-1)
   - [Usage](#usage-2)


## MediaCrawler Class

### Overview

The `MediaCrawler` class is responsible for crawling web pages and extracting media files such as images, audio, and video files. It extends the `WebCrawler` class from the `crawler4j` library and implements methods to visit web pages, identify media elements, download media files, and store their details in a CSV file.

### Dependencies

- `crawler4j`: A Java library for web crawling and scraping.
- `jsoup`: A Java library for working with HTML documents.

### Class Structure

- **Member Variables:**
   - `filters`, `imgPatterns`, `audioPatterns`, `videoPatterns`: Regular expressions to filter URLs based on file extensions.
   - `imageFolderName`, `audioFolderName`, `videoFolderName`: Names of folders to store respective media files.
   - `imageFolder`, `audioFolder`, `videoFolder`: `File` objects representing storage folders for media files.
   - `crawlDomains`: List of crawl domains to restrict crawling.
   - `csvFile`: `File` object representing the CSV file to store media file details.
   - `MAX_FILES`: Maximum number of files to be downloaded for each media type.
   - `imageUrls`, `audioUrls`, `videoUrls`, `mediaUrls`: Sets to keep track of downloaded media URLs.
   - `existingUrls`: Set to store existing media URLs extracted in previous runs.

- **Constructor:**
   - Initializes the class by setting up storage folders, CSV file, and loading existing media URLs from CSV.

- **Methods:**
   - `shouldVisit`: Overrides method to determine whether a page should be visited based on its URL and file extensions.
   - `visit`: Overrides method to visit a web page, extract media elements, and download media files.
   - `resolveMediaUrl`: Resolves the absolute URL of a media element.
   - `downloadAndSaveFile`: Downloads and saves a media file to the appropriate folder.
   - `createConnection`: Creates an HTTP connection to download media files.
   - `getFileName`: Extracts the file name from a URL.
   - `writeCsvRecord`: Writes media file details to the CSV file.

### Usage

The `MediaCrawler` class is typically used in conjunction with the `MediaCrawlController` class to initiate the crawling process.

---

# MediaCrawlController Class

### Overview

The `MediaCrawlController` class is responsible for controlling the web crawling process using the `crawler4j` library. It sets up the crawling configuration, initializes the CrawlController, and starts crawling for the specified domain(s) using the `MediaCrawler` class.

### Dependencies

- `crawler4j`: A Java library for web crawling and scraping.

## Class Structure

- **Member Variables:**
   - `config`: Instance of `CrawlConfig` to configure crawling settings.

- **Constructor:**
   - Initializes the `CrawlConfig` with default settings such as crawl storage folder, maximum depth of crawling, and inclusion of binary content.

- **Methods:**
   - `startCrawling`: Initiates the crawling process for the specified list of crawl domains.
   - `createController` : Handles the creation and configuration of the CrawlController.

### Usage

Instantiate the `MediaCrawlController` class and call the `startCrawling` method with the list of crawl domains to begin the crawling process.


---

## RunCrawler Class

### Overview

The `RunCrawler` class contains the `main` method to start the web crawling process. It creates an instance of the `MediaCrawlController` class and calls the `startCrawling` method with the list of seed URLs.

### Dependencies

None

### Class Structure

- **Main Method:**
   - Instantiates the `MediaCrawlController` class.
   - Calls the `startCrawling` method with a list of seed URLs to initiate the crawling process.

## Usage

Run the `main` method of the `RunCrawler` class to start crawling for the specified seed URLs.

----



This updated `README.md` file includes all the specified sections and provides comprehensive instructions on setting up, configuring, running, and extending the project.
