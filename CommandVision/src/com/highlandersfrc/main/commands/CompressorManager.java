package com.highlandersfrc.main.commands;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;


public class CompressorManager {
    
  public Compressor airCompressor;
  public DigitalInput systemInput;
 
public void monkeyTime() {
  
            airCompressor = new Compressor(1,1);
}

public void managePressureSwitch() {
    
    
    boolean pressureSwitch = systemInput.get();                //Need to check if this will really work
    if(pressureSwitch=true) {
        airCompressor.stop();                                  //This means PSI is > than 115 so we turn compressor off
    }
    else {
        airCompressor.start();                                 //This means PSI is < than 95 so we turn the compressor on
    }
    
}
}