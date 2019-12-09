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

public class MainActivity extends AppCompatActivity implements USBSerialListener {

    USBSerialConnector mcConnector;
    EditText txText;
    EditText rxText;
    ImageView yesData;
    ImageView noData;
    Button btSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcConnector = USBSerialConnector.getInstance();
        txText = (EditText) findViewById(R.id.txText);
        rxText = (EditText) findViewById(R.id.rxText);
        yesData = (ImageView) findViewById(R.id.yesData);
        noData = (ImageView) findViewById(R.id.noData);
        btSend = (Button) findViewById(R.id.btSend);
        txText.setEnabled(false);
        rxText.setEnabled(false);
        yesData.setEnabled(false);
        noData.setEnabled(false);
        btSend.setEnabled(false);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = txText.getText().toString();
                mcConnector.writeAsync(data.getBytes());
            }
        });
    }

    @Override
    public void onDataReceived (byte[] data) {
        if (data != null) {
            rxText.setText(rxText.getText() + Utilities.bytesToString(data) + "\n");
            yesData.setVisibility(View.VISIBLE);
            noData.setVisibility(View.INVISIBLE);
        } else {
            noData.setVisibility(View.VISIBLE);
            yesData.setVisibility(View.INVISIBLE);
        }
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
        btSend.setEnabled(true);
    }

    @Override
    public void onDeviceDisconnected() {
        Toast.makeText(this, "Device disconnected", Toast.LENGTH_LONG).show();
        finish();
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
