package util;

import javafx.scene.control.Alert;
import model.Airport;
import model.Flight;
import model.Passenger;
import model.tda.*;
import model.tda.graph.Vertex;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Random;

public class Utility {

    //static init
    static {
    }

    public static String format(double value){
        return new DecimalFormat("###,###,###.##").format(value);
    }
    public static String $format(double value){
        return new DecimalFormat("$###,###,###.##").format(value);
    }

    public static void fill(int[] a, int bound) {
        for (int i = 0; i < a.length; i++) {
            a[i] = new Random().nextInt(bound);
        }
    }

    public static int random(int bound) {
        return new Random().nextInt(bound);
    }
    public static int random(int bound1, int bound2) {
        int min = Math.min(bound1, bound2);
        int max = Math.max(bound1, bound2);
        return new Random().nextInt(max - min + 1) + min;
    }

    public static int compare(Object a, Object b) {
        switch (instanceOf(a, b)){
            case "Integer":
                Integer int1 = (Integer)a; Integer int2 = (Integer)b;
                return int1 < int2 ? -1 : int1 > int2 ? 1 : 0; //0 == equal
            case "String":
                String st1 = (String)a; String st2 = (String)b;
                return st1.compareTo(st2)<0 ? -1 : st1.compareTo(st2) > 0 ? 1 : 0;
            case "Character":
                Character c1 = (Character)a; Character c2 = (Character)b;
                return c1.compareTo(c2)<0 ? -1 : c1.compareTo(c2)>0 ? 1 : 0;
            case "Flight":
                Flight fl1 = (Flight)a; Flight fl2 = (Flight)b;
                return compare(fl1.getFlightID(), fl2.getFlightID());
            case "Passenger":
                Passenger p1 = (Passenger)a; Passenger p2 = (Passenger)b;
                return compare(p1.getId(), p2.getId());
            case "Airport":
                Airport a1 = (Airport)a; Airport a2 = (Airport)b;
                return compare(a1.getCode(), a2.getCode());
            case "Vertex":
                Vertex v1 = (Vertex)a; Vertex v2 = (Vertex)b;
                return compare(v1.data, v2.data);
        }
        return 2; //Unknown
    }

    private static String instanceOf(Object a, Object b) {
        if(a instanceof Integer && b instanceof Integer) return "Integer";
        if(a instanceof String && b instanceof String) return "String";
        if(a instanceof Character && b instanceof Character) return "Character";
        if (a instanceof Flight && b instanceof Flight) return "Flight";
        if (a instanceof Passenger && b instanceof Passenger) return "Passenger";
        if (a instanceof Airport && b instanceof Airport) return "Airport";
        if (a instanceof Vertex && b instanceof Vertex ) return "Vertex";
        return "Unknown";
    }

    public static int maxArray(int[] a) {
        int max = a[0]; //first element
        for (int i = 1; i < a.length; i++) {
            if(a[i]>max){
                max=a[i];
            }
        }
        return max;
    }

    public static int[] getIntegerArray(int n) {
        int[] newArray = new int[n];
        for (int i = 0; i < n; i++) {
            newArray[i] = random(9999);
        }
        return newArray;
    }


    public static int[] copyArray(int[] a) {
        int n = a.length;
        int[] newArray = new int[n];
        for (int i = 0; i < n; i++) {
            newArray[i] = a[i];
        }
        return newArray;
    }

    public static String show(int[] a, int n) {
        String result="";
        for (int i = 0; i < n; i++) {
            result+=a[i]+" ";
        }
        return result;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al hashear la contraseña");
        }
    }

    public static void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Error de validación");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public static int contarPaisesVisitados(SinglyLinkedList sLL) throws ListException, StackException {
        int count;
        LinkedStack stack = new LinkedStack();
        //lleno un stack auxiliar con los paises visitados;
            for (int i=1;i<=sLL.size();i++){
            Flight flight= (Flight) sLL.getNode(i).data;
            String country = flight.getOrigin().getCountry();
            stack.push(country);
        }
        LinkedQueue queue = new LinkedQueue();//cola aux
            while (!stack.isEmpty()){//Remuevo los elemento de la pila y solo encolo paises unicos
                String country = (String) stack.pop();
                if (queue.isEmpty())
                    queue.enQueue(country);
                else if (!queue.contains(country)){
                    queue.enQueue(country);
                }
            }
            count = queue.size();
        return count;
    }
}
