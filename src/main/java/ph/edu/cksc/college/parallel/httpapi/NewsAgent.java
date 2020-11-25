package ph.edu.cksc.college.parallel.httpapi;

import org.apache.commons.io.IOUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewsAgent implements Runnable {

    private URL httpUrl;
    private String  urlStr;
    private String name;
    public static  double startTime;       // for grand total time
    public static  int    runningCount;    // for getting the last thread to end
    private NewsParser newsParser;
    private List<News> newsList;
    private OnAgentFinishedListener listener;



    /**
     * Constructor
     */
    public NewsAgent(String name, String url, OnAgentFinishedListener listener) {
        //super(Name);
        this.name = name;
        urlStr = url;
        this.listener = listener;
        if(urlStr.startsWith("https://data.gmanetwork.com/")){

            newsParser = new NewsParserGMA();
        }else if(urlStr.startsWith("https://news.abs-cbn.com/")){
            newsParser = new NewsParserABSCBN();
        }else if(urlStr.startsWith("https://cnnphilippines.com/")){
            newsParser = new NewsParserCNN();
        }else if(urlStr.startsWith("https://www.pna.gov.ph/")){
            newsParser = new NewsParserPNA();
        }

        // TODO for others
    }

    /**
     * Connect to HTTP Server and read page returns time duration in seconds
     */
    public double connectAndRead() {

        double startTime = (new Date()).getTime();

        try {
            String content = urlStr;
            httpUrl = new URL(urlStr);
            if(!urlStr.startsWith("https://data.gmanetwork.com/")){
                BufferedReader in = new BufferedReader(new InputStreamReader(httpUrl.openStream()));
                content = IOUtils.toString(in);
                in.close();
            }
            if(newsParser!=null){
                newsList = newsParser.extractNews(content);
            }else{
                newsList = new ArrayList<>();
            }
            listener.onFinished(newsList);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return ((new Date()).getTime() - startTime) / 1000;
    }

    /**
     * Execute the thread
     */
    public void run() {
        String name = this.name;
        System.out.println("Started Thread " + name);

        System.out.println("Elapsed Time for Thread " + name +
                ": " + connectAndRead());

        runningCount--;
        if (runningCount == 0)
            System.out.println("Grand Total Time (for all threads): " +
                    ((new Date()).getTime() - startTime) / 1000);
    }

    public List<News> getNewsList() {
        return newsList;
    }
}
