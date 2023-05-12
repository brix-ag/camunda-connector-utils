package ch.brix.camunda.connector.util.gson.delimitedCollections.lists;

import ch.brix.camunda.connector.util.gson.delimitedCollections.DelimitedCollection;

import java.util.ArrayList;
import java.util.Locale;

public class CommaSeparatedLocaleList extends ArrayList<Locale> implements DelimitedCollection<Locale> {

    @Override
    public Locale toGenericType(String s) {
        return Locale.forLanguageTag(s);
    }

}