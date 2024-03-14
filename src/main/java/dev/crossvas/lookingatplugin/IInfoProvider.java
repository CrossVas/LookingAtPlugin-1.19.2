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

    default void text(ILookingAtHelper helper, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), ChatFormatting.WHITE, false, false);
    }

    default void centeredText(ILookingAtHelper helper, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), ChatFormatting.WHITE, false, true);
    }

    default void appendText(ILookingAtHelper helper, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), ChatFormatting.WHITE, true, false);
    }

    default void text(ILookingAtHelper helper, boolean append, boolean centered, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), ChatFormatting.WHITE, append, centered);
    }

    default void text(ILookingAtHelper helper, ChatFormatting formatting, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), formatting, false, false);
    }

    default void centeredText(ILookingAtHelper helper, ChatFormatting formatting, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), formatting, false, true);
    }

    default void appendText(ILookingAtHelper helper, ChatFormatting formatting, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), formatting, true, false);
    }

    default void text(ILookingAtHelper helper, boolean append, boolean centered, ChatFormatting formatting, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), formatting, append, centered);
    }

    default void text(ILookingAtHelper helper, Component text) {
        helper.addTextElement(text, ChatFormatting.WHITE, false, false);
    }

    default void centeredText(ILookingAtHelper helper, Component text) {
        helper.addTextElement(text, ChatFormatting.WHITE, false, true);
    }

    default void appendText(ILookingAtHelper helper, Component text) {
        helper.addTextElement(text, ChatFormatting.WHITE, true, false);
    }

    default void text(ILookingAtHelper helper, boolean append, boolean centered, Component text) {
        helper.addTextElement(text, ChatFormatting.WHITE, append, centered);
    }
}
