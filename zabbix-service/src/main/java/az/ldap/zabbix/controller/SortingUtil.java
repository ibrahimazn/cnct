package az.ldap.zabbix.controller;

import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;

/**
 * Sorting utility to handle sorting related activities.
 *
 */
public class SortingUtil {

    /**
     * Makes sure that utility classes (classes that contain only static methods or fields in their API) do not have a
     * public constructor.
     */
    protected SortingUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * Method to check if the given parameter for sorting is valid.
     *
     * @param clazz class of the object.
     * @param field name
     * @return string field name
     */
    public static String checkSortingParameter(Class<? extends Object> clazz, String field) {

        String fieldName = field.toLowerCase().trim();

        Field[] fields = clazz.getDeclaredFields();
        // For order by class fields
        for (Field classField : fields) {

            String name = classField.getName().toLowerCase();
            if (fieldName.equals(name) || fieldName.equals("-" + name)) {

                return classField.getName();
            }
        }

        if (fields.length > 0) {
            if (field.contains("-")) {
                return field.substring(1,field.length());
            }
            return field.substring(1,field.length());
        }

        return null;
    }

    /**
     * Method to check if the given sort direction.
     *
     * @param clazz class of the object
     * @param field field name parameter
     * @return Direction of sorting
     */
    public static Sort.Direction checkSortStyle(Class<? extends Object> clazz, String field) {
        String fieldName = field.toLowerCase().trim();
        Field[] fields = clazz.getDeclaredFields();

        // For order by class fields
        for (Field classField : fields) {
            String name = classField.getName().toLowerCase();

            if (fieldName.equals(name)) {
                return Sort.Direction.ASC;
            } else if (fieldName.equals("-" + name)) {
                return Sort.Direction.DESC;
            }
        }

            if (field.contains("-")) {
                return Sort.Direction.DESC;
            }
        return Sort.Direction.ASC;
    }

}
