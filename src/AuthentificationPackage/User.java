package AuthentificationPackage;

import java.io.*;

/**Класс User хранит персональные данные пользователя для его авторизации в системе*/
public class User implements Serializable {
   private String name;
   private String password;
   private String email;
   boolean isAdmin;

   public User(String name, String password, String email) {
      this.name=name;
      this.password=password;
      this.email=email;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public boolean isAdmin() {
      return isAdmin;
   }

   /**Перегруженный метод equals() сравнивает только имена пользователей,
    * чтобы не допустить 2 одинаковых в системе*/
   @Override
   public boolean equals (Object o) {
      if (o==null)
         return false;
      User u=(User)o;
      return this.name.equals(u.name);
   }

   @Override
   public String toString() {
      return "name: "+name+"; password: "+password;
   }
}
