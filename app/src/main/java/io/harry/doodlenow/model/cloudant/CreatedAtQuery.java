package io.harry.doodlenow.model.cloudant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatedAtQuery {
    Map<String, Object> selector;
    List<Map<String, String>> sort;

    public CreatedAtQuery(long from, long to) {
        selector = new HashMap<>();
        HashMap<String, Long> period = new HashMap<>();
        period.put("$gte", from);
        period.put("$lt", to);

        selector.put("createdAt", period);

        sort = new ArrayList<>();
        HashMap<String, String> sortCriteria = new HashMap<>();
        sortCriteria.put("createdAt", "desc");
        sort.add(sortCriteria);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreatedAtQuery that = (CreatedAtQuery) o;

        if (selector != null ? !selector.equals(that.selector) : that.selector != null)
            return false;
        return sort != null ? sort.equals(that.sort) : that.sort == null;

    }

    @Override
    public int hashCode() {
        int result = selector != null ? selector.hashCode() : 0;
        result = 31 * result + (sort != null ? sort.hashCode() : 0);
        return result;
    }
}
