package dev.crossvas.lookingatplugin.mods.top.info;

import ic2.api.crops.ICrop;
import ic2.api.crops.ICropRegistry;
import ic2.core.block.crops.CropRegistry;
import ic2.core.block.crops.CropTileEntity;
import ic2.core.platform.registries.IC2Items;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class CropIconProvider implements IBlockDisplayOverride {

    @Override
    public boolean overrideStandardInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player player, Level level, BlockState blockState, IProbeHitData iProbeHitData) {
        BlockEntity blockEntity = level.getBlockEntity(iProbeHitData.getPos());
        if (blockEntity instanceof CropTileEntity tile) {
            ItemStack icon = new ItemStack(IC2Items.CROP_STICKS);
            ICrop crop = tile.getCrop();
            Component name;
            Component discovered = Component.empty();
            if (crop != null) {
                int maxSize = crop.getGrowthSteps();
                int currentSize = tile.getGrowthStage();
                if (tile.getScanLevel() < 1 && currentSize < maxSize && crop != ICropRegistry.WEED && crop != CropRegistry.SEA_WEED) {
                    icon = new ItemStack(IC2Items.CROP_SEED);
                    name = Component.translatable("info.crop.ic2.data.unknown");
                } else {
                    name = crop.getName();
                    icon = !crop.getDisplayItem().isEmpty() ? crop.getDisplayItem() : new ItemStack(IC2Items.CROP_SEED);
                    discovered = crop.discoveredBy();
                }
                iProbeInfo.horizontal().item(icon, new ItemStyle().width(20)).vertical(new LayoutStyle().alignment(ElementAlignment.ALIGN_TOPLEFT)).text(name).text(CompoundText.create().text("§oby ").text(discovered).text("§r").style(TextStyleClass.INFO));
                iProbeInfo.text(CompoundText.create().style(TextStyleClass.MODNAME).text(((ModContainer) ModList.get().getModContainerById(ForgeRegistries.BLOCKS.getKey(tile.getBlockState().getBlock()).getNamespace()).get()).getModInfo().getDisplayName()));
                return true;
            }
        }

        return false;
    }
}
