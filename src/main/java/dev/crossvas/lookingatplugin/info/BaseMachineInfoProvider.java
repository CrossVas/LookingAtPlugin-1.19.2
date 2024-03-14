package dev.crossvas.lookingatplugin.info;

import dev.crossvas.lookingatplugin.IInfoProvider;
import dev.crossvas.lookingatplugin.ILookingAtHelper;
import dev.crossvas.lookingatplugin.LookingAtCommon;
import dev.crossvas.lookingatplugin.mods.jade.style.ColorStyle;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.machine.single.BaseAdvMachineTileEntity;
import ic2.core.block.base.tiles.impls.machine.single.BaseMachineTileEntity;
import ic2.core.block.machines.tiles.mv.RefineryTileEntity;
import ic2.core.block.machines.tiles.mv.SlowGrinderTileEntity;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.text.DecimalFormat;

public enum BaseMachineInfoProvider implements IInfoProvider {
    INSTANCE;

    @Override
    public void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (canHandle(player)) {
            if (blockEntity instanceof BaseMachineTileEntity tile) {
                helper.addText(Component.translatable("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tile.getTier())), false);
                helper.addText(Component.translatable("ic2.probe.eu.max_in.name", tile.getMaxInput()), false);
                helper.addText(Component.translatable("ic2.probe.eu.usage.name", tile.getEnergyPerTick()), false);

                if (tile instanceof SlowGrinderTileEntity grinder) {
                    helper.addText(Component.translatable("ic2.probe.scrap.chance.name", Formatters.XP_FORMAT.format(grinder.getChance(0.25F) * 100.0F)), false);
                }

                if (tile instanceof RefineryTileEntity refinery) {
                    LookingAtCommon.addTankInfo(helper, refinery);
                }

                int speed;
                int maxSpeed;
                Component label;
                float progress;

                if (tile instanceof BaseAdvMachineTileEntity advancedMachine) {
                    speed = advancedMachine.speed;
                    maxSpeed = advancedMachine.getMaxSpeed();
                    progress = (float) speed / maxSpeed;
                    label = advancedMachine.getSpeedName().copy().append(": " + new DecimalFormat().format(progress * 100.0) + "%").withStyle(ChatFormatting.WHITE);
                    helper.addBarElement(speed, maxSpeed, label, ColorStyle.ORANGE.aColor);
                }
            }
        }
    }

    @Override
    public IFilter getFilter() {
        return SpecialFilters.EU_READER;
    }
}
