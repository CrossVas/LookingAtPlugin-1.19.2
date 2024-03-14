package dev.crossvas.lookingatplugin.info;

import dev.crossvas.lookingatplugin.IInfoProvider;
import dev.crossvas.lookingatplugin.ILookingAtHelper;
import dev.crossvas.lookingatplugin.LookingAtCommon;
import dev.crossvas.lookingatplugin.helpers.ColorStyle;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.base.tiles.impls.machine.multi.BaseMultiMachineTileEntity;
import ic2.core.block.machines.tiles.hv.PressureAlloyFurnaceTileEntity;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.text.DecimalFormat;

public enum BaseMultiblockMachineInfoProvider implements IInfoProvider {
    THIS;

    @Override
    public void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseTileEntity base) {
            if (base instanceof BaseMultiMachineTileEntity multiblock) {
                text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(multiblock.getTier()));
                text(helper, "ic2.probe.eu.max_in.name", multiblock.getMaxInput());
                text(helper, "ic2.probe.eu.usage.name", multiblock.getEnergyPerTick());
                if (blockEntity instanceof PressureAlloyFurnaceTileEntity furnace) {
                    int speed = furnace.getSpeed();
                    int maxSpeed = furnace.getMaxSpeed();
                    Component speedName = furnace.getSpeedName();
                    double scaledProgress = (double) speed / maxSpeed;
                    if (speed > 0) {
                        helper.addBarElement(speed, maxSpeed, speedName.copy().append(": " + new DecimalFormat().format(scaledProgress * 100.0) + "%").withStyle(ChatFormatting.WHITE), ColorStyle.ORANGE.aColor);
                    }
                }
                if (!multiblock.isValid) {
                    long time = multiblock.clockTime(512);
                    helper.addBarElement((int) time, 512, Component.literal("Next Reform: ").append(String.valueOf(512 - time)).append(" Ticks").withStyle(ChatFormatting.WHITE), ColorUtils.GRAY);
                }
                LookingAtCommon.addTankInfo(helper, multiblock);
            }
        }
    }

    @Override
    public IFilter getFilter() {
        return SpecialFilters.EU_READER;
    }
}
