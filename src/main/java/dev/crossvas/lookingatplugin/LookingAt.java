package dev.crossvas.lookingatplugin;

import dev.crossvas.lookingatplugin.mods.top.LookingAtTOPPlugin;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LookingAt.ID)
public class LookingAt {

    public static final String ID = "lookingat";

    public static final ResourceLocation FORGE_FLUID = new ResourceLocation("fluid");

    public LookingAt() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::loadIMC);
    }

    public void loadIMC(InterModEnqueueEvent e) {
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", LookingAtTOPPlugin::new);
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}
