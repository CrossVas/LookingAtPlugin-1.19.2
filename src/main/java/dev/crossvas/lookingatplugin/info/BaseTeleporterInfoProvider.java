package dev.crossvas.lookingatplugin.info;

import dev.crossvas.lookingatplugin.IInfoProvider;
import dev.crossvas.lookingatplugin.ILookingAtHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.machines.tiles.mv.BaseTeleporterTileEntity;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Set;

public enum BaseTeleporterInfoProvider implements IInfoProvider {
    THIS;

    @Override
    public void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof BaseTeleporterTileEntity tp) {
                text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tp.getTier()));
                text(helper, "ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(tp.getSinkTier()));
                Set<BaseTeleporterTileEntity.LocalTarget> targets = tp.getTargets();
                String name = tp.name;
                String networkID = tp.networkID;
                if (!targets.isEmpty()) {
                    text(helper, Component.translatable("gui.ic2.base_teleporter.name").append(": ").append(name).getString());
                    text(helper, Component.translatable("gui.ic2.base_teleporter.network").append(": ").append(networkID).getString());
                    helper.addPaddingElement(0, 3);
                    simpleText(helper, false, false, ChatFormatting.GOLD, "ic2.probe.base_teleporter.connections");
                    for (BaseTeleporterTileEntity.LocalTarget target : targets) {
                        if (!target.getPos().equals(tp.getPosition())) {
                            text(helper, Component.literal(" - " + target.getName()).getString());
                        }
                    }
                }
            }
        }
    }

    @Override
    public IFilter getFilter() {
        return SpecialFilters.EU_READER;
    }
}
