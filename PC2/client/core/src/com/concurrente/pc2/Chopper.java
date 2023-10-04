package com.concurrente.pc2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.io.*;
import java.net.Socket;

public class Chopper implements Serializable {
    private Sprite sprite;
    private int money;
    private int energy;
    private int index;
    private Vector2 position;
    private final boolean isDebug;
    private ShapeRenderer shapeRenderer;
    private OutputStream out;
    private boolean bIsActive;

    public Chopper(int index,float x, float y,boolean isDebug){
        this.bIsActive = true;
        this.index = index;
        this.isDebug = isDebug;
        Texture texture = new Texture("chopper.png");
        this.sprite = new Sprite(texture);
        this.sprite.setScale(0.1f);
        this.money = 0;
        this.energy = 100;
        float scale = .7f;
        sprite.setSize(sprite.getWidth() * scale, sprite.getHeight() * scale);
        sprite.setScale(scale);
        position = new Vector2(x, y);
        sprite.setPosition(x, y);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        if (this.isDebug) {
            shapeRenderer = new ShapeRenderer();
        }
    }

    public void draw(SpriteBatch batch) {
        if (!bIsActive){
            return;
        }
        sprite.draw(batch);
    }
    public void setActive(boolean bIsActive){
        this.bIsActive = bIsActive;
    }

    public void move(float dx, float dy) {
        position.x += dx;
        position.y += dy;
        if(dx < 0){
            sprite.setRotation(180);
        }else if(dx > 0){
            sprite.setRotation(0);
        }
        sprite.translate(dx, dy);
    }
    public void translate(float dx, float dy){
        sprite.setPosition(dx, dy);
    }

    public void debugMode() {
        if (isDebug) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.rect(sprite.getBoundingRectangle().x, sprite.getBoundingRectangle().y, sprite.getBoundingRectangle().width, sprite.getBoundingRectangle().height);
            shapeRenderer.end();
        }
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
    public float getX() {
        return sprite.getX();
    }
    public float getY() {
        return sprite.getY();
    }

    public void sendData(Socket clientSocket) throws IOException {
        out = clientSocket.getOutputStream();
        String data = getIndex() + "," + getX() + "," + getY() + "," + sprite.getRotation();
        out.write("update".getBytes());
        out.write(data.getBytes());
        out.flush();
    }
    public void sendDisconnect(Socket clientSocket) throws IOException {
        out = clientSocket.getOutputStream();
        String data = getIndex() + "";
        out.write("disconnect".getBytes());
        out.write(data.getBytes());
        out.flush();
    }

    public void setMoney(int money){
        this.money = money;
    }
    public void setEnergy(int energy){
        this.energy = energy;
    }
    public int getIndex(){
        return index;
    }
    public void setRotation(float rotation){
        this.sprite.setRotation(rotation);
    }
}
