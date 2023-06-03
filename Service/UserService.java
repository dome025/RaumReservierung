package Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;

import Entity.User;

public class UserService {
    Connection connection;
    public UserService(Connection connection) {
        this.connection = connection;
    }

    public void createUser(String firstName, String lastName) {
        User u = new User(firstName, lastName);
        try {
            Statement stm = this.connection.createStatement();
            stm.execute(MessageFormat.format("INSERT INTO user VALUES (\"{0}\", \"{1}\", \"{2}\")", u.getId(), firstName, lastName));
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public void editUser(String id, String firstName, String lastName) {
        try {
            Statement stm = this.connection.createStatement();
            stm.execute(MessageFormat.format("UPDATE user SET first_name = \"{0}\", last_name = \"{1}\" WHERE id = \"{2}\"", firstName, lastName, id));
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public void deleteUser(String id) {
        try {
            Statement stm = this.connection.createStatement();
            stm.execute(MessageFormat.format("DELETE FROM user WHERE id = \"{0}\"", id));
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public String[][] getAllUsers() {
        ResultSet rs = null;
        try {
            Statement stm = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery("SELECT * FROM user");
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
        String[][] users = new String[i < 26 ? 26 : i][3];
        try {
            int j = 0;
            while (rs.next()) {
                users[j][0] = rs.getString("id");
                users[j][1] = rs.getString("first_name");
                users[j][2] = rs.getString("last_name");
                j++;
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return users;
    }

    public String[] getAllUserIds() {
        ResultSet rs = null;
        try {
            Statement stm = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery("SELECT * FROM user");
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

    public User getUserById(String id) {
        ResultSet rs = null;
        try {
            Statement stm = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery(MessageFormat.format("SELECT * FROM user WHERE id = \"{0}\"", id));
        } catch (Exception exception) {
            System.out.println(exception);
        }
        String firstName = null;
        String lastName = null;
        try{
            rs.next();
            firstName = rs.getString("first_name");
            lastName = rs.getString("last_name");
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return new User(id, firstName, lastName);
    }

    public int getNumberOfUsers() {
        try {
            Statement stm = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stm.executeQuery("SELECT COUNT(*) user_count FROM user");
            rs.next();
            return rs.getInt("user_count");
        } catch (Exception exception) {
            System.out.println(exception);
            return 0;
        }
    }
}
