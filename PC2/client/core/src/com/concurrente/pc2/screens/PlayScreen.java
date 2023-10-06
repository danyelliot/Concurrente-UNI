package com.concurrente.pc2.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.concurrente.pc2.Chopper;
import com.concurrente.pc2.GameMap;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;

public class PlayScreen implements Screen {
    SpriteBatch batch;
    Chopper clientChopper;
    float speed = 80.0f;
    private GameMap map;
    private Socket clientSocket;
    private OrthographicCamera camera;
    private int index;
    InputStream inputStream;
    DataInputStream dataInputStream;
    private Vector<Chopper> players;
    public PlayScreen(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        clientSocket.setTcpNoDelay(true);
        inputStream = clientSocket.getInputStream();
        dataInputStream = new DataInputStream(inputStream);
        index = dataInputStream.readInt();
        players = new Vector<>();
        loadMap();
        setInitialPosition();
        for(int i = 0; i < index; i++){
            loadOtherPlayers();
        }
        players.add(clientChopper);
    }
    public void loadOtherPlayers() throws IOException{
        int indexLocal = dataInputStream.readInt();
        float x = dataInputStream.readFloat();
        float y = dataInputStream.readFloat();
        Chopper chopper = new Chopper(indexLocal,x,y,false,map.getWorld());
        players.add(chopper);
        System.out.println("Cliente " + indexLocal + " conectado");
    }
    private void setInitialPosition() throws IOException{
        float x = dataInputStream.readFloat();
        float y = dataInputStream.readFloat();
        clientChopper = new Chopper(index,x,y,true,map.getWorld());
        System.out.println("Cliente " + index + " conectado");
    }
    @Override
    public void show() {
        batch = new SpriteBatch();
        Thread inputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    handleInput();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        inputThread.start();
        Thread sendData = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        clientChopper.sendData(clientSocket);
                        Thread.sleep(100);
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        sendData.start();
        Thread recieveData = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    inputStream = clientSocket.getInputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                dataInputStream = new DataInputStream(inputStream);
                while (true) {
                    try {
                        for (Chopper chopper : players) {
                            byte[] buffer = new byte[1024];
                            String data = new String(buffer,0,inputStream.read(buffer));
                            final String[] dataSplit = data.split(",");
                            final int sizeTemp = Integer.parseInt(dataSplit[0]);
                            if (sizeTemp != players.size()) {
                                Gdx.app.postRunnable(new Runnable() {
                                    @Override
                                    public void run() {

                                        for(int i = players.size(); i < sizeTemp; i++){
                                            Chopper chopper = new Chopper(i,0,0,false, map.getWorld());
                                            players.add(chopper);
                                            System.out.println("Cliente " + i + " conectado");
                                        }
                                    }
                                });
                            }
                            int i = Integer.parseInt(dataSplit[1]);
                            float x = Float.parseFloat(dataSplit[2]);
                            float y = Float.parseFloat(dataSplit[3]);
                            float rotation = Float.parseFloat(dataSplit[4]);
                            boolean bIsActive = Boolean.parseBoolean(dataSplit[5]);
                            if(i == index){
                                continue;
                            }
                            players.get(i).translate(x,y);
                            players.get(i).setRotation(rotation);
                            players.get(i).setActive(bIsActive);
                        }
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        recieveData.start();

    }

    public void loadMap() {
        camera = new OrthographicCamera();
        float viewportWidth = Gdx.graphics.getWidth();
        float viewportHeight = Gdx.graphics.getHeight();
        camera.setToOrtho(false, viewportWidth, viewportHeight);
        map = new GameMap(camera, true);
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(55/255.0f, 102/255.0f, 108/255.0f, 1);
        map.render();
        batch.begin();
        for(Chopper chopper : players){
            chopper.draw(batch);
        }
        batch.end();
        clientChopper.debugMode();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (Chopper chopper : players) {
            chopper.dispose();
        }
        map.dispose();
        try {
            clientChopper.sendDisconnect(clientSocket);
            clientSocket.close();
            clientChopper.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void handleInput() {
        boolean bCanMove = false;
        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dx = -speed;
            bCanMove = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dx = speed;
            bCanMove = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            dy = speed;
            bCanMove = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            dy = -speed;
            bCanMove = true;
        }
        if (bCanMove) {
            clientChopper.forceMove(dx, dy);
        }else{
            clientChopper.stopMove();
        }
    }
}
