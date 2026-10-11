package de.ovgu.cs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Fireball extends FlyingObstacle{
    public Animation<TextureRegion> currentAnimation;

    public Fireball(Texture texture) {
        super(texture);
        currentAnimation = initializeAnimation(texture, 2,1);
    }

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

    public void draw(SpriteBatch spriteBatch, float stateTime) {
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        spriteBatch.draw(currentFrame, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
