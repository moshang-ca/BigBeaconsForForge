package org.moshang.bigbeaconsforforge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("removal")
@Mod(BigBeaconsForForge.MOD_ID)
public class BigBeaconsForForge {
	public static final String MOD_ID = "bigbeaconsforforge";
	public static GameRules.Key<GameRules.BooleanValue> BEACON_FLIGHT = GameRules.register("beaconFlight", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));

    public static final DeferredRegister<MobEffect> DR = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MOD_ID);
    public static final MobEffect FLIGHT = new FlightEffect();
    public static final RegistryObject<MobEffect> FLIGHT_EFFECT = DR.register("flight", () -> FLIGHT);

    public static final ResourceLocation PACKET_ID = new ResourceLocation(MOD_ID, "mod_check");
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(PACKET_ID)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

    public BigBeaconsForForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        DR.register(bus);
        register();
    }


    public static void register() {
        INSTANCE.registerMessage(
                0,
                PacketServer.class,
                PacketServer::encode,
                PacketServer::decode,
                PacketServer::handle
        );

        INSTANCE.registerMessage(
                1,
                PacketClient.class,
                PacketClient::encode,
                PacketClient::decode,
                PacketClient::handle
        );
    }
}
