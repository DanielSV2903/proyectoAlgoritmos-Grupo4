package model.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import model.Flight;
import model.tda.ListException;
import model.tda.SinglyLinkedList;

import java.io.IOException;

public class FlightSinglyLinkedListDeserializer extends JsonDeserializer<SinglyLinkedList> {

    @Override
    public SinglyLinkedList deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        SinglyLinkedList flightList = new SinglyLinkedList();

        // Esperamos un START_ARRAY
        if (p.getCurrentToken() != JsonToken.START_ARRAY) {
            throw ctxt.mappingException("Expected START_ARRAY token");
        }

        while (p.nextToken() != JsonToken.END_ARRAY) {
            // Deserializamos cada elemento como Flight
            Flight flight = p.readValueAs(Flight.class);
            flightList.add(flight);
        }

        return flightList;
    }
}