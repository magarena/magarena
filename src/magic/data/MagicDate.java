package magic.data;

public class MagicDate {
    
    private int year;
    private int month;
    private int day;
    
    public MagicDate(final int year, final int month, final int day) {
        this.setYear(year);
        this.setMonth(month);
        this.setDay(day);
    }

    public MagicDate(final int year, int month) {
        this.setYear(year);
        this.setMonth(month);
        this.setDay(1);
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(int month) {
        if(month < 1 || month > 12){
            throw new IllegalArgumentException("Month must be between 1 and 12, inclusive");
        }else{
            this.month = month;
        }
    }

    /**
     * @return the day
     */
    public int getDay() {
        return day;
    }

    /**
     * @param day the day to set
     */
    public void setDay(int day) {
        if(day < 1 || day > 31){
            throw new IllegalArgumentException("Day must be between 1 and 31, inclusive");
        }else{
            this.day = day;
        }
    }
}
