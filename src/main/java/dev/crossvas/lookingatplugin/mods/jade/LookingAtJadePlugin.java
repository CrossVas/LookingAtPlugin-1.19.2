package dev.crossvas.lookingatplugin.mods.jade;

import dev.crossvas.lookingatplugin.LookingAt;
import dev.crossvas.lookingatplugin.mods.jade.info.CropIconProvider;
import dev.crossvas.lookingatplugin.mods.jade.info.JadeBlockEntityDataProvider;
import dev.crossvas.lookingatplugin.mods.jade.info.JadeTooltipRenderer;
import dev.crossvas.lookingatplugin.mods.jade.info.JadeTankInfoRemover;
import ic2.core.block.crops.CropBlock;
import ic2.core.block.crops.CropTileEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class LookingAtJadePlugin implements IWailaPlugin {

    public static final ResourceLocation TANK_RENDER = LookingAt.rl("remove_renders_fluid");
    public static final ResourceLocation TOP_STYLE = LookingAt.rl("force_top_style");

    @Override
    public void registerClient(IWailaClientRegistration r) {
        r.addConfig(TOP_STYLE, true);
        r.addConfig(TANK_RENDER, true);
        r.registerBlockComponent(new CropIconProvider(), CropBlock.class);
        r.registerBlockIcon(new CropIconProvider(), CropBlock.class);
        r.registerBlockComponent(JadeTooltipRenderer.INSTANCE, Block.class);
        r.registerBlockComponent(JadeTankInfoRemover.INSTANCE, Block.class);
    }

    @Override
    public void register(IWailaCommonRegistration r) {
        r.registerBlockDataProvider(JadeBlockEntityDataProvider.INSTANCE, BlockEntity.class);
        r.registerBlockDataProvider(new CropIconProvider(), CropTileEntity.class);
    }
}
