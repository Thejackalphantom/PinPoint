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
                Handler.postDelayed(ConnectionChecker, 200);
            }
        };
        Handler.post(ConnectionChecker);
    }

    @Override
    public void onDataReceived (byte[] incomingData) {
        //Boolean to check whether or not the incoming data is complete
        boolean isComplete = false;
        //Write incomingData to data so that it can be used in other methods
        data = incomingData;
        //Convert byte array to string
        String dataString = Utilities.bytesToString(incomingData);
        //Convert it to a char array for checking of every symbol
        char[] dataCharArr = dataString.toCharArray();
        //Following code goes through every symbol in the dataCharArr to check whether or not it holds up to the agreed upon $tab2,XXX,XXX,XXX/r/n
        if(dataString.length() == 22) {
            for (int index = 0; index < 22; index++) {
                switch(index)
                {
                    case 0:
                        if(Character.toString(dataCharArr[index]) != "$")
                        {
                            isComplete = false;
                        }
                        break;
                    case 1:
                        if(Character.toString(dataCharArr[index]) != "t")
                        {
                            isComplete = false;
                        }
                    case 2:
                        if(Character.toString(dataCharArr[index]) != "a")
                        {
                            isComplete = false;
                        }
                    case 3:
                        if(Character.toString(dataCharArr[index]) != "b")
                        {
                            isComplete = false;
                        }
                    case 5:
                        if(Character.toString(dataCharArr[index]) != "0")
                        {
                            isComplete = false;
                        }
                    case 6:
                        if(Character.toString(dataCharArr[index]) != "2")
                        {
                            isComplete = false;
                        }
                    case 7:
                        if(Character.toString(dataCharArr[index]) != ",")
                        {
                            isComplete = false;
                        }
                    case 8:
                        if(Character.isDigit(dataCharArr[index]) == false)
                        {
                            isComplete = false;
                        }
                    case 9:
                        if(Character.isDigit(dataCharArr[index]) == false)
                        {
                            isComplete = false;
                        }
                    case 10:
                        if(Character.isDigit(dataCharArr[index]) == false)
                        {
                            isComplete = false;
                        }
                    case 11:
                        if(Character.toString(dataCharArr[index]) != ",")
                        {
                            isComplete = false;
                        }
                    case 12:
                        if(Character.isDigit(dataCharArr[index]) == false)
                        {
                            isComplete = false;
                        }
                    case 13:
                        if(Character.isDigit(dataCharArr[index]) == false)
                        {
                            isComplete = false;
                        }
                    case 14:
                        if(Character.isDigit(dataCharArr[index]) == false)
                        {
                            isComplete = false;
                        }
                    case 15:
                        if(Character.toString(dataCharArr[index]) != ",")
                        {
                            isComplete = false;
                        }
                    case 16:
                        if(Character.isDigit(dataCharArr[index]) == false)
                        {
                            isComplete = false;
                        }
                    case 17:
                        if(Character.isDigit(dataCharArr[index]) == false)
                        {
                            isComplete = false;
                        }
                    case 18:
                        if(Character.isDigit(dataCharArr[index]) == false)
                        {
                            isComplete = false;
                        }
                    case 19:
                        if(Character.toString(dataCharArr[index]) != "/")
                        {
                            isComplete = false;
                        }
                        break;
                    case 20:
                        if(Character.toString(dataCharArr[index]) != "r")
                        {
                            isComplete = false;
                        }
                        break;
                    case 21:
                        if(Character.toString(dataCharArr[index]) != "/")
                        {
                            isComplete = false;
                        }
                        break;
                    case 22:
                        if(Character.toString(dataCharArr[index]) != "n")
                        {
                            isComplete = false;
                        }
                        break;
                    default:
                        isComplete = true;
                        break;
                }
            }
        }
        else
        {
            isComplete = false;
        }
        if(isComplete == true) {
            //Current issue: Below code causes severe errors and instability
            writeDataToApp(dataString);
        }
        else{}
        //empty the data so that the display will turn red when no data is recieved next time
        data = null;
    }

    public void writeDataToApp (String recievedData)
    {
        //todo: Data recieved is not always complete

        //Split the string into an array so the seperate data can be called easily
        String[] dataArr = recievedData.split(",");
        //These are the different string to be shown
        //Index is often out of bounds due to the entire string not being here all the time.
        String tab = dataArr[0];
        String inc = recievedData;
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
