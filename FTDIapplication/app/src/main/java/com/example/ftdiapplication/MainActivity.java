package com.example.ftdiapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

//android libraries
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//the usb-serial Libraries
import com.example.ftdiapplication.ResponseStatus;
import com.example.ftdiapplication.USBSerialConnector;
import com.example.ftdiapplication.USBSerialListener;
import com.example.ftdiapplication.Utilities;

import java.util.Arrays;
import java.util.regex.Pattern;
public class MainActivity extends AppCompatActivity implements USBSerialListener {

    USBSerialConnector mcConnector;
    EditText txText;
    EditText rxText;
    ImageView yesData;
    ImageView noData;
    String[] dataArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcConnector = USBSerialConnector.getInstance();
        txText = (EditText) findViewById(R.id.txText);
        rxText = (EditText) findViewById(R.id.rxText);
        yesData = (ImageView) findViewById(R.id.yesData);
        noData = (ImageView) findViewById(R.id.noData);
        txText.setEnabled(false);
        rxText.setEnabled(false);
        yesData.setEnabled(false);
        noData.setEnabled(false);
        /*Data array that will contain the data recieved through the serial antennae.
          The array will always have the following index: 0 is tab,
          1 is inclination, 2 is azimuth degrees, 3 is heightside.
        */
        dataArray = new String[4];
    }

    @Override
    public void onDataReceived (byte[] data) {
        //TODO: Fix java.io.IOException: Already Closed &
        if (data != null) {
            //Set the recieving data icon to visible
            yesData.setVisibility(View.VISIBLE);
            //Set no data recieved icon to invisible
            noData.setVisibility(View.INVISIBLE);
            //Current issue: Below code causes severe errors and instability
            //Split data string to fill in the array
            String dataString = Utilities.bytesToString(data);
            writeDataToApp(dataString);

        } else {
            //Set no data recieved icon to visible
            noData.setVisibility(View.VISIBLE);
            //Set the recieving data icon to invisible
            yesData.setVisibility(View.INVISIBLE);
        }
    }

    public void writeDataToApp (String data)
    {
        //Create an array of the data through a split.
        //Pattern.quote is utility
        dataArray = data.split(Pattern.quote(","));

        //Seperate the array into strings
        String tab = dataArray[0];
        String inc = dataArray[1];
        String azi = dataArray[2];
        String his = dataArray[3];

        //Write array into app
        rxText.setText(inc);
    }

    @Override
    public void onErrorReceived(String data) {
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeviceReady(ResponseStatus responseStatus) {
        txText.setEnabled(true);
        rxText.setEnabled(true);
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
