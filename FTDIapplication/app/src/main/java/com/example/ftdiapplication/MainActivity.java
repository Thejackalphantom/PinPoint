package com.example.ftdiapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

//android libraries
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements USBSerialListener {

    //Class for the USBSerial Connector API
    USBSerialConnector mcConnector;
    //Class for the field in the app where the inclination will be shown
    EditText incText;
    //Class for the field in the app where the azimuth degree will be shown
    EditText aziText;
    //Class for the field in the app where the high side degree will be shown
    EditText hisText;
    //Class for the field in the app where the no incoming data display will be shown
    ImageView yesData;
    //Class for the field in the app where the incoming data display will be shown
    ImageView noData;
    //Class for the handler to handle the data processing code every second
    Handler Handler;
    //Class that will run all the data processing code
    Runnable ConnectionChecker;
    //String that contains the recieved data from the FTDI chip
    String data;
    //Boolean to check connection
    Boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Intialize the app layout
        setContentView(R.layout.activity_main);
        //Intialize the USBSerialConnector api
        mcConnector = USBSerialConnector.getInstance();
        //Get the ID of all the fields that require any manipulaton by code.
        incText = (EditText) findViewById(R.id.inclination);
        aziText = (EditText) findViewById(R.id.azimuthDegrees);
        hisText = (EditText) findViewById(R.id.heightside);
        yesData = (ImageView) findViewById(R.id.yesData);
        noData = (ImageView) findViewById(R.id.noData);
        //Pre-emptively disable the fields for better showing, enabled with onDeviceReady()
        incText.setEnabled(false);
        aziText.setEnabled(false);
        hisText.setEnabled(false);
        yesData.setEnabled(false);
        noData.setEnabled(false);
        //Create empty string for data
        data = "";
        //Set connected to false by default
        connected = false;
        //Set a handler and runnable up to execute the connection checking code
        Handler = new Handler();
        ConnectionChecker = new Runnable()
        {
            public void run()
            {
            if (connected == true) {
                //Set the recieving data icon to visible
                yesData.setVisibility(View.VISIBLE);
                //Set no data recieved icon to invisible
                noData.setVisibility(View.INVISIBLE);
                //Check given data
                dataCheck(data);
                //Clear the string once finished writing to the app
            } else {
                //Set no data recieved icon to visible
                noData.setVisibility(View.VISIBLE);
                //Set the recieving data icon to invisible
                yesData.setVisibility(View.INVISIBLE);
            }
            //Reset connected for proper connection check
            connected = false;
            Handler.postDelayed(ConnectionChecker, 200);
            }
        };
        Handler.post(ConnectionChecker);
    }

    @Override
    public void onDataReceived (byte[] incomingData) {
        //set Connected to true
        connected = true;
        //Write incomingData to data so that it can be used in other methods
        data += Utilities.bytesToString(incomingData);
    }

    public void dataCheck(String recievedData)
    {
        //Check incoming data and add to it untill the data recieved is a complete string.
        //Check if the string's length is or exceeds 21 characters, the length of the expected string
        if(recievedData.length() == 17 || recievedData.length() > 17) {
            //Check if the string starts and ends with with the proper protocol
            if (recievedData.substring(0, 17).startsWith("$tab") || recievedData.substring(0, 17).endsWith("/r/n")){
                //Now, write the data to the app
                writeDataToApp(recievedData.substring(0, 17));
                //TODO: Fix the string not being cleared
                //Clear the string
                data = "";
            }
        }
    }

    public void writeDataToApp (String checkedData)
    {
        //todo: Data recieved is not always complete

        //Split the string into an array so the seperate data can be called easily
        String[] dataArr = checkedData.split(",");
        //These are the different string to be shown
        //Index is often out of bounds due to the entire string not being here all the time.
        String tab = dataArr[0];
        String inc = dataArr[1];
        String azi = dataArr[2];
        String his = dataArr[3];

        //setText writes the text to the app
        aziText.setText(azi);
        incText.setText(inc);
        hisText.setText(his);
    }

    @Override
    public void onDeviceReady(ResponseStatus responseStatus) {
        //This code is executed whenever the device is ready to roll
        incText.setEnabled(true);
        aziText.setEnabled(true);
        yesData.setEnabled(true);
        noData.setEnabled(true);
    }

    @Override
    public void onDeviceDisconnected() {
        //This is executed when the usb serial ftdi device is disconnected
        Toast.makeText(this, "Device disconnected", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        //This is executed when the program resumes
        super.onResume();
        mcConnector.setUsbSerialListener(this);
        mcConnector.init(this, 9600);
    }

    @Override
    protected void onPause() {
        //This is executed when the program is paused
        super.onPause();
        mcConnector.pausedActivity();
    }
}
