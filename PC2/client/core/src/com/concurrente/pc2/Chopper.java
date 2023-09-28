package com.concurrente.pc2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Chopper {
    private Sprite sprite;
    private int money;
    private int energy;
    private Vector2 position;
    private final boolean isDebug;
    private ShapeRenderer shapeRenderer;

    public Chopper(boolean isDebug){
        this.isDebug = isDebug;
        Texture texture = new Texture("chopper.png");
        this.sprite = new Sprite(texture);
        this.sprite.setScale(0.1f);
        this.money = 0;
        this.energy = 100;
        float scale = 1.1f;
        sprite.setSize(sprite.getWidth() * scale, sprite.getHeight() * scale);
        sprite.setScale(scale);
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float spriteWidth = sprite.getWidth();
        float spriteHeight = sprite.getHeight();
        float spriteX = (screenWidth - spriteWidth) / 2;
        float spriteY = (screenHeight - spriteHeight) / 2;
        this.position = new Vector2(spriteX, spriteY);
        sprite.setPosition(spriteX, spriteY);
        if (this.isDebug) {
            shapeRenderer = new ShapeRenderer();
        }
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void move(float dx, float dy) {
        position.x += dx;
        position.y += dy;
        sprite.translate(dx, dy);
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
    public float getWidth() {
        return sprite.getWidth();
    }
    public float getHeight() {
        return sprite.getHeight();
    }
}
