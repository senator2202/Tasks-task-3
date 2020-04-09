package ArchivePackage.Forms;

import ArchivePackage.ArchiveClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ArchiveForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonGetProfiles;
    private JButton buttonSearch;
    private JButton buttonAddProfile;
    private JButton buttonLogin;
    private JTextPane textPaneProfiles;
    private JButton buttonRegister;
    private JButton buttonModify;
    private ArchiveClient archiveClient;
    private ArchiveForm thisForm;

    public ArchiveForm(ArchiveClient archiveClient) {
        this();
        this.archiveClient=archiveClient;
    }

    private ArchiveForm() {
        archiveClient=new ArchiveClient();
        thisForm=this;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonGetProfiles);

        buttonGetProfiles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onGetProfiles();
            }
        });

        buttonSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSearch();
            }
        });
        buttonAddProfile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {onAddProfile();}
        });
        buttonRegister.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                RegForm auth=new RegForm(thisForm);
                auth.pack();
                auth.setVisible(true);
            }
        });
        buttonLogin.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttonLogin.getText().equals("Login")) {
                    LoginForm login = new LoginForm(thisForm);
                    login.pack();
                    login.setVisible(true);
                }
                else {
                    archiveClient.setUser(null);
                    disableOptions();
                }
            }
        });
        buttonModify.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ModifyForm mf=new ModifyForm(thisForm);
                mf.pack();
                mf.setVisible(true);
            }
        });
    }

    void enableOptions() {
        buttonGetProfiles.setEnabled(true);
        buttonSearch.setEnabled(true);
        buttonRegister.setEnabled(false);
        buttonLogin.setText("Logout");

        if (archiveClient.getUser().isAdmin()) {
            buttonAddProfile.setEnabled(true);
            buttonModify.setEnabled(true);
        }
    }

    void addSearchResult(String res) {
        textPaneProfiles.setText(res);
    }

    private void disableOptions() {
        buttonGetProfiles.setEnabled(false);
        buttonSearch.setEnabled(false);
        buttonRegister.setEnabled(true);
        buttonAddProfile.setEnabled(false);
        buttonModify.setEnabled(false);
        buttonLogin.setText("Login");
    }


    private void onAddProfile() {
        AddForm add=new AddForm(this);
        add.pack();
        add.setVisible(true);
    }

    private void onGetProfiles() {
        textPaneProfiles.setText(archiveClient.getAllStudentProfiles());
    }

    private void onSearch() {
        SearchForm sf=new SearchForm(this);
        sf.pack();
        sf.setVisible(true);
    }

    public static void main(String[] args) {
        ArchiveForm dialog = new ArchiveForm();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public ArchiveClient getArchiveClient() {
        return archiveClient;
    }
}
