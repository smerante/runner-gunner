package com.studios0110.runnergunner.handler;

/**
 * Created by Sam Merante on 2018-10-20.
 */
public class AxisMapper {
    public float leftStickX,leftStickY,leftStickAngle;
    public float rightStickX,rightStickY,rightStickAngle;
    public float L2, R2;

    public AxisMapper(float leftStickX, float leftStickY, float leftStickAngle, float rightStickX, float rightStickY, float rightStickAngle, float l2, float r2) {
        this.leftStickX = leftStickX;
        this.leftStickY = leftStickY;
        this.leftStickAngle = leftStickAngle;
        this.rightStickX = rightStickX;
        this.rightStickY = rightStickY;
        this.rightStickAngle = rightStickAngle;
        L2 = l2;
        R2 = r2;
    }

    public float getLeftStickX() {
        return leftStickX;
    }

    public void setLeftStickX(float leftStickX) {
        this.leftStickX = leftStickX;
    }

    public float getLeftStickY() {
        return leftStickY;
    }

    public void setLeftStickY(float leftStickY) {
        this.leftStickY = -leftStickY;
    }

    public float getLeftStickAngle() {
        return leftStickAngle;
    }

    public void setLeftStickAngle(float leftStickAngle) {
        this.leftStickAngle = leftStickAngle;
    }

    public float getRightStickX() {
        return rightStickX;
    }

    public void setRightStickX(float rightStickX) {
        this.rightStickX = rightStickX;
    }

    public float getRightStickY() {
        return rightStickY;
    }

    public void setRightStickY(float rightStickY) {
        this.rightStickY = -rightStickY;
    }

    public float getRightStickAngle() {
        return rightStickAngle;
    }

    public void setRightStickAngle(float rightStickAngle) {
        this.rightStickAngle = rightStickAngle;
    }

    public float getL2() {
        return L2;
    }

    public void setL2(float l2) {
        L2 = l2;
    }

    public float getR2() {
        return R2;
    }

    public void setR2(float r2) {
        R2 = r2;
    }
}
