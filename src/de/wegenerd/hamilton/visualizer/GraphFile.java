package de.wegenerd.hamilton.visualizer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;

public class GraphFile {
    private StringProperty graphName;
    private File file;
    private static HashMap<String, BigInteger> solutions = new HashMap<>();

    static {
        try {
            URL resource = GraphFile.class.getResource("./graphs/solutions.txt");
            File file = new File(resource.toURI());
            Files.lines(file.toPath())
                    .filter(s -> s.matches(".* \\d+"))
                    .forEach(s -> {
                        String[] split = s.split(" ");
                        solutions.put(split[0], new BigInteger(split[1]));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    GraphFile(File file) {
        graphName = new SimpleStringProperty(this, "algorithmName", file.getName());
        this.file = file;
    }

    public boolean checkSolution(BigInteger result) {
        return solutions.get(file.getName()).equals(result);
    }

    public File getFile() {
        return file;
    }

    public StringProperty graphNameProperty() {
        return graphName;
    }

    public BigInteger getSolution() {
        return solutions.get(file.getName());
    }
}
