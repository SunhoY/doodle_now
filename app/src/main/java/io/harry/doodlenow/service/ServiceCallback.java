package io.harry.doodlenow.service;

/**
 * Created by harry on 16. 6. 29.
 */
public interface ServiceCallback<T> {
    void onSuccess(T item);
    void onFailure(String message);
}
