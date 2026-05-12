package com.wachi.sable_sa_compat;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;


@Mod(SableSaCompat.MODID)
public class SableSaCompat {
    public static final String MODID = "sable_sa_compat";
    public static final Logger LOGGER = LogUtils.getLogger();

    //config variables are in both sides but only changed/used by their respective enviroment
    public static final double grapplinRange = 75;
    public static boolean hideFpArmorHand;
    public static boolean allowBlockEntityPicking;

    public SableSaCompat(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);

        modEventBus.addListener(this::client);
        modEventBus.addListener(this::common);
    }

    @SubscribeEvent
    public void configReload(ModConfigEvent.Reloading event) {
        if(event.getConfig().getModId().equals(MODID)) {
            switch (event.getConfig().getType()){
                case CLIENT: loadClientConfig();
                case COMMON: loadCommonConfig();
            }
        }
    }

    public void client(FMLClientSetupEvent event){
        loadClientConfig();
    }

    public void common(FMLCommonSetupEvent event){
        loadCommonConfig();
    }

    public static void loadClientConfig(){
        hideFpArmorHand = ClientConfig.HIDE_FIRST_PERSON_ARMOR.getAsBoolean();
    }

    public static void loadCommonConfig(){
        allowBlockEntityPicking = CommonConfig.ALLOW_BLOCK_ENTITY_PICKING.getAsBoolean();
    }
}
