package View;

import java.text.MessageFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import Entity.Reservation;
import Entity.Room;
import Entity.User;
import Exception.ReservationBlockedException;
import Service.ReservationService;
import Service.RoomService;
import Service.UserService;

public class ReservationForm {
    private JDialog dialog;
    private JComboBox<String> reservationSelect;
    private JLabel userLabel;
    private JComboBox<String> userInput;
    private JLabel userFullNameLabel;
    private JLabel roomLabel;
    private JComboBox<String> roomInput;
    private JLabel roomNameLabel;
    private JLabel startDayLabel;
    private JTextField startDayInput;
    private JLabel startMonthLabel;
    private JTextField startMonthInput;
    private JLabel startYearLabel;
    private JTextField startYearInput;
    private JLabel startHourLabel;
    private JTextField startHourInput;
    private JLabel startMinuteLabel;
    private JTextField startMinuteInput;
    private JLabel endDayLabel;
    private JTextField endDayInput;
    private JLabel endMonthLabel;
    private JTextField endMonthInput;
    private JLabel endYearLabel;
    private JTextField endYearInput;
    private JLabel endHourLabel;
    private JTextField endHourInput;
    private JLabel endMinuteLabel;
    private JTextField endMinuteInput;
    private JButton submit;
    private JButton delete;

    public ReservationForm (JFrame frame, UserService userService, RoomService roomService, ReservationService reservationService, JTable reservationTable) {
        this.dialog = new JDialog();
        this.dialog.setTitle("Reservierung erstellen");
        this.dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.dialog.setSize(587, 455);
        this.dialog.setResizable(false);
        this.dialog.setVisible(true);

        String[] reservationIds = reservationService.getAllReservationIds();
        String[] selectValues = new String[1 + reservationIds.length];
        selectValues[0] = "-";
        System.arraycopy(reservationIds, 0, selectValues, 1, reservationIds.length);
        this.reservationSelect = new JComboBox<String>(selectValues);
        this.reservationSelect.setSize(250, 30);
        this.reservationSelect.setLocation(150, 25);

        this.reservationSelect.addActionListener(e -> {
            String id = this.reservationSelect.getSelectedItem().toString();
            if (!id.equals("-")) {
                Reservation reservation = reservationService.getReservationById(id);
                this.userInput.setSelectedItem(reservation.getUser().getId());
                this.roomInput.setSelectedItem(reservation.getRoom().getId());
                String startDateTime = reservation.getStartDateTime();
                String endDateTime = reservation.getEndDateTime();
                String[] startDateTimeParts = startDateTime.split("(-|:|\\u0020)");
                String[] endDateTimeParts = endDateTime.split("(-|:|\\u0020)");
                this.startYearInput.setText(startDateTimeParts[0]);
                this.startMonthInput.setText(startDateTimeParts[1]);
                this.startDayInput.setText(startDateTimeParts[2]);
                this.startHourInput.setText(startDateTimeParts[3]);
                this.startMinuteInput.setText(startDateTimeParts[4]);
                this.endYearInput.setText(endDateTimeParts[0]);
                this.endMonthInput.setText(endDateTimeParts[1]);
                this.endDayInput.setText(endDateTimeParts[2]);
                this.endHourInput.setText(endDateTimeParts[3]);
                this.endMinuteInput.setText(endDateTimeParts[4]);
            } else {
                this.userInput.setSelectedIndex(0);
                this.roomInput.setSelectedIndex(0);
                this.startYearInput.setText("");
                this.startMonthInput.setText("");
                this.startDayInput.setText("");
                this.startHourInput.setText("");
                this.startMinuteInput.setText("");
                this.endYearInput.setText("");
                this.endMonthInput.setText("");
                this.endDayInput.setText("");
                this.endHourInput.setText("");
                this.endMinuteInput.setText("");
            }
        });

        //User
        this.userLabel = new JLabel("Benutzer");
        this.userLabel.setSize(100, 30);
        this.userLabel.setLocation(25, 80);

        this.userInput = new JComboBox<String>(userService.getAllUserIds());
        this.userInput.setSize(250, 30);
        this.userInput.setLocation(150, 80);
        
        User user = userService.getUserById(this.userInput.getSelectedItem().toString());
        this.userFullNameLabel = new JLabel(MessageFormat.format("{0} {1}", user.getFirstName(), user.getLastName()));
        this.userFullNameLabel.setSize(200, 30);
        this.userFullNameLabel.setLocation(450, 80);
        this.userInput.addActionListener(e -> {
            this.dialog.remove(this.userFullNameLabel);
            User selectedUser = userService.getUserById(this.userInput.getSelectedItem().toString());
            this.userFullNameLabel.setText(selectedUser.getFullName());
            this.dialog.add(this.userFullNameLabel);
            this.dialog.repaint();
        });

        //Room
        this.roomLabel = new JLabel("Raum");
        this.roomLabel.setSize(100, 30);
        this.roomLabel.setLocation(25, 135);

        this.roomInput = new JComboBox<String>(roomService.getAllRoomIds());
        this.roomInput.setSize(250, 30);
        this.roomInput.setLocation(150, 135);
        
        Room room = roomService.getRoomById(this.roomInput.getSelectedItem().toString());
        this.roomNameLabel = new JLabel(room.getIdentification());
        this.roomNameLabel.setSize(200, 30);
        this.roomNameLabel.setLocation(450, 135);
        this.roomInput.addActionListener(e -> {
            this.dialog.remove(this.roomNameLabel);
            Room selectedRoom = roomService.getRoomById(this.roomInput.getSelectedItem().toString());
            this.roomNameLabel.setText(selectedRoom.getIdentification());
            this.dialog.add(this.roomNameLabel);
            this.dialog.repaint();
        });

        //Start-DateTime
        JRootPane startDateTimePane = new JRootPane();
        startDateTimePane.setBounds(25, 190, 250, 160);
        startDateTimePane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Startzeitpunkt", TitledBorder.LEFT, TitledBorder.TOP));
        this.startDayLabel = new JLabel("Tag");
        this.startDayLabel.setSize(50, 30);
        this.startDayLabel.setLocation(15, 15);

        this.startDayInput = new JTextField();
        this.startDayInput.setSize(50, 30);
        this.startDayInput.setLocation(15,45);

        this.startMonthLabel = new JLabel("Monat");
        this.startMonthLabel.setSize(50, 30);
        this.startMonthLabel.setLocation(75, 15);

        this.startMonthInput = new JTextField();
        this.startMonthInput.setSize(50, 30);
        this.startMonthInput.setLocation(75, 45);

        this.startYearLabel = new JLabel("Jahr");
        this.startYearLabel.setSize(100, 30);
        this.startYearLabel.setLocation(135, 15);

        this.startYearInput = new JTextField();
        this.startYearInput.setSize(100, 30);
        this.startYearInput.setLocation(135, 45);

        this.startHourLabel = new JLabel("Stunde");
        this.startHourLabel.setSize(100, 30);
        this.startHourLabel.setLocation(15, 80);

        this.startHourInput = new JTextField();
        this.startHourInput.setSize(100, 30);
        this.startHourInput.setLocation(15, 110);

        this.startMinuteLabel = new JLabel("Minute");
        this.startMinuteLabel.setSize(100, 30);
        this.startMinuteLabel.setLocation(125, 80);

        this.startMinuteInput = new JTextField();
        this.startMinuteInput.setSize(100, 30);
        this.startMinuteInput.setLocation(125, 110);

        startDateTimePane.add(this.startDayLabel);
        startDateTimePane.add(this.startDayInput);
        startDateTimePane.add(this.startMonthLabel);
        startDateTimePane.add(this.startMonthInput);
        startDateTimePane.add(this.startYearLabel);
        startDateTimePane.add(this.startYearInput);
        startDateTimePane.add(this.startHourLabel);
        startDateTimePane.add(this.startHourInput);
        startDateTimePane.add(this.startMinuteLabel);
        startDateTimePane.add(this.startMinuteInput);

        //End-DateTime
        JRootPane endDateTimePane = new JRootPane();
        endDateTimePane.setBounds(300, 190, 250, 160);
        endDateTimePane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Endzeitpunkt", TitledBorder.LEFT, TitledBorder.TOP));
        this.endDayLabel = new JLabel("Tag");
        this.endDayLabel.setSize(50, 30);
        this.endDayLabel.setLocation(15, 15);

        this.endDayInput = new JTextField();
        this.endDayInput.setSize(50, 30);
        this.endDayInput.setLocation(15,45);

        this.endMonthLabel = new JLabel("Monat");
        this.endMonthLabel.setSize(50, 30);
        this.endMonthLabel.setLocation(75, 15);

        this.endMonthInput = new JTextField();
        this.endMonthInput.setSize(50, 30);
        this.endMonthInput.setLocation(75, 45);

        this.endYearLabel = new JLabel("Jahr");
        this.endYearLabel.setSize(100, 30);
        this.endYearLabel.setLocation(135, 15);

        this.endYearInput = new JTextField();
        this.endYearInput.setSize(100, 30);
        this.endYearInput.setLocation(135, 45);

        this.endHourLabel = new JLabel("Stunde");
        this.endHourLabel.setSize(100, 30);
        this.endHourLabel.setLocation(15, 80);

        this.endHourInput = new JTextField();
        this.endHourInput.setSize(100, 30);
        this.endHourInput.setLocation(15, 110);

        this.endMinuteLabel = new JLabel("Minute");
        this.endMinuteLabel.setSize(100, 30);
        this.endMinuteLabel.setLocation(125, 80);

        this.endMinuteInput = new JTextField();
        this.endMinuteInput.setSize(100, 30);
        this.endMinuteInput.setLocation(125, 110);

        endDateTimePane.add(this.endDayLabel);
        endDateTimePane.add(this.endDayInput);
        endDateTimePane.add(this.endMonthLabel);
        endDateTimePane.add(this.endMonthInput);
        endDateTimePane.add(this.endYearLabel);
        endDateTimePane.add(this.endYearInput);
        endDateTimePane.add(this.endHourLabel);
        endDateTimePane.add(this.endHourInput);
        endDateTimePane.add(this.endMinuteLabel);
        endDateTimePane.add(this.endMinuteInput);

        this.submit = new JButton("Speichern");
        this.submit.addActionListener(e -> {
            String startDate = MessageFormat.format(
                "{0}-{1}-{2} {3}:{4}:00",
                this.startYearInput.getText(),
                this.startMonthInput.getText(),
                this.startDayInput.getText(),
                this.startHourInput.getText(),
                this.startMinuteInput.getText()
            );
           String endDate = MessageFormat.format(
                "{0}-{1}-{2} {3}:{4}:00",
                this.endYearInput.getText(),
                this.endMonthInput.getText(),
                this.endDayInput.getText(),
                this.endHourInput.getText(),
                this.endMinuteInput.getText()
            );
            try {
                String id = this.reservationSelect.getSelectedItem().toString();
                if (id.equals("-")) {
                    reservationService.createReservation(
                        this.userInput.getSelectedItem().toString(),
                        this.roomInput.getSelectedItem().toString(),
                        startDate,
                        endDate
                    );
                } else {
                    reservationService.editReservation(
                        id,
                        this.userInput.getSelectedItem().toString(),
                        this.roomInput.getSelectedItem().toString(),
                        startDate,
                        endDate
                    );
                }

                String[][] reservations = reservationService.getAllReservations();
                String reservationColumns[] = {"ID", "Benutzer", "Raum", "Von", "Bis"};
                DefaultTableModel model = new DefaultTableModel(reservations, reservationColumns);
                reservationTable.setModel(model);

                this.dialog.dispose();
            } catch (ReservationBlockedException exception) {
                JDialog reservationBlockedDialog = new JDialog();
                reservationBlockedDialog.setTitle("Reservierung fehlgeschlagen");
                reservationBlockedDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                reservationBlockedDialog.setSize(410, 80);
                reservationBlockedDialog.setResizable(false);
                JTextPane errorMessagePane = new JTextPane();
                errorMessagePane.setEditable(false);
                errorMessagePane.setText(exception.getMessage());
                reservationBlockedDialog.add(errorMessagePane);
                reservationBlockedDialog.setVisible(true);
            }
        });
        this.submit.setSize(100, 30);
        this.submit.setLocation(450, 370);

        this.delete = new JButton("Löschen");
        this.delete.addActionListener(e -> {
            String id = this.reservationSelect.getSelectedItem().toString();
            if (!id.equals("-")) {
                reservationService.deleteReservation(id);

                String[][] reservations = reservationService.getAllReservations();
                String reservationColumns[] = {"ID", "Benutzer", "Raum", "Von", "Bis"};
                DefaultTableModel reservationTableModel = new DefaultTableModel(reservations, reservationColumns);
                reservationTable.setModel(reservationTableModel);

                this.dialog.dispose();
            }
        });
        this.delete.setSize(100, 30);
        this.delete.setLocation(325, 370);

        this.dialog.add(this.reservationSelect);
        this.dialog.add(this.userLabel);
        this.dialog.add(this.userInput);
        this.dialog.add(this.userFullNameLabel);
        this.dialog.add(this.roomLabel);
        this.dialog.add(this.roomInput);
        this.dialog.add(this.roomNameLabel);
        this.dialog.add(startDateTimePane);
        this.dialog.add(endDateTimePane);
        this.dialog.add(this.submit);
        this.dialog.add(this.delete);
    }
}
