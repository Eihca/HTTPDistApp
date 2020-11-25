package ph.edu.cksc.college.parallel.httpapi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.io.Receiver;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HTTPAPIDistApp {

    static final int PORT = 8088;
    String serverHost;
    Undertow server;
    Gson gson;

    public HTTPAPIDistApp() {
        gson = new Gson();
    }

    public void run() throws UnsupportedEncodingException {
        runServer();
    }

    public void runServer() {
        server = Undertow.builder()
                .addHttpListener(PORT, "0.0.0.0")
                .setHandler(Handlers.routing()
                        .get("/", this::getRoot)
                        .post("/capitalize", this::postCapitalize)
                        .post("/news", this::postNews)
                        .setFallbackHandler(this::fallback)
                ).build();
        server.start();
    }

    void getRoot(HttpServerExchange exchange) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Hello World");
    }

    void postCapitalize(HttpServerExchange exchange) {
        exchange.getRequestReceiver().receiveFullBytes((exchange1, message) -> {
            String data = new String(message);
            System.out.println(data);
            // get JSON data
            Type type = new TypeToken<String[]>() {
            }.getType();
            String[] list = gson.fromJson(data, type);
            // process
            for (int i = 0; i < list.length; i++) {
                list[i] = list[i].toUpperCase();
            }
            // prepare JSON result
            String json = gson.toJson(list);
            exchange1.getResponseSender().send(json);
        });
    }

    void postNews(HttpServerExchange exchange) {
        exchange.getRequestReceiver().receiveFullBytes((exchange1, message) -> {
            String data = new String(message);
            System.out.println(data);
            // get JSON data
            Type type = new TypeToken<List<String>>() {
            }.getType();
            List<String> list = gson.fromJson(data, type);
            // process
            ExecutorService executor;
            executor = Executors.newFixedThreadPool(5);
            List<News> newsList;
            newsList = new ArrayList<>();
            OnAgentFinishedListener listener = list1 -> {
                synchronized (newsList) {
                    if (list1.size() > 0) {
                        if (!list1.get(0).title.equals("TODO"))
                            newsList.addAll(list1);
                    }
                }
            };
            NewsAgent.startTime = (new Date()).getTime();  // for grand total time!
            NewsAgent.runningCount = 1;
            for (int i = 0; i < list.size(); i++) {
            Runnable worker = new NewsAgent("1", list.get(i), listener);
            executor.execute(worker);
            }
            executor.shutdown();
            while (!executor.isTerminated()) {   }

            System.out.println("Grand Total Time (for starting threads): " +
                    ((new Date()).getTime() - NewsAgent.startTime) / 1000);
            // prepare JSON result
            String json = gson.toJson(newsList);
            exchange1.getResponseSender().send(json);
        });
    }

    void fallback(HttpServerExchange exchange) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Invalid URL");
    }

    public static void main(final String[] args) throws UnsupportedEncodingException {

        if (args.length > 0) {
            String serverHost = null;
            serverHost = args[0];
            HTTPAPIDistAppClient app = new HTTPAPIDistAppClient(serverHost);
            app.run();
        }else {
            HTTPAPIDistApp app = new HTTPAPIDistApp();
            app.run();
        }
    }
}
