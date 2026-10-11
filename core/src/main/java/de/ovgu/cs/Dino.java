package de.ovgu.cs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;


public class Dino extends Component {

    /** Flag to control animation playback */
    public boolean playAnimation;

    /** Animation for the walking state */
    private Animation<TextureRegion> walkAnimation;

    /** Animation for the ducked (crouching) state */
    public Animation<TextureRegion> duckedAnimation;

    /** Reference to the currently playing animation */
    public Animation<TextureRegion> currentAnimation;

    public Animation<TextureRegion> jumpAnimation;

    /** Flag indicating whether the dinosaur is in ducking state */
    public boolean isDucking;
    public boolean isJumping;

    float jumpTimer;
    float jumpDuration = 1.5f;


    /**
     * Constructs a new Dino with the specified texture and initializes its animations.
     *
     * @param texture The sprite sheet texture containing animation frames.
     *               Must contain frames arranged in FRAME_COLS columns and FRAME_ROWS rows.
     */
    public Dino(Texture texture) {
        super(texture);
        setSize(1,1);
        setPosition(0,0);
        playAnimation = true;
        walkAnimation = initializeAnimation(texture, 2,1);
        currentAnimation = walkAnimation;
        isDucking = false;
        isJumping = false;
        jumpTimer = 0f;
    }

    public Animation<TextureRegion> initializeAnimation(String filename, int FRAME_COLS, int FRAME_ROWS){
        Texture spriteSheet = new Texture(filename);

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet,
            spriteSheet.getWidth() / FRAME_COLS,
            spriteSheet.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] animationFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                animationFrames[index++] = tmp[i][j];
            }
        }
        return new Animation<TextureRegion>(0.2f, animationFrames);
    }

    /**
     * Initializes an animation from a sprite sheet texture.
     * Splits the sprite sheet into individual frames and creates an Animation object.
     *
     * @param spriteSheet The texture containing all animation frames
     * @return An Animation object containing the extracted frames with a 0.2f frame duration
     */
    public Animation<TextureRegion> initializeAnimation(Texture spriteSheet, int FRAME_COLS, int FRAME_ROWS){
        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet,
            spriteSheet.getWidth() / FRAME_COLS,
            spriteSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] animationFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                animationFrames[index++] = tmp[i][j];
            }
        }
        return new Animation<TextureRegion>(0.2f, animationFrames);
    }

    /**
     * Updates the dinosaur's position during a jump.
     *
     * @param delta Time elapsed since the last frame
     */
    public void jumpHandler(float delta) {
        if (isJumping){
            jumpTimer+=delta;
            //System.out.println(jumpTimer);

            if (jumpTimer <= 2.4f){
                //stegosaurus.translateY(jump(jumpTimer,jumpDuration));
                setY(jumpFunction(jumpTimer,jumpDuration));
            }
            else {
                setY(0f);
                isJumping = false;
            }
        }
    }

    /**
     * Calculates the jumping arc using a mathematical function.
     *
     * @param x Current time in the jump animation
     * @param Duration Total duration of the jump
     * @return The calculated Y position for the jump
     */
    private static float jumpFunction(float x, float Duration){
        float sqrt4 = (float) sqrt(4);
        return (float)(-pow(x*(/*(1f)*/(sqrt4/Duration)) - sqrt(2.5) ,2f) + 2.5);
    }

    /**
     * Renders the dinosaur with its current animation frame.
     * Selects the appropriate animation based on the dinosaur's state (ducking or walking)
     * and draws the current frame using the provided SpriteBatch.
     *
     * @param spriteBatch The SpriteBatch used for rendering
     * @param stateTime The current time in seconds since the animation started
     */
    public void draw(SpriteBatch spriteBatch, float stateTime) {
        if(isDucking) {
            currentAnimation = duckedAnimation;
        }else if(isJumping){
            currentAnimation = jumpAnimation;
        }else currentAnimation = walkAnimation;

        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        spriteBatch.draw(currentFrame, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
