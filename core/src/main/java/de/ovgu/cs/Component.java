package de.ovgu.cs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Component extends Sprite {
    
    /**
     * Constructs a new Component with the specified texture.
     * The texture is passed to the parent Sprite class for rendering.
     * 
     * @param texture The LibGDX texture to be used for this component.
     *               Must be a valid, loaded texture resource.
     * @throws NullPointerException if the provided texture is null
     */
    public Component(Texture texture) {
        super(texture);
    }
    
    /**
     * Returns the bounding rectangle of this component for collision detection.
     * This method provides a convenient way to access the component's bounds
     * for physics and collision checking.
     * 
     * @return A Rectangle object representing the bounds of this component
     */
    public Rectangle Rec() {
        return this.getBoundingRectangle();
    }
}
