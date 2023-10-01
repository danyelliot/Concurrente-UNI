package com.concurrente.server.server;

public class Vector2 {
    private float x;
    private float y;
    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }
    public void set(float x, float y){
        this.x = x;
        this.y = y;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
}
