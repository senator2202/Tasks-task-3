package ArchivePackage.Forms;

import AuthentificationPackage.Authentification;
import AuthentificationPackage.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldUsername;
    private JTextField textFieldEmail;
    private JLabel labelUsername;
    private JPasswordField passwordField;
    private JLabel labelPassword;
    private JLabel labelEmail;
    private ArchiveForm parentForm;
    private RegForm thisForm;

    public RegForm() {
        thisForm=this;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        buttonOK.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                User user=new User(textFieldUsername.getText(),
                        passwordField.getText(),textFieldEmail.getText());
                if (Authentification.registerUser(user)) {
                    parentForm.getArchiveClient().setUser(user);
                    parentForm.enableOptions();
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(thisForm,"user already exists","error",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    public RegForm(ArchiveForm parentForm) {
        this();
        this.parentForm=parentForm;
    }

    public static void main(String[] args) {
        RegForm dialog = new RegForm();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
