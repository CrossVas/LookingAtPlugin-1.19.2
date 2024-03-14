package dev.crossvas.lookingatplugin;

import ic2.core.inventory.filter.IFilter;
import ic2.core.utils.helpers.StackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IInfoProvider {

    void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player);

    IFilter getFilter();

    default boolean canHandle(Player player) {
        return StackUtil.hasHotbarItems(player, getFilter());
    }

    default void simpleText(ILookingAtHelper helper, boolean append, boolean centered, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), ChatFormatting.WHITE, append, centered);
    }

    default void simpleText(ILookingAtHelper helper, boolean append, boolean centered, ChatFormatting formatting, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), formatting, append, centered);
    }
}
