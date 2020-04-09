package ArchivePackage.Forms;

import AuthentificationPackage.Authentification;
import AuthentificationPackage.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldUsername;
    private JLabel labelUsername;
    private JLabel labelPassword;
    private JPasswordField passwordField;
    ArchiveForm parentForm;
    LoginForm thisForm;

    public LoginForm() {
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
                User user=Authentification.getUser(textFieldUsername.getText(),passwordField.getText());
                if(user!=null) {
                    parentForm.getArchiveClient().setUser(user);
                    parentForm.enableOptions();
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(thisForm,"user doesn't exist","error",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    public LoginForm(ArchiveForm parentForm) {
        this();
        this.parentForm=parentForm;
    }

    public static void main(String[] args) {
        LoginForm dialog = new LoginForm();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
