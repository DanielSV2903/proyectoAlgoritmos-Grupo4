package model.datamanagment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import model.tda.CircularLinkedList;

import java.io.File;
import java.util.List;

public class UserManager {
    private final CircularLinkedList users = new CircularLinkedList();
    private final String filePath = "C:\\Users\\DanielSV\\Documents\\2025\\Proyecto Algoritmos y Estructuras de Datos\\cretaAirlines\\src\\main\\java\\data\\users.json";

    public void loadUsers() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(filePath);
            if (!file.exists()) return;
            List<User> userList = mapper.readValue(file, new TypeReference<>() {});
            for (User u : userList) {
                users.add(u);
                System.out.println("Usuario cargado desde JSON:");
                System.out.println("  Nombre: " + u.getName());
                System.out.println("  Email: " + u.getEmail());
                System.out.println("  Password HASH: " + u.getPassword());
                System.out.println("  Rol: " + u.getRole());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User validateLogin(String email, String passwordPlain) {
        String hashed = util.Utility.hashPassword(passwordPlain);
        try {
            System.out.println("---- VALIDACIÓN DE LOGIN ----");
            System.out.println("Email ingresado: " + email);
            System.out.println("Password ingresado: " + passwordPlain);
            System.out.println("Password hasheado: " + hashed);
            for (int i = 1; i <= users.size(); i++) {
                User user = (User) users.getNode(i).data;
                System.out.println("Comparando con:");
                System.out.println("  Email guardado: " + user.getEmail());
                System.out.println("  Hash guardado: " + user.getPassword());

                if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(hashed)) {
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void addUser(User user) {
        users.add(user);
    }

    public void saveUsers() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<User> list = users.toTypedList();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CircularLinkedList getUsers() {
        return users;
    }
}
