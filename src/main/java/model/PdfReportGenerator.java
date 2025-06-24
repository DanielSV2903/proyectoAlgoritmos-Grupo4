package model;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;

public class PdfReportGenerator {

    public static void generateStatsReport(String topAirports, String topRoutes,
                                           String topPassengers, String occupancy, String path) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            // Título
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Reporte Estadístico - Creta Airlines", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Secciones
            document.add(new Paragraph("Top 5 Aeropuertos con más vuelos salientes:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            document.add(new Paragraph(topAirports + "\n"));

            document.add(new Paragraph("Rutas más utilizadas:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            document.add(new Paragraph(topRoutes + "\n"));

            document.add(new Paragraph("Pasajeros con más vuelos realizados:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            document.add(new Paragraph(topPassengers + "\n"));

            document.add(new Paragraph("Porcentaje promedio de ocupación de vuelos:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            document.add(new Paragraph(occupancy + "\n"));

            document.close();
            System.out.println("PDF generado en: " + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
