package AuthentificationPackage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**Класс, предоставляющий статик методы авторизации в домашней библиотеке*/
public class Authentification {

    /**Метод регистрирует пользователя в системе, записывая его данные в файл*/
    public static boolean registerUser(String name, String password, String email) {
        ArrayList<User> users=readFromFile();
        if(userExists(name)) {
            System.out.println("Пользователь с таким именем уже существует!");
            return false;
        }
        if (users==null) {
            users= new ArrayList<>();
        }
        User user = new User(name, encrypt(password),email);
        isAdmin(user);
        users.add(user);
        writeToFile(users);
        return true;
    }

    /**Метод регистрирует пользователя в системе, записывая его данные в файл*/
    public static boolean registerUser(User user) {
        ArrayList<User> users=readFromFile();
        if(userExists(user.getName())) {
            System.out.println("Пользователь с таким именем уже существует!");
            return false;
        }
        if (users==null) {
            users= new ArrayList<>();
        }
        encrypt(user);
        isAdmin(user);
        users.add(user);
        writeToFile(users);
        return true;
    }

    /**Возвращает объект класса User, если такие имя пользователя и пароль хранятся в базе данных.
     * В противном случае возвращает null*/
    public static User getUser(String name, String password) {
        ArrayList<User> users=readFromFile();
        if (users==null)
            return null;
        for (User u: users) {
            if (u.getName().equals(name) && decrypt(u.getPassword()).equals(password)) {
                User user=new User(name, password,u.getEmail());
                isAdmin(user);
                return user;
            }
        }
        return null;
    }

    /**Возвращает емэйл-адрес Админа*/
    public static String getAdminEmail() {
        ArrayList<User> users=readFromFile();
        return users.get(0).getEmail();
    }

    /**Возврашает емэйл-адреса всех зарегистрированных пользовтелей библиотеки*/
    public static ArrayList<String> getAllEmails() {
        ArrayList<User> users=readFromFile();
        ArrayList<String> emails=new ArrayList<>();
        for (User user: users) {
            if (!user.isAdmin)
                emails.add(user.getEmail());
        }
        return emails;
    }

    public static String getAllUsers() {
        String res=new String();
        ArrayList<User> users=readFromFile();
        for (User u: users) {
            decrypt(u);
            res=res+u+"\n";
        }
        return res;
    }

    /**Проверка на то, существует ли пользователь с таким именем в базе данных*/
    private static boolean userExists(String name) {
        ArrayList<User> users=readFromFile();
        if (name==null || name.length()==0 || users==null)
            return false;
        for (User u: users) {
            if(u.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**Загрузка данных о пользователях из файла*/
    private static ArrayList<User> readFromFile() {
        ArrayList<User> users=null;
        try {
            FileInputStream fis = new FileInputStream("Users.txt");
            ObjectInputStream oin = new ObjectInputStream(fis);
            users=(ArrayList<User>)oin.readObject();
        } catch (Exception e) {
        }
        return users;
    }

    /**Запись данных пользователей в файл*/
    private static void writeToFile(ArrayList<User> users) {
        try {
            FileOutputStream fos = new FileOutputStream("Users.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
            oos.flush();
            oos.close();
        } catch (Exception e) {
        }
    }

    /**Фукнция шифрования паролей пользователей, чтобы он не хранился в открытом виде в файле*/
    private static String encrypt(String password) {
        char []arr=password.toCharArray();
        String res=new String();
        for (int i=0; i<arr.length; i++) {
            arr[i]=(char)(arr[i]+50);
            res+=arr[i];
        }
        return res;
    }

    public static void encrypt(User user) {
        char []arr=user.getPassword().toCharArray();
        String res=new String();
        for (int i=0; i<arr.length; i++) {
            arr[i]=(char)(arr[i]+50);
            res+=arr[i];
        }
        user.setPassword(res);
    }

    /**Функция дешифровки паролей пользователей при чтения их из файла*/
    private static String decrypt(String password) {
        char []arr=password.toCharArray();
        String res=new String();
        for (int i=0; i<arr.length; i++) {
            arr[i]=(char)(arr[i]-50);
            res+=arr[i];
        }
        return res;
    }

    private static void decrypt(User user) {
        char []arr=user.getPassword().toCharArray();
        String res=new String();
        for (int i=0; i<arr.length; i++) {
            arr[i]=(char)(arr[i]-50);
            res+=arr[i];
        }
        user.setPassword(res);
    }

    /**Проверка, является ли пользователь админом*/
    private static void isAdmin(User user) {
        ArrayList<User> users=readFromFile();
        if (users==null) {
            user.isAdmin=true;
            return;
        }
        if (users.size()==0 && user.getName().equals("Admin")) {
            user.isAdmin = true;
        }
        String adminName=users.get(0).getName();
        String adminPassword=decrypt(users.get(0).getPassword());

        if(adminName.equals(user.getName()) && adminPassword.equals(user.getPassword())) {
            user.isAdmin=true;
        }
    }
}
