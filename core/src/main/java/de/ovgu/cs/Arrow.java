package de.ovgu.cs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Arrow extends FlyingObstacle{
    public Arrow(Texture texture) {
        super(texture);
    }

    public void draw(SpriteBatch spriteBatch, float stateTime) {
        spriteBatch.draw(getTexture(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
