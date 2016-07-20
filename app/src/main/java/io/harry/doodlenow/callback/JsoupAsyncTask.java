package io.harry.doodlenow.callback;

import android.os.AsyncTask;
import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class JsoupAsyncTask extends AsyncTask<Void, Void, Document> {
    public static final String CHROME_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";

    final String url;
    final JsoupCallback jsoupCallback;

    public JsoupAsyncTask(String url, JsoupCallback jsoupCallback) {
        this.url = url.substring(url.indexOf("http"));
        this.jsoupCallback = jsoupCallback;
    }

    @Override
    protected void onPostExecute(Document document) {
        String title = document.select("meta[property=og:title]").attr("content");
        String description = document.select("meta[property=og:description]").attr("content");
        String imageUrl = document.select("meta[property=og:image]").attr("content");
        if(TextUtils.isEmpty(title)) {
            title = document.title();
        }
        if(TextUtils.isEmpty(description)) {
            description = document.select("meta[name=description]").attr("content");
        }

        jsoupCallback.onSuccess(title, description, imageUrl);
    }

    @Override
    protected Document doInBackground(Void... params) {
        try {
            return Jsoup.connect(url).userAgent(CHROME_USER_AGENT).get();
        } catch (IOException e) {
            e.printStackTrace();
            return Document.createShell(url);
        }
    }
}