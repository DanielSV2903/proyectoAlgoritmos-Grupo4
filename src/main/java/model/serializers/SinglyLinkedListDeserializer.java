package model.serializers;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import model.tda.SinglyLinkedList;

import java.io.IOException;
import java.util.List;

public class SinglyLinkedListDeserializer extends JsonDeserializer<SinglyLinkedList> {
    @Override
    public SinglyLinkedList deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        List<?> list = p.readValueAs(List.class);
        SinglyLinkedList sll = new SinglyLinkedList();
        for (Object item : list) {
            sll.add(item);
        }
        return sll;
    }
}