package com.flyu.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture birds[];
	int index = 0;
	float birdY = 0;
	float velocity = 0;

	int gameState = 0;
	float gravity = 2;

	int birdHeight = 130;
	int birdWidth = 130;


	Texture topTube;
	Texture bottomTube;

	float gap = 500;

	float bTubeheight = 0;
	float tTubeheight = 0;

	float bTubeWidth = 0;
	float tTubeWidth = 0;


	float maxTubeOffset ;
	Random randomGenerator;

	float tubeVelocity = 4;
    int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeoffset = new float[numberOfTubes];
    float distanceBetweenTubes;


    Circle birdCircle ;
	ShapeRenderer shapeRenderer ;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;


	int score = 0;
	int scoring_tube = 0;

	Texture unitDigit;
	Texture tenthDigit;

	Texture gameStartScreen;
	Texture gameOverScreen;

	int gameOver = 0;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background-night.png");
        birds = new Texture[18];
		birds[0] = new Texture("redbird-upflap.png");
		birds[1] = new Texture("redbird-upflap.png");
		birds[2] = new Texture("redbird-upflap.png");

		birds[3] = new Texture("redbird-downflap.png");
		birds[4] = new Texture("redbird-downflap.png");
		birds[5] = new Texture("redbird-downflap.png");

		birds[6] = new Texture("bluebird-upflap.png");
		birds[7] = new Texture("bluebird-upflap.png");
		birds[8] = new Texture("bluebird-upflap.png");

		birds[9] = new Texture("bluebird-downflap.png");
		birds[10] = new Texture("bluebird-downflap.png");
		birds[11] = new Texture("bluebird-downflap.png");

		birds[12] = new Texture("yellowbird-upflap.png");
		birds[13] = new Texture("yellowbird-upflap.png");
		birds[14] = new Texture("yellowbird-upflap.png");

		birds[15] = new Texture("yellowbird-downflap.png");
		birds[16] = new Texture("yellowbird-downflap.png");
		birds[17] = new Texture("yellowbird-downflap.png");

		index = 0;

		bottomTube = new Texture("pipe-bottom.png");
		topTube = new Texture("pipe-top.png");

		bTubeheight = Gdx.graphics.getHeight();
		tTubeheight = bTubeheight;

		bTubeWidth = 270;
		tTubeWidth = 270;

		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap/2 - 100;
		randomGenerator = new Random();


	    distanceBetweenTubes = Gdx.graphics.getWidth() * 3/4;
        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];





	    birdCircle = new Circle();
	    shapeRenderer = new ShapeRenderer();


	    gameStartScreen = new Texture("game-start.png");
	    gameOverScreen = new Texture("gameover.png");
	}

	public void startGame() {
		birdY = Gdx.graphics.getHeight()/2 - birds[index].getHeight()/2 ;
		for(int i = 0; i < numberOfTubes; i++) {

			tubeoffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

			tubeX[i] = Gdx.graphics.getWidth()/2 - tTubeWidth/2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}
		scoring_tube = 0;
		velocity = 0;
	}


	@Override
	public void render () {

		//Gdx.app.log("FINIS--gameSTATE", Integer.toString(gameState));

		if(score < 10){
			//Gdx.app.log("SCORE-UNIT", Integer.toString(0));
			//Gdx.app.log("SCORE-TENTH", Integer.toString(score));
			tenthDigit = new Texture("0.png");
			unitDigit = new Texture(Integer.toString(score)+".png");
		}else{
			int unit = score % 10;
			int tenth = score/10;
			//Gdx.app.log("SCORE-UNIT", Integer.toString(unit));
			//Gdx.app.log("SCORE-TENTH", Integer.toString(tenth));
			tenthDigit = new Texture(Integer.toString(tenth)+".png");
			unitDigit = new Texture(Integer.toString(unit)+".png");
		}

		batch.begin();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		if(gameState == 1) {


			if(tubeX[scoring_tube] + tTubeWidth  < Gdx.graphics.getWidth()) {
				score++;

				Gdx.app.log("SCORE ", Integer.toString(score));

				if(scoring_tube < numberOfTubes) {
					scoring_tube++;
				}else{
					scoring_tube = 0;
				}

			}

			if(Gdx.input.isTouched()) {
				// Whenever the Screen of the User is touched
				velocity = -30;


			}

			//Gdx.app.log("OFFSET", String.valueOf(tubeoffset[0]));
			for(int i = 0; i < numberOfTubes; i++) {

				if(tubeX[i] < -tTubeWidth) {
					tubeX[i] = (numberOfTubes-1) * distanceBetweenTubes + Gdx.graphics.getWidth()/2;
					tubeoffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				}else{
					tubeX[i] -= tubeVelocity;
				}


				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight()/2 + gap/2 + tubeoffset[i] , tTubeWidth, tTubeheight );
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight()/2 - gap / 2 - bTubeheight + tubeoffset[i], bTubeWidth, bTubeheight);

				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight()/2 + gap/2 + tubeoffset[i], tTubeWidth, tTubeheight);
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight()/2 - gap / 2 - bTubeheight + tubeoffset[i], bTubeWidth, bTubeheight);
			}


			if(birdY > 0 || velocity < 0 ) {
				velocity += gravity;
				birdY -= velocity;

				if(birdY + birdHeight > Gdx.graphics.getHeight()) {
					birdY = Gdx.graphics.getHeight() - birdHeight;
				}
			}


			index++;
			index = index % 18;

			batch.draw(birds[index], Gdx.graphics.getWidth()/2 - birdWidth/2 , birdY, birdWidth , birdHeight  );

			//shapeRenderer.setColor(Color.RED);
			//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

			for(int i = 0 ; i < numberOfTubes; i++){
				//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight()/2 + gap/2 + tubeoffset[i], tTubeWidth, tTubeheight);
				//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight()/2 - gap / 2 - bTubeheight + tubeoffset[i], bTubeWidth, bTubeheight);

				if(Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i] ) )  {
					gameState = 2;
					break;
				}
			}
		}

		else if(gameState == 0) {
			float width = Gdx.graphics.getWidth();
			float height = Gdx.graphics.getHeight();

			batch.draw(gameStartScreen, width/2 - width/4, height/2 - height/4 , width/2, height/2);
			if(Gdx.input.isTouched()) {
				// Whenever the Screen of the User is touched
				//Gdx.app.log("FINISHED", "TOUCHED");
				gameState = 1;
				score = 0;
				startGame();
			}
		}

		else if (gameState == 2){
			float width = Gdx.graphics.getWidth();
			float height = Gdx.graphics.getHeight();

			batch.draw(gameOverScreen, width/2 - width/3, height/2 - height/6 , width/1.5f, height/3);

			if(Gdx.input.isTouched()) {
				// Whenever the Screen of the User is touched
				//Gdx.app.log("FINISHED", "TOUCHED");
				gameState = 1;
				score = 0;
				startGame();
				scoring_tube = 0;
			}
		}

		//Gdx.app.log("HEIGHT BIRD", Float.toString(birdY));
		//Gdx.app.log("HEIGHT SCREEN", Float.toString(Gdx.graphics.getHeight()));

		batch.draw(tenthDigit, Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight()-150 , 150, 150);
		batch.draw(unitDigit, Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight()-150, 150, 150);



		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birdHeight/2, birdWidth/2);



		batch.end();
		shapeRenderer.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
