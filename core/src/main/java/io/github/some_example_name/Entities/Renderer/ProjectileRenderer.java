package io.github.some_example_name.Entities.Renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


import io.github.some_example_name.Mapa;
import io.github.some_example_name.Entities.Itens.Weapon.Projectile;;

public class ProjectileRenderer {
    private final Mapa mapa;
    private final Animation<TextureRegion> shootAnimation;
    private final Texture projectileTexture;
    private final int tileSize;
   // private final Texture glowTexture; // Nova textura para o brilho
   // private final float GLOW_SCALE = 1.5f; // Escala do brilho em relação ao projétil

    public ProjectileRenderer(Mapa mapa, int tileSize) {
        this.mapa = mapa;
        this.tileSize = tileSize;
        
        projectileTexture = new Texture("ITENS/Pistol/shoot.png");
        System.out.println(projectileTexture);
        this.shootAnimation = createAnimation(projectileTexture, 4, 0.2f);
        //glowTexture = new Texture("ITENS/Pistol/glow_yellow.png");
    }

    private Animation<TextureRegion> createAnimation(Texture texture, int frameCount, float frameDuration) {
        int frameWidth = texture.getWidth() / frameCount;
        int frameHeight = texture.getHeight();
        
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = new TextureRegion(texture, i * frameWidth, 0, frameWidth, frameHeight);
        }
        
        Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(Animation.PlayMode.NORMAL); 
        return animation;
    }

    public void render(SpriteBatch batch, float delta, float offsetX, float offsetY) {
        for (Projectile projectile : mapa.getProjectiles()) {
            TextureRegion frame = shootAnimation.getKeyFrame(projectile.getStateTime(), false);
            
            float width = projectile.getWidth() * tileSize;
            float height = projectile.getHeight() * tileSize;
            
        
            float x = offsetX + projectile.getPosition().x * tileSize - width / 2;
            float y = offsetY + projectile.getPosition().y * tileSize - height / 2;
            
   
            batch.draw(
                frame,
                x,
                y,
                width / 2,
                height / 2,
                width,
                height,
                1, 1,
                projectile.getAngle()
            );
            
            projectile.updateStateTime(delta);
        }
    }
    

    public void dispose() {
        projectileTexture.dispose();
    }
}