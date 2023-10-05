package com.concurrente.pc2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;

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
    private Body naveBody;
    private boolean bCanMove = true;
    private Vector2 lastPosition;

    public Chopper(int index,float x, float y,boolean isDebug, World world){
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
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 3, sprite.getHeight() / 4);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);
        naveBody = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.4f;
        naveBody.createFixture(fixtureDef);
        naveBody.setUserData(this);


        shape.dispose();

        Timer.schedule(savePositionTask, 0.02f, 0.02f);
    }
    Timer.Task savePositionTask = new Timer.Task() {
        @Override
        public void run() {
            lastPosition = new Vector2(sprite.getX(), sprite.getY());
        }
    };
    public void draw(SpriteBatch batch) {
        if (!bIsActive){
            return;
        }
        sprite.draw(batch);
        //set a timer for 0.02 seconds for save the last position

    }
    public void setActive(boolean bIsActive){
        this.bIsActive = bIsActive;
    }

    public void setCanMove(boolean bCanMove){
        this.bCanMove = bCanMove;
    }


    public void move(float dx, float dy) {
        if (!bCanMove){
            return;
        }
        position.x += dx;
        position.y += dy;
        if(dx < 0){
            sprite.setRotation(180);
        }else if(dx > 0){
            sprite.setRotation(0);
        }
        sprite.translate(dx, dy);
        naveBody.setTransform(sprite.getX() + sprite.getWidth()/2 ,sprite.getY() + sprite.getHeight()/2,0);

    }
    public void moveBack(){
        sprite.setPosition(lastPosition.x, lastPosition.y);
    }
    public void translate(float dx, float dy){
        sprite.setPosition(dx, dy);
    }

    public void debugMode() {
        if (isDebug) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.rect(sprite.getBoundingRectangle().x, sprite.getBoundingRectangle().y+10, sprite.getBoundingRectangle().width, sprite.getBoundingRectangle().height-20);
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
