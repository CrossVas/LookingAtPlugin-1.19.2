package dev.crossvas.lookingatplugin.info;

import dev.crossvas.lookingatplugin.IInfoProvider;
import dev.crossvas.lookingatplugin.ILookingAtHelper;
import dev.crossvas.lookingatplugin.LookingAtCommon;
import dev.crossvas.lookingatplugin.helpers.Formatter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.BaseGeneratorTileEntity;
import ic2.core.block.generators.tiles.*;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public enum BaseGeneratorInfoProvider implements IInfoProvider {
    THIS;

    @Override
    public void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseGeneratorTileEntity gen) {
            float euProduction = gen.getEUProduction();
            text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(gen.getSourceTier()));
            text(helper, "ic2.probe.eu.output.current.name", Formatter.formatNumber((double) euProduction, 5));

            text(helper, "ic2.probe.eu.output.max.name", gen.getMaxEnergyOutput());
            if (gen instanceof SolarTurbineTileEntity solarTurbine) {
                int heat = solarTurbine.getHeat();
                text(helper, "ic2.probe.heat.name", Formatter.THERMAL_GEN.format((double) ((float) heat / 240.0F)));
                LookingAtCommon.addTankInfo(helper, solarTurbine);
            }
            if (gen instanceof ThermalGeneratorTileEntity thermal) {
                float subProduction = thermal.subProduction.getProduction(2000.0F);
                text(helper, "ic2.probe.production.passive.name", Formatter.THERMAL_GEN.format((double) subProduction));
                LookingAtCommon.addTankInfo(helper, thermal);
            }
            if (gen instanceof GeoGenTileEntity geo) {
                LookingAtCommon.addTankInfo(helper, geo);
            }
            if (gen instanceof LiquidFuelGenTileEntity liquidGen) {
                LookingAtCommon.addTankInfo(helper, liquidGen);
            }

            if ((gen instanceof SlagGenTileEntity || gen instanceof FuelGenTileEntity) && gen.fuel > 0) {
                helper.addBarElement(gen.fuel, gen.getMaxFuel(), Component.translatable("ic2.probe.fuel.storage.name").append(String.valueOf(gen.fuel)), ColorUtils.DARK_GRAY);
            }
        }
    }

    @Override
    public IFilter getFilter() {
        return SpecialFilters.EU_READER;
    }
}
