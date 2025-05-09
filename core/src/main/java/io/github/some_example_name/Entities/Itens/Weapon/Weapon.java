package io.github.some_example_name.Entities.Itens.Weapon;


import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Vector2;


import com.badlogic.gdx.physics.box2d.Body;


import io.github.some_example_name.Mapa;
import io.github.some_example_name.Entities.Inventory.Item;


public abstract class Weapon implements Item {
    protected float fireRate;
    protected float damage;
    protected int ammo;
    protected float timeSinceLastShot = 0f;
    public boolean canShoot = true;
    protected Vector2 position;
    public Body body;

    private float floatTime = 0f;
    private static final float FLOAT_SPEED = 2f;
    private static final float FLOAT_AMPLITUDE = 1f;

    protected TextureRegion icon;
    protected boolean reloading = false;
    protected float reloadProgress = 0;
    protected int maxAmmo;
    protected Vector2[] occupiedCells;


    protected int gridWidth;
    protected int gridHeight;

    public int getGridWidth() { return gridWidth; }
    public int getGridHeight() { return gridHeight; }
    
    public enum TipoMao {
        UMA_MAO, DUAS_MAOS;
    }
    public abstract TipoMao getTipoMao();

    public TextureRegion getIcon() {
        return icon;
    }
    public boolean isReloading() {
        return reloading;
    }
    public float getReloadProgress() {
        return reloadProgress;
    }
    public int getMaxAmmo() {
        return maxAmmo;
    }
    protected Weapon() {
        this.maxAmmo = 30;
    }
    public abstract void shoot(Vector2 position, Vector2 direction);
    public abstract void update(float delta);



    public float getFireRate() { return fireRate; }
    public float getDamage() { return damage; }
    public int getAmmo() { return ammo; }

    public abstract TextureRegion getCurrentFrame(float delta);
    public abstract Vector2 getPosition();
    public abstract void createBody(Vector2 position);
    

    public void destroyBody() {
      
    }


    public void setPosition(Vector2 position) {
        this.position = position.cpy();
    }
    public abstract Vector2 getMuzzleOffset();
    public void rotate() {
        int temp = gridWidth;
        gridWidth = gridHeight;
        gridHeight = temp;
    }
    public Vector2[] getOccupiedCells() {
        Vector2[] cells = new Vector2[gridWidth * gridHeight];
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                cells[y * gridWidth + x] = new Vector2(x, y);
            }
        }
        return cells;
    }

    public void setGridSize(int width, int height) {
        this.gridWidth = width;
        this.gridHeight = height;
    }

    public abstract void reload();

    public void updateFloatation(float delta) {
        floatTime += delta * FLOAT_SPEED;
    }

    public float getFloatOffset() {
        return (float) Math.sin(floatTime) * FLOAT_AMPLITUDE;
    }

    

}