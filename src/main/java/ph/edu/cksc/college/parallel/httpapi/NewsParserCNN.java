package ph.edu.cksc.college.parallel.httpapi;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsParserCNN implements NewsParser{

    @Override
    public List<News> extractNews(String content) {
        List<News> list = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/d", Locale.US);
        int start = content.indexOf("Latest Articles");
        int end = content.indexOf("</section>", start);
        content = content.substring(start, end);


        Pattern titlePat = Pattern.compile("<a href=\"(/news/(\\d{4}/\\d+/\\d+)/.+?)\">(.+?)</a>", Pattern.DOTALL);
        //DOTALL - including newline
        //don't be greedy ?
        //html tag .
        //() - group result

        //skip non news item, the first
        Matcher matcher = titlePat.matcher(content);
        while(matcher.find()) {
            News news = new News();
            news.title = matcher.group(1);
            //0 whole match
            news.company = "CNNph";
            news.link = "https://cnnphilippines.com"+matcher.group(1);
            try{
                news.date = format.parse(matcher.group(2));
            }catch(Exception e){
                e.printStackTrace();
            }
            list.add(news);
        }
        return list;
    }
}
