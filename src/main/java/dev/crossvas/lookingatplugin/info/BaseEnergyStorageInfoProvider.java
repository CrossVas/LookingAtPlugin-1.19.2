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
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public enum BaseEnergyStorageInfoProvider implements IInfoProvider {
    THIS;

    @Override
    public void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof BaseEnergyStorageTileEntity energyStorage) {
                helper.addTextElement(Component.translatable("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(energyStorage.getSourceTier())), ChatFormatting.WHITE, false);
                helper.addTextElement(Component.translatable("ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(energyStorage.getTier())), ChatFormatting.WHITE, false);
                helper.addTextElement(Component.translatable("ic2.probe.eu.output.name", energyStorage.getProvidedEnergy()), ChatFormatting.WHITE, false);

                EnergyContainer result = EnergyContainer.getContainer(energyStorage);
                long averageIn = result.getAverageIn();
                long averageOut = result.getAverageOut();
                long packetsIn = result.getPacketsIn();
                long packetsOut = result.getPacketsOut();
                if (averageIn > 0 || averageOut > 0) {
                    if (averageIn > 0) {
                        helper.addTextElement(Component.translatable("tooltip.item.ic2.eu_reader.cable_flow_in", Formatters.EU_FORMAT.format((long) averageIn)), ChatFormatting.AQUA, false);
                    }
                    if (averageOut > 0) {
                        helper.addTextElement(Component.translatable("tooltip.item.ic2.eu_reader.cable_flow_out", Formatters.EU_FORMAT.format((long) averageOut)), ChatFormatting.AQUA, false);
                    }
                    if (packetsIn > 0) {
                        helper.addTextElement(Component.translatable("tooltip.item.ic2.eu_reader.packet_flow_in", Formatters.EU_READER_FORMAT.format((long) packetsIn)), ChatFormatting.AQUA, false);
                    }
                    if (packetsOut > 0) {
                        helper.addTextElement(Component.translatable("tooltip.item.ic2.eu_reader.packet_flow_out", Formatters.EU_READER_FORMAT.format((long) packetsOut)), ChatFormatting.AQUA, false);
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
