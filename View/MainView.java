package View;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import java.awt.*;

import Service.ReservationService;
import Service.RoomService;
import Service.UserService;

public class MainView {
    private RoomService roomService;
    private UserService userService;
    private JButton createReservationButton;

    public MainView(RoomService roomService, UserService userService, ReservationService reservationService) {
        this.roomService = roomService;
        this.userService = userService;

        //Main Window
        JFrame frame = new JFrame();
	frame.setSize(1250,1000);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setTitle("Raum Reservierung");
        frame.setLayout(new BorderLayout());
	//frame.getContentPane().setLayout(null);

        //Reservation-Elemente
        String[][] reservations = reservationService.getAllReservations();
        String reservationColumns[] = {"ID", "Benutzer", "Raum", "Von", "Bis"};
        JTable reservationTable = new JTable(reservations, reservationColumns);
        reservationTable.setDefaultEditor(Object.class, null);

        JScrollPane rvsp = new JScrollPane(reservationTable);
        rvsp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Reservierungen", TitledBorder.LEFT, TitledBorder.TOP));
        //rvsp.setBounds(645, 20, 625, 460);
        //frame.add(rvsp);

        this.createReservationButton = new JButton("Reservierungsdialog");
        //this.createReservationButton.setSize(300, 30);
	//this.createReservationButton.setLocation(1295, 123);
        //frame.add(this.createReservationButton);
        this.createReservationButton.addActionListener(e -> {
                new ReservationForm(frame, this.userService, this.roomService, reservationService, reservationTable);
        });

        //User-Elemente
        String[][] users = this.userService.getAllUsers();
        String userColumns[] = {"ID", "Vorname", "Nachname"};
        JTable userTable = new JTable(users, userColumns);
        userTable.setDefaultEditor(Object.class, null);

        JScrollPane usp = new JScrollPane(userTable);
        usp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Benutzer", TitledBorder.LEFT, TitledBorder.TOP));
        //usp.setBounds(20, 485, 625, 460);
        //frame.add(usp);

        JButton createUserButton = new JButton("Benutzerdialog");
		//createUserButton.setSize(300, 30);
		//createUserButton.setLocation(1295, 75);
		//frame.add(createUserButton);
		createUserButton.addActionListener(e -> {
			new UserForm(frame, this.userService, userTable, this, reservationTable, reservationService);
		});

        //Room-Elemente
        String[][] rooms = this.roomService.getAllRooms();
        String roomColumns[] = {"ID", "Kennzeichnung", "Name"};
        JTable roomTable = new JTable(rooms, roomColumns);
        roomTable.setDefaultEditor(Object.class, null);

        JScrollPane rsp = new JScrollPane(roomTable);
        rsp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Räume", TitledBorder.LEFT, TitledBorder.TOP));
        //rsp.setBounds(20, 20, 625, 460);
        //frame.add(rsp);

		JButton createRoomButton = new JButton("Raumdialog");
		//createRoomButton.setSize(300, 30);
		//createRoomButton.setLocation(1295, 27);
		//frame.add(createRoomButton);
		createRoomButton.addActionListener(e -> {
			new RoomForm(frame, this.roomService, roomTable, this, reservationTable, reservationService);
		});

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(2, 2));
       // p.setBounds(0, 0, 500, 500);
        p.add(usp);
        p.add(rsp);
        p.add(rvsp);
        //p.setSize(200, 200);

        frame.add(p, BorderLayout.WEST);

        JPanel bp = new JPanel();
        bp.setLayout(new BoxLayout(bp, BoxLayout.Y_AXIS));
        //bp.setBounds(500, 500, 500, 500);
        bp.add(Box.createRigidArea(new Dimension(50, 50)));
        bp.add(createUserButton);
        bp.add(Box.createRigidArea(new Dimension(50, 20)));
        bp.add(createRoomButton);
        bp.add(Box.createRigidArea(new Dimension(50, 20)));
        bp.add(createReservationButton);
        //bp.setSize(500, 500);

        frame.add(bp, BorderLayout.CENTER);

        enableReservationButton();
		frame.setVisible(true);
    }

    public void enableReservationButton() {
        this.createReservationButton.setEnabled(this.userService.getNumberOfUsers() != 0 && this.roomService.getNumberOfRooms() != 0);
    }
}
