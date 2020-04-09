package ArchivePackage.Forms;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldFirstName;
    private JTextField textFieldLastName;
    private JLabel labelFirstName;
    private JLabel labelLastName;
    private SearchForm thisForm;
    private ArchiveForm parentForm;

    public SearchForm() {
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
                String res=parentForm.getArchiveClient().searchStudentProfile(textFieldFirstName.getText(),
                        textFieldLastName.getText());
                parentForm.addSearchResult(res);
                dispose();
            }
        });
    }

    public SearchForm(ArchiveForm parentForm) {
        this();
        this.parentForm=parentForm;
    }

    public static void main(String[] args) {
        SearchForm dialog = new SearchForm();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
