package View;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Entity.Room;
import Service.ReservationService;
import Service.RoomService;

public class RoomForm {
    private JDialog dialog;
    private JComboBox<String> roomSelect;
    private JLabel identificationLabel;
    private JTextField identificationInput;
    private JLabel nameLabel;
    private JTextField nameInput;
    private JButton submit;
    private JButton delete;

    public RoomForm(JFrame frame, RoomService roomService, JTable roomTable, MainView mainView, JTable reservationTable, ReservationService reservationService) {
        this.dialog = new JDialog();
        this.dialog.setTitle("Raum erstellen");
        this.dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.dialog.setSize(475, 255);
        this.dialog.setResizable(false);
        this.dialog.setVisible(true);

        String[] roomIds = roomService.getAllRoomIds();
        String[] selectValues = new String[1 + roomIds.length];
        selectValues[0] = "-";
        System.arraycopy(roomIds, 0, selectValues, 1, roomIds.length);
        this.roomSelect = new JComboBox<String>(selectValues);
        this.roomSelect.setSize(300, 30);
        this.roomSelect.setLocation(125, 20);

        this.roomSelect.addActionListener(e -> {
            String id = this.roomSelect.getSelectedItem().toString();
            if (!id.equals("-")) {
                Room room = roomService.getRoomById(id);
                this.identificationInput.setText(room.getIdentification());
                this.nameInput.setText(room.getName());
            } else {
                this.identificationInput.setText("");
                this.nameInput.setText("");
            }
        });

        this.identificationLabel = new JLabel("Kennzeichnung");
        this.identificationLabel.setSize(300, 30);
        this.identificationLabel.setLocation(25, 70);

        this.identificationInput = new JTextField();
        this.identificationInput.setSize(300, 30);
        this.identificationInput.setLocation(125, 70);

        this.nameLabel = new JLabel("Name");
        this.nameLabel.setSize(300, 30);
        this.nameLabel.setLocation(25, 120);

        this.nameInput = new JTextField();
        this.nameInput.setSize(300, 30);
        this.nameInput.setLocation(125, 120);

        this.submit = new JButton("Speichern");
        this.submit.addActionListener(e -> {
            if (!this.identificationInput.getText().isEmpty()) {
                String id = this.roomSelect.getSelectedItem().toString();
                if (id.equals("-")) {
                    roomService.createRoom(this.identificationInput.getText(), this.nameInput.getText());
                } else {
                    roomService.editRoom(id, this.identificationInput.getText(), this.nameInput.getText());
                }

                String[][] rooms = roomService.getAllRooms();
                String roomColumns[] = {"ID", "Kennzeichnung", "Name"};
                DefaultTableModel model = new DefaultTableModel(rooms, roomColumns);
                roomTable.setModel(model);

                String[][] reservations = reservationService.getAllReservations();
                String reservationColumns[] = {"ID", "Benutzer", "Raum", "Von", "Bis"};
                DefaultTableModel reservationTableModel = new DefaultTableModel(reservations, reservationColumns);
                reservationTable.setModel(reservationTableModel);

                mainView.enableReservationButton();

                this.dialog.dispose();
            } else {
                JDialog errorDialog = new JDialog();
                errorDialog.setTitle("Fehlerhafte Angaben");
                errorDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                errorDialog.setSize(410, 80);
                errorDialog.setResizable(false);
                errorDialog.setVisible(true);
                JLabel errorLabel = new JLabel("Bitte prüfen Sie die, in das Formular, eingegebenen Daten auf deren Richtigkeit.");
                errorLabel.setSize(400, 30);
                errorLabel.setLocation(10, 5);
                errorDialog.add(errorLabel);
            }
        });
        this.submit.setSize(100, 30);
        this.submit.setLocation(325, 170);

        this.delete = new JButton("Löschen");
        this.delete.addActionListener(e -> {
            String id = this.roomSelect.getSelectedItem().toString();
            if (!id.equals("-")) {
                roomService.deleteRoom(id);

                String[][] rooms = roomService.getAllRooms();
                String roomColumns[] = {"ID", "Vorname", "Nachname"};
                DefaultTableModel roomTableModel = new DefaultTableModel(rooms, roomColumns);
                roomTable.setModel(roomTableModel);

                String[][] reservations = reservationService.getAllReservations();
                String reservationColumns[] = {"ID", "Benutzer", "Raum", "Von", "Bis"};
                DefaultTableModel reservationTableModel = new DefaultTableModel(reservations, reservationColumns);
                reservationTable.setModel(reservationTableModel);

                mainView.enableReservationButton();
                this.dialog.dispose();
            }
        });
        this.delete.setSize(100, 30);
        this.delete.setLocation(200, 170);

        this.dialog.add(this.roomSelect);
        this.dialog.add(this.identificationLabel);
        this.dialog.add(this.identificationInput);
        this.dialog.add(this.nameLabel);
        this.dialog.add(this.nameInput);
        this.dialog.add(this.submit);
        this.dialog.add(this.delete);
    }
}
