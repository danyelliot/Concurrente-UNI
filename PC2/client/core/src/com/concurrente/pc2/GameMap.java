package com.concurrente.pc2;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class GameMap{
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;
    World world;
    public GameMap(OrthographicCamera camera, boolean debug){
        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawBodies(debug);
        debugRenderer.setDrawContacts(debug);
        debugRenderer.setDrawAABBs(debug);
        debugRenderer.setDrawInactiveBodies(debug);
        map = new TmxMapLoader().load("map2.tmx");
        world = new World(new Vector2(0,0),true);
        this.camera = camera;
        loadCollisions();
        renderer = new OrthogonalTiledMapRenderer(map);
        renderer.setView(camera);
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                Object userDataA = fixtureA.getBody().getUserData();
                Object userDataB = fixtureB.getBody().getUserData();
                if (userDataB instanceof Bullet) {
                    if (userDataA instanceof Chopper){
                        ((Chopper) userDataA).makeDamage(10);
                    }
                    ((Bullet) userDataB).destroy();
                }

            }

            @Override
            public void endContact(Contact contact) {
            }
            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }
            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }
    public World getWorld(){
        return world;
    }
    public void dispose(){
        map.dispose();
        renderer.dispose();
        debugRenderer.dispose();
    }

    public void render() {
        renderer.render();
        debugRenderer.render(world, camera.combined);

    }
    private void loadCollisions(){
        MapLayer collisionLayer =  map.getLayers().get("collisions");
        for (MapObject object : collisionLayer.getObjects()) {
            if(object instanceof PolygonMapObject){
                Polygon polygon = ((PolygonMapObject) object).getPolygon();

                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                float[] vertices = polygon.getTransformedVertices();
                Vector2[] worldVertices = new Vector2[vertices.length / 2];
                for (int i = 0; i < worldVertices.length; i++) {
                    worldVertices[i] = new Vector2(vertices[i * 2], vertices[i * 2 + 1]);
                }

                bodyDef.position.set(0, 0);
                Body body = world.createBody(bodyDef);

                ChainShape shape = new ChainShape();
                shape.createLoop(worldVertices);

                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.filter.categoryBits = 0x0001;
                body.createFixture(fixtureDef);

                body.setUserData(object);
                shape.dispose();
            }
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(rect.x + rect.width / 2, rect.y + rect.height / 2);

                Body body = world.createBody(bodyDef);

                PolygonShape shape = new PolygonShape();

                shape.setAsBox(rect.width / 2, rect.height / 2);

                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.filter.categoryBits = 0x0001;

                body.createFixture(fixtureDef);
                body.setUserData(object);

                shape.dispose();
            }
        }
    }

}
