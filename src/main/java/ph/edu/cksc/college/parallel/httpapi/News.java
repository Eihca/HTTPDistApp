package ph.edu.cksc.college.parallel.httpapi;

import java.util.Date;
import java.util.Set;

public class News {

    //package access
    String company;
    Date date;
    String title;
    String summary;
    String link;
    String content;
    Set<String> keywords;

    public String toString() {
        return "News(" + company + ", " + date + ", " + title + ", " + link + " , "+ keywords + ")";
    }

}
