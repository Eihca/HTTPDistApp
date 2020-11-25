package ph.edu.cksc.college.parallel.httpapi;

public class FeedMessage {

    String title;
    String description;
    String link;
    String author;
    String pubDate;
    String GUID;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String pubDate) {
        this.author = author;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getGUID() {
        return pubDate;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }
    @Override
    public String toString() {
        return "FeedMessage [title=" + title + ", date=" + pubDate + ", description=" + description + ", link=" + link + ", author=" + author + "]";
    }

}