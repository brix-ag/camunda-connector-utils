package ch.brix.camunda.connector.util.gson.delimitedCollections.sets;

import ch.brix.camunda.connector.util.gson.delimitedCollections.DelimitedCollection;

import java.util.HashSet;
import java.util.Locale;

public class CommaSeparatedLocaleSet extends HashSet<Locale> implements DelimitedCollection<Locale> {

    @Override
    public Locale toGenericType(String s) {
        return Locale.forLanguageTag(s);
    }

}