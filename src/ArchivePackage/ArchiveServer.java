package ArchivePackage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**Многопоточный сервер для обрбаботки запросов к базе данных Архива*/
public class ArchiveServer {
    /**список дел в архиве*/
    static ArrayList<StudentProfile> studentProfiles;

    /**Сервис для контроля количества потоков клиентов*/
    static ExecutorService executeIt = Executors.newFixedThreadPool(2);

    /**Нельзя создать напрямую объект класса*/
    private void ArchiveServer() {}

    public static void main(String [] args) {
        studentProfiles =new ArrayList<>();
        readFromFile();//чтение архива из xml Файла
        try (ServerSocket server = new ServerSocket(3345)) {//сокет на стороне сервера
             System.out.println("Server socket created, command console is ready to listen to server commands");
             while (!server.isClosed()) {
                Socket client = server.accept();//ожидаем подключение клиента, получаем клиентский сокет

                 /**новый поток для каждого подлючения обрабатывается в отдельном классе*/
                executeIt.execute(new MonoThreadClientHandler(client));
            }

            /** закрытие пула нитей после завершения работы всех нитей*/
            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Метод возвращает из архива список имен студентов в формате "Имя Фамилия"*/
    static ArrayList<String> getStudentNames() {
        ArrayList<String> names=new ArrayList<>();
        for (StudentProfile sp: studentProfiles) {
            names.add(sp.getFirstName()+" "+sp.getLastName());
        }
        return names;
    }

    /**Метод возвращает объект класса StudentProfile, если такие Имя и Фамилия хранятся в архиве*/
    static StudentProfile getStudentProfile(String firstName, String lastName ) {
        for (StudentProfile sp: studentProfiles) {
            if (sp.getFirstName().equals(firstName) && sp.getLastName().equals(lastName))
                return sp;
        }
        return null;
    }

    /**Метод добавляет профиль студента в архив, если студента с таким именем еще нет
     * (переопределен метод equals в классе StudentProfile)*/
    static void addProfile(StudentProfile studentProfile) {
        if (!exists(studentProfile)) {//если такого студента еще нет в архиве
            studentProfiles.add(studentProfile);//то добавляем его
        }
        else {//если студент уже есть в архиве
            for (StudentProfile sp: studentProfiles) {
                if (sp.equals(studentProfile)) {//обновляем его данные (все, кроме имени и фамилии)
                    sp.setDateOfBirth(studentProfile.getDateOfBirth());
                    sp.setStartYear(studentProfile.getStartYear());
                    sp.setAverageMark(studentProfile.getAverageMark());
                }
            }
        }
        writeToFile();//запись архива в файл
    }

    /**Проверка на существования профиля в архиве*/
    private static boolean exists(StudentProfile studentProfile) {
        for (StudentProfile sp: studentProfiles) {
            if (sp.equals(studentProfile))
                return true;
        }
        return false;
    }

    /**Метод возвращает профиль студента по имени и фамилии в текстовом виде*/
    static String searchProfile(String firstName, String lastName) {
        String res=new String();
        for (StudentProfile sp: studentProfiles) {
            if (sp.getFirstName().equals(firstName) && sp.getLastName().equals(lastName))
                res=res+sp+"\n";
        }
        return res;
    }

    /**Метод возвращает все профили студентов в архиве в текстовом виде*/
    static String getStudentProfiles() {
        String res=new String();
        for (StudentProfile sp: studentProfiles) {
            res=res+sp+"\n";
        }
        return res;
    }

    /**Метод читает архив дел из xml файла в список studentProfiles*/
    static void readFromFile() {
        String filepath = "StudentProfiles.xml";
        File xmlFile = new File(filepath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();
            // получаем узлы с именем Profile
            // теперь XML полностью загружен в память
            // в виде объекта Document
            NodeList nodeList = document.getElementsByTagName("Profile");

            // создадим из него список объектов Language
            //List<StudentProfile> profList = new ArrayList<StudentProfile>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                studentProfiles.add(getStudentProfile(nodeList.item(i)));
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    /**Метод достает из узла в xml документе информацию возвращает объект StudentProfile*/
    static StudentProfile getStudentProfile(Node node) {
        StudentProfile profile = new StudentProfile();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            profile.setFirstName(getTagValue("firstName", element));
            profile.setLastName(getTagValue("lastName", element));
            String birth=getTagValue("dateOfBirth",element);
            String []temp=birth.split("-");
            int year=Integer.parseInt(temp[0])-1900;
            int month=Integer.parseInt(temp[1])-1;
            int day=Integer.parseInt(temp[2]);
            profile.setDateOfBirth(new Date(year,month,day));
            profile.setStartYear(Integer.parseInt(getTagValue("startYear",element)));
            profile.setAverageMark(Double.parseDouble(getTagValue("averageMark",element)));

        }
        return profile;
    }

    /**Метод вынисмает значение элемента(узла) в xml документе*/
    static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    /**Метод записывает архив дел из списка studentProfiles в xml Файл*/
    static void writeToFile() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element rootElement =
                    doc.createElementNS("","Profiles");
            doc.appendChild(rootElement);
            for (StudentProfile sp: studentProfiles) {
                rootElement.appendChild(getProfileNode(doc,sp));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult console = new StreamResult(System.out);
            StreamResult file = new StreamResult(new File("StudentProfiles.xml"));
            transformer.transform(source, file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**Метод создает и возвращает узел Profile для xml документа*/
    static Node getProfileNode(Document doc, StudentProfile studentProfile) {
        Element profile = doc.createElement("Profile");
        profile.appendChild(getProfileNodeElements(doc,profile,"firstName", studentProfile.getFirstName()));
        profile.appendChild(getProfileNodeElements(doc,profile,"lastName", studentProfile.getLastName()));
        profile.appendChild(getProfileNodeElements(doc,profile,"dateOfBirth", studentProfile.getDateOfBirth().toString()));
        profile.appendChild(getProfileNodeElements(doc,profile,"startYear",new Integer(studentProfile.getStartYear()).toString()));
        profile.appendChild(getProfileNodeElements(doc,profile,"averageMark",new Double(studentProfile.getAverageMark()).toString()));
        return profile;
    }

    /**Метод создает и возвращет поля и значения для узла Profile в xml документе*/
    static   Node getProfileNodeElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
}
