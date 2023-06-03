package Entity;
import java.util.UUID;

public class Room {
    private String id;
    private String identification;
    private String name;

    public Room(String identification, String name) {
        this.id = UUID.randomUUID().toString();
        this.identification = identification;
        this.name = name;
    }

    public Room(String id, String identification, String name) {
        this.id = id;
        this.identification = identification;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public String getIdentification() {
        return this.identification;
    }

    public String getName() {
        return this.name;
    }
}
