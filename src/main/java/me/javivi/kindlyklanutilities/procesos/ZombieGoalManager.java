package me.javivi.kindlyklanutilities.procesos;

import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Zombie;

@Mod.EventBusSubscriber
public class ZombieGoalManager {

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();

        // Si la entidad es un Zombi...
        if (entity instanceof Zombie) {
            Zombie zombie = (Zombie) entity;

            // Agrego los goals
            zombie.goalSelector.addGoal(2, new ZombieDoubleJumpGoal(zombie));
            zombie.goalSelector.addGoal(3, new ZombieFleeGoal(zombie));
        }
    }
}
