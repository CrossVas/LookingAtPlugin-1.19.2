package dev.crossvas.lookingatplugin.info;

import dev.crossvas.lookingatplugin.IInfoProvider;
import dev.crossvas.lookingatplugin.ILookingAtHelper;
import ic2.api.items.readers.IWrenchTool;
import ic2.core.block.base.features.IWrenchableTile;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.platform.registries.IC2Items;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public enum WrenchableInfoProvider implements IInfoProvider {
    THIS;

    @Override
    public void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (canHandle(player)) {
            ItemStack handHeldStack = player.getMainHandItem();
            ItemStack wrenchStack = IC2Items.WRENCH.getDefaultInstance();
            if (blockEntity instanceof IWrenchableTile wrenchableTile) {
                double actualRate = ((IWrenchTool) IC2Items.WRENCH.asItem()).getActualLoss(wrenchStack, wrenchableTile.getDropRate(player));
                if (actualRate > 0) {
                    if (handHeldStack.getItem() instanceof IWrenchTool tool) {
                        double dropChance = tool.getActualLoss(handHeldStack, wrenchableTile.getDropRate(player));
                        helper.addItemElement(wrenchStack, Component.literal(String.valueOf(Mth.floor(dropChance * 100.0))).append("% ").append(Component.translatable("ic2.probe.wrenchable.drop_chance.info").withStyle(ChatFormatting.GRAY)), true);
                    } else {
                        helper.addItemElement(wrenchStack, Component.translatable("ic2.probe.wrenchable.info").withStyle(ChatFormatting.GRAY), true);
                    }
                }
            }
        }
    }

    @Override
    public IFilter getFilter() {
        return SpecialFilters.ALWAYS_TRUE;
    }
}
