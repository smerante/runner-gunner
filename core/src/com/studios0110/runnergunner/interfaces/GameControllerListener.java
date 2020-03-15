package com.studios0110.runnergunner.interfaces;

import com.studios0110.runnergunner.handler.AxisMapper;

/**
 * Created by Sam Merante on 2018-10-19.
 */
public interface GameControllerListener {
    public void buttonDown(int controllerI, String button);
    public void buttonUp(int controllerI,String button);
    public void povMoved(int controllerI,String pov);
    public void axisMoved(int controllerI,String axis, AxisMapper axisMapper);
}
