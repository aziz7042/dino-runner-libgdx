package de.ovgu.cs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Obstacle extends Component {
    /**
     * Constructs a new Obstacle with the specified texture.
     *
     * @param texture The texture to be applied to this obstacle.
     *               Must be a valid LibGDX Texture object.
     * @throws NullPointerException if the provided texture is null
     */
    public Obstacle(Texture texture) {
        super(texture);
    }

    public void move(float speed, float delta){
        translateX(-speed * delta);
    }

    public abstract void draw(SpriteBatch spriteBatch, float stateTime);
}
