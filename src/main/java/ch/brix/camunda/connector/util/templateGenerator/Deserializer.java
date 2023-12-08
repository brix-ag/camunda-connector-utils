package ch.brix.camunda.connector.util.templateGenerator;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Deserializer {

    public static <T> T deserialize(String json, Gson gson, Class<T> templateClass) {
        Object request = gson.fromJson(json, templateClass);
        try {
            parseClass(request, json, gson, new HashSet<>());
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new JsonParseException(e);
        }
        return (T) request;
    }

    private static void parseClass(Object parent, String json, Gson gson, Set<Class<?>> processed) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<Class<?>, Method> setters = findSetters(parent.getClass());
        for (Field field : parent.getClass().getDeclaredFields()) {
            PropertyGroup pg = field.getDeclaredAnnotation(PropertyGroup.class);
            if (pg != null && !processed.contains(field.getType())) {
                Object o = gson.fromJson(json, field.getType());
                if (setters.containsKey(field.getType())) {
                    setters.get(o.getClass()).invoke(parent, o);
                } else {
                    throw new NoSuchMethodException("Setter for class " + field.getType().getName() + " missing.");
                }
                processed.add(field.getType());
                parseClass(o, json, gson, processed);
            }
            PropertyDefinition pd = field.getDeclaredAnnotation(PropertyDefinition.class);
            if (pd == null)
                continue;
            if (pd.choiceClasses().length > 0 && pd.choiceClasses().length == pd.choiceValues().length) {
                Class<?>[] choiceClasses = pd.choiceClasses();
                for (Class<?> choiceClass : choiceClasses) {
                    if (processed.contains(choiceClass))
                        continue;
                    Object o = gson.fromJson(json, choiceClass);
                    if (setters.containsKey(choiceClass)) {
                        setters.get(o.getClass()).invoke(parent, o);
                    } else {
                        throw new NoSuchMethodException("Setter for class " + choiceClass.getName() + " missing.");
                    }
                    processed.add(choiceClass);
                    parseClass(o, json, gson, processed);
                }
            } else if (!pd.choiceEnum().equals(PropertyDefinition.Null.class)) {
                try {
                    Method getChoiceClass = pd.choiceEnum().getDeclaredMethod("getChoiceClass");
                    for (Enum<?> enumConstant : pd.choiceEnum().getEnumConstants()) {
                        Class<?> clazz = (Class<?>) getChoiceClass.invoke(enumConstant);
                        if (processed.contains(clazz))
                            continue;
                        Object o = gson.fromJson(json, clazz);
                        if (setters.containsKey(clazz)) {
                            setters.get(o.getClass()).invoke(parent, o);
                        } else {
                            throw new NoSuchMethodException("Setter for class " + clazz.getName() + " missing.");
                        }
                        parseClass(o, json, gson, processed);
                    }
                } catch (NoSuchMethodException | SecurityException ignore) {}
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

}
