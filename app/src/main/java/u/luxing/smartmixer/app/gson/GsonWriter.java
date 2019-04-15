package u.luxing.smartmixer.app.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.TreeMap;

/**
 * Handles writing a json using the GSON API.
 *
 * @author hockeyhurd
 * @version 10/10/2018.
 */
public final class GsonWriter {

   // private static final Logger logger = Module.getInstance().getLogger();
    private static final Gson GSON_BUILDER = new GsonBuilder().setPrettyPrinting().create();

    private final String dirPath;

    /**
     * Creates a GsonWriter object and placing json at
     * the working directory of the program.
     */
    public GsonWriter() {
        this.dirPath = "";
    }

    /**
     * Creates a GsonWriter object.
     *
     * @param dirPath directory to write json files to.
     */
    public GsonWriter(String dirPath) {
        if (!dirPath.endsWith(File.separator))
            dirPath += File.separator;

        this.dirPath = dirPath;
    }

    /**
     * Writes a json file.
     *
     * @param encodeable IEncodeable object.
     * @param <T> Generic type T.
     * @throws IOException IOException
     */
    public <T> String write(final u.luxing.smartmixer.app.gson.IEncodeable encodeable) throws IOException {
        final Map<String, Object> json = new TreeMap<>();
        encodeable.encode(json);



        //final File outputFile = new File(dirPath + fileName + ".json");

        final StringWriter fileWriter = new StringWriter(1000);
        GSON_BUILDER.toJson(json, fileWriter);

        fileWriter.flush();
        fileWriter.close();
        return fileWriter.toString();
    }

}
