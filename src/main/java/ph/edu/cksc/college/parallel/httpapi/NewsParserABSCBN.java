package ph.edu.cksc.college.parallel.httpapi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsParserABSCBN implements NewsParser{

    @Override
    public List<News> extractNews(String content) {
        List<News> list = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("MM/d/yy", Locale.US);
        int start = content.indexOf("content tabs-inner current");
        int end = content.indexOf("<div class=\"content tabs-inner\"", start);
        content = content.substring(start, end);

        //System.out.println(content);

        Pattern titlePat = Pattern.compile("<a href=\"/(.+?)/(\\d+/\\d+/\\d{2})/(.+?)\">(.+?)</a>", Pattern.DOTALL);

        Matcher matcher = titlePat.matcher(content);
        while(matcher.find()) {
            News news = new News();
            news.title = matcher.group(4);
            //0 whole match
            news.company = "ABS-CBN";
            news.link = "https://news.abs-cbn.com/" + matcher.group(1) + matcher.group(2) + "/" + matcher.group(3);
            try{
                news.date = format.parse(matcher.group(2));
            }catch(Exception e){
                e.printStackTrace();
            }
            list.add(news);


            //System.out.println(news);
        }
        return list;
    }
}
