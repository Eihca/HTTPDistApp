package ph.edu.cksc.college.parallel.httpapi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HTTPAPIDistAppClient {
    static final int PORT = 8088;
    String serverHost;
    Gson gson;

    public HTTPAPIDistAppClient(String serverHost) {
        this.serverHost = serverHost;
        gson = new Gson();
    }

    public void run() throws UnsupportedEncodingException {
        // client side
        HttpPost httpPost = new HttpPost("http://" + serverHost + ":" + PORT + "/capitalize");
        // prepare JSON data
        String[] data = new String[]{"one", "two"};
        String json = gson.toJson(data);
        StringEntity stringEntity = new StringEntity(json);
        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpPost)) {
            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                System.out.println(result);
                // convert JSON result back to Java array
                Type type = new TypeToken<String[]>() {
                }.getType();
                String[] list = gson.fromJson(result, type);
                for (String item : list) {
                    System.out.println(item);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        readNews();
    }

    private void readNews() throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost("http://" + serverHost + ":" + PORT + "/news");
        // prepare JSON data
        List<String> urls = new ArrayList<>();

        FileReader reader = null;
        try {
            reader = new FileReader("urls.txt");
            urls = IOUtils.readLines(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String json = gson.toJson(urls);
        StringEntity stringEntity = new StringEntity(json);
        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpPost)) {
            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                System.out.println(result);
                // convert JSON result back to Java array
                Type type = new TypeToken<List<News>>() {
                }.getType();
                List<News> list = gson.fromJson(result, type);
                for (News item : list) {
                    System.out.println(item);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
