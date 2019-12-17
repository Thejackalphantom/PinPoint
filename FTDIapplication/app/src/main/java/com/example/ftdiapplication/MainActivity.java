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

    USBSerialConnector mcConnector;
    EditText incText;
    EditText aziText;
    EditText hisText;
    ImageView yesData;
    ImageView noData;
    Handler Handler;
    Runnable DataRunner;
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
