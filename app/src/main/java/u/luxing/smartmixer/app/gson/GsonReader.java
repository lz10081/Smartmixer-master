package u.luxing.smartmixer.app.gson;

import android.util.JsonReader;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

/**
 * Handles reading a json using the GSON API.
 *
 * @author hockeyhurd
 * @version 10/11/2018.
 */
public final class GsonReader<T, C extends u.luxing.smartmixer.app.gson.IGsonResponder<Map<String, Object>>> {

    private Reader reader;
    private final u.luxing.smartmixer.app.gson.IGsonInterpreter<T, C> interpreter;


    private static final Gson gson = new Gson();

    public GsonReader(final String name, final u.luxing.smartmixer.app.gson.IGsonInterpreter<T, C> interpreter) {
        try {
            this.reader = new FileReader(name);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.interpreter = interpreter;
    }

    public GsonReader(final File file, final boolean deleteOnExit, final u.luxing.smartmixer.app.gson.IGsonInterpreter<T, C> interpreter) {
        try {
            this.reader = new FileReader(file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (deleteOnExit) {
          //  FileManager.getInstance().deleteFile(file);
        }

        this.interpreter = interpreter;
    }

    public GsonReader(final byte[] bytes, final u.luxing.smartmixer.app.gson.IGsonInterpreter<T, C> interpreter) {
        final ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);

        this.reader = new InputStreamReader(byteStream);
        this.interpreter = interpreter;
    }

    /**
     * Reads json file and returns object specified.
     *
     * @return T result.
     * @throws IOException IOException.
     */
    public T read() {
        /*if (!file.exists()) {
            logger.error("File: " + file.getName() + ", does not exist!");
            return null;
        }*/

        final JsonReader jsonReader = new JsonReader(reader);
        final C response = gson.fromJson(String.valueOf(jsonReader),interpreter.getResponseClass());
        final T result = interpreter.interpret(response.getResponse());

        return result;
    }

}
