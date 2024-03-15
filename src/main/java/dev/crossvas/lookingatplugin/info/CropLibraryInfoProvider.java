package dev.crossvas.lookingatplugin.info;

import dev.crossvas.lookingatplugin.IInfoProvider;
import dev.crossvas.lookingatplugin.ILookingAtHelper;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergySink;
import ic2.core.block.base.tiles.impls.BaseCropLibraryTileEntity;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.helpers.StackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public enum CropLibraryInfoProvider implements IInfoProvider {
    THIS;

    @Override
    public void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseCropLibraryTileEntity lib) {
            if (lib instanceof IEnergySink sink) {
                text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(sink.getSinkTier()));
                text(helper, "ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(sink.getSinkTier()));
                text(helper, "ic2.probe.eu.usage.name", 1);
            }
            int cropCount = lib.syncer.getCropCount();
            int statCount = lib.syncer.getStatCount();
            int sizeLimit = lib.storage.getSizeLimit();
            int typeLimit = lib.storage.getTypeLimit();
            int statLimit = lib.storage.getStatLimit();
            if (typeLimit != -1) {
                text(helper, "ic2.probe.crop_library.type.name", cropCount, typeLimit);
                text(helper, "ic2.probe.crop_library.stat.name", statCount, statLimit * typeLimit);
                text(helper, "ic2.probe.crop_library.size.name", sizeLimit);
            }
            helper.addItemGridElement(StackUtil.copyNonEmpty(lib.storage.getTypes()), Component.translatable("ic2.probe.crop_library.name"), ChatFormatting.YELLOW);
        }
    }

    @Override
    public IFilter getFilter() {
        return SpecialFilters.EU_READER;
    }
}
