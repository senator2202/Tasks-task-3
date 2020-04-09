package ArchivePackage.Forms;

import ArchivePackage.StudentProfile;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Date;

public class ModifyForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel jPanelName;
    private JLabel labelFirstName;
    private JPanel jPanelDateOfBirth;
    private JComboBox comboBoxDay;
    private JComboBox comboBoxMonth;
    private JComboBox comboBoxYear;
    private JLabel labelDateOfBirth;
    private JLabel labelStartYear;
    private JTextField textFieldAverageMark;
    private JLabel labelAverageMark;
    private JComboBox comboBoxStartYear;
    private JComboBox comboBoxStudents;
    private ArchiveForm parentForm;

    public ModifyForm() {

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
                StudentProfile sp=new StudentProfile();
                String firstName=((String)comboBoxStudents.getSelectedItem()).split(" ")[0];
                String lastName=((String)comboBoxStudents.getSelectedItem()).split(" ")[1];
                sp.setFirstName(firstName);
                sp.setLastName(lastName);
                sp.setAverageMark(Double.parseDouble(textFieldAverageMark.getText()));
                int day = comboBoxDay.getSelectedIndex()+1;
                int month = comboBoxMonth.getSelectedIndex();
                int year = 120 - comboBoxYear.getSelectedIndex();
                sp.setDateOfBirth(new Date(year, month, day));
                sp.setStartYear(2020 - comboBoxStartYear.getSelectedIndex());
                parentForm.getArchiveClient().addStudentProfile(sp);
                dispose();
            }
        });

    }

    public ModifyForm(ArchiveForm parentForm) {
        this();
        this.parentForm=parentForm;
        comboBoxStudents.addItem("");
        for (String s: parentForm.getArchiveClient().getStudentNames()) {
            comboBoxStudents.addItem(s);
        }

        comboBoxStudents.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName=((String)comboBoxStudents.getSelectedItem()).split(" ")[0];
                String lastName=((String)comboBoxStudents.getSelectedItem()).split(" ")[1];
                StudentProfile sp=parentForm.getArchiveClient().getStudentProfile(firstName,lastName);
                comboBoxDay.setSelectedIndex(sp.getDateOfBirth().toLocalDate().getDayOfMonth()-1);
                comboBoxDay.setEnabled(true);
                comboBoxMonth.setSelectedIndex(sp.getDateOfBirth().getMonth());
                comboBoxMonth.setEnabled(true);
                comboBoxYear.setSelectedIndex(120-sp.getDateOfBirth().getYear());
                comboBoxYear.setEnabled(true);
                textFieldAverageMark.setText(new Double (sp.getAverageMark()).toString());
                textFieldAverageMark.setEnabled(true);
                comboBoxStartYear.setSelectedIndex(2020-sp.getStartYear());
                comboBoxStartYear.setEnabled(true);

            }
        });
    }

    public static void main(String[] args) {
        ModifyForm dialog = new ModifyForm();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
