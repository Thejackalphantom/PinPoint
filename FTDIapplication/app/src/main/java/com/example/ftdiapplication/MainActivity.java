package com.example.ftdiapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

//android libraries
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//the usb-serial Libraries

public class MainActivity extends AppCompatActivity implements USBSerialListener {

    USBSerialConnector mcConnector;
    EditText incText;
    EditText aziText;
    EditText hisText;
    ImageView yesData;
    ImageView noData;
    byte[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcConnector = USBSerialConnector.getInstance();
        incText = (EditText) findViewById(R.id.inclination);
        aziText = (EditText) findViewById(R.id.azimuthDegrees);
        hisText = (EditText) findViewById(R.id.heightside);
        yesData = (ImageView) findViewById(R.id.yesData);
        noData = (ImageView) findViewById(R.id.noData);
        incText.setEnabled(false);
        aziText.setEnabled(false);
        hisText.setEnabled(false);
        yesData.setEnabled(false);
        noData.setEnabled(false);
    }

    @Override
    public void onDataReceived (byte[] incomingData) {
        data = incomingData;
        if (incomingData != null) {
            //Set the recieving data icon to visible
            yesData.setVisibility(View.VISIBLE);
            //Set no data recieved icon to invisible
            noData.setVisibility(View.INVISIBLE);
            //Current issue: Below code causes severe errors and instability
            writeDataToApp(data);
        } else {
            //Set no data recieved icon to visible
            noData.setVisibility(View.VISIBLE);
            //Set the recieving data icon to invisible
            yesData.setVisibility(View.INVISIBLE);
        }
    }

    public void writeDataToApp (byte[] recievedData)
    {
        //todo: Either fix or replace split to stop the crashes
        //Convert byte array to string
        String dataString = Utilities.bytesToString(recievedData);

        //These are the different string to be shown
        String tab = "";
        String inc = "";
        String azi = "";
        String his = "";

        //setText writes the text to the app
        aziText.setText(dataString);
        incText.setText(inc);
        hisText.setText(his);
    }

    @Override
    public void onDeviceReady(ResponseStatus responseStatus) {
        incText.setEnabled(true);
        aziText.setEnabled(true);
        yesData.setEnabled(true);
        noData.setEnabled(true);
    }

    @Override
    public void onDeviceDisconnected() {
        Toast.makeText(this, "Device disconnected", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mcConnector.setUsbSerialListener(this);
        mcConnector.init(this, 9600);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mcConnector.pausedActivity();

    }
}
