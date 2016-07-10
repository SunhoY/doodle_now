package io.harry.doodlenow.callback;

import android.os.AsyncTask;

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
        String description = document.select("meta[name=description]").attr("content");
        jsoupCallback.onSuccess(title, description);
    }

    @Override
    protected Document doInBackground(Void... params) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            return Document.createShell(url);
        }
    }
}