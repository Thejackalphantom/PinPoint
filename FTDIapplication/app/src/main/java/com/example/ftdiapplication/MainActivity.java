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
    //Class for the field in the app where the no incoming dataBuffer display will be shown
    ImageView yesData;
    //Class for the field in the app where the incoming dataBuffer display will be shown
    ImageView noData;
    //Class for the imageview in the app for the degree circle pointer
    ImageView pointer;
    //Class for the handler to handle the dataBuffer processing code every second

    Handler Handler;
    //Class that will check the connection status, ran at 300ms
    Runnable ConnectionChecker;
    //Class that will handle the data, ran at 150ms
    Runnable DataHandler;
    //String that contains the recieved dataBuffer from the FTDI chip
    StringBuffer dataBuffer;
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
        pointer = (ImageView) findViewById(R.id.pointer);

        //Create empty string for dataBuffer to be used as buffer
        dataBuffer = new StringBuffer(27);
        //Set connected to false by default
        connected = false;
        //Set a handler and runnable up to execute the connection checking code
        Handler = new Handler();
        //This Runnable handles the connection check of the app.
        ConnectionChecker = new Runnable()
        {
            public void run()
            {
            if (connected == true) {
                //Set the recieving dataBuffer icon to visible
                yesData.setVisibility(View.VISIBLE);
                //Set no dataBuffer recieved icon to invisible
                noData.setVisibility(View.INVISIBLE);
            } else {
                //Set no dataBuffer recieved icon to visible
                noData.setVisibility(View.VISIBLE);
                //Set the recieving dataBuffer icon to invisible
                yesData.setVisibility(View.INVISIBLE);
            }
            //Reset connected for proper connection check
            connected = false;
            //Repeat previous runnable code with a 300ms delay
            Handler.postDelayed(ConnectionChecker, 300);
            }
        };
        Handler.post(ConnectionChecker);
        //This Runnable handles the data processing of the incomming serial data
        DataHandler = new Runnable()
        {
            public void run()
            {
                if (connected == true) {
                    //Check given dataBuffer
                    dataCheck(dataBuffer);
                    //Clear the buffer
                    dataBuffer.delete(0,27);
                }
                //Repeat previous runnable code with a 150ms delay
                Handler.postDelayed(DataHandler, 150);
            }
        };
        Handler.post(DataHandler);
    }

    @Override
    public void onDataReceived (byte[] incomingData) {
        //set Connected to true
        connected = true;
        //Write incomingData to dataBuffer so that it can be used in other methods
        dataBuffer.append(Utilities.bytesToString(incomingData));
    }

    public void dataCheck(StringBuffer recievedData)
    {
        String dataString = recievedData.toString();
        //Check incoming dataBuffer and add to it untill the dataBuffer recieved is a complete string.
        //Check if the string's length is or exceeds 21 characters, the length of the expected string
        if(recievedData.length() > 17) {
            //Check if the string starts and ends with with the proper protocol
            if (dataString.startsWith("$tab")){
                //Now, write the dataBuffer to the app
                writeDataToApp(dataString);
            }
        }
    }

    public void writeDataToApp (String checkedData)
    {
        //Split the string into an array so the seperate dataBuffer can be called easily
        //The first index in the array is the tab, which is never shown
        String[] dataArr = checkedData.split(",");
        //The variables below are the different string to be shown.
        //This is the inclination
        String inc = dataArr[1];
        //This is the azimuth degrees
        String azi = dataArr[2];
        //The height side is used for the degree circle
        String his = dataArr[3];

        //setText writes the text to the app
        aziText.setText(azi);
        incText.setText(inc);
        hisText.setText(his);

        //setPivot sets the rotational pivot for the rotation
        pointer.setRotation(Float.parseFloat(azi));
    }

    @Override
    public void onDeviceReady(ResponseStatus responseStatus) {
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
