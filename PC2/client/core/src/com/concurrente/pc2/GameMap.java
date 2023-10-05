package com.concurrente.pc2;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class GameMap{
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;
    World world;
    public GameMap(OrthographicCamera camera, boolean debug){
        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawBodies(debug);
        debugRenderer.setDrawContacts(debug);
        debugRenderer.setDrawAABBs(debug);
        debugRenderer.setDrawInactiveBodies(debug);
        map = new TmxMapLoader().load("map.tmx");
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
                System.out.println(userDataA.getClass());
                System.out.println(userDataB.getClass());

                if ((userDataA instanceof GameMap || userDataA instanceof Chopper) && (userDataB instanceof GameMap || userDataB instanceof Chopper)) {
                    System.out.println("Chocaron");
                    if (userDataA instanceof Chopper) {
                        ((Chopper) userDataA).moveBack();
                        return;
                    }
                    ((Chopper) userDataB).moveBack();
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
        world.step(1/60f, 6, 2);
        debugRenderer.render(world, camera.combined);

    }
    private void loadCollisions(){
        MapLayer collisionLayer =  map.getLayers().get("collisions");
        System.out.println(collisionLayer.getObjects().getCount());
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
                body.setUserData(object);

                ChainShape shape = new ChainShape();
                shape.createLoop(worldVertices);

                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                body.createFixture(fixtureDef);

                shape.dispose();
            }
        }
    }

}
