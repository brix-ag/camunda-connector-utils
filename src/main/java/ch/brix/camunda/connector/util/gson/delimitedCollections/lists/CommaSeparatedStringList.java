package ch.brix.camunda.connector.util.gson.delimitedCollections.lists;

import ch.brix.camunda.connector.util.gson.delimitedCollections.DelimitedCollection;

import java.util.ArrayList;

public class CommaSeparatedStringList extends ArrayList<String> implements DelimitedCollection<String> {

    @Override
    public String toGenericType(String s) {
        return s;
    }

}