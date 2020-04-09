package ArchivePackage.Forms;

import ArchivePackage.StudentProfile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

public class AddForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldFirstName;
    private JTextField textFieldLastName;
    private JLabel labelFirstName;
    private JLabel labelLastName;
    private JComboBox comboBoxDay;
    private JComboBox comboBoxMonth;
    private JComboBox comboBoxYear;
    private JLabel labelDateOfBirth;
    private JPanel jPanelDateOfBirth;
    private JPanel jPanelName;
    private JTextField textFieldAverageMark;
    private JLabel labelStartYear;
    private JLabel labelAverageMark;
    private JComboBox comboBoxStartYear;
    private AddForm thisForm;
    private ArchiveForm parentForm;

    public AddForm() {
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
                if (textFieldFirstName.getText().length()!=0 && textFieldLastName.getText().length()!=0
                    && textFieldAverageMark.getText().length()!=0) {
                    StudentProfile sp=new StudentProfile();
                    sp.setFirstName(textFieldFirstName.getText());
                    sp.setLastName(textFieldLastName.getText());
                    sp.setAverageMark(Double.parseDouble(textFieldAverageMark.getText()));
                    int day = comboBoxDay.getSelectedIndex();
                    int month = comboBoxMonth.getSelectedIndex()-1;
                    int year = 120 - comboBoxYear.getSelectedIndex()+1;
                    sp.setDateOfBirth(new Date(year, month, day));
                    sp.setStartYear(2020 - comboBoxStartYear.getSelectedIndex()+1);
                    parentForm.getArchiveClient().addStudentProfile(sp);
                    dispose();
                }
                else
                    JOptionPane.showMessageDialog(thisForm, "some fields are empty!!", "error", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    public AddForm(ArchiveForm parentForm) {
        this();
        this.parentForm=parentForm;
    }

    public static void main(String[] args) {
        AddForm dialog = new AddForm();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
