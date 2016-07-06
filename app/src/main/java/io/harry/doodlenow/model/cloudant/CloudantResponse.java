package io.harry.doodlenow.model.cloudant;

import java.util.List;

public class CloudantResponse<T> {
    public int total_rows;
    public List<CloudantDocument<T>> rows;
}