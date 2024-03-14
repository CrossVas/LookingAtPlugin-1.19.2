package dev.crossvas.lookingatplugin.info;

import dev.crossvas.lookingatplugin.IInfoProvider;
import dev.crossvas.lookingatplugin.ILookingAtHelper;
import dev.crossvas.lookingatplugin.helpers.ColorStyle;
import ic2.api.crops.ICrop;
import ic2.api.crops.ICropRegistry;
import ic2.api.crops.ICropTile;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public enum CropInfoProvider implements IInfoProvider {
    THIS;

    @Override
    public void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof ICropTile tile) {
            ICrop crop = tile.getCrop();

            // storage stats
            int fertilizer = tile.getFertilizerStorage();
            int water = tile.getWaterStorage();
            int weedex = tile.getWeedExStorage();

            // env stats
            int nutrients = tile.getNutrients();
            int humidity = tile.getHumidity();
            int env = tile.getEnvironmentQuality();
            int light = tile.getLightLevel();

            if (crop != null) {
                // growth info
                int maxStage = crop.getGrowthSteps();
                int currentStage = tile.getGrowthStage();
                int scanLevel = tile.getScanLevel();
                int growthSpeed = tile.calculateGrowthSpeed();
                int points = tile.getGrowthPoints();
                int maxPoints = crop.getGrowthDuration(tile);

                // stats info
                int growth = tile.getGrowthStat();
                int gain = tile.getGainStat();
                int resistance = tile.getResistanceStat();
                boolean canGrow = crop.canGrow(tile);
                boolean waterLogCompat = crop.getCropType().isCompatible(tile.isWaterLogged());

                if (scanLevel < 1 && currentStage < maxStage && crop != ICropRegistry.WEED && crop != ICropRegistry.SEA_WEED) {
                    text(helper, "info.crop.ic2.data.unknown");
                } else {
//                    text(helper, "jei.ic2.reactor.by", crop.discoveredBy().getString());
                }
                if (scanLevel < 4 && currentStage < maxStage) {
                    helper.addBarElement(scanLevel, 4, Component.translatable("ic2.probe.crop.info.scan", scanLevel, 4), ColorStyle.GREEN.aColor);
                } else {
                    centeredText(helper, ChatFormatting.YELLOW, "ic2.probe.crop.growth");
                    if (currentStage < maxStage) {
                        helper.addBarElement(currentStage, maxStage, Component.translatable("ic2.probe.crop.info.stage", currentStage, maxStage), ColorStyle.GREEN.aColor);
                        helper.addBarElement(points, maxPoints, Component.translatable("ic2.probe.crop.info.points", points, maxPoints), ColorStyle.GREEN.aColor);
                        if (canGrow && waterLogCompat) {
                            centeredText(helper, ChatFormatting.GOLD, "ic2.probe.crop.grow.rate", growthSpeed);
                        } else {
                            centeredText(helper, ChatFormatting.RED, "ic2.probe.crop.grow.not");
                        }
                    } else {
                        helper.addBarElement(currentStage, maxStage, Component.translatable("ic2.probe.crop.info.stage_done"), ColorStyle.GREEN.aColor);
                    }

                    if (scanLevel >= 4) {
                        // title
                        centeredText(helper, ChatFormatting.YELLOW, "ic2.probe.crop.stats");
                        helper.addBarElement(growth, 31, Component.translatable("ic2.probe.crop.info.growth", growth, 31), ColorStyle.AQUA.aColor);
                        helper.addBarElement(gain, 31, Component.translatable("ic2.probe.crop.info.gain", gain, 31), ColorStyle.PURPLE.aColor);
                        helper.addBarElement(resistance, 31, Component.translatable("ic2.probe.crop.info.resistance", resistance, 31), ColorStyle.GOLD.aColor);

                        int stress = (crop.getProperties().getTier() - 1) * 4 + growth + gain + resistance;
                        int maxStress = crop.getStatInfluence(tile, humidity, nutrients, env) * 5;
                        helper.addBarElement(stress, maxStress, Component.translatable("ic2.probe.crop.info.needs", stress, maxStress), ColorStyle.AQUA.aColor);
                    }
                }
            }

            // title
            centeredText(helper, ChatFormatting.YELLOW, "ic2.probe.crop.storage");
            helper.addBarElement(fertilizer, 300, Component.translatable("ic2.probe.crop.info.fertilizer", fertilizer, 300), ColorStyle.BROWN.aColor);
            helper.addBarElement(water, 200, Component.translatable("ic2.probe.crop.info.water", water, 200), ColorStyle.CORNFLOWER.aColor);
            helper.addBarElement(weedex, 150, Component.translatable("ic2.probe.crop.info.weedex", weedex, 150), ColorStyle.PINK.aColor);

            // title
            centeredText(helper, ChatFormatting.YELLOW, "ic2.probe.crop.env");
            helper.addBarElement(nutrients, 20, Component.translatable("ic2.probe.crop.info.nutrients", nutrients, 20), ColorStyle.MONO_LIME.aColor);
            helper.addBarElement(humidity, 20, Component.translatable("ic2.probe.crop.info.humidity", humidity, 20), ColorStyle.CORNFLOWER.aColor);
            helper.addBarElement(env, 10, Component.translatable("ic2.probe.crop.info.env", env, 10), ColorStyle.AQUA.aColor);
            helper.addBarElement(light, 15, Component.translatable("ic2.probe.crop.info.light", light, 15), ColorStyle.YELLOW.aColor);
        }
    }

    @Override
    public IFilter getFilter() {
        return SpecialFilters.CROP_SCANNER;
    }
}
