package dev.crossvas.lookingatplugin.mods.waila.info;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IDataWriter;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.data.FluidData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class WailaTankInfoRemover implements IDataProvider<BlockEntity> {

    public static final List<Block> TANK_REMOVAL = new ArrayList<>();

    @Override
    public void appendData(IDataWriter iDataWriter, IServerAccessor<BlockEntity> iServerAccessor, IPluginConfig iPluginConfig) {
        if (TANK_REMOVAL.contains(iServerAccessor.getTarget().getBlockState().getBlock())) {
            iDataWriter.blockAll(FluidData.class);
        }
    }
}
