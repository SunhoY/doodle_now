package io.harry.doodlenow.model;

import java.util.List;

public class CloudantResponse<T> {
    public int total_rows;
    public int offset;
    public List<T> rows;
}