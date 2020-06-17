package com.example.testlite.InterfaceModule;


import com.example.testlite.PogoClasses.BeconDeviceClass;

import java.util.ArrayList;

public interface MyBeaconScanner {
    void onBeaconFound(ArrayList<BeconDeviceClass> byteQueue);
    void noBeaconFound();
}
