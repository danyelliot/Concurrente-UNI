package com.concurrente.pc2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Bullet {
    private final float x;
    private final float y;
    private final Sprite sprite;
    private Body bulletBody;
    private boolean bCanDestroy = false;
    private float speed = 8000.0f;
    public Bullet(float x, float y, float rotation, World world){
        this.x = x;
        this.y = y;
        Texture texture = new Texture("bullet.png");
        sprite = new Sprite(texture);
        sprite.setPosition(x,y);
        sprite.setRotation(rotation);
        if (rotation == 180){
            speed = -speed;
        }
        createBody(world);
        addMovement();
    }
    void createBody(World world){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 6, sprite.getHeight() / 10);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        bulletBody = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        fixtureDef.filter.categoryBits = 0x0003;
        fixtureDef.filter.maskBits = ~0x0003 | 0x0001 | 0x0002;
        bulletBody.createFixture(fixtureDef);
        bulletBody.setUserData(this);
        shape.dispose();
    }
    void addMovement(){
        bulletBody.setLinearVelocity(new Vector2(speed,0));
    }
    public void draw(SpriteBatch batch){
        sprite.setPosition(bulletBody.getPosition().x - sprite.getWidth()/2, bulletBody.getPosition().y - sprite.getHeight()/2);
        sprite.draw(batch);
    }
    public void destroy(){
        bCanDestroy = true;
    }
    public void dispose(World world){
        sprite.getTexture().dispose();
        world.destroyBody(bulletBody);
    }
    public boolean canDestroy(){
        return bCanDestroy;
    }
}
