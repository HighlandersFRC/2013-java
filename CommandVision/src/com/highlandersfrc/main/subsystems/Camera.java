package com.highlandersfrc.main.subsystems;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;

/**
 *
 * @author David
 */
public class Camera extends Subsystem {
    
    AxisCamera camera;

    public void initDefaultCommand() {
        //No intialization needed.
    }
    
    public Camera() {
        camera = AxisCamera.getInstance();
    }
    
    public ColorImage getImage() throws AxisCameraException, NIVisionException {
        return camera.getImage();
    }
}
