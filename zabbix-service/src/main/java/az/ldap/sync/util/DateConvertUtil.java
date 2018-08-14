package az.ldap.sync.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date convert util to handle all the date conversion.
 */
public abstract class DateConvertUtil {

    /**
     * @return current system time stamp
     * @throws Exception raise if error
     */
    public static Long getTimestamp() throws Exception {
        return System.currentTimeMillis() / 1000;
    }


    public static int getDateDiff(Date newerDate, Date olderDate) {
        int diffInDays = (int)( (newerDate.getTime() - olderDate.getTime()) / (1000 * 60 * 60 * 24) );
        return diffInDays;
    }

    /**
     * Conver the date to mongodb date format.
     *
     * @param date to convert
     * @return mongodb formatted date
     * @throws ParseException if error occurs.
     */
    public static String convertDateToMongoDateString(Date date) throws ParseException {
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .format(date);
        SimpleDateFormat originalFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        date = originalFormat.parse(date.toString());
        String formattedDate = targetFormat.format(date);
        Date mongoDate = targetFormat.parse(formattedDate);
        return formattedDate;
    }

    /**
     * Conver the date to mongodb date format.
     *
     * @param format to convert
     * @param dateString date string value
     * @return mongodb formatted date
     * @throws ParseException if error occurs.
     */
    public static Date getMongoDateByFormatAndString(String format, String dateString) throws ParseException {
        Date date = getTimeByDateAndFormat(dateString, format);
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .format(date);
        SimpleDateFormat originalFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        date = originalFormat.parse(date.toString());
        String formattedDate = targetFormat.format(date);
        Date mongoDate = targetFormat.parse(formattedDate);
        return mongoDate;
    }

    /**
     * Get the time by date and format.
     *
     * @param dateValue date value.
     * @param dateFormat format of the date.
     * @return date time.
     * @throws ParseException error occurs.
     */
    public static Date getTimeByDateAndFormat(String dateValue, String dateFormat) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String dateInString = dateValue;
        Date date = formatter.parse(dateInString);
        return date;
    }
}
