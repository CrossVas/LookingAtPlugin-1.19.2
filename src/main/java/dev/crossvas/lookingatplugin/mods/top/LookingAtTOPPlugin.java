package dev.crossvas.lookingatplugin.mods.top;

import dev.crossvas.lookingatplugin.LookingAt;
import dev.crossvas.lookingatplugin.LookingAtCommon;
import mcjty.theoneprobe.api.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class LookingAtTOPPlugin implements IProbeInfoProvider, Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe probe) {
        probe.registerProvider(this);
        return null;
    }

    @Override
    public ResourceLocation getID() {
        return LookingAt.rl("data");
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player player, Level level, BlockState blockState, IProbeHitData iProbeHitData) {
        LookingAtCommon.addInfo(new LookingAtTOPHelper(iProbeInfo), level.getBlockEntity(iProbeHitData.getPos()), player);
    }
}
