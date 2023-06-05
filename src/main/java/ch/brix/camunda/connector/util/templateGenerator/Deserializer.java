package ch.brix.camunda.connector.util.templateGenerator;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Deserializer {

    public static <T> T deserialize(String json, Gson gson, Class<T> templateClass) {
        Object request = gson.fromJson(json, templateClass);
        try {
            parseClass(request, json, gson);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new JsonParseException(e);
        }
        return (T) request;
    }

    private static void parseClass(Object parent, String json, Gson gson) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<Class<?>, Method> setters = findSetters(parent.getClass());
        for (Field field : parent.getClass().getDeclaredFields()) {
            PropertyDefinition pd = field.getDeclaredAnnotation(PropertyDefinition.class);
            if (pd == null || pd.choiceValues().length == 0 || pd.choiceClasses().length != pd.choiceValues().length)
                continue;
            Class<?>[] choiceClasses = pd.choiceClasses();
            for (Class<?> choiceClass : choiceClasses) {
                Object o = gson.fromJson(json, choiceClass);
                if (setters.containsKey(choiceClass)) {
                    setters.get(o.getClass()).invoke(parent, o);
                } else {
                    throw new NoSuchMethodException("Setter for class " + choiceClass.getName() + " missing or incorrectly named.");
                }
                parseClass(o, json, gson);
            }
        }
    }

    private static Map<Class<?>, Method> findSetters(Class<?> clazz) {
        Map<Class<?>, Method> setters = new HashMap<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("set") && method.getParameterCount() == 1)
                setters.put(method.getParameterTypes()[0], method);
        }
        return setters;
    }

    public static String toSetterName(Field field) {
        return "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
    }

}
