package u.luxing.smartmixer.app.gson;

import java.util.Map;

/**
 * Interface for Gson support for Java objects.
 *
 * @author hockeyhurd
 * @version 10/10/2018.
 */
public interface IEncodeable {

    /**
     * Encodes an IEncodeable object to json via this function call.
     *
     * @param json json as represented in a String->Object Map.
     */
    void encode(Map<String, Object> json);

}
