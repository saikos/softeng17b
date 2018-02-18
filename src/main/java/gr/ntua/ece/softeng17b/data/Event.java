package gr.ntua.ece.softeng17b.data;

public class Event {

    private long id;
    private String title;
    private String description;
    private Long subject;
    private int ticketsAvailable;
    private Place place;


    public Event(long id, String title, String description, Long subject, int ticketsAvailable, Place place) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.ticketsAvailable = ticketsAvailable;
        this.place = place;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getSubject() {
        return subject;
    }

    public int getTicketsAvailable() {
        return ticketsAvailable;
    }

    public Place getPlace() {
        return place;
    }
}
