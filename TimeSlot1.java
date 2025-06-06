package basicWeb;

public class TimeSlot1 {
    private String day;
    private String startTime;
    private String endTime;

    public TimeSlot1(String day, String startTime, String endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

	public String getDay() { return day; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
}