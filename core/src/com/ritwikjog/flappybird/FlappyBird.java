package com.ritwikjog.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.awt.Color;
import java.util.Random;

import sun.rmi.runtime.Log;

import static java.awt.Color.*;
import static java.awt.Color.RED;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	ShapeRenderer shapeRenderer;


	Texture[] birds;
	Texture topTube;
	Texture bottomTube;
	Texture gameOver;

	int flapState = 0;
	int gameState = 0;
	float birdY = 0;
	float velocity = 0;
	float gap = 500;
	float maxGap;
	Random random;
	float maxOffset;
	int tubeX;

	BitmapFont font;

	Circle birdCircle;
	Rectangle topTubeRectangle;
	Rectangle bottomTubeRectangle;

	int score;
	int flag;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		shapeRenderer = new ShapeRenderer();

		birdCircle = new Circle();
		topTubeRectangle = new Rectangle();
		bottomTubeRectangle = new Rectangle();
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
		maxGap = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		random = new Random();
		tubeX = Gdx.graphics.getWidth();
		score = 0;
		flag=0;

		gameOver = new Texture("gameOver.png");

		font = new BitmapFont();
		font.setColor(255,255,255,1);
		font.getData().setScale(10);

	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.draw(topTube, tubeX, Gdx.graphics.getHeight()/2 + gap/2 + maxOffset);
        batch.draw(bottomTube, tubeX, Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight() + maxOffset);
        batch.draw(birds[flapState], Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2, birdY);

		if(Gdx.input.justTouched() && gameState!=2){
			birdY+=300;
			velocity = 0;
			gameState = 1;
		}

		if(gameState == 1){
			tubeX -= 4;

			if(topTubeRectangle.x < Gdx.graphics.getWidth()/2 && flag == 0)
			{
				score++;
				flag = 1;
				Gdx.app.log("Score", Integer.toString(score));
			}

			if(birdY<0)
			{
				gameState = 2;
			}

			if(tubeX + topTube.getWidth() < 0)
			{
				flag = 0;
				tubeX = Gdx.graphics.getWidth();
				maxOffset = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()/2 - 200);
			}
			velocity++;
			birdY -= velocity;
		} else if(gameState == 2){

			batch.draw(gameOver, Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2, Gdx.graphics.getHeight()/2 - gameOver.getHeight()/2);

			if(Gdx.input.justTouched()) {
				gameState = 1;
				birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
				tubeX = Gdx.graphics.getWidth();
				velocity = 0;

			}

		}

		font.draw(batch, String.valueOf(score), 100, 200);

		if(flapState == 0)
		{
			flapState = 1;
		}
		else
		{
			flapState = 0;
		}

		batch.end();
		birdCircle.set(Gdx.graphics.getWidth()/2, birdY + birds[flapState].getHeight()/2, birds[flapState].getWidth()/2);
		topTubeRectangle.set(tubeX, Gdx.graphics.getHeight()/2 + gap/2 + maxOffset, topTube.getWidth(), topTube.getHeight());
		bottomTubeRectangle.set(tubeX, Gdx.graphics.getHeight()/2 - gap/2 + maxOffset - bottomTube.getHeight(), bottomTube.getWidth(), bottomTube.getHeight());
		if(Intersector.overlaps(birdCircle, topTubeRectangle) || Intersector.overlaps(birdCircle, bottomTubeRectangle)){
			gameState = 2;
			score = 0;
		}

	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
