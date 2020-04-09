package ArchivePackage;

import java.io.Serializable;
import java.sql.Date;

/**Класс, описывающий дело студента, которое хранится в архиве*/
public class StudentProfile implements Serializable {
    private String firstName;
    private String lastName;
    private java.sql.Date dateOfBirth;
    private int startYear;
    private double averageMark;

    @Override
    public String toString() {
        return "first name: "+firstName+"; last name: "+lastName+
                "; date of birth: "+dateOfBirth+"; start of education: "+startYear+
                "; average mark: "+averageMark;
    }

    @Override
    public boolean equals(Object o) {
        if (o==null)
            return false;
        StudentProfile sp=(StudentProfile)o;
        if (firstName.equals(sp.firstName) && lastName.equals(sp.lastName))
            return true;
        return false;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public double getAverageMark() {
        return averageMark;
    }

    public void setAverageMark(double averageMark) {
        this.averageMark = averageMark;
    }

    public static class StudentProfileBuilder{
        StudentProfile studentProfile;

        public StudentProfileBuilder() {
            studentProfile =new StudentProfile();
        }

        public StudentProfileBuilder withFirstName(String firstName) {
            studentProfile.firstName=firstName;
            return this;
        }

        public StudentProfileBuilder withLastName(String lastName) {
            studentProfile.lastName=lastName;
            return this;
        }

        public StudentProfileBuilder withDateOfBirth(Date dateOfBirth) {
            studentProfile.dateOfBirth=dateOfBirth;
            return this;
        }

        public StudentProfileBuilder withStartYear(int startYear) {
            studentProfile.startYear=startYear;
            return this;
        }

        public StudentProfileBuilder withEverageMark(double averageMark) {
            studentProfile.averageMark=averageMark;
            return this;
        }

        public StudentProfile Build() {
            return studentProfile;
        }
    }
}
