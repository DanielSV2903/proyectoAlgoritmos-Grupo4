package model.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Passenger;
import model.tda.SinglyLinkedList;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PassengerSinglyLinkedListDeserializer extends JsonDeserializer<SinglyLinkedList> {

    @Override
    public SinglyLinkedList deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        List<?> list = mapper.readValue(parser, List.class);

        SinglyLinkedList result = new SinglyLinkedList();

        for (Object obj : list) {
            // Asegurarse de convertir bien el Map al objeto Passenger
            if (obj instanceof Map) {
                Passenger passenger = mapper.convertValue(obj, Passenger.class);
                result.add(passenger);
            }
        }

        return result;
    }
}