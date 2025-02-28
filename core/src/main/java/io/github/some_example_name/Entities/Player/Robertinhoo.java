package io.github.some_example_name.Entities.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.MapRenderer;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import io.github.some_example_name.Mapa;
import io.github.some_example_name.Entities.Enemies.Box2dLocation;
import io.github.some_example_name.Entities.Itens.Weapon.Pistol;
import io.github.some_example_name.Entities.Itens.Weapon.Weapon;
import io.github.some_example_name.Entities.Inventory.Inventory;
import io.github.some_example_name.MapRenderer;


public class Robertinhoo  implements Steerable<Vector2> {
    

    public Body body;
    public static final int RUN = 1;
    public static final int DASH = 2;
    public static final int SPAWN = 3;
    public static final int DYING = 4;
    public static final int DEAD = 5;
    public static final int LEFT = 8;
    public static final int RIGHT = 7;
    public static final int UP = 9;
    public static final int TOP = 1;
    public static final int DOWN = -1;
    public static final int IDLE = 6;
    public static final float ACCELERATION = 4f;
    public static final float DASH_DURATION = 0.1f;
    public static final float DASH_COOLDOWN = 1f;
    public static final float DASH_SPEED = 15f;
    public static final int TILE_SIZE = 1;

    public int state = SPAWN;
    public int dir = IDLE;
    public int lastDir = DOWN;
    private boolean isInvulnerable = false;

    public final Mapa map;
    public final Rectangle bounds = new Rectangle();
    public final Vector2 pos = new Vector2();
    private PlayerWeaponSystem weaponSystem;
     private OrthographicCamera camera;
     private Weapon weaponToPickup;
     private MapRenderer mapRenderer;

 
    private float dashTime = 0;
    private float dashCooldownTime = 0;

    private Weapon currentWeapon;
    private Inventory inventory;

    public Robertinhoo(Mapa map, int x, int y,MapRenderer mapRenderer) {
        this.map = map;
        pos.set(x, y);
        bounds.set(pos.x, pos.y, TILE_SIZE, TILE_SIZE);
        state = SPAWN;
        this.weaponSystem = new PlayerWeaponSystem(this, mapRenderer);
        this.inventory = new Inventory(this);
        
    

       
        createBody(x, y);

        
    }

    public void equipWeapon(Weapon weapon) {
        this.currentWeapon = weapon;
           if (weapon instanceof Pistol) {
 
  
    }

    }
    public Inventory getInventory() { return inventory; }
    public Weapon getCurrentWeapon() { 
        return inventory.getEquippedWeapon();
    }


    


      private void createBody(int x, int y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = false;

        body = map.world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f); // Tamanho do corpo (1x1 tile)

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;
        body.setUserData("PLAYER");
        System.out.println("[DEBUG] Criando corpo do Robertinho em (" + x + ", " + y + ")");

        body.createFixture(fixtureDef);
        body.setAngularDamping(2f);
        shape.dispose();
    }

    public void update(float deltaTime) {
        if (weaponSystem != null) {
            weaponSystem.update(deltaTime);
            applyAimRotation();
        }

        Weapon currentWeapon = getCurrentWeapon();
        if (currentWeapon != null) {
            currentWeapon.update(deltaTime);
        }
        // Reduz o cooldown do dash
        if (dashCooldownTime > 0) dashCooldownTime -= deltaTime;
    
        // Verifica se está no estado de dash
        if (dashTime > 0) {
            dashTime -= deltaTime;
    
            if (dashTime <= 0) {
                // Termina o dash
                state = IDLE;
                isInvulnerable = false;
                body.setLinearVelocity(0, 0);
            }
        } else {
            processKeys();
        }
        linearVelocity.set(body.getLinearVelocity());
        pos.set(body.getPosition().x - 0.5f, body.getPosition().y - 0.5f);
        bounds.setPosition(pos);
        linearVelocity.set(body.getLinearVelocity());
   
        angularVelocity = body.getAngularVelocity();




    }

    

    private void processKeys() {
        Vector2 moveDir = new Vector2();
        boolean isMoving = false;
        if (Gdx.input.isKeyPressed(Keys.W)){
            moveDir.y += 1;
            isMoving = true;
            dir = UP;
            lastDir = UP; // Atualiza lastDir
        } 
    
        if (Gdx.input.isKeyPressed(Keys.S)){
            isMoving = true;
            dir = DOWN;
            moveDir.y -= 1;
            lastDir = DOWN; // Atualiza lastDir
        } 
    
        if (Gdx.input.isKeyPressed(Keys.D)) {
            moveDir.x += 1;
            isMoving = true;
            dir = RIGHT;
            lastDir = RIGHT; // Atualiza lastDir
        }
        if (Gdx.input.isKeyPressed(Keys.A)) {
            moveDir.x -= 1;
            isMoving = true;
            dir = LEFT;
            lastDir = LEFT; // Atualiza lastDir
        }
    
        if (Gdx.input.isKeyPressed(Keys.SPACE) && dashCooldownTime <= 0 && state != DASH) {
            if (!moveDir.isZero()) {
                moveDir.nor();
                state = DASH;
                dashTime = DASH_DURATION;
                dashCooldownTime = DASH_COOLDOWN;
                isInvulnerable = true;
                body.setLinearVelocity(moveDir.scl(DASH_SPEED));
            }
        } else if (!moveDir.isZero()) {
            moveDir.nor();
            state = RUN;
            body.setLinearVelocity(moveDir.scl(ACCELERATION));
        } else {
            state = IDLE;
            body.setLinearVelocity(0, 0);
            if (!isMoving) {
                dir = IDLE;
            }
        }

        if(Gdx.input.isKeyJustPressed(Keys.E)) {
          
            System.out.println(weaponToPickup);
            if(weaponToPickup != null) {
                if (inventory.addWeapon(weaponToPickup)) {
                    weaponToPickup.destroyBody();
                    map.getWeapons().remove(weaponToPickup);
                    clearWeaponToPickup();
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Keys.M)) {
            Weapon currentWeapon = getCurrentWeapon();
            if (currentWeapon != null) {
         
                Vector2 firePosition = body.getPosition().cpy();
                
          
                Vector2 rotatedOffset = currentWeapon.getMuzzleOffset().cpy()
                    .rotateDeg(weaponSystem.getAimAngle());
   
                firePosition.add(rotatedOffset);
            
                Vector2 aimDirection = weaponSystem.getAimDirection().cpy().nor();
                
            
                currentWeapon.shoot(firePosition, aimDirection);
            }
        }
    }

    public PlayerWeaponSystem getWeaponSystem() {
        return weaponSystem;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void updateCameraViewport(int width, int height) {
        if(camera != null) {
            camera.viewportWidth = width;
            camera.viewportHeight = height;
            camera.update();
        }
    }


public void applyAimRotation() {
    if (inventory.getEquippedWeapon() != null) {
        float angle = weaponSystem.getAimAngle();
        body.setTransform(body.getPosition(), (float) Math.toRadians(angle));
    }
}

public void setMapRenderer(MapRenderer mapRenderer) {
    this.weaponSystem = new PlayerWeaponSystem(this, mapRenderer);
}

public void setWeaponToPickup(Weapon weapon) {
    this.weaponToPickup = weapon;
}

public void clearWeaponToPickup() {
    this.weaponToPickup = null;
}



    private Vector2 linearVelocity = new Vector2();
    private float angularVelocity = 0f;
    private float maxLinearSpeed = 10f;
    private float maxAngularSpeed = 10f;
    private float maxLinearAcceleration = 10f;
    private float maxAngularAcceleration = 10f;
    private boolean tagged = false;

    @Override
    public Vector2 getLinearVelocity() {
     
        return body.getLinearVelocity();
    }



    @Override
    public float getAngularVelocity() {
        return angularVelocity;
    }



    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public float getBoundingRadius() {
        return 0.5f;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }
    @Override
    public void setOrientation(float orientation) {
       
            body.setTransform(body.getPosition(), orientation);
        
        
    }

      @Override
    public Location<Vector2> newLocation() {
        return new Box2dLocation();
    }
    

@Override
public float vectorToAngle(Vector2 vector) {
    return (float) Math.atan2(vector.y, vector.x); // Removido o "-" do eixo Y
}
@Override
public Vector2 angleToVector(Vector2 outVector, float angle) {
    outVector.set((float)Math.cos(angle), (float)Math.sin(angle));
    return outVector;
}
    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0.1f; // Example value
    }

    @Override
    public void setZeroLinearSpeedThreshold(float threshold) {
        // Implement logic for setting the zero linear speed threshold
    }

    @Override
    public float getOrientation() {
        return body.getAngle(); // Assuming you're using Box2D for physics
    }

 

}
