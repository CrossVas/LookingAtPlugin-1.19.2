package dev.crossvas.lookingatplugin.mods.top.style;

import ic2.core.utils.math.ColorUtils;
import ic2.probeplugin.override.styles.IProgressStyleBuilder;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.NumberFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class TOPBarStyles {

    public static IProgressStyle tank(int color, Component fluidName) {
        int width = Math.max(Minecraft.getInstance().font.width(fluidName.getString()) + 5, 118);
        return IProgressStyleBuilder.bounds(118, 12).borderlessColor(color, color, ColorUtils.doubleDarker(color)).prefix(fluidName).numberFormat(NumberFormat.NONE);
    }

    public static IProgressStyle bar(int color, Component label) {
        int width = Math.max(Minecraft.getInstance().font.width(label.getString()) + 5, 118);
        return IProgressStyleBuilder.bounds(118, 12).borderlessColor(color, ColorUtils.darker(color), ColorUtils.doubleDarker(color)).prefix(label).numberFormat(NumberFormat.NONE);
    }
}
