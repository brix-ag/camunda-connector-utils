package ch.brix.camunda.connector.util.gson.delimitedCollections.sets;

import ch.brix.camunda.connector.util.gson.delimitedCollections.DelimitedCollection;

import java.util.HashSet;

public class CommaSeparatedIntegerSet extends HashSet<Integer> implements DelimitedCollection<Integer> {

    @Override
    public Integer toGenericType(String s) {
        return Integer.valueOf(s);
    }

}