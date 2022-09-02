package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter
{
	SpriteBatch batch;
	int clk;
	Anim animation;
	boolean dir = true;
	int frameSetX = 0;

	@Override
	public void create ()
	{
		batch = new SpriteBatch();
//		animation = new Anim("animation.png", 7, 4, Animation.PlayMode.LOOP); Заменена на строчку ниже
		animation = new Anim(Animation.PlayMode.LOOP);

	}

	@Override
	public void render ()
	{
		ScreenUtils.clear(1, 0, 0.5f, 1);

		animation.setTime(Gdx.graphics.getDeltaTime());

		float x = Gdx.input.getX() - animation.getFrame().getRegionWidth()/2;
		float y = Gdx.graphics.getHeight()-Gdx.input.getY() - animation.getFrame().getRegionHeight()/2;

//		if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) clk++;
//		if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) clk--;

//		Gdx.graphics.setTitle("Сделан(о) " + clk + " клик(ов)");

		if (Gdx.input.isKeyJustPressed(Input.Keys.R))
		{
			dir = false;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.L))
		{
			dir = true;
		}

		if (frameSetX + 130 >= Gdx.graphics.getWidth())
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
			frameSetX += 3;
		}
		else
		{
			frameSetX -= 3;
		}

		batch.begin();
		batch.draw(animation.getFrame(), frameSetX, 0);
//		batch.draw(img, x, y);
		batch.end();
	}
	
	@Override
	public void dispose ()
	{
		batch.dispose();
		animation.dispose();
	}
}
