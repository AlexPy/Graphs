package graphlibrary.core;

/**
 * Обертка над именем файла
 */
public class GraphSource {

    private String filename;

    public GraphSource(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}

