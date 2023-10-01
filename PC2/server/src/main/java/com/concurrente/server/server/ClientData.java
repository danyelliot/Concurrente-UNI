package com.concurrente.server.server;

public class ClientData {
    private int index;
    private int money;
    private int energy;
    private float x;
    private float y;
    public ClientData(int index){
        this.index = index;
    }
    public void update(float x, float y){
        this.x = x;
        this.y = y;
        //System.out.println("Cliente " + index + " actualizado en " + x + ", " + y);
    }

}
