package dev.crossvas.lookingatplugin.mods.waila.info;

import ic2.api.crops.ICrop;
import ic2.api.crops.ICropRegistry;
import ic2.api.crops.ICropTile;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.platform.registries.IC2Items;
import ic2.core.utils.helpers.StackUtil;
import mcp.mobius.waila.api.*;
import mcp.mobius.waila.api.component.ItemComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class CropIconProvider implements IDataProvider<BlockEntity>, IBlockComponentProvider {

    @Override
    public @Nullable ITooltipComponent getIcon(IBlockAccessor accessor, IPluginConfig config) {
        ItemStack iconStack = IC2Items.CROP_STICKS.getDefaultInstance();
        if (accessor.getBlockEntity() instanceof ICropTile cropTile) {
            CompoundTag tag = accessor.getData().raw().getCompound("CropInfo");
            int maxStage = tag.getInt("growthSteps");
            int currentStage = tag.getInt("growthStage");
            int scanLevel = tag.getInt("scanLevel");
            if (cropTile.getCrop() != null) {
                boolean condition = scanLevel < 1 && currentStage < maxStage && cropTile.getCrop() != ICropRegistry.WEED && cropTile.getCrop() != ICropRegistry.SEA_WEED;
                if (condition) {
                    iconStack = IC2Items.CROP_SEED.getDefaultInstance();
                } else {
                    iconStack = cropTile.getCrop().getDisplayItem();
                }
            }
        }
        return new ItemComponent(iconStack);
    }

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if (!StackUtil.hasHotbarItems(accessor.getPlayer(), SpecialFilters.CROP_SCANNER)) {
            return;
        }

        CompoundTag tag = accessor.getData().raw().getCompound("CropInfo");
        if (accessor.getBlockEntity() instanceof ICropTile tile) {
            int maxStage = tag.getInt("growthSteps");
            int currentStage = tag.getInt("growthStage");
            int scanLevel = tag.getInt("scanLevel");
            ICrop crop = tile.getCrop();
            if (crop != null) {
                tooltip.setLine(WailaConstants.OBJECT_NAME_TAG).with(Component.empty());
                if (scanLevel < 1 && currentStage < maxStage && crop != ICropRegistry.WEED && crop != ICropRegistry.SEA_WEED) {
                    tooltip.addLine(Component.translatable("info.crop.ic2.data.unknown").withStyle(ChatFormatting.WHITE));
                } else {
                    tooltip.setLine(WailaConstants.OBJECT_NAME_TAG).with(crop.getName().copy().withStyle(ChatFormatting.WHITE));
                    tooltip.addLine(Component.translatable("jei.ic2.reactor.by", crop.discoveredBy().getString()).withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.ITALIC));
                }
            }
        }
    }

    @Override
    public void appendData(IDataWriter iDataWriter, IServerAccessor<BlockEntity> iServerAccessor, IPluginConfig iPluginConfig) {
        if (iServerAccessor.getTarget() instanceof ICropTile tile) {
            CompoundTag tag = new CompoundTag();
            ICrop crop = tile.getCrop();
            if (crop != null) {
                // growth info
                tag.putInt("growthSteps", crop.getGrowthSteps());
                tag.putInt("growthStage", tile.getGrowthStage());
                tag.putInt("scanLevel", tile.getScanLevel());
            }
            iDataWriter.raw().put("CropInfo", tag);
        }
    }
}
