package com.concurrente.pc2.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.IOException;

public class MenuScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private Table table;
    private TextButton playButton;
    private TextButton exitButton;
    private Label title;
    private TextField ipField;
    private TextField portField;
    Game game;

    public MenuScreen(Game game) {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());
        this.game = game;
    }
    @Override
    public void show() {
        table = new Table();
        table.setDebug(true);
        table.setWidth(stage.getWidth());
        table.align(Align.center| Align.top);
        table.setPosition(0, Gdx.graphics.getHeight());
        loadUI();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }
    void loadUI(){
        title = new Label("Battle Chopper", skin);
        title.setFontScale(2.5f);
        title.setAlignment(Align.center);
        ipField = new TextField("192.168.1.22", skin);
        ipField.setMaxLength(15);
        ipField.setAlignment(Align.center);
        ipField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return Character.isDigit(c) || c == '.';
            }
        });
        ipField.setPosition(Gdx.graphics.getWidth() / 2.0f - ipField.getWidth() / 2, Gdx.graphics.getHeight() / 2.0f - ipField.getHeight() / 2 + 50);
        portField = new TextField("222", skin);
        portField.setMaxLength(5);
        portField.setAlignment(Align.center);
        portField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return Character.isDigit(c);
            }
        });
        portField.setPosition(Gdx.graphics.getWidth() / 2.0f - portField.getWidth() / 2, Gdx.graphics.getHeight() / 2.0f - portField.getHeight() / 2 + 100);
        playButton = new TextButton("Play", skin, "default");
        playButton.setPosition(Gdx.graphics.getWidth() / 2.0f - playButton.getWidth() / 2, Gdx.graphics.getHeight() / 2.0f - playButton.getHeight() / 2);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    game.setScreen(new PlayScreen(ipField.getText(), Integer.parseInt(portField.getText())));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                dispose();
            }
        });
        exitButton = new TextButton("Exit", skin, "default");
        exitButton.setPosition(Gdx.graphics.getWidth() / 2.0f - exitButton.getWidth() / 2, Gdx.graphics.getHeight() / 2.0f - exitButton.getHeight() / 2 - 50);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                dispose();
            }
        });
        table.add(title).padTop(10);
        table.row();
        table.add(ipField).padTop(10).width(300);
        table.row();
        table.add(portField).padTop(10);
        table.row();
        table.add(playButton).padTop(10);
        table.row();
        table.add(exitButton).padTop(10);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(.5f, .5f, .5f, 1);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }
}
