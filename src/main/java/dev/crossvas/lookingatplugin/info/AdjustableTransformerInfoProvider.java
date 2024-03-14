package dev.crossvas.lookingatplugin.info;

import dev.crossvas.lookingatplugin.IInfoProvider;
import dev.crossvas.lookingatplugin.ILookingAtHelper;
import dev.crossvas.lookingatplugin.helpers.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.storage.tiles.transformer.AdjustableTransformerTileEntity;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public enum AdjustableTransformerInfoProvider implements IInfoProvider {
    INSTANCE;

    @Override
    public void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseTileEntity baseTile) {
            if (baseTile instanceof AdjustableTransformerTileEntity transformer) {
                int energyPacket = transformer.energyPacket;
                int packetCount = transformer.packetCount;

                helper.addText(Component.translatable("ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(transformer.getSinkTier())), false);
                helper.addText(Component.translatable("ic2.probe.eu.output.max.name", energyPacket), false);
                helper.addText(Component.translatable("ic2.probe.transformer.packets.name", packetCount), false);

                EnergyContainer result = EnergyContainer.getContainer(transformer);
                long averageOut = result.getAverageOut();
                long packetsOut = result.getPacketsOut();
                if (averageOut > 0) {
                    helper.addText(Component.translatable("tooltip.item.ic2.eu_reader.cable_flow", Formatters.EU_FORMAT.format(averageOut)).withStyle(ChatFormatting.AQUA), false);
                    helper.addText(Component.translatable("tooltip.item.ic2.eu_reader.packet_flow", Formatters.EU_FORMAT.format(packetsOut)).withStyle(ChatFormatting.AQUA), false);
                }
            }
        }
    }

    @Override
    public IFilter getFilter() {
        return SpecialFilters.EU_READER;
    }
}
