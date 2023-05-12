package ch.brix.camunda.connector.util.gson.delimitedCollections.lists;

import ch.brix.camunda.connector.util.gson.delimitedCollections.DelimitedCollection;

import java.util.ArrayList;

public class CommaSeparatedIntegerList extends ArrayList<Integer> implements DelimitedCollection<Integer> {

    @Override
    public Integer toGenericType(String s) {
        return Integer.valueOf(s);
    }

}