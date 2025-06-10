package model.datamanagment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

public abstract class GenericManager<T> {
    protected final String filePath;
    protected final ObjectMapper mapper = new ObjectMapper();

    public GenericManager(String filePath) {
        this.filePath = filePath;
    }

    // Implementado por cada subclase según la estructura interna (lista, árbol, etc.)
    protected abstract void addToCollection(T item);
    protected abstract List<T> getCollectionAsList();
    protected abstract void loadFromList(List<T> list);

    public void load() {
        try {
            File file = new File(filePath);
            if (!file.exists()) return;
            List<T> list = mapper.readValue(file, new TypeReference<>() {});
            loadFromList(list);
            System.out.println("✔ Datos cargados desde " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            List<T> list = getCollectionAsList();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), list);
            System.out.println("✔ Datos guardados en " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(T item) {
        addToCollection(item);
    }

    public abstract List<T> getAll(); // Devuelve una lista para uso general
}
