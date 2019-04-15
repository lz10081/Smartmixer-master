package u.luxing.smartmixer.app.gson;

import java.util.Map;

/**
 * Interface interpreter for GSON reading.
 *
 * @author hockeyhurd
 * @version 10/11/2018.
 */
public interface IGsonInterpreter<T, C extends u.luxing.smartmixer.app.gson.IGsonResponder<Map<String, Object>>> {

    Class<C> getResponseClass();

    T interpret(Map<String, Object> json);

}
