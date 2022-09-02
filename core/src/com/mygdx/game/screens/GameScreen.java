package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.Main;

public class GameScreen implements Screen
{
    private Main game;
    private SpriteBatch batch;
//    private Texture img;
    private Rectangle startRect;
    private Anim animation;
    private boolean dir = true;
    private int frameSetX = 0;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Vector2 mapPosition;
    private float step = 12;
    private Rectangle mapSize;
    private ShapeRenderer shapeRenderer;

    public GameScreen(Main game)
    {
        this.game = game;
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
//        img = new Texture("win.png");
//        startRect = new Rectangle(0, 0, img.getWidth(), img.getHeight());

//        animation = new Anim("animation.png", 7, 4, Animation.PlayMode.LOOP); Заменена на строчку ниже
        animation = new Anim(Animation.PlayMode.LOOP);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1.28f;

        map = new TmxMapLoader().load("map/map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        mapPosition = new Vector2();
//        map.getLayers().get("Объекты").getObjects().getByType(RectangleMapObject.class); Второе(камера) выбрано по типу но можно и по имени get("камера")? Ниже:
        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("Объекты").getObjects().get("камера");
        camera.position.x = tmp.getRectangle().x;
        camera.position.y = tmp.getRectangle().y;
        tmp = (RectangleMapObject) map.getLayers().get("Объекты").getObjects().get("граница");
        mapSize = tmp.getRectangle();
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        camera.update();
        ScreenUtils.clear(Color.BROWN);

        animation.setTime(Gdx.graphics.getDeltaTime());

        float x = Gdx.input.getX() - animation.getFrame().getRegionWidth()/2;
        float y = Gdx.graphics.getHeight()-Gdx.input.getY() - animation.getFrame().getRegionHeight()/2;



        if (Gdx.input.isKeyJustPressed(Input.Keys.R))
        {
            dir = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L))
        {
            dir = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && mapSize.x < (camera.position.x - 1))
        {
            camera.position.x -= step;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && (mapSize.x + mapSize.width) > (camera.position.x + 1))
        {
            camera.position.x += step;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
        {
            camera.position.y += step;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
        {
            camera.position.y -= step;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_SUBTRACT))
        {
            camera.zoom += 0.1f;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ADD) && camera.zoom > 0)
        {
            camera.zoom -= 0.1f;
        }

        if (frameSetX - 120 >= Gdx.graphics.getWidth())
        {
            dir = false;
        }
        if (frameSetX <= 0)
        {
            dir = true;
        }


        if (!animation.getFrame().isFlipX() && !dir)
        {
            animation.getFrame().flip(true, false);
        }
        if (animation.getFrame().isFlipX() && dir)
        {
            animation.getFrame().flip(true, false);
        }


        if (dir)
        {
            frameSetX += 2;
        }
        else
        {
            frameSetX -= 2;
        }


        ScreenUtils.clear(Color.BROWN);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
//        batch.draw(img, 0, 0);
        batch.draw(animation.getFrame(), frameSetX, 0);
        batch.end();

        mapRenderer.setView(camera);
        mapRenderer.render();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(mapSize.x, mapSize.y, mapSize.width, mapSize.height);
        shapeRenderer.end();


        if (Gdx.input.isKeyJustPressed(Input.Keys.S))
    {
        dispose();
        game.setScreen(new MenuScreen(game));
    }

    }

    @Override
    public void resize(int width, int height)
    {
        camera.viewportWidth = width;
        camera.viewportHeight = height;

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {
        this.batch.dispose();
//        this.img.dispose();
        this.animation.dispose();
    }
}
