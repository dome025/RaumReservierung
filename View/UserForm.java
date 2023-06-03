package View;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Entity.User;
import Service.ReservationService;
import Service.UserService;

public class UserForm {
    private JDialog dialog;
    private JComboBox<String> userSelect;
    private JLabel firstNameLabel;
    private JTextField firstNameInput;
    private JLabel lastNameLabel;
    private JTextField lastNameInput;
    private JButton submit;
    private JButton delete;

    public UserForm(JFrame frame, UserService userService, JTable userTable, MainView mainView, JTable reservationTable, ReservationService reservationService) {
        this.dialog = new JDialog();
        this.dialog.setTitle("Benutzer erstellen");
        this.dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.dialog.setSize(475, 255);
        this.dialog.setResizable(false);
        this.dialog.setVisible(true);

        String[] userIds = userService.getAllUserIds();
        String[] selectValues = new String[1 + userIds.length];
        selectValues[0] = "-";
        System.arraycopy(userIds, 0, selectValues, 1, userIds.length);
        this.userSelect = new JComboBox<String>(selectValues);
        this.userSelect.setSize(300, 30);
        this.userSelect.setLocation(125, 20);

        this.userSelect.addActionListener(e -> {
            String id = this.userSelect.getSelectedItem().toString();
            if (!id.equals("-")) {
                User user = userService.getUserById(id);
                this.firstNameInput.setText(user.getFirstName());
                this.lastNameInput.setText(user.getLastName());
            } else {
                this.firstNameInput.setText("");
                this.lastNameInput.setText("");
            }
        });

        this.firstNameLabel = new JLabel("Vorname");
        this.firstNameLabel.setSize(300, 30);
        this.firstNameLabel.setLocation(25, 70);

        this.firstNameInput = new JTextField();
        this.firstNameInput.setSize(300, 30);
        this.firstNameInput.setLocation(125, 70);

        this.lastNameLabel = new JLabel("Nachname");
        this.lastNameLabel.setSize(300, 30);
        this.lastNameLabel.setLocation(25, 120);

        this.lastNameInput = new JTextField();
        this.lastNameInput.setSize(300, 30);
        this.lastNameInput.setLocation(125, 120);

        this.submit = new JButton("Speichern");
        this.submit.addActionListener(e -> {
            if (!this.firstNameInput.getText().isEmpty() && !this.lastNameInput.getText().isEmpty()) {
                String id = this.userSelect.getSelectedItem().toString();
                if (id.equals("-")) {
                    userService.createUser(this.firstNameInput.getText(), this.lastNameInput.getText());
                } else {
                    userService.editUser(id, this.firstNameInput.getText(), this.lastNameInput.getText());
                }

                String[][] users = userService.getAllUsers();
                String userColumns[] = {"ID", "Vorname", "Nachname"};
                DefaultTableModel userTableModel = new DefaultTableModel(users, userColumns);
                userTable.setModel(userTableModel);

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
            String id = this.userSelect.getSelectedItem().toString();
            if (!id.equals("-")) {
                userService.deleteUser(id);

                String[][] users = userService.getAllUsers();
                String userColumns[] = {"ID", "Vorname", "Nachname"};
                DefaultTableModel userTableModel = new DefaultTableModel(users, userColumns);
                userTable.setModel(userTableModel);

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

        this.dialog.add(this.userSelect);
        this.dialog.add(this.firstNameLabel);
        this.dialog.add(this.firstNameInput);
        this.dialog.add(this.lastNameLabel);
        this.dialog.add(this.lastNameInput);
        this.dialog.add(this.submit);
        this.dialog.add(this.delete);
    }
}
