package model.datamanagment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Flight;
import model.Passenger;
import model.Ticket;
import model.serializers.SinglyLinkedListDeserializer;
import model.tda.AVL;
import model.tda.ListException;
import model.tda.SinglyLinkedList;
import model.tda.TreeException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TicketManager {
    private List<Ticket> tickets;
    private final String filePath="src/main/java/data/tickets.json";
    private FlightManager flightManager;
    public TicketManager() {
        tickets=new ArrayList<>();
        flightManager=new FlightManager();
        loadPassengers();
    }
    public void loadPassengers() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            SimpleModule module = new SimpleModule();
            module.addDeserializer(SinglyLinkedList.class, new SinglyLinkedListDeserializer());
            mapper.registerModule(module);
            File file = new File(filePath);
            if (!file.exists()) return;
            List<Ticket> ticketList = mapper.readValue(file, new TypeReference<>() {});
            for (Ticket ticket : ticketList) {
                tickets.add(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addTicket(Ticket ticket) throws ListException {
        if (!tickets.contains(ticket)){
            ticket.setId(tickets.size()+1);
            tickets.add(ticket);
            Flight flight= flightManager.getFlight(ticket.getFlID());
            flight.addPassenger(ticket.getPassenger());
            flight.setOccupancy(flight.getPassengers().size());
            flightManager.updateFlight(flight);
        saveTickets();
        }
    }
    public void setTicket(Ticket ticket){
        tickets.remove(ticket);
        saveTickets();
    }
    public void updateTicket(Ticket ticket){
        if (tickets.contains(ticket)) {
        tickets.remove(ticket);
        tickets.add(ticket);
        saveTickets();
        }
    }


    private void saveTickets() {
        File file = new File(filePath);
        if(file.exists())
            file.delete();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), tickets);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
