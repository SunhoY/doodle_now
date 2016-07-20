package io.harry.doodlenow.serviceinterface;

import java.util.List;

import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.ServiceCallback;

public interface IDoodleService {
    void saveDoodle(Doodle doodle, final ServiceCallback<Void> serviceCallback);
    void getDoodles(long from, long to, final ServiceCallback<List<Doodle>> serviceCallback);
}
