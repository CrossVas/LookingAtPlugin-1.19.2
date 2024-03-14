package dev.crossvas.lookingatplugin.info;

import dev.crossvas.lookingatplugin.IInfoProvider;
import dev.crossvas.lookingatplugin.ILookingAtHelper;
import dev.crossvas.lookingatplugin.helpers.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.BaseChargePadTileEntity;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public enum ChargePadInfoProvider implements IInfoProvider {
    THIS;

    @Override
    public void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseChargePadTileEntity pad) {
            text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(pad.getSinkTier()));
            text(helper, "ic2.probe.eu.max_in.name", pad.maxInput);
            text(helper, "ic2.probe.chargepad.transferrate.name", pad.transferLimit);
            text(helper, "ic2.probe.chargepad.radius.name", pad.range + 1.0F);
            EnergyContainer result = EnergyContainer.getContainer(pad);
            int averageIn = result.getAverageIn();
            int packetsIn = result.getPacketsIn();
            if (averageIn > 0) {
                helper.addPaddingElement(0, 3);
                text(helper, ChatFormatting.AQUA, "tooltip.item.ic2.eu_reader.cable_flow_in", Formatters.EU_FORMAT.format((long) averageIn));
                text(helper, ChatFormatting.AQUA, "tooltip.item.ic2.eu_reader.packet_flow_in", Formatters.EU_FORMAT.format((long) packetsIn));
            }
        }
    }

    @Override
    public IFilter getFilter() {
        return SpecialFilters.EU_READER;
    }
}
