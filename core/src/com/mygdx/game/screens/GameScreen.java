package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import static com.mygdx.game.PhysX.PPM;


public class GameScreen implements Screen
{
    private final Main game;
    private final SpriteBatch batch;
//    private Texture img;
//    private Rectangle startRect;
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
    private final Music music;
    private final Sound sound;
    public static ArrayList<Body> bodies;

    public GameScreen(Main game)
    {
        this.game = game;
        bodies = new ArrayList<>();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
//        img = new Texture("win.png");
        music = Gdx.audio.newMusic(Gdx.files.internal("ocean.mp3"));
        music.setLooping(true);
        music.setVolume(0.1f);
        music.play();
        sound = Gdx.audio.newSound(Gdx.files.internal("XP.mp3"));
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
        camera.position.x = body.getPosition().x*PPM;
        camera.position.y = body.getPosition().y*PPM;

        camera.update();
        ScreenUtils.clear(Color.BROWN);

        animation.setTime(Gdx.graphics.getDeltaTime());

        float x = Gdx.input.getX() - animation.getFrame().getRegionWidth()/2;
        float y = Gdx.graphics.getHeight()-Gdx.input.getY() - animation.getFrame().getRegionHeight()/2;



//        if (Gdx.input.isKeyJustPressed(Input.Keys.R))
//        {
//            dir = false;
//        }
//        if (Gdx.input.isKeyJustPressed(Input.Keys.L))
//        {
//            dir = true;
//        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
        {

            body.applyForceToCenter(new Vector2(-2000f, 0), true);
            dir = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
        {
            body.applyForceToCenter(new Vector2(2000f, 0), true);
            dir = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
        {
            body.applyForceToCenter(new Vector2(0, 20000f), true);

        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_SUBTRACT))
        {
            camera.zoom += 0.1f;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ADD) && camera.zoom > 0)
        {
            camera.zoom -= 0.1f;
        }


//        if (frameSetX - 120 >= Gdx.graphics.getWidth())
//        {
//            dir = false;
//        }
//        if (frameSetX <= 0)
//        {
//            dir = true;
//        }





        if (dir)
        {

            frameSetX += 1;
        }
        else
        {
            frameSetX -= 1;
        }
        if (!animation.getFrame().isFlipX() && dir) {
            animation.getFrame().flip(false, false);
        }
        if (animation.getFrame().isFlipX() && dir) {
            animation.getFrame().flip(true, false);
        }
        if (animation.getFrame().isFlipX() && !dir) {
            animation.getFrame().flip(false, false);
        }
        if (!animation.getFrame().isFlipX() && !dir) {
            animation.getFrame().flip(true, false);

        }


        ScreenUtils.clear(Color.BROWN);

        mapRenderer.setView(camera);
        mapRenderer.render(bg);

        System.out.println(body.getLinearVelocity());


//        batch.setProjectionMatrix(camera.combined);
        heroRect.x = body.getPosition().x - heroRect.width/2;
        heroRect.y = body.getPosition().y - heroRect.height/2;

        float xx = Gdx.graphics.getWidth()/2 - heroRect.getWidth()/2/camera.zoom;
        float yy = Gdx.graphics.getHeight()/2 - heroRect.getHeight()/2/camera.zoom;
        batch.begin();
        batch.draw(animation.getFrame(), xx, yy, heroRect.width, heroRect.height);
//        batch.draw(animation.getFrame(), frameSetX, 0);

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

        for (int i = 0; i < bodies.size(); i++)
        {
            sound.play();
            physX.destroyBody(bodies.get(i));
        }
        bodies.clear();

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
        this.music.dispose();
        this.sound.dispose();
    }
}
