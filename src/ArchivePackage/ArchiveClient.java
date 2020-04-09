package ArchivePackage;

import ArchivePackage.Forms.ArchiveForm;
import AuthentificationPackage.User;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**Класс-клиент, обращается к серверу с запросами на чтение/изменение данных в архиве дел.
 * Имеет графияеское представления в виде главной формы ArchiveForm*/
public class ArchiveClient {
    /**Сокет для подключения к серверу*/
    private Socket clientSocket;

    /**Объект для обеспечения прав доступа к архиву*/
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArchiveClient() {

    }

    public static void main(String []args) {
        ArchiveClient ac=new ArchiveClient();
        ac.start();
    }

    /**Метод создает и отображает форму ArchiveForm*/
    public void start() {
        ArchiveForm af=new ArchiveForm(this);
        af.setPreferredSize(new Dimension(1000,300));
        af.pack();
        af.setVisible(true);
        System.exit(0);
    }

    /**Метод отправляет запрос на сервер для получения всех имен студентов в архиве в формате
     * списка строк (Строка = "Имя Фамилия"), принимает его и возвращает*/
    public ArrayList<String> getStudentNames() {
        try {
            clientSocket = new Socket("localhost", 3345);
            ObjectOutputStream oos=new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois=new ObjectInputStream(clientSocket.getInputStream());
            oos.writeObject("names");//запрос
            oos.flush();
            Thread.sleep(200);//ожидание, пока сервер обработает запрос и вышлет ответ
            ArrayList<String> in = (ArrayList<String>) ois.readObject();
            clientSocket.close();
            return in;
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            //e.printStackTrace();
        }
        return null;
    }

    /**Метод отправляет запрос на сервер для получения всех дел в архиве в формате текстовой строки,
     * принимает эту строку и возвращает ее*/
    public String getAllStudentProfiles() {
        try {
            clientSocket = new Socket("localhost", 3345);
            ObjectOutputStream oos=new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois=new ObjectInputStream(clientSocket.getInputStream());
            oos.writeObject("All");
            oos.flush();
            Thread.sleep(100);
            String in = ois.readUTF();
            clientSocket.close();
            return in;
        } catch (IOException | InterruptedException e) {
            //e.printStackTrace();
        }
        return null;
    }

    /**Метод передает объект StudentProfile на сервер для добавления его в архив*/
    public void addStudentProfile(StudentProfile studentProfile) {
       try {
            clientSocket = new Socket("localhost", 3345);
            ObjectOutputStream oos=new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois=new ObjectInputStream(clientSocket.getInputStream());
            oos.writeObject(studentProfile);
            oos.flush();
            Thread.sleep(100);
            String in = ois.readUTF();
            clientSocket.close();
        } catch (IOException | InterruptedException e) {
            //e.printStackTrace();
        }
    }

    /**Метод отправляет запрос на получение объекта StudentProfile, принимает его от сервера и возвращает*/
    public StudentProfile getStudentProfile(String firstName, String lastName ) {
        try {
            clientSocket = new Socket("localhost", 3345);
            ObjectOutputStream oos=new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois=new ObjectInputStream(clientSocket.getInputStream());
            oos.writeObject("modify "+firstName+" "+lastName);
            oos.flush();
            Thread.sleep(200);
            StudentProfile in =  (StudentProfile) ois.readObject();
            clientSocket.close();
            return in;
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            //e.printStackTrace();
        }
        return null;
    }

    /**Метод отправляет запрос на поиск дела на сервер, принимает его в строковом виде и возвращает*/
    public String searchStudentProfile(String firstName, String lastName) {
        try {
            clientSocket = new Socket("localhost", 3345);
            ObjectOutputStream oos=new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois=new ObjectInputStream(clientSocket.getInputStream());
            oos.writeObject("search "+firstName+" "+lastName);
            oos.flush();
            Thread.sleep(100);
            String in = ois.readUTF();
            clientSocket.close();
            return in;
        } catch (IOException | InterruptedException e) {
            //e.printStackTrace();
        }
        return null;
    }

}
