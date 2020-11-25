package ph.edu.cksc.college.parallel.httpapi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsParserPNA implements NewsParser{

    @Override
    public List<News> extractNews(String content) {
        List<News> list = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        int start = content.indexOf("Latest News");
        int end = content.indexOf("</ul>", start);
        content = content.substring(start, end);

        //System.out.println(content);
        Pattern titlePat = Pattern.compile("<a class=\"title\" href=\"(.+?)\">(.+?)</a><br>\\s+<span class=\"date\">Date Posted: (.+?)</span>" , Pattern.DOTALL);

        Matcher matcher = titlePat.matcher(content);
        while(matcher.find()) {
            News news = new News();
            news.title = matcher.group(2);
            //0 whole match
            news.company = "PNA";
            news.link = matcher.group(1);
            try{
                news.date = format.parse(matcher.group(3));
            }catch(Exception e){
                e.printStackTrace();
            }
            list.add(news);
            //System.out.println(news);
        }
        return list;
    }
}
