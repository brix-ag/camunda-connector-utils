package ch.brix.camunda.connector.util.gson.delimitedCollections.lists;

import ch.brix.camunda.connector.util.gson.delimitedCollections.DelimitedCollection;

import java.util.ArrayList;

public class CommaSeparatedLongList extends ArrayList<Long> implements DelimitedCollection<Long> {

    @Override
    public Long toGenericType(String s) {
        return Long.valueOf(s);
    }

}