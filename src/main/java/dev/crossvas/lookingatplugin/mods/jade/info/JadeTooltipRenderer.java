package dev.crossvas.lookingatplugin.mods.jade.info;

import dev.crossvas.lookingatplugin.TagRefs;
import dev.crossvas.lookingatplugin.helpers.Formatter;
import dev.crossvas.lookingatplugin.helpers.GuiHelper;
import dev.crossvas.lookingatplugin.mods.jade.JadeHelper;
import dev.crossvas.lookingatplugin.mods.jade.JadeRefs;
import dev.crossvas.lookingatplugin.mods.jade.style.ColorStyle;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.fluids.FluidStack;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.*;
import snownee.jade.impl.ui.ProgressStyle;


public class JadeTooltipRenderer implements IBlockComponentProvider, IEntityComponentProvider {

    public static final JadeTooltipRenderer INSTANCE = new JadeTooltipRenderer();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        append(iTooltip, blockAccessor);
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        append(iTooltip, entityAccessor);
    }

    private void append(ITooltip tooltip, Accessor<?> accessor) {
        CompoundTag serverData = accessor.getServerData();
        if (serverData.contains(TagRefs.TAG_DATA, Tag.TAG_LIST)) {
            ListTag list = serverData.getList(TagRefs.TAG_DATA, Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag serverTag = list.getCompound(i);
                if (serverTag.contains(TagRefs.TAG_TEXT, Tag.TAG_STRING)) {
                    Component text = Component.Serializer.fromJson(serverTag.getString(TagRefs.TAG_TEXT));
                    boolean append = serverTag.getBoolean("append");
                    if (append) {
                        tooltip.append(text.copy().withStyle(JadeHelper.getFormattingStyle()));
                    } else {
                        tooltip.add(text.copy().withStyle(JadeHelper.getFormattingStyle()));
                    }
                }
                if (serverTag.contains(TagRefs.TAG_ITEM)) {
                    ItemStack stack = ItemStack.of(serverTag.getCompound(TagRefs.TAG_ITEM));
                    Component text = Component.Serializer.fromJson(serverTag.getCompound(TagRefs.TAG_ITEM).getString("stackText"));
                    tooltip.add(tooltip.getElementHelper().spacer(0, 3));
                    tooltip.add(tooltip.getElementHelper().item(stack).align(IElement.Align.LEFT).translate(new Vec2(-2, -5)));
                    if (text != null) {
                        tooltip.append(text);
                    }
                }
                if (serverTag.contains(TagRefs.TAG_ENERGY)) {
                    int current = serverTag.getInt(TagRefs.TAG_ENERGY);
                    int max = serverTag.getInt(TagRefs.TAG_MAX);
                    if (JadeHelper.forceTopStyle()) {
                        BoxStyle boxStyle = JadeHelper.getStyle(ColorStyle.RED);
                        IProgressStyle progressStyle = JadeHelper.getProgressStyle(ColorStyle.RED);
                        Component text = Component.Serializer.fromJson(serverTag.getString("energyText"));
                        tooltip.add(tooltip.getElementHelper().progress((float) current / max, text.copy().withStyle(JadeHelper.getFormattingStyle()), progressStyle, boxStyle, true));
                    } else {
                        Component text = Component.translatable("ic2.probe.eu.storage.full.name", current, max).withStyle(ChatFormatting.GRAY);
                        IProgressStyle progressStyle = tooltip.getElementHelper().progressStyle().color(-5636096, -10092544);
                        tooltip.add(tooltip.getElementHelper().progress((float) current / max, text, progressStyle, BoxStyle.DEFAULT, true));
                    }

                }
                if (serverTag.contains(TagRefs.TAG_FLUID)) {
                    if (accessor instanceof BlockAccessor blockAccessor) {
                        Block block = blockAccessor.getBlock();
                        JadeTankInfoRemover.TANK_REMOVAL.add(block);
                    }
                    FluidStack fluid = FluidStack.loadFluidStackFromNBT(serverTag.getCompound(TagRefs.TAG_FLUID));
                    int max = serverTag.getInt(TagRefs.TAG_MAX);
                    if (fluid.getAmount() > 0) {
                        if (JadeHelper.forceTopStyle()) {
                            IElementHelper helper = tooltip.getElementHelper();
                            IProgressStyle progressStyle = helper.progressStyle().overlay(helper.fluid(fluid));
                            tooltip.add(helper.progress((float) fluid.getAmount() / max, Component.translatable("ic2.barrel.info.fluid", fluid.getDisplayName(), Formatter.formatNumber(fluid.getAmount(), String.valueOf(fluid.getAmount()).length() - 1), Formatter.formatNumber(max, String.valueOf(max).length() - 1)).withStyle(JadeHelper.getFormattingStyle()), progressStyle,
                                    JadeHelper.getStyle(GuiHelper.getColorForFluid(fluid)), true));
                        } else {
                            String current = IDisplayHelper.get().humanReadableNumber(fluid.getAmount(), "B", true);
                            String maxS = IDisplayHelper.get().humanReadableNumber(max, "B", true);
                            Component text;
                            if (accessor.showDetails()) {
                                text = Component.translatable("jade.fluid2", IDisplayHelper.get().stripColor(fluid.getDisplayName()).withStyle(ChatFormatting.WHITE), Component.literal(current).withStyle(ChatFormatting.WHITE), maxS).withStyle(ChatFormatting.GRAY);
                            } else {
                                text = Component.translatable("jade.fluid", IDisplayHelper.get().stripColor(fluid.getDisplayName()), current);
                            }
                            IElementHelper helper = tooltip.getElementHelper();
                            IProgressStyle progressStyle = helper.progressStyle().overlay(helper.fluid(fluid));
                            tooltip.add(helper.progress((float) fluid.getAmount() / max, text, progressStyle, BoxStyle.DEFAULT, true));
                        }
                    }
                }
                if (serverTag.contains(TagRefs.TAG_BAR)) {
                    int color = serverTag.getInt(TagRefs.TAG_BAR_COLOR);
                    int current = serverTag.getInt(TagRefs.TAG_BAR);
                    int max = serverTag.getInt(TagRefs.TAG_MAX);
                    BoxStyle boxStyle = JadeHelper.forceTopStyle() ? JadeHelper.getStyle(color) : BoxStyle.DEFAULT;
                    IProgressStyle progressStyle = JadeHelper.forceTopStyle() ? JadeHelper.getProgressStyle(color) : new ProgressStyle().color(color, ColorUtils.darker(color));
                    Component label = Component.Serializer.fromJson(serverTag.getString("barText"));
                    tooltip.add(tooltip.getElementHelper().progress((float) current / max, label, progressStyle, boxStyle, true));
                }
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeRefs.INFO_RENDERER;
    }
}
