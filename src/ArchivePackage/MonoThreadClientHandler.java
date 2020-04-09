package ArchivePackage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**однопоточный класс для работы с 1 клиентом на сервере ArchiveServer*/
public class MonoThreadClientHandler implements Runnable {
    private Socket clientDialog;

    public MonoThreadClientHandler(Socket client) {
        clientDialog = client;
    }

    @Override
    public void run() {

        try {
            ObjectOutputStream out=new ObjectOutputStream(clientDialog.getOutputStream());
            ObjectInputStream in=new ObjectInputStream(clientDialog.getInputStream());

            while (!clientDialog.isClosed()) {
                Object entry= in.readObject();
                if(entry instanceof String) {
                    if (entry.equals("All")) {
                        String profiles = ArchiveServer.getStudentProfiles();
                        out.writeUTF(profiles);
                        out.flush();
                        System.out.println("Server returned all profiles");
                    }
                    if (entry.equals("names")) {
                        ArrayList<String> names=ArchiveServer.getStudentNames();
                        out.writeObject(names);
                        out.flush();
                        System.out.println("Server returned student names");
                    }
                    if (!entry.equals("names") && !entry.equals("all")) {
                        String []res=((String)entry).split(" ");
                        if (res[0].equals("search")) {
                            String firstName = res[1];
                            String lastName = res[2];
                            out.writeUTF(ArchiveServer.searchProfile(firstName, lastName));
                            out.flush();
                            System.out.println("Server replied on search result");
                        }
                        if (res[0].equals("modify")) {
                            String firstName = res[1];
                            String lastName = res[2];
                            out.writeObject(ArchiveServer.getStudentProfile(res[1],res[2]));
                            out.flush();
                            System.out.println("Server replied on modify result");
                        }
                    }
                }
                if (entry instanceof StudentProfile) {//addProfile
                    ArchiveServer.addProfile((StudentProfile)entry);
                    out.writeUTF("done");
                    out.flush();
                    System.out.println("Server added profile");
                }
            }

            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");

            // закрываем сначала каналы сокета !
            in.close();
            out.close();

            // потом закрываем сокет общения с клиентом в нити моносервера
            clientDialog.close();

            System.out.println("Closing connections & channels - DONE.");
        } catch (IOException  | ClassNotFoundException e) {
            //e.printStackTrace();
        }
    }
}
