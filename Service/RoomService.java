package Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;

import Entity.Room;

public class RoomService {
    Connection connection;
    public RoomService(Connection connection) {
        this.connection = connection;
    }

    public void createRoom(String identification, String name) {
        Room r = new Room(identification, name);
        try {
            Statement stm = this.connection.createStatement();
            stm.execute(MessageFormat.format("INSERT INTO room VALUES (\"{0}\", \"{1}\", \"{2}\")", r.getId(), identification, name));
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public void editRoom(String id, String identification, String name) {
        try {
            Statement stm = this.connection.createStatement();
            stm.execute(MessageFormat.format("UPDATE room SET identification = \"{0}\", name = \"{1}\" WHERE id = \"{2}\"", identification, name, id));
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public void deleteRoom(String id) {
        try {
            Statement stm = this.connection.createStatement();
            stm.execute(MessageFormat.format("DELETE FROM room WHERE id = \"{0}\"", id));
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public String[][] getAllRooms() {
        ResultSet rs = null;
        try {
            Statement stm = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery("SELECT * FROM room");
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
        String[][] rooms = new String[i < 26 ? 26 : i][3];
        try {
            int j = 0;
            while (rs.next()) {
                rooms[j][0] = rs.getString("id");
                rooms[j][1] = rs.getString("identification");
                rooms[j][2] = rs.getString("name");
                j++;
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return rooms;
    }

    public String[] getAllRoomIds() {
        ResultSet rs = null;
        try {
            Statement stm = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery("SELECT * FROM room");
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

    public Room getRoomById(String id) {
        ResultSet rs = null;
        try {
            Statement stm = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery(MessageFormat.format("SELECT * FROM room WHERE id = \"{0}\"", id));
        } catch (Exception exception) {
            System.out.println(exception);
        }
        String identification = null;
        String name = null;
        try{
            rs.next();
            identification = rs.getString("identification");
            name = rs.getString("name");
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return new Room(id, identification, name);
    }

    public int getNumberOfRooms() {
        try {
            Statement stm = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stm.executeQuery("SELECT COUNT(*) room_count FROM room");
            rs.next();
            return rs.getInt("room_count");
        } catch (Exception exception) {
            System.out.println(exception);
            return 0;
        }
    }
}