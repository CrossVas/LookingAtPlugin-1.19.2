package dev.crossvas.lookingatplugin.info;

import dev.crossvas.lookingatplugin.IInfoProvider;
import dev.crossvas.lookingatplugin.ILookingAtHelper;
import dev.crossvas.lookingatplugin.helpers.ColorStyle;
import dev.crossvas.lookingatplugin.helpers.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.api.items.electric.ElectricItem;
import ic2.core.block.base.tiles.BaseInventoryTileEntity;
import ic2.core.block.base.tiles.impls.BaseBatteryStationTileEntity;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.time.DurationFormatUtils;

public enum BatteryStationInfoProvider implements IInfoProvider {
    THIS;

    @Override
    public void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof BaseBatteryStationTileEntity station) {
                text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(station.getSourceTier()));
                text(helper, "ic2.probe.eu.output.max.name", station.getMaxEnergyOutput());

                ItemStack toCharge = station.getStackInSlot(16);
                long missingEnergy = ElectricItem.MANAGER.getCapacity(toCharge) - ElectricItem.MANAGER.getCharge(toCharge);
                long maxCapacity = 0;
                int averageIn = station.getMissingEnergy().getIntValue();
                int capacity = station.getMissingEnergy().getIntKey();
                int maxTransfer = ElectricItem.MANAGER.getTransferLimit(toCharge);
                for (int i = 0; i < station.getChargingSlots(); i++) {
                    ItemStack battery = station.getStackInSlot(i);
                    if (!battery.isEmpty()) {
                        maxCapacity += ElectricItem.MANAGER.getCapacity(battery);
                    }
                }

                if (missingEnergy > 0) {
                    int chargeEnergy = (int) Math.min(maxTransfer, missingEnergy);
                    simpleText(helper, false, false, ChatFormatting.GOLD, "ic2.probe.chargingBench.eta.name",
                            DurationFormatUtils.formatDuration(chargeEnergy <= 0 ? 0L : (missingEnergy / chargeEnergy * 50L), "HH:mm:ss"));
                }
                if (capacity > 0) {
                    int dischargeEnergy = Math.min(averageIn, capacity);
                    helper.addBarElement(capacity, (int) maxCapacity, Component.translatable("ic2.probe.discharging.eta.name",
                            DurationFormatUtils.formatDuration(dischargeEnergy <= 0 ? 0L : (capacity / dischargeEnergy * 50L), "HH:mm:ss")), ColorStyle.BLUE.aColor);
                }
                EnergyContainer result = EnergyContainer.getContainer(station);
                int averageOut = result.getAverageOut();
                int packetsOut = result.getPacketsOut();
                if (averageOut > 0) {
                    helper.addPaddingElement(0, 3);
                    simpleText(helper, false, false, ChatFormatting.AQUA, "tooltip.item.ic2.eu_reader.cable_flow_out", Formatters.EU_FORMAT.format(averageOut));
                    simpleText(helper, false, false, ChatFormatting.AQUA, "tooltip.item.ic2.eu_reader.packet_flow_out", Formatters.EU_FORMAT.format(packetsOut));
                }
            }
        }
    }

    @Override
    public IFilter getFilter() {
        return SpecialFilters.EU_READER;
    }
}
