import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.*;
import java.util.regex.*;

public class Crawler {
    Pattern pattern;
    private int limit = 50; // maximum number of sites to crawl

    private Queue<String> queue;
    private Set<String> visited;

    public Crawler() {
        // Instiantiate both the Queue and Set
        queue = new LinkedList<>();
        visited = new HashSet<>();


        // Creates a pattern with basic regualar expression for finding web addresses within a text
        String regex = "\\b(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }
    
    public void discoverWeb(String startSite) {
        int siteCount = 1;

        // Add startSite to the queue
        queue.add(startSite);

        // Start a while loop that will run until the queue is empty or siteCount exceeds limit.
        while (!queue.isEmpty()) {
            String website = queue.poll();

            // Uses the pattern to find matching text within the website.
            Matcher match = pattern.matcher(readURL(website));
            while (match.find()) {
                String link = getRootURL(match.group());

                if (!visited.contains(link)) {
                    visited.add(link);
                    queue.add(link);
                    System.out.printf("Crawling Website # %d found: %s\n", siteCount, link);

                    siteCount++;

                    if (siteCount > limit) {
                        System.out.println("\nNumber of sites left to crawl = " + queue.size());

                        return;
                    }
                }
            }
        }
    }

    public static String getRootURL(String fullURL) {
        try {
            URL url = new URL(fullURL);
            return url.getProtocol() + "://" + url.getHost();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private String readURL(String webSite) {
        StringBuilder rawHtml = new StringBuilder("");
        try {
            URL url = new URL(webSite);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                rawHtml.append(line);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("\tProblem while crawling website: " + webSite);
        }
        return rawHtml.toString();
    }
}
