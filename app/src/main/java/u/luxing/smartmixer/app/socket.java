package u.luxing.smartmixer.app;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
/**
 * This class is designed setup the socket
 *
 * @author Luxing Zeng
 * @version 4/1/2019.
 */

public final class socket {
    private static BluetoothSocket mmSocket;

    /**
     *
     * @param Device
     * @return BluetoothSocket
     */
    public static BluetoothSocket establishSocket(BluetoothDevice Device) {
        UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); //Standard SerialPortService ID
        Log.e("UUID", uuid.toString());
        try {
            mmSocket = Device.createRfcommSocketToServiceRecord(uuid);
            Log.e("socket", String.valueOf(Device.createRfcommSocketToServiceRecord(uuid)));
            if (!mmSocket.isConnected()) {
                mmSocket.connect();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mmSocket;
    }

    /**
     *
     * @param send
     */
    public void sendData(String send){
        try {
            OutputStream mmOutStream = mmSocket.getOutputStream();
            mmOutStream.write(send.getBytes());
            mmOutStream.write("\u0004\u0004".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return String
     * @throws IOException
     */
    public String recvData() throws IOException {
        InputStream mmInStream = mmSocket.getInputStream();
        return String.valueOf(mmInStream.read());
    }

}
