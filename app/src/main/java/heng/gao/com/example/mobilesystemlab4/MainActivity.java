package heng.gao.com.example.mobilesystemlab4;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
// Android apps must have a MainActivity class that extends Activity or AppCompatActivity class
public class MainActivity extends AppCompatActivity {


    //define class variables
    private static final String LOGTAG = "Main UI"; //Logcat messages from UI are identified
    private NetworkConnectionAndReceiver networkConnectionAndReceiver = null;
    public static String CMD;
    public static String usr;

    // Class methods
    @Override
    //Extend the onCreate method, called whenever an activity is started
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOGTAG, "Starting task4 app"); // Report to Logcat

        // Instantiate the network connection and receiver object
        networkConnectionAndReceiver =  new NetworkConnectionAndReceiver(this);
        networkConnectionAndReceiver.start(); // Start socket-receiver thread

        TextView myTextViewBox = (TextView)findViewById(R.id.myTextViewBox);
        myTextViewBox.setMovementMethod(new ScrollingMovementMethod() );


    }

    public void registerMethod(View v){
        Log.i(LOGTAG,"register method invoked");
        CMD = "REGISTER";
        TextView myUsrNameDisplayBox= findViewById(R.id.myUsrNameDisplayBox);
        EditText myUsrNameEditBox = findViewById(R.id.myUsrNameEditBox);
        usr = myUsrNameEditBox.getText().toString();
        myUsrNameDisplayBox.setText(usr);

        Log.i(LOGTAG," Register "  + usr + "as a new user");
        //create a transmitter thread to communicate
        Transmitter transmitter = new Transmitter(networkConnectionAndReceiver.getStreamOut(), CMD + usr);
        transmitter.start(); // Run on its own thread

        myUsrNameEditBox.setText("");
    }

    public void unregisterMethod(View v){
        Log.i(LOGTAG,"unregister method invoked");
        CMD = "UNREGISTER";
        TextView myUsrNameDisplayBox= findViewById(R.id.myUsrNameDisplayBox);
        myUsrNameDisplayBox.setText("Unregister now");

        Log.i(LOGTAG," Register "  + usr + "as a new user");
        if(networkConnectionAndReceiver.getStreamOut() != null) {
            //create a transmitter thread to communicate
            Transmitter transmitter = new Transmitter(networkConnectionAndReceiver.getStreamOut(), CMD);
            transmitter.start(); // Run on its own thread
        }
    }

    public void whoMethod(View v){
        Log.i(LOGTAG,"who method invoked");
        CMD = "WHO";
        Log.i(LOGTAG," commond Who is triggered");
        TextView whoBox = findViewById(R.id.myWhoViewBox);
        whoBox.setText("");
        if(networkConnectionAndReceiver.getStreamOut() != null) {
            //create a transmitter thread to communicate
            Transmitter transmitter = new Transmitter(networkConnectionAndReceiver.getStreamOut(), CMD);
            transmitter.start(); // Run on its own thread
        }
    }


    public void sendMethod(View v)
    {
        Log.i(LOGTAG,"send method invoked");
        // Get the text area for commands to be transmitted as defined in the Res dir xml code
        TextView myEditTextBox = findViewById(R.id.myEditTextBox);
        String editText = myEditTextBox.getText( ).toString( );

        myEditTextBox.setText("");
        Vibrator vibrator= (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(200); //200 milliseconds are the length of vibration
        if(networkConnectionAndReceiver.getStreamOut() != null){
            //create a transmitter thread to communicate
            Transmitter transmitter = new Transmitter(networkConnectionAndReceiver.getStreamOut(), editText);
            transmitter.start(); // Run on its own thread
        }
    }

    public void inviteMethod(View v)
    {
        Log.i(LOGTAG,"invite method invoked");
        CMD = "INVITE";
        // Get the text area for commands to be transmitted as defined in the Res dir xml code
        TextView myEditTextBox = findViewById(R.id.myEditTextBox);
        String target_usr = myEditTextBox.getText().toString( );

        myEditTextBox.setText("");
        if(networkConnectionAndReceiver.getStreamOut() != null){
            //create a transmitter thread to communicate
            Transmitter transmitter = new Transmitter(networkConnectionAndReceiver.getStreamOut(), CMD + " " + target_usr);
            transmitter.start(); // Run on its own thread
        }
    }




    public void killMethod(View v)
    {
        Log.i(LOGTAG,"kill method invoked");
        CMD = "UNREGISTER";
        //unregister when quit
        String UNREGISTER_CMD = "UNREGISTER ";
        if(usr != null){
            //create a transmitter thread to communicate
            Transmitter transmitter = new Transmitter(networkConnectionAndReceiver.getStreamOut(), CMD + usr);
            transmitter.start(); // Run on its own thread
        }

        //quit the app
        System.exit(0);
    }


//    @Override
//    //Create an options menu
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        // Uses res>menu>main.xml
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    } //End of app onCreateOptionsMenu
//
//    @Override
//    //Called when an item is selected from the options menu
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings)
//        {
//        return true;
//        }
//        return super.onOptionsItemSelected(item);
//    } //End of app onOptionsItemSelected method
}