package ch.brix.camunda.connector.util.gson.delimitedCollections.sets;

import ch.brix.camunda.connector.util.gson.delimitedCollections.DelimitedCollection;

import java.util.HashSet;

public class CommaSeparatedLongSet extends HashSet<Long> implements DelimitedCollection<Long> {

    @Override
    public Long toGenericType(String s) {
        return Long.valueOf(s);
    }

}