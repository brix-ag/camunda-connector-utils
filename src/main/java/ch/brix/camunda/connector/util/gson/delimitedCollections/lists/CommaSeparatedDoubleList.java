package ch.brix.camunda.connector.util.gson.delimitedCollections.lists;

import ch.brix.camunda.connector.util.gson.delimitedCollections.DelimitedCollection;

import java.util.ArrayList;

public class CommaSeparatedDoubleList extends ArrayList<Double> implements DelimitedCollection<Double> {

    @Override
    public Double toGenericType(String s) {
        return Double.valueOf(s);
    }

}