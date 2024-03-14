package dev.crossvas.lookingatplugin.info;

import dev.crossvas.lookingatplugin.IInfoProvider;
import dev.crossvas.lookingatplugin.ILookingAtHelper;
import dev.crossvas.lookingatplugin.helpers.Formatter;
import ic2.api.tiles.readers.IEUStorage;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.machines.tiles.lv.ElectrolyzerTileEntity;
import ic2.core.block.machines.tiles.mv.ChargedElectrolyzerTileEntity;
import ic2.core.block.storage.tiles.CreativeSourceTileEntity;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public enum EUStorageInfoProvider implements IInfoProvider {
    THIS;

    @Override
    public void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (canHandle(player)) {
            if (blockEntity instanceof BaseTileEntity baseTile) {
                if (baseTile instanceof CreativeSourceTileEntity) {
                    helper.addEnergyElement(1, 1, Component.translatable("ic2.probe.eu.storage.name", "Infinite").withStyle(ChatFormatting.WHITE));
                } else if (baseTile instanceof IEUStorage storage && !(baseTile instanceof ElectrolyzerTileEntity || baseTile instanceof ChargedElectrolyzerTileEntity)) {
                    helper.addEnergyElement(storage.getStoredEU(), storage.getMaxEU(), Component.translatable("ic2.probe.eu.storage.full.name", Formatter.formatInt(storage.getStoredEU(), 4), Formatter.formatInt(storage.getMaxEU(), 4)).withStyle(ChatFormatting.WHITE));
                }
            }
        }
    }

    @Override
    public IFilter getFilter() {
        return SpecialFilters.EU_READER;
    }
}
