package com.studios0110.runnergunner.handler;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.studios0110.runnergunner.interfaces.GameControllerListener;

import java.util.HashMap;

/**
 * Created by Sam Merante on 2018-10-19.
 */

class ControllerMap {
    HashMap<Integer, String> gamePadButtons,ps4Buttons;
    HashMap<String, String> pov;
    HashMap<Integer, String> axis;
    float leftX = 0, leftY = 0, leftAngle;
    float rightX = 0, rightY = 0, rightAngle;
    AxisMapper axisMapper;

    ControllerMap() {
        int A = 0, B = 1, X = 2, Y = 3, L1 = 4, R1 = 5, SELECT = 6, START = 7, L3 = 8, R3 = 9;
        axisMapper = new AxisMapper(0, 0, 0, 0, 0, 0, 0, 0);
        gamePadButtons = new HashMap<Integer, String>();
        ps4Buttons = new HashMap<Integer, String>();
        pov = new HashMap<String, String>();
        axis = new HashMap<Integer, String>();

        gamePadButtons.put(A, "A");
        gamePadButtons.put(B, "B");
        gamePadButtons.put(X, "X");
        gamePadButtons.put(Y, "Y");
        gamePadButtons.put(L1, "L1");
        gamePadButtons.put(R1, "R1");
        gamePadButtons.put(SELECT, "SELECT");
        gamePadButtons.put(START, "START");
        gamePadButtons.put(L3, "L3");
        gamePadButtons.put(R3, "R3");

        ps4Buttons.put(A, "Square");
        ps4Buttons.put(B, "X");
        ps4Buttons.put(X, "Circle");
        ps4Buttons.put(Y, "Triangle");
        ps4Buttons.put(L1, "L1");
        ps4Buttons.put(R1, "R1");
        ps4Buttons.put(L3, "SELECT");
        ps4Buttons.put(R3, "START");
        ps4Buttons.put(10, "L3");
        ps4Buttons.put(11, "R3");
        ps4Buttons.put(12, "Playstation");
        ps4Buttons.put(13, "TouchPad");

        pov.put("west", "left");
        pov.put("east", "right");
        pov.put("north", "up");
        pov.put("south", "down");
        pov.put("center", "middle");
        pov.put("southEast", "down-right");
        pov.put("southWest", "down-left");
        pov.put("northWest", "up-left");
        pov.put("northEast", "up-right");
        axis.put(0, "L-up");
        axis.put(1, "L-left");
        axis.put(2, "R-up");
        axis.put(3, "R-left");
        axis.put(4, "R2");
        axis.put(5, "L-down");
        axis.put(6, "L-right");
        axis.put(7, "R-down");
        axis.put(8, "R-right");
        axis.put(9, "L2");
    }

    public String getPOV(PovDirection value) {
        return pov.get(value.name());
    }

    public String getButton(Controller controller, int buttonCode) {
        if(controller.getName().equals("Wireless Controller")){
            return ps4Buttons.get(buttonCode);
        }
        else
            return gamePadButtons.get(buttonCode);
    }

    public String getAxis(Controller controller, int axisCode, float value) {
        setLeftAngle(controller, axisCode, value);
        setRightAngle(controller, axisCode, value);
        if (axisCode == 0) {
            if (value > 0) return this.axis.get(5);
        }
        if (axisCode == 1) {
            if (value > 0) return this.axis.get(6);
        }
        if (axisCode == 2) {
            if (value > 0) return this.axis.get(7);
        }
        if (axisCode == 3) {
            if (value > 0) return this.axis.get(8);
        }
        if (axisCode == 4) {
            if (value > 0) { //Mapping L2[0,1]
                this.axisMapper.setL2(value);
                if(value < 0.1) this.axisMapper.setL2(0);
                return this.axis.get(9);
            }else{//Mapping R2[-1,0]
                this.axisMapper.setR2(-value);
                if(value > -0.1) this.axisMapper.setR2(0);
            }
        }
        return this.axis.get(axisCode);
    }
    public void setLeftAngle(Controller controller,int axisCode, float value) {
        //Left stick
        if (axisCode > 1 || Math.abs(value) < 0.004) return;

        if (axisCode == 0) this.leftY = value;
        if (axisCode == 1) this.leftX = value;
        if(Math.abs(this.leftX) < 0.5 && Math.abs(this.leftY) < 0.5){
            this.leftX = 0;
            this.leftY = 0;
            if(controller.getName().equals("Wireless Controller")){
                this.axisMapper.setRightStickX(this.leftX);
                this.axisMapper.setRightStickY(this.leftY);
            }else{
                this.axisMapper.setLeftStickX(this.leftX);
                this.axisMapper.setLeftStickY(this.leftY);
            }
            return;
        }

        double hyp = Math.sqrt(Math.pow(leftX, 2) + Math.pow(leftY, 2));
        double rad = Math.asin((leftY / hyp));
        double angle = rad * (180 / Math.PI);
        if (leftX <= 0 && leftY <= 0)
            angle += 90;
        else if (leftX <= 0)
            angle += 90;
        else if (leftY > 0)
            angle = (-1.0f * angle) + 270;
        else if (leftY <= 0)
            angle = (-1.0f * angle) + 270;
        this.leftAngle = (float) angle;

        if(controller.getName().equals("Wireless Controller")){
            this.axisMapper.setRightStickX(this.leftX);
            this.axisMapper.setRightStickY(this.leftY);
            this.axisMapper.setRightStickAngle(this.leftAngle);
        }
        else{
            this.axisMapper.setLeftStickX(this.leftX);
            this.axisMapper.setLeftStickY(this.leftY);
            this.axisMapper.setLeftStickAngle(this.leftAngle);
        }
    }
    public void setRightAngle(Controller controller, int axisCode, float value) {
        //right stick
        if (axisCode <2 || axisCode > 3 || Math.abs(value) < 0.004) return;

        if (axisCode == 2) this.rightY = value;
        if (axisCode == 3) this.rightX = value;
        if(Math.abs(this.rightX) < 0.5 && Math.abs(this.rightY) < 0.5){
            this.rightX = 0;
            this.rightY = 0;
            if(controller.getName().equals("Wireless Controller")){
                this.axisMapper.setLeftStickX(this.rightX);
                this.axisMapper.setLeftStickY(this.rightY);
            }else{
                this.axisMapper.setRightStickX(this.rightX);
                this.axisMapper.setRightStickY(this.rightY);
            }
            return;
        }
        double hyp = Math.sqrt(Math.pow(rightX, 2) + Math.pow(rightY, 2));
        double rad = Math.asin((rightY / hyp));
        double angle = rad * (180 / Math.PI);
        if (rightX <= 0 && rightY <= 0)
            angle += 90;
        else if (rightX <= 0 && rightY > 0)
            angle += 90;
        else if (rightX > 0 && rightY > 0)
            angle = (-1.0f * angle) + 270;
        else if (rightX > 0 && rightY <= 0)
            angle = (-1.0f * angle) + 270;
        this.rightAngle = (float) angle;

        if(controller.getName().equals("Wireless Controller")){
            this.axisMapper.setLeftStickX(this.rightX);
            this.axisMapper.setLeftStickY(this.rightY);
            this.axisMapper.setLeftStickAngle(this.rightAngle);
        }
        else{
            this.axisMapper.setRightStickX(this.rightX);
            this.axisMapper.setRightStickY(this.rightY);
            this.axisMapper.setRightStickAngle(this.rightAngle);
        }

    }
}

public class ControllerHandler implements ControllerListener {

    public ControllerMap controllerMap;
    public GameControllerListener gameControllerListener;
    private int player;

    public ControllerHandler(int i){
        this.player = i;
        controllerMap = new ControllerMap();
    }
    public void addGameControllerListener(GameControllerListener gameControllerListener){
        this.gameControllerListener = null;
        this.gameControllerListener = gameControllerListener;
    }
    @Override
    public void connected(Controller controller) {
        System.out.println(controller + " connected");
    }

    @Override
    public void disconnected(Controller controller) {
        System.out.println(controller + " disconnected");
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if(this.gameControllerListener != null) this.gameControllerListener.buttonDown(this.player, this.controllerMap.getButton(controller, buttonCode));
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        if(this.gameControllerListener != null) this.gameControllerListener.buttonUp(this.player, this.controllerMap.getButton(controller, buttonCode));
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if(this.gameControllerListener != null) this.gameControllerListener.axisMoved(this.player, this.controllerMap.getAxis(controller, axisCode,value),this.controllerMap.axisMapper);
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        if(this.gameControllerListener != null) this.gameControllerListener.povMoved(this.player, this.controllerMap.getPOV(value));
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        System.out.println("xSliderMoved moved " + sliderCode + " : " + value);
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        System.out.println("ySliderMoved moved " + sliderCode + " : " + value);
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        System.out.println("accelerometerMoved moved " + accelerometerCode + " : " + value);
        return false;
    }
}
