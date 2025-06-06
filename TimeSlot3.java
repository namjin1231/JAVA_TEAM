package basicWeb;
 public class TimeSlot3 {
    private final String day;
    private final String startTime;
    private final String endTime;
    public TimeSlot3(String day, String startTime, String endTime) {
        this.day = day; this.startTime = startTime; this.endTime = endTime;
    }
    public String getDay() { return day; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
}