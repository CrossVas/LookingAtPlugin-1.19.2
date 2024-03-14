package dev.crossvas.lookingatplugin.info;

import dev.crossvas.lookingatplugin.IInfoProvider;
import dev.crossvas.lookingatplugin.ILookingAtHelper;
import dev.crossvas.lookingatplugin.helpers.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseInventoryTileEntity;
import ic2.core.block.base.tiles.impls.BaseEnergyStorageTileEntity;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public enum BaseEnergyStorageInfoProvider implements IInfoProvider {
    THIS;

    @Override
    public void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof BaseEnergyStorageTileEntity energyStorage) {
                text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(energyStorage.getSourceTier()));
                text(helper, "ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(energyStorage.getTier()));
                text(helper, "ic2.probe.eu.output.name", energyStorage.getProvidedEnergy());

                EnergyContainer result = EnergyContainer.getContainer(energyStorage);
                long averageIn = result.getAverageIn();
                long averageOut = result.getAverageOut();
                long packetsIn = result.getPacketsIn();
                long packetsOut = result.getPacketsOut();
                if (averageIn > 0 || averageOut > 0) {
                    if (averageIn > 0) {
                        simpleText(helper, false, false, ChatFormatting.AQUA, "tooltip.item.ic2.eu_reader.cable_flow_in", Formatters.EU_FORMAT.format((long) averageIn));
                    }
                    if (averageOut > 0) {
                        simpleText(helper, false, false, ChatFormatting.AQUA, "tooltip.item.ic2.eu_reader.cable_flow_out", Formatters.EU_FORMAT.format((long) averageOut));
                    }
                    if (packetsIn > 0) {
                        simpleText(helper, false, false, ChatFormatting.AQUA, "tooltip.item.ic2.eu_reader.packet_flow_in", Formatters.EU_READER_FORMAT.format((long) packetsIn));
                    }
                    if (packetsOut > 0) {
                        simpleText(helper, false, false, ChatFormatting.AQUA, "tooltip.item.ic2.eu_reader.packet_flow_out", Formatters.EU_READER_FORMAT.format((long) packetsOut));
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
