package magic.data;

public class MagicDate {
    
    private final int year;
    private final int month;
    private final int day;
    
    public MagicDate(final int aYear, final int aMonth, final int aDay) {
        year = aYear;
        if (aMonth < 1 || aMonth > 12){
            throw new IllegalArgumentException("Month must be between 1 and 12, inclusive");
        } else {
            month = aMonth;
        }
        if (aDay < 1 || aDay > 31){
            throw new IllegalArgumentException("Day must be between 1 and 31, inclusive");
        }else{
            day = aDay;
        }
    }

    public MagicDate(final int year, int month) {
        this(year, month, 1);
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @return the day
     */
    public int getDay() {
        return day;
    }
}
