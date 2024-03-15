package dev.crossvas.lookingatplugin.mods.waila;

import dev.crossvas.lookingatplugin.mods.waila.info.CropIconProvider;
import dev.crossvas.lookingatplugin.mods.waila.info.WailaTankInfoRemover;
import dev.crossvas.lookingatplugin.mods.waila.info.WailaBlockEntityDataProvider;
import dev.crossvas.lookingatplugin.mods.waila.info.WailaTooltipRenderer;
import ic2.core.block.crops.CropBlock;
import ic2.core.block.crops.CropTileEntity;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class LookingAtWailaPlugin implements IWailaPlugin {

    @Override
    public void register(IRegistrar registry) {
        registry.addBlockData(new CropIconProvider(), CropTileEntity.class);
        registry.addComponent(new CropIconProvider(), TooltipPosition.BODY, CropBlock.class);
        registry.addIcon(new CropIconProvider(), CropBlock.class);
        registry.addBlockData(new WailaTankInfoRemover(), BlockEntity.class, 500);
        registry.addBlockData(new WailaBlockEntityDataProvider(), BlockEntity.class);
        registry.addComponent((IBlockComponentProvider) new WailaTooltipRenderer(), TooltipPosition.BODY, Block.class);
    }
}
