package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.Main;

public class GameScreen implements Screen
{
    private Main game;
    private SpriteBatch batch;
    private Texture img;
    private Rectangle startRect;
    private ShapeRenderer shapeRenderer;
    Anim animation;
    boolean dir = true;
    int frameSetX = 0;

    public GameScreen(Main game)
    {
        this.game = game;
        batch = new SpriteBatch();
        img = new Texture("win.png");
        startRect = new Rectangle(0, 0, img.getWidth(), img.getHeight());
        shapeRenderer = new ShapeRenderer();
//        batch = new SpriteBatch();
        animation = new Anim("animation.png", 7, 4, Animation.PlayMode.LOOP);
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
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

        if (frameSetX + 30 >= Gdx.graphics.getWidth())
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
        batch.begin();
        batch.draw(img, 0, 0);
        batch.draw(animation.getFrame(), frameSetX, 0);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(startRect.x, startRect.y, startRect.width, startRect.height);
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
        this.shapeRenderer.dispose();
        this.animation.dispose();
    }
}
