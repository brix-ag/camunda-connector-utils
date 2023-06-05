package com.acme.camunda.connector.acmeSessionConnector.grouped;

import ch.brix.camunda.connector.util.gson.delimitedCollections.DelimitedCollection;
import ch.brix.camunda.connector.util.gson.delimitedCollections.DelimitedCollectionsAdapter;
import ch.brix.camunda.connector.util.templateGenerator.Deserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GroupedDemo {

    private static final String json = "{" +
            "\"action\":\"login\"," +
            "\"username\":\"John Doe\"," +
            "\"password\":\"123456\"," +
            "\"token\":\"BRqP7v77L#!e2%HvJb#eCrkp$B!5RPs8\"" +
            "}";

    private static final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(DelimitedCollection.class, new DelimitedCollectionsAdapter())
            .create();

    public static void main(String[] args) {
        GroupedRequest request = Deserializer.deserialize(json, gson, GroupedRequest.class);
        System.out.println(request.getAction());
        System.out.println(request.getLogin().getUsername());
        System.out.println(request.getLogin().getPassword());
        System.out.println(request.getLogout().getToken());
    }
}
