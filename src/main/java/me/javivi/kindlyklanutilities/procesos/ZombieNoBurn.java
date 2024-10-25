package me.javivi.kindlyklanutilities.procesos;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.damagesource.DamageSource;

@Mod.EventBusSubscriber
public class ZombieNoBurn {

    @SubscribeEvent
    public static void onZombieDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie) event.getEntity();

            // Verificar si el daño es por fuego y si es de día
            if (event.getEntity().isOnFire() && zombie.level().isDay()) {
                zombie.clearFire(); // "Apaga" el fuego, pero el daño se ve
                zombie.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
                zombie.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0)); //Añado fire resistance al zombi
                event.setCanceled(true); // Cancelo el daño. No es eficiente. Por eso pongo el fire resistance

            }
        }
    }
}
