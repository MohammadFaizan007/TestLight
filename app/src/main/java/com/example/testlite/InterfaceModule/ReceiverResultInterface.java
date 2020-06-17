package com.example.testlite.InterfaceModule;


import com.example.testlite.EncodeDecodeModule.ByteQueue;

public interface ReceiverResultInterface {

    void onScanSuccess(int successCode, ByteQueue byteQueue);
    void onScanFailed(int errorCode);


}
