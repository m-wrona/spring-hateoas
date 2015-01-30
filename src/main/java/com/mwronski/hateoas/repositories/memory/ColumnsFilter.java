package com.mwronski.hateoas.repositories.memory;

import com.google.common.collect.ImmutableSet;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Set;

/**
 * Filter allows to serialize/deserialize only chosen columns and skip the unwanted ones.
 *
 * @author Michal Wronski
 * @date 27-05-2014
 * @see GsonBuilder
 * @see Gson
 */
final class ColumnsFilter implements ExclusionStrategy {

    private final Set<String> includeFields;

    /**
     * Create instance
     *
     * @param includeColumns columns that should be serialized/deserialized
     */
    ColumnsFilter(String... includeColumns) {
        includeFields = ImmutableSet.copyOf(includeColumns);
    }

    @Override
    public boolean shouldSkipClass(Class<?> arg0) {
        return false;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes field) {
        if (includeFields.isEmpty()) {
            // all fields should be taken
            return false;
        }
        return !includeFields.contains(field.getName());
    }

}
