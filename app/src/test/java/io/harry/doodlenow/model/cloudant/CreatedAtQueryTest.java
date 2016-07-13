package io.harry.doodlenow.model.cloudant;

import com.google.gson.Gson;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CreatedAtQueryTest {
    @Test
    public void whenFromAndToProvided_shouldCreateQueryObject() throws Exception {
        CreatedAtQuery createdAtQuery = new CreatedAtQuery(0L, 1000L);

        Gson gson = new Gson();
        String json = gson.toJson(createdAtQuery);

        assertThat(json).isEqualTo("{\"selector\":{\"createdAt\":{\"$gte\":0,\"$lt\":1000}},\"sort\":[{\"createdAt\":\"desc\"}]}");
    }
}