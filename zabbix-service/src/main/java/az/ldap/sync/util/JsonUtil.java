package az.ldap.sync.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.json.JSONObject;

/**
 * JSON validator to handle NULL validations.
 */
public abstract class JsonUtil {
    /**
     * @param object JSON array
     * @param key value
     * @return string value
     * @throws Exception raise if error
     */
    public static String getStringValue(JSONObject object, String key) throws Exception {
        if (object.has(key)) {
            return object.getString(key);
        } else {
            return null;
        }
    }

    /**
     * @param object JSON array
     * @param key value
     * @return boolean status
     * @throws Exception raise if error
     */
    public static Boolean getBooleanValue(JSONObject object, String key) throws Exception {
        if (object.has(key)) {
            return object.getBoolean(key);
        } else {
            return false;
        }
    }

    /**
     * @param object JSON array
     * @param key value
     * @return integer value.
     * @throws Exception raise if error
     */
    public static Integer getIntegerValue(JSONObject object, String key) throws Exception {
        if (object.has(key)) {
            return Integer.valueOf(object.getInt(key));
        } else {
            return null;
        }
    }
    
    /**
     * @param object JSON array
     * @param key value
     * @return long value.
     * @throws Exception raise if error
     */
    public static Long getLongValue(JSONObject object, String key) throws Exception {
        if (object.has(key)) {
            return Long.valueOf(object.getLong(key));
        } else {
            return null;
        }
    }
    
    /**
     * @param object JSON array
     * @param key value
     * @return long value.
     * @throws Exception raise if error
     */
    public static Long getHostLongValue(JSONObject object, String key) throws Exception {
        if (object.has(key)) {
            return Long.valueOf(object.getLong(key));
        } else {
            return 0L;
        }
    }
        
    /**
     * @param object JSON array
     * @param key value
     * @return long value.
     * @throws Exception raise if error
     */
    public static Double getDoubleValue(JSONObject object, String key) throws Exception {
        if (object.has(key)) {
        	if(object.getString(key) != null) {
        		return Double.valueOf(object.getLong(key));
        	} else {
        		return 0.00;
        	}
        } else {
            return 0.00;
        }
    }

    /**
     * Converting date time validation.
     *
     * @param dateTime date Time object.
     * @return date time.
     * @throws Exception error.
     */
    public static ZonedDateTime convertToZonedDateTime(String dateTime) {
        if (dateTime == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        java.util.Date date = null;
        try {
            date = format.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final ZoneId systemDefault = ZoneId.systemDefault();
        return ZonedDateTime.ofInstant(date.toInstant(), systemDefault);
    }

    /**
     * To check not null validation for double values.
     *
     * @param value of the plan.
     * @return values.
     * @throws Exception if error occurs.
     */
    public static Double getDoubleValue(Double value) throws Exception {
        if (value != null) {
            return value;
        } else {
            return 0.00;
        }
    }
}
