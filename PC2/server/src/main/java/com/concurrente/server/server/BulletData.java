package com.concurrente.server.server;

import java.net.Socket;

public class BulletData {
    private float x;
    private float y;
    private int index;
    private float rotation;
    private Socket socket;
    public BulletData(int index,float x, float y, float rotation, Socket socket){
        this.x = x;
        this.y = y;
        this.index = index;
        this.rotation = rotation;
        this.socket = socket;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public int getIndex(){
        return index;
    }
}
