package dev.crossvas.lookingatplugin.info;

import dev.crossvas.lookingatplugin.IInfoProvider;
import dev.crossvas.lookingatplugin.ILookingAtHelper;
import dev.crossvas.lookingatplugin.LookingAtCommon;
import dev.crossvas.lookingatplugin.helpers.ColorStyle;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.machine.single.BaseAdvMachineTileEntity;
import ic2.core.block.base.tiles.impls.machine.single.BaseMachineTileEntity;
import ic2.core.block.machines.tiles.lv.RareEarthExtractorTileEntity;
import ic2.core.block.machines.tiles.mv.RareEarthCentrifugeTileEntity;
import ic2.core.block.machines.tiles.mv.RefineryTileEntity;
import ic2.core.block.machines.tiles.mv.SlowGrinderTileEntity;
import ic2.core.block.machines.tiles.mv.VacuumCannerTileEntity;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.text.DecimalFormat;

public enum BaseMachineInfoProvider implements IInfoProvider {
    THIS;

    @Override
    public void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (canHandle(player)) {
            if (blockEntity instanceof BaseMachineTileEntity tile) {
                text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tile.getTier()));
                text(helper, "ic2.probe.eu.max_in.name", tile.getMaxInput());
                text(helper, "ic2.probe.eu.usage.name", tile.getEnergyPerTick());

                if (tile instanceof SlowGrinderTileEntity grinder) {
                    text(helper, "ic2.probe.scrap.chance.name", Formatters.XP_FORMAT.format(grinder.getChance(0.25F) * 100.0F));
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
                    if (speed > 0) {
                        helper.addBarElement(speed, maxSpeed, label, ColorStyle.ORANGE.aColor);
                    }
                }
                if (tile instanceof VacuumCannerTileEntity canner) {
                    speed = canner.speed;
                    maxSpeed = canner.getMaxSpeed();
                    progress = (float) speed / maxSpeed;
                    label = canner.getSpeedName().copy().append(": " + new DecimalFormat().format(progress * 100.0) + "%").withStyle(ChatFormatting.WHITE);
                    if (speed > 0) {
                        helper.addBarElement(speed, maxSpeed, label, ColorStyle.ORANGE.aColor);
                    }
                }
                if (tile instanceof RareEarthExtractorTileEntity extractor) {
                    float material = extractor.materialProgress;
                    float maxMaterial = extractor.getMaxSubProgress();
                    if (material > 0) {
                        helper.addBarElement((int) material, (int) maxMaterial, Component.translatable("ic2.probe.progress.material.name", Formatters.EU_READER_FORMAT.format(material)), ColorStyle.PURPLE.aColor);
                    }
                }
                if (tile instanceof RareEarthCentrifugeTileEntity centrifugal) {
                    float material = centrifugal.materialProgress;
                    float maxMaterial = centrifugal.getMaxSubProgress();
                    if (material > 0.0F) {
                        helper.addBarElement((int) material, (int) maxMaterial, Component.translatable("ic2.probe.progress.material.name", Formatters.EU_READER_FORMAT.format(material)), ColorStyle.PURPLE.aColor);
                    }

                    speed = centrifugal.getSpeed();
                    maxSpeed = centrifugal.getMaxSpeed();
                    progress = (float) speed / maxSpeed;
                    label = centrifugal.getSpeedName().copy().append(": " + new DecimalFormat().format(progress * 100.0) + "%").withStyle(ChatFormatting.WHITE);;
                    if (speed > 0) {
                        helper.addBarElement(speed, maxSpeed, label, ColorStyle.ORANGE.aColor);
                    }
                }

                float machineProgress = tile.getProgress();
                float maxMachineProgress = tile.getMaxProgress();
                float machineProgressTick = tile.progressPerTick;
                if (machineProgress > 0) {
                    int scaledProgress = (int) Math.min(6.0E7F, machineProgress / machineProgressTick);
                    int scaledProgressMax = (int) Math.min(6.0E7F, maxMachineProgress / machineProgressTick);
                    label = Component.translatable("ic2.probe.progress.full.name", scaledProgress, scaledProgressMax).append(" t").withStyle(ChatFormatting.WHITE);
                    helper.addBarElement(scaledProgress, scaledProgressMax, label, ColorStyle.BLUE.aColor);
                }
            }
        }
    }

    @Override
    public IFilter getFilter() {
        return SpecialFilters.EU_READER;
    }
}
