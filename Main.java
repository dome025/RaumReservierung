import java.sql.*;

import javax.swing.UIManager;

import Service.ReservationService;
import Service.RoomService;
import Service.UserService;
import View.MainView;

public class Main {

	public static void main(String[] args)
	{
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            return;
        }
		RoomService rs;
		UserService us;
		ReservationService reservationService;
		Connection connection = null;
		try {
			//connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/room_reservation", "", ""); Zum Benutzen einkommentieren und entsprechend anpassen.
			Statement stm = connection.createStatement();
			stm.execute("CREATE DATABASE IF NOT EXISTS room_reservation");
			stm.execute("USE room_reservation");
			stm.execute("CREATE TABLE IF NOT EXISTS room(id varchar(255), identification varchar(255) NOT NULL, name varchar(255) DEFAULT NULL, PRIMARY KEY (id))");
			stm.execute("CREATE TABLE IF NOT EXISTS user(id varchar(255), first_name varchar(255) NOT NULL, last_name varchar(255) NOT NULL, PRIMARY KEY (id))");
			stm.execute("CREATE TABLE IF NOT EXISTS reservation(id varchar(255), start_date_time DATETIME, end_date_time DATETIME, user_id varchar(255), room_id varchar(255), PRIMARY KEY (id), FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE, FOREIGN KEY (room_id) REFERENCES room(id) ON DELETE CASCADE)");
		} catch (Exception exception) {
			System.out.println(exception);
		}
		rs = new RoomService(connection);
		us = new UserService(connection);
		reservationService = new ReservationService(connection, us, rs);
		new MainView(rs, us, reservationService);
	}
}
