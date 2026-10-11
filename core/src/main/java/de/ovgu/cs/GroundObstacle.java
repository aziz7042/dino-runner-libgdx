package de.ovgu.cs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GroundObstacle extends Obstacle{ //maybe add different sized rectangles?
    public GroundObstacle(Texture texture) {
        super(texture);
        setY(0);
    }

    public void draw(SpriteBatch spriteBatch, float stateTime){
        spriteBatch.draw(getTexture(), getX(),getY(),getWidth(),getHeight());
    }
}
