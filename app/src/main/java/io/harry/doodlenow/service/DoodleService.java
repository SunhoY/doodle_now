package io.harry.doodlenow.service;

import java.util.List;

import io.harry.doodlenow.model.Doodle;

public interface DoodleService {
    void createDoodle(Doodle doodle, ServiceCallback<String> serviceCallback);
    void retrieveDoodle(String id, ServiceCallback<Doodle> serviceCallback);
    void retrieveDoodles(ServiceCallback<List<Doodle>> serviceCallback);
}
