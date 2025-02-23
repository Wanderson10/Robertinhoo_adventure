
package io.github.some_example_name.Entities.Itens.Contact;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import io.github.some_example_name.Entities.Itens.Weapon.Projectile;
import io.github.some_example_name.Entities.Enemies.Enemy;


public class ProjectileContactListenter  implements ContactListener {



    @Override
    public void beginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();

        // Se o corpo A for um projectile, tratar colisão com o corpo B
        if (userDataA instanceof Projectile) {
            handleProjectileCollision((Projectile) userDataA, userDataB);
        }
        // Se o corpo B for um projectile, tratar colisão com o corpo A
        if (userDataB instanceof Projectile) {
            handleProjectileCollision((Projectile) userDataB, userDataA);
        }
    }

    private void handleProjectileCollision(Projectile projectile, Object otherUserData) {
        // Se colidiu com um inimigo...
        if (otherUserData instanceof Enemy) {
            projectile.destroy();
        }
        // Se colidiu com uma parede (userData "WALL")
        else if (otherUserData instanceof String && ((String) otherUserData).equals("WALL")) {
            projectile.destroy();
        }
    }

    @Override
    public void endContact(Contact contact) { }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { }
    
}
