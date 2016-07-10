package io.harry.doodlenow.callback;

import android.os.AsyncTask;
import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class JsoupAsyncTask extends AsyncTask<Void, Void, Document> {
    final String url;
    final JsoupCallback jsoupCallback;

    public JsoupAsyncTask(String url, JsoupCallback jsoupCallback) {
        this.url = url;
        this.jsoupCallback = jsoupCallback;
    }

    @Override
    protected void onPostExecute(Document document) {
        String title = document.title();
        if(TextUtils.isEmpty(title)) {
            title = document.select("meta[property=og:title]").attr("content");
        }
        String description = document.select("meta[name=description]").attr("content");
        if(TextUtils.isEmpty(description)) {
            description = document.select("meta[property=og:description]").attr("content");
        }
        jsoupCallback.onSuccess(title, description);
    }

    @Override
    protected Document doInBackground(Void... params) {
        try {
            return Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36").get();
        } catch (IOException e) {
            e.printStackTrace();
            return Document.createShell(url);
        }
    }
}