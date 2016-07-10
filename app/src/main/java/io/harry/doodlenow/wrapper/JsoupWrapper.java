package io.harry.doodlenow.wrapper;

import io.harry.doodlenow.callback.JsoupAsyncTask;
import io.harry.doodlenow.callback.JsoupCallback;

public class JsoupWrapper {
    public void getDocument(final String url, final JsoupCallback callback) {
        new JsoupAsyncTask(url, callback).execute();
    }
}
