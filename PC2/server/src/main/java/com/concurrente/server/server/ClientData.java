package com.concurrente.server.server;

public class ClientData {
    private int index;
    private int money;
    private int energy;
    private int x;
    private int y;
    public ClientData(int index){
        this.index = index;
    }
    public void update(int x, int y){
        this.x = x;
        this.y = y;
    }

}
