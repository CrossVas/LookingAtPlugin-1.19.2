package dev.crossvas.lookingatplugin.mods.waila.info;

import dev.crossvas.lookingatplugin.LookingAtCommon;
import dev.crossvas.lookingatplugin.mods.waila.WailaLookingAtHelper;
import mcp.mobius.waila.api.*;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WailaBlockEntityDataProvider implements IDataProvider<BlockEntity> {

    @Override
    public void appendData(IDataWriter iDataWriter, IServerAccessor<BlockEntity> iServerAccessor, IPluginConfig iPluginConfig) {
        WailaLookingAtHelper helper = new WailaLookingAtHelper();
        LookingAtCommon.addInfo(helper, iServerAccessor.getTarget(), iServerAccessor.getPlayer());
        helper.transferData(iDataWriter.raw());
    }
}
