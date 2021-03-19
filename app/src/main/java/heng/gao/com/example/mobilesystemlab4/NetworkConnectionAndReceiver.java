package heng.gao.com.example.mobilesystemlab4;

import android.app.Service;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class NetworkConnectionAndReceiver extends Thread{
    //Declare class variables
    private Socket socket = null;
    private static final int SERVERPORT = 9999; // This is the port that we are connecting to
    // Channel simulator is 9998
    private static final String SERVERIP = "10.0.2.2";  // This is the host's loopback address
    private static final String LOGTAG = "Network and receiver"; // Identify logcat messages

    private boolean terminated = false; // When FALSE keep thread alive and polling

    private PrintWriter streamOut; // Transmitter stream
    private BufferedReader streamIn; // Receiver stream
    private AppCompatActivity parentRef; // Reference to main user interface(UI)
    private TextView receiverDisplay; // Receiver display
    public static String message = null; //Received message

    //class constructor
    public NetworkConnectionAndReceiver(AppCompatActivity parentRef)
    {
        this.parentRef=parentRef; // Get reference to UI
    }
    // Start new thread method
    public void run()
    {
        //Create socket and input output streams
        try {   //Create socket
            // InetAddress svrAddr = InetAddress.getByName(SERVERIP);
            InetAddress svrAddr = InetAddress.getLocalHost();
            socket = new Socket(svrAddr, SERVERPORT);
            //Setup i/o streams
            //streamOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            streamOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            //streamIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            streamIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (UnknownHostException uhe) {
            Log.e(LOGTAG, "Unknownhost\n" + uhe.getStackTrace().toString());
            terminated = true;
        }
        catch (Exception e) {
            Log.e(LOGTAG, "Socket failed\n" + e.getMessage());
            e.printStackTrace();
            terminated = true;
        }

        //receiver
        while(!terminated) // Keep thread running
        {
            try {
                message = streamIn.readLine(); // Read a line of text from the input stream
                Log.i(LOGTAG, "MSG recv : " + message);

                String cmd = MainActivity.CMD;
                // If the message has text then display it
                if (message != null && message != "") {
                    /*TODO*/
                    if(cmd == "WHO"){
                        /* display to who view*/
                        receiverDisplay= parentRef.findViewById(R.id.myWhoViewBox);
                    }
                    else{
                        /*display to text view*/
                        receiverDisplay = parentRef.findViewById(R.id.myTextViewBox);
                    }

                    //Run code in run() method on UI thread
                    sleep(10);
                    parentRef.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Display message, and old text in the receiving text area
                            receiverDisplay.append(message + "\n");
                            Vibrator vibrator= (Vibrator) parentRef.getSystemService(Service.VIBRATOR_SERVICE);
                            vibrator.vibrate(200); //200 milliseconds are the length of vibration
                        }
                    });
                    sleep(10);

                }
            }
            catch (Exception e) {
                Log.e(LOGTAG, "Receiver failed\n" + e.getMessage());
                e.printStackTrace();
            }
        }
        //Call disconnect method to close i/o streams and socket
        disconnect();
        Log.i(LOGTAG,"Thread now closing");
    }

    //  Method for closing socket and i/o streams
    public void disconnect()
    {
        try {
            streamIn.close();
            streamOut.close();
        }
        catch(Exception e)
        {/*do nothing*/}

        try {
            socket.close();
        }
        catch(Exception e)
        {/*do nothing*/}

        this.terminated = true;
    }

    // Getter method for returning the output stream for the transmitter to use
    public PrintWriter getStreamOut() {return this.streamOut; }

    // Setter method for terminating this thread
    // Set value to true to close thread
    public void closeThread(boolean value) { this.terminated = value; }
}