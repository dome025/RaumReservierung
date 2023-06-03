package Entity;
import java.util.UUID;

public class Reservation {
    private String id;
    private User user;
    private Room room;
    private String startDateTime;
    private String endDateTime;

    public Reservation(User user, Room room) {
        this.id = UUID.randomUUID().toString();
        this.room = room;
        this.user = user;
    }

    public Reservation(String id, User user, Room room, String startDateTime, String endDateTime) {
        this.id = id;
        this.room = room;
        this.user = user;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public String getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public Room getRoom() {
        return this.room;
    }

    public String getStartDateTime() {
        return this.startDateTime;
    }

    public String getEndDateTime() {
        return this.endDateTime;
    }
}
