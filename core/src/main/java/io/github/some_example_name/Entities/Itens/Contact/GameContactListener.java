package io.github.some_example_name.Entities.Itens.Contact;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import io.github.some_example_name.Entities.Enemies.Ratinho;
import io.github.some_example_name.Entities.Inventory.Item;
import io.github.some_example_name.Entities.Itens.Ammo.Ammo;
import io.github.some_example_name.Entities.Itens.Ammo.Ammo9mm;
import io.github.some_example_name.Entities.Itens.Weapon.Projectile;
import io.github.some_example_name.Entities.Itens.Weapon.Weapon;
import io.github.some_example_name.Entities.Player.Robertinhoo;

import com.badlogic.gdx.Gdx;

public class GameContactListener implements ContactListener {
      private Robertinhoo player;


      public GameContactListener(Robertinhoo player) {
        this.player = player;
    }

      
        @Override
        public void beginContact(Contact contact) {
            Fixture fixA = contact.getFixtureA();
            Fixture fixB = contact.getFixtureB();
        
            Object dataA = fixA.getBody().getUserData();
            Object dataB = fixB.getBody().getUserData();
        
        
            if (dataA != null && dataB != null) {
                if(dataA.equals("PLAYER") && dataB instanceof Weapon) {
                    player.setWeaponToPickup((Weapon) dataB);
                } else if(dataB.equals("PLAYER") && dataA instanceof Weapon) {
                    player.setWeaponToPickup((Weapon) dataA);
                }
            }


            if (dataA instanceof Ammo && "PLAYER".equals(dataB)) {
                player.setAmmoToPickup((Ammo) dataA);
            } else if (dataB instanceof Ammo && "PLAYER".equals(dataA)) {
                player.setAmmoToPickup((Ammo) dataB);
            }
            System.out.println("dataA"+dataA);
            System.out.println("DataB"+dataB);
            if ((dataA instanceof Projectile && "WALL".equals(dataB)) || 
            (dataB instanceof Projectile && "WALL".equals(dataA))) {
            System.out.println("chego aqui vei");
            Projectile projectile = (dataA instanceof Projectile) ? 
                (Projectile) dataA : (Projectile) dataB;
            
                projectile.startDestruction();
        }


            if ((dataA instanceof Projectile && dataB instanceof Ratinho) || 
            (dataB instanceof Projectile && dataA instanceof Ratinho)) {

            Projectile projectile = (dataA instanceof Projectile) ? 
                (Projectile) dataA : (Projectile) dataB;
            Ratinho ratinho = (dataB instanceof Ratinho) ? 
                (Ratinho) dataB : (Ratinho) dataA;

       
            ratinho.takeDamage(projectile.getDamage());
         
            projectile.startDestruction();

            if (ratinho.getHealth() <= 0) {
                ratinho.destroy();
        }

        
    }
        }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

  
        if(fixA.getUserData() != null && fixB.getUserData() != null) {
            if(fixA.getUserData().equals("PLAYER") && fixB.getUserData() instanceof Weapon) {
                player.clearWeaponToPickup();
                Gdx.app.log("AimDebug","PAROU CONTATO");
            } else if(fixB.getUserData().equals("PLAYER") && fixA.getUserData() instanceof Weapon) {
                player.clearWeaponToPickup();
            }

            if(fixA.getUserData().equals("PLAYER") && fixB.getUserData() instanceof Item) {
                player.clearItemToPickup();
            } else if(fixB.getUserData().equals("PLAYER") && fixA.getUserData() instanceof Item) {
                player.clearItemToPickup();
            }
        }
        
    }


    @Override public void preSolve(Contact contact, Manifold oldManifold) {}
    @Override public void postSolve(Contact contact, ContactImpulse impulse) {}
    
}