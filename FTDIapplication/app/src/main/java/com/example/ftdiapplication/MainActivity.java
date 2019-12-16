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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements USBSerialListener {

    USBSerialConnector mcConnector;
    EditText txText;
    EditText rxText;
    ImageView yesData;
    ImageView noData;
    ArrayList dataArray;
    /*Data array that will contain the data recieved through the serial antennae.
      The array will always have the following index: 0 is tab,
      1 is inclination, 2 is azimuth degrees, 3 is heightside.
    */

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
        dataArray = new ArrayList<>();
    }

    @Override
    public void onDataReceived (byte[] data) {
        if (data != null) {
            //Set the recieving data icon to visible
            yesData.setVisibility(View.VISIBLE);
            //Set no data recieved icon to invisible
            noData.setVisibility(View.INVISIBLE);
            //Current issue: Below code causes severe errors and instability
            String dataString = Utilities.bytesToString(data);
            writeDataToApp(dataString);
//            rxText.setText(dataString);
        } else {
            //Set no data recieved icon to visible
            noData.setVisibility(View.VISIBLE);
            //Set the recieving data icon to invisible
            yesData.setVisibility(View.INVISIBLE);
        }
    }

    public void writeDataToApp (String dataString)
    {
        //todo: Either fix or replace split to stop the crashes
        String stringToAdd = "";

        dataArray.clear();
        //This is basically a really ugly split.
        char[] dataCharArr = dataString.toCharArray();
        for(char c : dataCharArr)
        {
            if(c != ",".toCharArray()[0])
            {
                stringToAdd += c;
            }
            else
            {
                dataArray.add(stringToAdd);
                stringToAdd = "";
            }
        }

        //Seperate the array into strings
        String tab = dataArray.get(0).toString();
        String inc = dataArray.get(1).toString();
        String azi = dataArray.get(2).toString();
        String his = dataArray.get(3).toString();

        //Write array into app
        rxText.setText(tab+" "+inc+" "+azi+""+his);
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
