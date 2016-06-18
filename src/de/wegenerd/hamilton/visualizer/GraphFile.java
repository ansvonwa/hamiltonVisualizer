package de.wegenerd.hamilton.visualizer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;

public class GraphFile {
    private StringProperty graphName;
    private File file;

    GraphFile(File file) {
        graphName = new SimpleStringProperty(this, "algorithmName", file.getName());
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public StringProperty graphNameProperty() {
        return graphName;
    }
}
