package simulation;

import model.Airport;
import model.Flight;
import model.Route;
import model.datamanagment.AirportManager;
import model.datamanagment.FlightManager;
import simulation.NetworkStats;
import simulation.RouteAnalyzer;
import simulation.NetworkVisualizer;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.tda.ListException;

public class AirlineNetworkSimulation {
    private final AirportManager airportManager;
    private final FlightManager flightManager;
    private final NetworkStats stats;
    private final RouteAnalyzer routeAnalyzer;
    private final NetworkVisualizer visualizer;
    private final Scanner scanner;
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public AirlineNetworkSimulation() {
        this.airportManager = new AirportManager();
        this.flightManager = new FlightManager();
        this.stats = new NetworkStats();
        this.routeAnalyzer = new RouteAnalyzer(flightManager.getFlights());
        this.visualizer = new NetworkVisualizer();
        this.scanner = new Scanner(System.in);
    }

    public void runSimulation() {
        boolean running = true;

        while (running) {
            mostrarMenu();
            int option = obtenerOpcion();

            try {
                switch (option) {
                    case 1:
                        agregarAeropuerto();
                        break;
                    case 2:
                        agregarVuelo();
                        break;
                    case 3:
                        mostrarAeropuertos();
                        break;
                    case 4:
                        mostrarVuelos();
                        break;
                    case 5:
                        mostrarEstadisticas();
                        break;
                    case 6:
                        buscarRutas();
                        break;
                    case 7:
                        running = false;
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private void mostrarMenu() {
        System.out.println("\n=== Simulación de Red Aérea ===");
        System.out.println("1. Agregar Aeropuerto");
        System.out.println("2. Agregar Vuelo");
        System.out.println("3. Mostrar Aeropuertos");
        System.out.println("4. Mostrar Vuelos");
        System.out.println("5. Mostrar Estadísticas");
        System.out.println("6. Buscar Rutas");
        System.out.println("7. Salir");
    }

    private int obtenerOpcion() {
        System.out.print("Seleccione una opción: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Por favor, ingrese un número válido");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private void agregarAeropuerto() throws ListException {
        scanner.nextLine(); // Limpiar buffer

        System.out.print("Ingrese código del aeropuerto: ");
        String codigo = scanner.nextLine();

        System.out.print("Ingrese nombre del aeropuerto: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese ciudad del aeropuerto: ");
        String ciudad = scanner.nextLine();

        System.out.print("Ingrese país del aeropuerto: ");
        String pais = scanner.nextLine();

        Airport airport = new Airport(codigo, nombre, ciudad, pais);
        airportManager.addAirports(airport);
        System.out.println("Aeropuerto agregado exitosamente");
    }

    private void agregarVuelo() throws ListException {
        scanner.nextLine(); // Limpiar buffer

        System.out.print("Ingrese ID del vuelo: ");
        int flightId = Integer.parseInt(scanner.nextLine());

        mostrarAeropuertos();
        System.out.print("Ingrese código del aeropuerto de origen: ");
        String codigoOrigen = scanner.nextLine();

        System.out.print("Ingrese código del aeropuerto de destino: ");
        String codigoDestino = scanner.nextLine();

        System.out.print("Ingrese fecha y hora de salida (yyyy-MM-dd HH:mm): ");
        String fechaHora = scanner.nextLine();
        LocalDateTime departureTime = LocalDateTime.parse(fechaHora, DATE_FORMAT);

        System.out.print("Ingrese capacidad del vuelo: ");
        int capacidad = Integer.parseInt(scanner.nextLine());

        Airport origen = buscarAeropuerto(codigoOrigen);
        Airport destino = buscarAeropuerto(codigoDestino);

        if (origen != null && destino != null) {
            Flight flight = new Flight(flightId, origen, destino, departureTime, capacidad);
            flightManager.addFlight(flight);
            System.out.println("Vuelo agregado exitosamente");
        } else {
            System.out.println("Error: Aeropuerto no encontrado");
        }
    }

    private Airport buscarAeropuerto(String codigo) throws ListException {
        for (int i = 1; i <= airportManager.getAirports().size(); i++) {
            Airport airport = (Airport) airportManager.getAirports().getNode(i).data;
            if (airport.getCode().equals(codigo)) {
                return airport;
            }
        }
        return null;
    }

    private void mostrarAeropuertos() throws ListException {
        System.out.println("\n=== Aeropuertos Registrados ===");
        for (int i = 1; i <= airportManager.getAirports().size(); i++) {
            Airport airport = (Airport) airportManager.getAirports().getNode(i).data;
            System.out.printf("Código: %s, Nombre: %s, Ciudad: %s, País: %s%n",
                    airport.getCode(),
                    airport.getName(),
                    airport.getCity(),
                    airport.getCountry());
        }
    }

    private void mostrarVuelos() throws ListException {
        System.out.println("\n=== Vuelos Registrados ===");
        for (int i = 1; i <= flightManager.getFlights().size(); i++) {
            Flight flight = (Flight) flightManager.getFlights().getNode(i).data;
            System.out.printf("ID: %d, Origen: %s, Destino: %s, Salida: %s, Capacidad: %d/%d%n",
                    flight.getFlightID(),
                    flight.getOrigin().getCity(),
                    flight.getDestination().getCity(),
                    flight.getDepartureTime().format(DATE_FORMAT),
                    flight.getOccupancy(),
                    flight.getCapacity());
        }
    }

    private void mostrarEstadisticas() throws ListException {
        stats.updateStats(flightManager.getFlights(), airportManager.getAirports());
        System.out.println(stats.toString());
    }

    private void buscarRutas() throws ListException {
        scanner.nextLine(); // Limpiar buffer

        System.out.print("Ingrese código de aeropuerto de origen: ");
        String origen = scanner.nextLine();

        System.out.print("Ingrese código de aeropuerto de destino: ");
        String destino = scanner.nextLine();

        Airport aeropuertoOrigen = buscarAeropuerto(origen);
        Airport aeropuertoDestino = buscarAeropuerto(destino);

        if (aeropuertoOrigen != null && aeropuertoDestino != null) {
            Route ruta = routeAnalyzer.findBestRoute(aeropuertoOrigen, aeropuertoDestino);
            if (ruta !=null) {
                System.out.println("Ruta encontrada:"+ruta);
            } else {
                System.out.println("No se encontraron rutas disponibles");
            }
        } else {
            System.out.println("Error: Aeropuerto no encontrado");
        }
    }


    public static void main(String[] args) {
        AirlineNetworkSimulation simulation = new AirlineNetworkSimulation();
        simulation.runSimulation();
    }
}