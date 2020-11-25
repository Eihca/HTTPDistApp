package ph.edu.cksc.college.parallel.httpapi;

import java.util.List;

public interface OnAgentFinishedListener {

    void  onFinished(List<News> newsList);
}
