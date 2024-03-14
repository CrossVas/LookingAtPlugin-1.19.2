package dev.crossvas.lookingatplugin.mods.jade.info;

import dev.crossvas.lookingatplugin.mods.jade.LookingAtJadePlugin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.TooltipPosition;
import snownee.jade.api.config.IPluginConfig;

import java.util.ArrayList;
import java.util.List;

public enum TankInfoRemover implements IBlockComponentProvider {
    INSTANCE;

    public static final ResourceLocation FORGE_FLUID = new ResourceLocation("fluid");
    public static final List<Block> TANK_REMOVAL = new ArrayList<>();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (TANK_REMOVAL.contains(blockAccessor.getBlock())) {
            iTooltip.remove(FORGE_FLUID);
        }
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.TAIL;
    }

    @Override
    public ResourceLocation getUid() {
        return LookingAtJadePlugin.TANK_RENDER;
    }
}
