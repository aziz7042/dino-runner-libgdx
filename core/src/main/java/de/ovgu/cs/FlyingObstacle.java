package de.ovgu.cs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public abstract class FlyingObstacle extends Obstacle{
    public float[] spawnHeights = {2,2.5f,1.5f}; //possible spawn heights

    public FlyingObstacle(Texture texture) {
        super(texture);
        setY(getRandom(spawnHeights));
    }

    public static float getRandom(float[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    public void move(float speed, float delta){
        translateX(-speed * 1.5f * delta); //make flying obstacles slightly faster
    }


}
