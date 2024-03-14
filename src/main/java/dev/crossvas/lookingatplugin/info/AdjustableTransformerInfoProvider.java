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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public enum AdjustableTransformerInfoProvider implements IInfoProvider {
    THIS;

    @Override
    public void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseTileEntity baseTile) {
            if (baseTile instanceof AdjustableTransformerTileEntity transformer) {
                int energyPacket = transformer.energyPacket;
                int packetCount = transformer.packetCount;

                simpleText(helper, false, "ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(transformer.getSinkTier()));
                simpleText(helper, false, "ic2.probe.eu.output.max.name", energyPacket);
                simpleText(helper, false, "ic2.probe.transformer.packets.name", packetCount);

                EnergyContainer result = EnergyContainer.getContainer(transformer);
                long averageOut = result.getAverageOut();
                long packetsOut = result.getPacketsOut();
                if (averageOut > 0) {
                    helper.addPaddingElement(0, 3);
                    simpleText(helper, false, ChatFormatting.AQUA, "tooltip.item.ic2.eu_reader.cable_flow", Formatters.EU_FORMAT.format(averageOut));
                    simpleText(helper, false, ChatFormatting.AQUA, "tooltip.item.ic2.eu_reader.packet_flow", Formatters.EU_FORMAT.format(packetsOut));
                }
            }
        }
    }

    @Override
    public IFilter getFilter() {
        return SpecialFilters.EU_READER;
    }
}
