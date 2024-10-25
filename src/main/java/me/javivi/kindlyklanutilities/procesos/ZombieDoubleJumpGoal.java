package me.javivi.kindlyklanutilities.procesos;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class ZombieDoubleJumpGoal extends Goal {

    private final Zombie zombie;
    private Player targetPlayer;
    private boolean doubleJumpReady = true;
    private int jumpCooldown = 0; // Para enfriar el salto
    private static final int MAX_COOLDOWN = 100; // 5 segundos (100 ticks)
    private static final double NORMAL_SPEED = 1.0; // Velocidad normal
    private static final double SPRINT_SPEED = 1.5; // Velocidad de sprint

    public ZombieDoubleJumpGoal(Zombie zombie) {
        this.zombie = zombie;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // Buscar el jugador más cercano en un rango de 20 bloques
        targetPlayer = zombie.level().getNearestPlayer(zombie, 15);
        return targetPlayer != null && zombie.distanceTo(targetPlayer) > 2; // Asegura que hay un jugador cerca
    }

    @Override
    public void tick() {
        if (jumpCooldown > 0) {
            jumpCooldown--;
        }

        double distanceToPlayer = targetPlayer != null ? zombie.distanceTo(targetPlayer) : 0;

        // Verifica si el jugador está en una posición más alta y realiza el doble salto
        if (targetPlayer != null && doubleJumpReady && jumpCooldown == 0) {
            double heightDifference = targetPlayer.getY() - zombie.getY();
            if (heightDifference > 1.0 && heightDifference <= 4.0) {
                // Si la diferencia de altura es de 1 a 2 bloques, realiza un salto hacia el jugador
                jumpTowardsPlayer();
                doubleJumpReady = false;
                jumpCooldown = MAX_COOLDOWN;
            }
        }

        // Si está en el suelo nuevamente, activar el doble salto
        if (zombie.onGround()) {
            doubleJumpReady = true;
        }

        // Movimiento hacia el jugador con sprint si está a más de 15 bloques de distancia
        if (distanceToPlayer > 5) {
            zombie.getNavigation().moveTo(targetPlayer, SPRINT_SPEED); // Sprint hacia el jugador
        } else {
            zombie.getNavigation().moveTo(targetPlayer, NORMAL_SPEED); // Velocidad normal si está cerca
        }
    }

    private void jumpTowardsPlayer() {
        Vec3 playerPos = targetPlayer.position();
        double deltaX = playerPos.x - zombie.getX();
        double deltaZ = playerPos.z - zombie.getZ();
        double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        // Calcula la dirección y la velocidad del salto
        double jumpVelocityX = deltaX / distance * 0.3;
        double jumpVelocityZ = deltaZ / distance * 0.3;

        // Aplica el movimiento hacia el jugador
        zombie.setDeltaMovement(new Vec3(jumpVelocityX, 0.75, jumpVelocityZ)); // Salto más alto hacia el jugador
        zombie.setJumping(true); // Empezar a saltar
    }
}

