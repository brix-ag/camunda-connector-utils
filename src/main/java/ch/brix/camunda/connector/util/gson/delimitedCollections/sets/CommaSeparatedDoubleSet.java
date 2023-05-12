package ch.brix.camunda.connector.util.gson.delimitedCollections.sets;

import ch.brix.camunda.connector.util.gson.delimitedCollections.DelimitedCollection;

import java.util.HashSet;

public class CommaSeparatedDoubleSet extends HashSet<Double> implements DelimitedCollection<Double> {

    @Override
    public Double toGenericType(String s) {
        return Double.valueOf(s);
    }

}