package ch.brix.camunda.connector.util.gson.delimitedCollections;

import ch.brix.camunda.connector.util.gson.delimitedCollections.lists.CommaSeparatedDoubleList;
import ch.brix.camunda.connector.util.gson.delimitedCollections.sets.CommaSeparatedIntegerSet;
import ch.brix.camunda.connector.util.gson.delimitedCollections.sets.CommaSeparatedLocaleSet;
import ch.brix.camunda.connector.util.gson.delimitedCollections.sets.CommaSeparatedLongSet;
import ch.brix.camunda.connector.util.gson.delimitedCollections.sets.CommaSeparatedStringSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Demo {

    private static final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(DelimitedCollection.class, new DelimitedCollectionsAdapter())
            .create();

    // to keep the demo simple the fields are not private with getters etc. but they should be
    private static class DemoRequest {
        CommaSeparatedDoubleList a;
        CommaSeparatedIntegerSet b;
        CommaSeparatedLongSet c;
        CommaSeparatedLocaleSet d;
        CommaSeparatedStringSet e;
    }

    public static void main(String[] args) {
        DemoRequest request = gson.fromJson("{" +
                "\"a\":\"1.1,1.1,2.2  , 3.3\"," + // 1.1,1.1,2.2,3.3
                "\"b\":\"1,1,2,2,3 , 3\"," + // 1,2,3 (any order)
                "\"c\":null," + // null
                "\"d\":\"en,de,fr\"," + // de,fr,en (any order)
                "\"e\":\"a,,   , b  ,c c, d  d ,a,, \"" + // a,b,c c,d  d (any order)
                "}", DemoRequest.class);
        System.out.println("a: " + request.a.toDelimitedString());
        System.out.println("b: " + request.b.toDelimitedString());
        System.out.println("c: " + request.c);
        System.out.println("d: " + request.d.toDelimitedString());
        System.out.println("e: " + request.e.toDelimitedString());

        System.out.println("sum of a: " + request.a.stream().reduce(0D, Double::sum));
    }
}
