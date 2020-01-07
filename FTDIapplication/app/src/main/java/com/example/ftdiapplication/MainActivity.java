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
    //Byte-array that contains the recieved data from the FTDI chip
    byte[] data;

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
        //Set a handler and runnable up to execute the connection checking code
        Handler = new Handler();
        ConnectionChecker = new Runnable()
        {
            public void run()
            {
            if (data != null) {
                //Set the recieving data icon to visible
                yesData.setVisibility(View.VISIBLE);
                //Set no data recieved icon to invisible
                noData.setVisibility(View.INVISIBLE);
            } else {
                //Set no data recieved icon to visible
                noData.setVisibility(View.VISIBLE);
                //Set the recieving data icon to invisible
                yesData.setVisibility(View.INVISIBLE);
            }
            //Set data to null for connection check
            data = null;
            Handler.postDelayed(ConnectionChecker, 200);
            }
        };
        Handler.post(ConnectionChecker);
    }

    @Override
    public void onDataReceived (byte[] incomingData) {
        //Write incomingData to data so that it can be used in other methods
        data = incomingData;
        //Convert byte array to string
        String dataString = Utilities.bytesToString(incomingData);
        //Check if the string starts with tab02 and ends with /r/n as this was the part of the agreed upon syntax that never changes.
        if(dataString.startsWith("tab02"))
        {
            writeDataToApp(dataString);
        }
    }

    public void writeDataToApp (String recievedData)
    {
        //todo: Data recieved is not always complete

        //Split the string into an array so the seperate data can be called easily
        String[] dataArr = recievedData.split(",");
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
