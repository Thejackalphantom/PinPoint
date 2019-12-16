package com.example.ftdiapplication;

public interface USBSerialListener {
    void onDataReceived(byte[] data);

    void onDeviceReady(ResponseStatus responseStatus);

    void onDeviceDisconnected();
}