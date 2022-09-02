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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.Main;
import com.mygdx.game.PhysX;

import java.util.Arrays;

public class GameScreen implements Screen
{
    private final Main game;
    private final SpriteBatch batch;
    private Texture img;
    private Rectangle startRect;
    private final Anim animation;
    private boolean dir = true;
    private int frameSetX = 0;
    private final OrthographicCamera camera;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Vector2 mapPosition;
    private final float step = 12;
//    private final Rectangle mapSize;
    private final ShapeRenderer shapeRenderer;
    private final int[] bg;
    private final int[] l1;
    private PhysX physX;
    private Body body;
    private final Rectangle heroRect;

    public GameScreen(Main game)
    {
        this.game = game;
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        img = new Texture("win.png");
//        startRect = new Rectangle(0, 0, img.getWidth(), img.getHeight());

//        animation = new Anim("animation.png", 7, 4, Animation.PlayMode.LOOP); Заменена на строчку ниже
        animation = new Anim(Animation.PlayMode.LOOP);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1.28f;

        map = new TmxMapLoader().load("map/map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        mapPosition = new Vector2();

        bg = new int[1];
        bg[0] = map.getLayers().getIndex("Фон");
        l1 = new int[1];
        l1[0] = map.getLayers().getIndex("Рельеф");
//        l1[1] = map.getLayers().getIndex("Фон");
        physX = new PhysX();
//        map.getLayers().get("Объекты").getObjects().getByType(RectangleMapObject.class); Второе(камера) выбрано по типу но можно и по имени get("камера")? Ниже:
        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("Сеттинг").getObjects().get("hero");
        heroRect = tmp.getRectangle();
        body = physX.addObject(tmp);

        Array<RectangleMapObject> objects = map.getLayers().get("Объекты").getObjects().getByType(RectangleMapObject.class);
//        mapSize = tmp.getRectangle();

        for (int i = 0; i < objects.size; i++)
        {
            physX.addObject(objects.get(i));
        }
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        camera.position.x = body.getPosition().x;
        camera.position.y = body.getPosition().y;
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
        {
            body.applyForceToCenter(new Vector2(-1000000, 0), true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
        {
            body.applyForceToCenter(new Vector2(1000000, 0), true);
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

        mapRenderer.setView(camera);
        mapRenderer.render(bg);

        System.out.println(body.getLinearVelocity());

        batch.setProjectionMatrix(camera.combined);
        heroRect.x = body.getPosition().x - heroRect.width/2;
        heroRect.y = body.getPosition().y - heroRect.height/2;
        batch.begin();
        batch.draw(animation.getFrame(), heroRect.x, heroRect.y, heroRect.width, heroRect.height);
        batch.draw(animation.getFrame(), frameSetX, 0);
        batch.end();


//        shapeRenderer.setProjectionMatrix(camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(Color.RED);
//        for (int i = 0; i < objects.size; i++)
//        {
//            Rectangle mapSize = objects.get(i).getRectangle();
//            shapeRenderer.rect(mapSize.x, mapSize.y, mapSize.width, mapSize.height);
//        }
//        shapeRenderer.end();

        mapRenderer.render(l1);
        physX.step();
        physX.debugDraw(camera);

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
        this.img.dispose();
        this.animation.dispose();
    }
}
