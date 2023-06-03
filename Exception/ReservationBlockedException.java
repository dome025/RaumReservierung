package Exception;

public class ReservationBlockedException extends Exception {
    public ReservationBlockedException() {
        super("Momentan gibt es für diesen Raum zum ausgewählten Zeitpunkt bereits eine Reservierung.");
    }
}
