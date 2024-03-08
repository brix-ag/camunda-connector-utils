package ch.brix.camunda.connector.util.templateGenerator;

import ch.brix.camunda.connector.util.gson.delimitedCollections.DelimitedCollection;
import ch.brix.camunda.connector.util.gson.delimitedCollections.DelimitedCollectionsAdapter;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

public class GsonProvider {

    public static Gson getDefaultGson() {
        return getDefaultGsonBuilder().create();
    }

    public static GsonBuilder getDefaultGsonBuilder() {
        return new GsonBuilder()
                .addDeserializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.hasModifier(Modifier.TRANSIENT) ||
                                f.getAnnotation(PropertyDefinition.class) == null;
                    }
                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeHierarchyAdapter(DelimitedCollection.class, new DelimitedCollectionsAdapter());
    }

}
