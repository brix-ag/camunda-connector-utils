package ch.brix.camunda.connector.util.gson.delimitedCollections.sets;

import ch.brix.camunda.connector.util.gson.delimitedCollections.DelimitedCollection;

import java.util.HashSet;

public class CommaSeparatedStringSet extends HashSet<String> implements DelimitedCollection<String> {

    @Override
    public String toGenericType(String s) {
        return s;
    }

}
