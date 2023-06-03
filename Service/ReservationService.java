package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

import Entity.Reservation;
import Entity.Room;
import Entity.User;
import Exception.ReservationBlockedException;

public class ReservationService {
    Connection connection;
    UserService userService;
    RoomService roomService;
    public ReservationService(Connection connection, UserService userService, RoomService roomService) {
        this.connection = connection;
        this.userService = userService;
        this.roomService = roomService;
    }

    public void createReservation(String userId, String roomId, String startDate, String endDate) throws ReservationBlockedException {
        User user = userService.getUserById(userId);
        Room room = roomService.getRoomById(roomId);
        Reservation reservation = new Reservation(user, room);
        try {
            PreparedStatement stm = this.connection.prepareStatement("SELECT * FROM reservation WHERE start_date_time < ? AND end_date_time > ? AND room_id = ?");
            stm.setString(1, endDate);
            stm.setString(2, startDate);
            stm.setString(3, roomId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                throw new ReservationBlockedException();
            }
        } catch (SQLException exception) {
            System.out.println(exception);
        }
        try {
            Statement stm = this.connection.createStatement();
            stm.execute(MessageFormat.format("INSERT INTO reservation VALUES (\"{0}\", \"{1}\", \"{2}\", \"{3}\", \"{4}\")", reservation.getId(), startDate , endDate, user.getId(), room.getId()));
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public void editReservation(String id, String userId, String roomId, String startDate, String endDate) throws ReservationBlockedException {
        try {
            PreparedStatement stm = this.connection.prepareStatement("SELECT * FROM reservation WHERE start_date_time < ? AND end_date_time > ? AND room_id = ?");
            stm.setString(1, endDate);
            stm.setString(2, startDate);
            stm.setString(3, roomId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                throw new ReservationBlockedException();
            }
        } catch (SQLException exception) {
            System.out.println(exception);
        }
        try {
            Statement stm = this.connection.createStatement();
            stm.execute(MessageFormat.format("UPDATE reservation SET start_date_time = \"{0}\", end_date_time = \"{1}\", user_id = \"{2}\", room_id = \"{3}\" WHERE id = \"{4}\"", startDate , endDate, userId, roomId, id));
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public void deleteReservation(String id) {
        try {
            Statement stm = this.connection.createStatement();
            stm.execute(MessageFormat.format("DELETE FROM reservation WHERE id = \"{0}\"", id));
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public String[][] getAllReservations() {
        ResultSet rs = null;
        try {
            Statement stm = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery("SELECT * FROM reservation");
        } catch (Exception exception) {
            System.out.println(exception);
        }
        int i = 0;
        try {
            while (rs.next()) {
                i++;
            }
            rs.beforeFirst();
        } catch (Exception exception) {
            System.out.println(exception);
        }
        String[][] users = new String[i < 26 ? 26 : i][5];
        try {
            int j = 0;
            while (rs.next()) {
                User user = userService.getUserById(rs.getString("user_id"));
                Room room = roomService.getRoomById(rs.getString("room_id"));
                users[j][0] = rs.getString("id");
                users[j][1] = user.getFullName();
                users[j][2] = room.getIdentification();
                users[j][3] = rs.getString("start_date_time");
                users[j][4] = rs.getString("end_date_time");
                j++;
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return users;
    }

    public String[] getAllReservationIds() {
        ResultSet rs = null;
        try {
            Statement stm = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery("SELECT * FROM reservation");
        } catch (Exception exception) {
            System.out.println(exception);
        }
        int i = 0;
        try {
            while (rs.next()) {
                i++;
            }
            rs.beforeFirst();
        } catch (Exception exception) {
            System.out.println(exception);
        }
        String[] ids = new String[i];
        try {
            int j = 0;
            while (rs.next()) {
                ids[j] = rs.getString("id");
                j++;
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return ids;
    }

    public Reservation getReservationById(String id) {
        ResultSet rs = null;
        try {
            Statement stm = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery(MessageFormat.format("SELECT * FROM reservation WHERE id = \"{0}\"", id));
        } catch (Exception exception) {
            System.out.println(exception);
        }
        String userId = null;
        String roomId = null;
        String startDateTime = null;
        String endDateTime = null;
        try{
            rs.next();
            userId = rs.getString("user_id");
            roomId = rs.getString("room_id");
            startDateTime = rs.getString("start_date_time");
            endDateTime = rs.getString("end_date_time");
        } catch (Exception exception) {
            System.out.println(exception);
        }
        User user = userService.getUserById(userId);
        Room room = roomService.getRoomById(roomId);
        return new Reservation(id, user, room, startDateTime, endDateTime);
    }
}