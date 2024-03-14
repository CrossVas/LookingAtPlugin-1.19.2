package dev.crossvas.lookingatplugin.mods.waila.info;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IDataWriter;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.data.FluidData;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TankInfoRemover implements IDataProvider<BlockEntity> {

    @Override
    public void appendData(IDataWriter iDataWriter, IServerAccessor<BlockEntity> iServerAccessor, IPluginConfig iPluginConfig) {
        if (dev.crossvas.lookingatplugin.mods.jade.info.TankInfoRemover.TANK_REMOVAL.contains(iServerAccessor.getTarget().getBlockState().getBlock())) {
            iDataWriter.blockAll(FluidData.class);
        }
    }
}
