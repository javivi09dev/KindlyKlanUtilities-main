package me.javivi.kindlyklanutilities.procesos;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class ZombieFleeGoal extends Goal {
    private final Zombie zombie;
    private Player targetPlayer;
    private static final double FLEE_DISTANCE = 10.0; // Distancia mínima para volver a la normalidad
    private static final double FLEE_SPEED = 1.5; // Velocidad de huida

    public ZombieFleeGoal(Zombie zombie) {
        this.zombie = zombie;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // Verificar si el zombi tiene salud baja y si hay un jugador cerca
        if (zombie.getHealth() < zombie.getMaxHealth() * 0.2) { // 50% de salud
            targetPlayer = zombie.level().getNearestPlayer(zombie, 5); // Buscar al jugador más cercano
            return targetPlayer != null; // Asegura que hay un jugador cerca
        }
        return false;
    }

    @Override
    public void tick() {
        if (targetPlayer != null) {
            // Calcular la dirección opuesta al jugador
            double dirX = zombie.getX() - targetPlayer.getX();
            double dirZ = zombie.getZ() - targetPlayer.getZ();
            double distance = Math.sqrt(dirX * dirX + dirZ * dirZ);

            // Normalizar la dirección
            if (distance > 0) {
                dirX /= distance; // Normalizar
                dirZ /= distance; // Normalizar
            }

            // Mover al zombi lejos del jugador usando la navegación
            double fleeX = zombie.getX() + dirX * 2; // Ajustar la distancia de huida
            double fleeZ = zombie.getZ() + dirZ * 2;

            // Mover al zombi a la posición de huida
            zombie.getNavigation().moveTo(fleeX, zombie.getY(), fleeZ, FLEE_SPEED); // Aumentar la velocidad de huida

            // Hacer que el zombi mire en la dirección opuesta
            zombie.lookAt(targetPlayer, 30.0F, 30.0F); // Ajusta los ángulos de visión
        }

        // Volver a la normalidad si está lo suficientemente lejos del jugador
        if (targetPlayer != null && zombie.distanceTo(targetPlayer) > FLEE_DISTANCE) {
            // Detener la meta de huida
            targetPlayer = null;
            // Regresar a otros comportamientos
            zombie.goalSelector.removeGoal(this); // Remover esta meta para volver a la normalidad
        }
    }
}
