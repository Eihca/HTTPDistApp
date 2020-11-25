package ph.edu.cksc.college.parallel.httpapi;

import java.util.List;

public interface NewsParser {

    List<News> extractNews(String content);

}
