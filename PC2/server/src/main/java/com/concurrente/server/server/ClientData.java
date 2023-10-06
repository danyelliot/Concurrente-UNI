package com.concurrente.server.server;

import java.net.Socket;

public class ClientData {
    private int index;
    private int money;
    private int energy;
    private float x;
    private float y;
    private float rotation;
    private boolean bIsActive;
    private Socket socket;
    public ClientData(int index, Socket socket){
        this.index = index;
        this.bIsActive = true;
        this.socket = socket;
    }
    public void update(float x, float y, float rotation, int energy, int money){
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.energy = energy;
        this.money = money;
    }
    public void setActive(boolean bIsActive){
        this.bIsActive = bIsActive;
    }
    public int getIndex(){
        return index;
    }
    public int getMoney(){
        return money;
    }
    public int getEnergy(){
        return energy;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public boolean isActive(){
        return bIsActive;
    }
    public Socket getSocket(){
        return socket;
    }
    public void setRotation(float rotation){
        this.rotation = rotation;
    }
    public float getRotation(){
        return rotation;
    }
}
