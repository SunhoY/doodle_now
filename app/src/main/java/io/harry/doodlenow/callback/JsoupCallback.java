package io.harry.doodlenow.callback;

public interface JsoupCallback {
    void onSuccess(String title, String content, String imageUrl);
    void onFailure();
}
