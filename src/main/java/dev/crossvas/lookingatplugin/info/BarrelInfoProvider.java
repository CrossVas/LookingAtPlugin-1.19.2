package dev.crossvas.lookingatplugin.info;

import dev.crossvas.lookingatplugin.IInfoProvider;
import dev.crossvas.lookingatplugin.ILookingAtHelper;
import dev.crossvas.lookingatplugin.helpers.ColorStyle;
import ic2.core.block.misc.tiles.BarrelTileEntity;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public enum BarrelInfoProvider implements IInfoProvider {
    THIS;

    @Override
    public void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BarrelTileEntity barrelTile) {
            // common
            int brewQuality = barrelTile.beerQuality;
            int alcoholLevel = barrelTile.getAlcoholLevel();
            int solidRatio = barrelTile.getSolidRatio();

            // 1
            int wheatAmount = barrelTile.wheatAmount;
            int hopsAmount = barrelTile.hopsAmount;
            int fluidAmount = barrelTile.fluidAmount;

            int age;
            double maxValue;
            double current;
            int maxFluidCapacity = BarrelTileEntity.FLUID_CAPACITY;
            int maxPotionCapacity = BarrelTileEntity.POTION_FLUID_CAPACITY;

            DecimalFormat format = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ROOT));
            FluidStack waterStack = new FluidStack(Fluids.WATER, fluidAmount);

            int brewType = barrelTile.brewType;
            switch (brewType) {
                case 0:
                case 3:
                case 4:
                case 6:
                case 7:
                case 8:
                case 9:
                default:
                    break;
                case 1:
                    age = barrelTile.age;
                    maxValue = 24000.0 * Math.pow(3.0, brewQuality == 4 ? 6.0 : (double) brewQuality);
                    current = age / maxValue * 100.0;

                    text(helper, getBrewType(brewType));
                    simpleText(helper, false, true, ChatFormatting.YELLOW, "ic2.probe.barrel.status.storage.name");
                    helper.addBarElement(wheatAmount, 64, Component.translatable("ic2.probe.barrel.beer.wheat.name", wheatAmount), ColorStyle.YELLOW.aColor);
                    helper.addBarElement(hopsAmount, 64, Component.translatable("ic2.probe.barrel.beer.hops.name", hopsAmount), ColorStyle.GREEN.aColor);
                    helper.addFluidElement(waterStack, maxFluidCapacity);

                    simpleText(helper, false, true, ChatFormatting.YELLOW, "ic2.probe.barrel.status.brew.name");
                    helper.addBarElement(brewQuality, 5, Component.translatable("ic2.probe.barrel.beer.quality." + brewQuality + ".name"), ColorStyle.BLUE.aColor);
                    helper.addBarElement(alcoholLevel, 6, Component.translatable("ic2.probe.barrel.beer.alc." + alcoholLevel + ".name"), ColorStyle.GREEN.aColor);
                    helper.addBarElement(solidRatio, 6, Component.translatable("ic2.probe.barrel.beer.solid." + solidRatio + ".name"), ColorStyle.YELLOW.aColor);
                    helper.addBarElement(age, (int) maxValue, Component.literal(format.format(current) + "%"), ColorStyle.BLUE.aColor);
                    break;
                case 2:
                    maxValue = barrelTile.timeNeededForRum();
                    age = (int) Math.min(barrelTile.age, maxValue);
                    text(helper, getBrewType(brewType));
                    simpleText(helper, false, true, ChatFormatting.YELLOW,"ic2.probe.barrel.status.brew.name");
                    helper.addBarElement(fluidAmount / 1000, 32, Component.translatable("ic2.probe.barrel.beer.sugar_cane.name", fluidAmount / 1000), ColorStyle.GREEN.aColor);
                    helper.addBarElement(age, (int) maxValue, Component.literal(format.format(Math.min(age, maxValue) * 100.0 / maxValue) + "%"), ColorStyle.BLUE.aColor);
                    break;
                case 5:
                    double ageWhisky = barrelTile.age;
                    int whiskyBrewTime = barrelTile.getWhiskBrewTime();
                    text(helper, getBrewType(brewType));
                    simpleText(helper, false, true, ChatFormatting.YELLOW, "ic2.probe.barrel.status.storage.name");
                    helper.addBarElement(hopsAmount, 16, Component.translatable("ic2.probe.barrel.whisky.grist.name", hopsAmount), ColorStyle.GREEN.aColor);
                    helper.addFluidElement(waterStack, maxFluidCapacity);
                    simpleText(helper, false, true, ChatFormatting.YELLOW, "ic2.probe.barrel.status.brew.name");
                    helper.addBarElement(Math.min(brewQuality, 50), 50, Component.translatable("ic2.probe.barrel.whisky.years.name", Math.min(brewQuality, 50)), ColorStyle.BLUE.aColor);
                    helper.addBarElement((int) ageWhisky, 1728000, Component.literal(format.format(ageWhisky / (whiskyBrewTime / 100.0)) + "%"), ColorStyle.BLUE.aColor);
                    break;
                case 10:
                    text(helper, getBrewType(brewType));
                    simpleText(helper, false, true, ChatFormatting.YELLOW, "ic2.probe.barrel.status.storage.name");
                    helper.addBarElement(wheatAmount, 20, Component.translatable("ic2.probe.barrel.beer.redstone.name", wheatAmount), ColorStyle.RED.aColor);
                    helper.addBarElement(hopsAmount, 20, Component.translatable("ic2.probe.barrel.beer.glowstone.name", hopsAmount), ColorStyle.YELLOW.aColor);
                    helper.addFluidElement(waterStack, maxPotionCapacity);
                    simpleText(helper, false, true, ChatFormatting.YELLOW, "ic2.probe.barrel.status.brew.name");
                    int brewedPotion = MobEffect.getId(barrelTile.potionType);
                    Component potionID = brewedPotion == -1 ? Component.translatable("tooltip.block.ic2.barrel.unknown") : barrelTile.potionType.getDisplayName();
                    text(helper, "ic2.probe.barrel.status.output.name", potionID);
                    helper.addBarElement(brewQuality, 6, Component.translatable("ic2.probe.barrel.potion.quality." + brewQuality + ".name", brewQuality), ColorStyle.BLUE.aColor);

                    age = barrelTile.age;
                    maxValue = 5000.0 * Math.pow(3.0, brewQuality);
                    current = age / maxValue;
                    helper.addBarElement(age, (int) maxValue, Component.literal(format.format(current * 100.0) + "%"), ColorStyle.BLUE.aColor);
                    break;
            }
        }
    }

    @Override
    public IFilter getFilter() {
        return SpecialFilters.THERMOMETER;
    }

    public String getBrewType(int type) {
        return switch (type) {
            case 0 -> "ic2.probe.barrel.status.empty.name";
            case 1 -> "ic2.probe.barrel.status.beer.name";
            case 2 -> "ic2.probe.barrel.status.rum.name";
            default -> "I AM ERROR";
            case 5 -> "ic2.probe.barrel.status.whisky.name";
            case 10 -> "ic2.probe.barrel.status.potion.name";
        };
    }
}
