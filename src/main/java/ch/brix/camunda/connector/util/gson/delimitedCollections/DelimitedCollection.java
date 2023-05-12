package ch.brix.camunda.connector.util.gson.delimitedCollections;

import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public interface DelimitedCollection<T> extends Collection<T> {

    default String getDelimiter() {
        return ",";
    }

    T toGenericType(String s);

    default String fromGenericType(T t) {
        return t.toString();
    }

    default String toDelimitedString() {
        return stream().map(this::fromGenericType)
                .collect(Collectors.joining(getDelimiter()));
    }

    default boolean trimElements() {
        return true;
    }

    default boolean discardBlanks() {
        return true;
    }

    default void addFromDelimitedString(String delimitedString) {
        for (String element : delimitedString.split(Pattern.quote(getDelimiter()))) {
            if (trimElements())
                element = element.trim();
            if (!discardBlanks() || !element.isBlank())
                add(toGenericType(element));
        }
    }
}
