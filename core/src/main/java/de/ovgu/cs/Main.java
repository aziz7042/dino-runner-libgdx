package de.ovgu.cs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.util.Random;

public class Main extends ApplicationAdapter {
    //Sound played upon collision with obstacles
    Sound collusionSound;

    //Background music
    Music music;

    //Textures for various game elements
    Texture backgroundTexture;
    Texture steinTexture;
    Texture feuerTexture;
    Texture kaktusTexture;

    Polygon steinHitbox;
    Polygon feuerHitbox;
    Polygon kaktusHitbox;
    Texture fireballTexture;
    Texture arrowTexture;

    Sprite dinoSprite;
    Polygon dinoHitbox;
    // Animation
    // Is used for all animations
    private float stateTime;
    SpriteBatch spriteBatch;
    ShapeRenderer shapeRenderer;
    FitViewport viewport;

    Array<Obstacle> obstacles;
    Array<Polygon> dropHitboxes;
    float newObstacleTimer;
    float nextDropTime = 2f;
    Dino stegosaurus;

    /**
     * Initializes the game, loading all resources and setting up the initial game state.
     * This includes loading textures, setting up animations, initializing the viewport,
     * and configuring the audio.
     */
    @Override
    public void create() {
        stateTime = 0f;
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(8, 5);

        //Audio initialization:
        collusionSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        // Can you guess the music :D ???
        // https://www.youtube.com/watch?v=MltJnhBLGtw
        //music = Gdx.audio.newMusic(Gdx.files.internal("mysterymusic.mp3"));
        //music.setLooping(true);
        //music.setVolume(.5f);
        //music.play();

        //Texture initialization:
        backgroundTexture = new Texture("cloudy_sky.png");
        steinTexture = new Texture("Stein.png");
        feuerTexture = new Texture("Feuer.png");
        kaktusTexture = new Texture("Kaktus.png");
        fireballTexture = new Texture("fireball.png");
        arrowTexture = new Texture("arrow.png");
        dropHitboxes = new Array<>();
        createHitboxes();
        Texture walkSheet = new Texture("stegosaurus_walking.png");

        obstacles = new Array<>();

        //create Dino
        stegosaurus = new Dino(walkSheet);
        stegosaurus.duckedAnimation = stegosaurus.initializeAnimation("stegosaurus_ducked.png", 2, 1);
        stegosaurus.jumpAnimation = stegosaurus.initializeAnimation("stegosaurus_jumping.png",1,1);
    }

    /**
     * Updates the viewport when the window is resized.
     *
     * @param width The new width of the window
     * @param height The new height of the window
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera
    }

      /**
     * Main game loop method. Processes input, updates game logic, and renders the frame.
     * This method is called continuously by the LibGDX framework.
     */
    @Override
    public void render() {
        // organize code into three methods
        input();
        stegosaurus.jumpHandler(Gdx.graphics.getDeltaTime());
        logic();
        draw();
    }

     /**
     * Handles player input for jumping and ducking actions.
     * Checks for space key presses for jumping and left shift for ducking.
     */
    private void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !stegosaurus.isJumping) {
            stegosaurus.isJumping = true;
            stegosaurus.jumpTimer = 0f; //reset the jump timer
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && !stegosaurus.isJumping) {
            stegosaurus.isDucking = true;
        }else stegosaurus.isDucking = false;
    }

    /**
     * Updates game logic including obstacle movement, collision detection,
     * and spawn timing. Handles removing obstacles that are off-screen or
     * have collided with the player.
     */
    private void logic() {
        //float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        stegosaurus.setY(MathUtils.clamp(stegosaurus.getY(), 0, worldHeight - stegosaurus.getWidth())); //height instead of width for this? -> since we are calculating Y-value?
        dinoHitbox.setPosition(stegosaurus.getX(), stegosaurus.getY());

        float delta = Gdx.graphics.getDeltaTime();

        manageObstacles(delta);

        // spawn new obstacle every second
        newObstacleTimer += delta;
        if (newObstacleTimer > 2f) {
            newObstacleTimer = 0;
            createObstacle();
        }
    }

    public void manageObstacles(float delta){
        for (int i = obstacles.size - 1; i >= 0; i--) {
            Polygon dropHitbox = dropHitboxes.get(i);
            Obstacle obstacle = obstacles.get(i);
            //float dropWidth = dropSprite.getWidth();
            //float dropHeight = dropSprite.getHeight();

            //Movement of the Drops
            obstacle.move(2f,delta);
            dropHitbox.setPosition(obstacle.getX(),obstacle.getY());;
            
            // Apply the drop position and size to the dropRectangle
            //obstacleRectangle.set(dropSprite.getX(), dropSprite.getY(), dropWidth, dropHeight);

            // if obstacle leaves the screen, remove it
            if (obstacle.getX() < (-1) ){
                obstacles.removeIndex(i);
                dropHitboxes.removeIndex(i);
            }
            else if (checkCollision(dropHitbox)) { // Check if the bucket overlaps the drop
                obstacles.removeIndex(i);// Remove the drop
                dropHitboxes.removeIndex(i);
                collusionSound.play();
            }
        }

        // Droplet spawn rate: 1 Droplet every sec
        newObstacleTimer += delta;
    }

    private boolean checkCollision(Polygon obstacleHitbox) {
        return Intersector.overlapConvexPolygons(dinoHitbox, obstacleHitbox);
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time


        spriteBatch.begin();

        //draw Background
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);

        //draw Dino
        stegosaurus.draw(spriteBatch, stateTime);

        //draw all obstacles
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(spriteBatch, stateTime);
        }

        spriteBatch.end();

        // REMOVE COMMENTING FOR HITBOX OUTLINE
        /*shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        for (Polygon p : dropHitboxes) {
            shapeRenderer.polygon(p.getTransformedVertices());
        }
        shapeRenderer.polygon(dinoHitbox.getTransformedVertices());
        shapeRenderer.end();*/
    }

    /**
     * Creates a new obstacle with random type and position.
     * Obstacles can be either stones, fire, or cacti, and can appear
     * either on the ground or at an elevated position.
     */
    private void createObstacle() {
        float worldWidth = viewport.getWorldWidth();

        Obstacle newObstacle;
        Polygon dropHitbox;

        Random rand = new Random();


        int rand_int1 = rand.nextInt(5);
        if (rand_int1 == 0){
            newObstacle = new GroundObstacle(steinTexture);
            newObstacle.setSize(1,1);
            dropHitbox = new Polygon(steinHitbox.getVertices());
        }
        else if (rand_int1 == 1) {
            newObstacle = new GroundObstacle(feuerTexture);
            dropHitbox = new Polygon(feuerHitbox.getVertices());
            newObstacle.setSize(1,1);
        }
        else if (rand_int1 == 2) {
            newObstacle = new GroundObstacle(kaktusTexture);
            dropHitbox = new Polygon(kaktusHitbox.getVertices());
            newObstacle.setSize(1,1);
        }
        else if (rand_int1 == 3) {
            newObstacle = new Fireball(fireballTexture);
            newObstacle.setSize(1,0.5f);
            dropHitbox = new Polygon(steinHitbox.getVertices());
        }
        else {
            newObstacle = new Arrow(arrowTexture);
            dropHitbox = new Polygon(steinHitbox.getVertices());
            newObstacle.setSize(1,0.5f);
        }

        newObstacle.setX(worldWidth-newObstacle.getWidth());

        obstacles.add(newObstacle);
        dropHitboxes.add(dropHitbox);
    }

    /**
     * Called when the game is paused.
     * Currently not implemented but reserved for future use.
     */
    @Override
    public void pause() {

    }

    /**
     * Disposes of all resources used by the game.
     * Should be called when the game is closed to prevent memory leaks.
     */

    @Override
    public void dispose() {
    }

    private Polygon loadHitboxFromFile(String path) {
        FileHandle file = Gdx.files.internal(path);
        String text = file.readString();
        String[] coordTexts = text.split(",\n");
        float[] coords = new float[coordTexts.length];
        for (int i = 0; i < coords.length; i++) {
            coords[i] = Float.parseFloat(coordTexts[i]);
        }
        return new Polygon(coords);
    }

    private void createHitboxes() {
        feuerHitbox = loadHitboxFromFile("Feuer_outline.txt");
        kaktusHitbox = loadHitboxFromFile("Kaktus_outline.txt");
        steinHitbox = loadHitboxFromFile("Stein_outline.txt");
        dinoHitbox = loadHitboxFromFile("stegosaurus_jumping_outline.txt");
    }
}
