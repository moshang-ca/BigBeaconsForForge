package org.moshang.bigbeaconsforforge.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.moshang.bigbeaconsforforge.BigBeaconsForForge;
import org.moshang.bigbeaconsforforge.PacketClient;

@Mod.EventBusSubscriber(modid = BigBeaconsForForge.MOD_ID)
public class PlayerEventHandler {
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if(event.getEntity() instanceof ServerPlayer serverPlayer) {
            BigBeaconsForForge.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PacketClient(true));
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if(event.getEntity() instanceof ServerPlayer serverPlayer) {
            BigBeaconsForForge.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PacketClient(true));
        }
    }
}
