package dev.crossvas.lookingatplugin.info;

import dev.crossvas.lookingatplugin.IInfoProvider;
import dev.crossvas.lookingatplugin.ILookingAtHelper;
import dev.crossvas.lookingatplugin.helpers.ColorStyle;
import dev.crossvas.lookingatplugin.helpers.EnergyContainer;
import ic2.api.energy.EnergyNet;
import ic2.api.items.electric.ElectricItem;
import ic2.core.block.base.tiles.impls.BaseChargingBenchTileEntity;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.time.DurationFormatUtils;

public enum ChargingBenchInfoProvider implements IInfoProvider {
    THIS;

    @Override
    public void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseChargingBenchTileEntity bench) {
            text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(bench.getSinkTier()));
            text(helper, "ic2.probe.eu.max_in.name", bench.getMaxInput());
            ItemStack battery = bench.getStackInSlot(16);
            int benchAverageOut = bench.getMissingEnergy().getIntValue();
            int toDischargeEnergy = ElectricItem.MANAGER.getCharge(battery);
            int transferLimit = ElectricItem.MANAGER.getTransferLimit(battery);
            int maxCapacity = ElectricItem.MANAGER.getCapacity(battery);
            int missingEnergy = 0;
            for (int i = 0; i < 16; i++) {
                ItemStack toCharge = bench.getStackInSlot(i);
                if (!toCharge.isEmpty()) {
                    missingEnergy += (ElectricItem.MANAGER.getCapacity(toCharge) - ElectricItem.MANAGER.getCharge(toCharge));
                }
            }

            if (missingEnergy > 0) {
                int i = Math.min(benchAverageOut, missingEnergy);
                text(helper, ChatFormatting.GOLD, "ic2.probe.chargingBench.eta.name",
                        DurationFormatUtils.formatDuration(i <= 0 ? 0L : (missingEnergy / i * 50L), "HH:mm:ss"));
            }
            if (toDischargeEnergy > 0) {
                int dischargeEnergy = Math.min(transferLimit, toDischargeEnergy);
                helper.addBarElement(toDischargeEnergy, maxCapacity, Component.translatable("ic2.probe.discharging.eta.name",
                        DurationFormatUtils.formatDuration(dischargeEnergy <= 0 ? 0L : (toDischargeEnergy / dischargeEnergy * 50L), "HH:mm:ss")), ColorStyle.BLUE.aColor);
            }

            EnergyContainer result = EnergyContainer.getContainer(bench);
            int averageIn = result.getAverageIn();
            int packetsIn = result.getPacketsIn();
            if (averageIn > 0) {
                helper.addPaddingElement(0, 3);
                text(helper, ChatFormatting.AQUA, "tooltip.item.ic2.eu_reader.cable_flow_in", Formatters.EU_FORMAT.format(averageIn));
                text(helper, ChatFormatting.AQUA, "tooltip.item.ic2.eu_reader.packet_flow_in", Formatters.EU_FORMAT.format(packetsIn));
            }
        }
    }

    @Override
    public IFilter getFilter() {
        return SpecialFilters.EU_READER;
    }
}
