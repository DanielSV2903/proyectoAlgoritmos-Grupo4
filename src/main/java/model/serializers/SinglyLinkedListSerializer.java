package model.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import model.tda.SinglyLinkedList;

import java.io.IOException;

public class SinglyLinkedListSerializer extends JsonSerializer<SinglyLinkedList> {

    @Override
    public void serialize(SinglyLinkedList value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        try {
            gen.writeObject(value.toTypedList()); // Serializa la lista como un ArrayList
        } catch (Exception e) {
            gen.writeNull(); // En caso de error, escribe null
        }
    }
}