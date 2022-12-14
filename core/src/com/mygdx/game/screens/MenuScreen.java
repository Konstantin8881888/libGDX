package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Main;

public class MenuScreen implements Screen
{

    private final Main game;
    private final SpriteBatch batch;
    private final Texture img;
    private final Rectangle startRect;
    private final ShapeRenderer shapeRenderer;
    private final Music music;
    private final Sound sound;
    private final Sound sound2;

    public MenuScreen(Main game)
    {
        this.game = game;
        batch = new SpriteBatch();
        img = new Texture("win.png");
        startRect = new Rectangle(0, 0, img.getWidth(), img.getHeight());
        shapeRenderer = new ShapeRenderer();

        music = Gdx.audio.newMusic(Gdx.files.internal("venera.mp3"));
        music.setLooping(true);
        music.setVolume(0.1f);
        music.play();

        sound = Gdx.audio.newSound(Gdx.files.internal("orujie.mp3"));
        sound2 = Gdx.audio.newSound(Gdx.files.internal("0085.mp3"));
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(Color.BLUE);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(startRect.x, startRect.y, startRect.width, startRect.height);
        shapeRenderer.end();


        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (startRect.contains(x, y))
            {
                sound2.play();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dispose();
                game.setScreen(new GameScreen(game));
            }
            else
            {
                sound.play();
            }
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
        this.music.dispose();
        this.sound.dispose();
        this.sound2.dispose();
    }
}
