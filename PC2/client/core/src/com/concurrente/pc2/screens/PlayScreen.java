package com.concurrente.pc2.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.concurrente.pc2.Chopper;
import com.concurrente.pc2.GameMap;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

public class PlayScreen implements Screen {
    SpriteBatch batch;
    Chopper clientChopper;
    float speed = 20.0f;
    private AtomicReference<Float> dx = new AtomicReference<>(0f);
    private AtomicReference<Float> dy = new AtomicReference<>(0f);
    private GameMap map;
    private Socket clientSocket;
    private OrthographicCamera camera;
    public PlayScreen(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        clientSocket.setTcpNoDelay(true);
        InputStream inputStream = clientSocket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        int index = dataInputStream.readInt();
        int x = dataInputStream.readInt();
        int y = dataInputStream.readInt();
        clientChopper = new Chopper(x,y,true);
    }
    @Override
    public void show() {
        batch = new SpriteBatch();
        loadMap();
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
    }

    public void loadMap() {
        camera = new OrthographicCamera();
        float viewportWidth = Gdx.graphics.getWidth();
        float viewportHeight = Gdx.graphics.getHeight();
        System.out.println("viewportWidth = " + viewportHeight);
        camera.setToOrtho(false, viewportWidth, viewportHeight);
        map = new GameMap(camera);
    }
    @Override
    public void render(float delta) {
        clientChopper.move(dx.get(), dy.get());
        ScreenUtils.clear(1, 0, 0, 1);
        map.render();
        batch.begin();
        clientChopper.draw(batch);
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
        clientChopper.dispose();
        map.dispose();
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dx.set(-speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dx.set(speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            dy.set(speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            dy.set(-speed);
        }
        dx.set(dx.get() * Gdx.graphics.getDeltaTime() * speed);
        dy.set(dy.get() * Gdx.graphics.getDeltaTime() * speed);
    }
}
