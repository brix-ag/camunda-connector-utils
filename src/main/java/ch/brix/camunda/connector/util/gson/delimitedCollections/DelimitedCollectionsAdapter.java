package ch.brix.camunda.connector.util.gson.delimitedCollections;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class DelimitedCollectionsAdapter implements JsonSerializer<DelimitedCollection<?>>, JsonDeserializer<DelimitedCollection<?>> {

    @Override
    public DelimitedCollection<?> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!jsonElement.isJsonPrimitive())
            throw new JsonParseException("Json string expected");
        if (jsonElement.isJsonNull())
            return null;
        try {
            Constructor<?> constructor = TypeToken.get(type).getRawType().getDeclaredConstructor();
            DelimitedCollection<?> collection = (DelimitedCollection<?>) constructor.newInstance();
            collection.addFromDelimitedString(jsonElement.getAsString());
            // you could inject other stuff here
            return collection;
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(DelimitedCollection<?> collection, Type type, JsonSerializationContext context) {
        return collection == null ? JsonNull.INSTANCE : new JsonPrimitive(collection.toDelimitedString());
    }
}

