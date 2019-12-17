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
    Runnable DataRunner;
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
        //Set a handler and runnable up to execute the data processing code every second
        Handler = new Handler();
        DataRunner = new Runnable()
        {
            public void run()
            {
                if (data != null) {
                    //Set the recieving data icon to visible
                    yesData.setVisibility(View.VISIBLE);
                    //Set no data recieved icon to invisible
                    noData.setVisibility(View.INVISIBLE);
                    //Current issue: Below code causes severe errors and instability
                    writeDataToApp(data);
                    //empty the data so that the display will turn red when no data is recieved
                    data = null;
                } else {
                    //Set no data recieved icon to visible
                    noData.setVisibility(View.VISIBLE);
                    //Set the recieving data icon to invisible
                    yesData.setVisibility(View.INVISIBLE);
                }
                Handler.postDelayed(DataRunner, 1000);
            }
        };
        Handler.post(DataRunner);
    }

    @Override
    public void onDataReceived (byte[] incomingData) {
        //This writes the incoming data to the variable within the class itself
        data = incomingData;
    }

    public void writeDataToApp (byte[] recievedData)
    {
        //todo: Data recieved is not always complete
        //Convert byte array to string
        String dataString = Utilities.bytesToString(recievedData);
        //Split the string into an array so the seperate data can be called easily
        String[] dataArr = dataString.split(",");
        //These are the different string to be shown
        //Index is often out of bounds due to the entire string not being here all the time.
        String tab = dataArr[0];
        String inc = dataString;
        String azi = dataArr[0];
        String his = dataArr[0];

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
