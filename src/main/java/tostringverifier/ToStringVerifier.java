package tostringverifier;

import org.junit.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is bare minimum toString verifier, it checks if all private fields appeared in toString text.
 * Created by vparashar on 27/04/2017.
 */
public class ToStringVerifier<T> {

    private Class<T> type;

    private T instance;
    private Set<String> exceptedFields;
    private Set<String> fields;

    public ToStringVerifier(Class<T> type) {
        exceptedFields = new HashSet<>();
        fields = getAllPrivateFieldNames(type);
        this.type = type;
    }

    public static <T> ToStringVerifier forClass(Class<T> type) {
        return new ToStringVerifier(type);
    }

    public ToStringVerifier with(T t) {
        this.instance = t;
        return this;
    }

    public ToStringVerifier exceptFields(String... exceptedFields) {
        Arrays.asList(exceptedFields).forEach(f -> this.exceptedFields.add(f));
        return this;
    }

    public ToStringVerifier withFields(String... fields) {
        this.fields = new HashSet<>();
        Arrays.asList(fields).forEach(f -> this.fields.add(f));
        return this;
    }

    public void verify() {
        String toString = instance.toString().toLowerCase();
        List<Field> allPrivateFields = getAllPrivateFields(type);
        allPrivateFields.stream()
                .filter(fn -> fields.contains(fn.getName()))
                .filter(fn -> !exceptedFields.contains(fn))
                .forEach(fn -> {
                    try {
                        fn.setAccessible(true);
                        String value = fn.get(instance) == null ? "null" : fn.get(instance).toString().toLowerCase();
                        Assert.assertTrue(toString.contains(value));
                    } catch (IllegalAccessException e) {
                        Assert.fail();
                        e.printStackTrace();
                    }
                });
    }

    private Set<String> getAllPrivateFieldNames(Class<?> claszz) {
        return getAllPrivateFields(claszz).stream()
                .map(f -> f.getName())
                .collect(Collectors.toSet());
    }

    private List<Field> getAllPrivateFields(Class<?> claszz) {
        Field[] declaredFields = claszz.getDeclaredFields();
        return Arrays.stream(declaredFields)
                .filter(f -> !f.isSynthetic())
                .filter(f -> Modifier.isPrivate(f.getModifiers()))
                .collect(Collectors.toList());
    }
}
