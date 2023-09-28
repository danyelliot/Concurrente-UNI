package com.concurrente.pc2;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameMap{
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    public GameMap(OrthographicCamera camera){
        map = new TmxMapLoader().load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        this.camera = camera;
        renderer.setView(camera);
    }

    public void dispose(){
        map.dispose();
        renderer.dispose();
    }

    public void render(){
        renderer.render();
    }
}
