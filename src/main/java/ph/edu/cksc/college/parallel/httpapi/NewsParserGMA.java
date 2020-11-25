package ph.edu.cksc.college.parallel.httpapi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsParserGMA  implements NewsParser{

    @Override
    public List<News> extractNews(String content) {
        List<News> list = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy", Locale.US);

        RSSFeedParser parser = new RSSFeedParser(content);
        Feed feed = parser.readFeed();

        //System.out.println(feed);
        for (FeedMessage feedmessage : feed.getMessages()) {
            //System.out.println(feedmessage);
            News news = new News();
            news.title = feedmessage.title;
            news.company = "GMA";
            news.link = feedmessage.link;
            try{
                news.date = format.parse(feedmessage.pubDate);
            }catch(Exception e) {
                e.printStackTrace();
            }
            list.add(news);

        }

/*
        int start = content.indexOf("<item>");
        int end = content.indexOf("</channel>", start);
        content = content.substring(start, end);

        Pattern titlePat = Pattern.compile("<item> <title><!\\[CDATA\\[(.+?)\\]\\]></title> (.+?) <link>(.+?)</link> <description><!\\[CDATA\\[]\\]></description> <pubDate>(.+?)</pubDate>", Pattern.DOTALL);

        Matcher matcher = titlePat.matcher(content);
        while(matcher.find()) {
            News news = new News();
            news.title = matcher.group(1);
            //0 whole match
          */
/*  if (news.title.startsWith("<![CDATA[")){
                int prefixLen = "<![CDATA[".length();
                System.out.println(news.title);
                news.title = news.title.substring(prefixLen, news.title.length()-prefixLen);
            }*//*

            news.company = "GMA";
            news.link = matcher.group(3);
            try{
                news.date = format.parse(matcher.group(4));
            }catch(Exception e) {
                e.printStackTrace();
            }
            list.add(news);

        }
*/


        return list;
    }
}
