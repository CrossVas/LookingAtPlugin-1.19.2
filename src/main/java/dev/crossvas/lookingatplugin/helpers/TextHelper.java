package dev.crossvas.lookingatplugin.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.text.NumberFormat;

public class TextHelper {

    private static final NumberFormat intFormatter = NumberFormat.getIntegerInstance();
    private static final NumberFormat doubleFormatter = NumberFormat.getNumberInstance();

    public static Component getHoursMinutes(int seconds) {
        int minutes = Mth.ceil(seconds / 60.0);
        int hours = minutes / 60;
        return hours > 0 ? Component.translatable("generic.info.hours_minutes", hours, minutes % 60) :
                Component.translatable("generic.info.minutes", minutes);
    }

    public static String format(long count) {
        return intFormatter.format(count);
    }

    public static String format(double count) {
        return doubleFormatter.format(count);
    }

    public static void renderScaledText(Minecraft mc, PoseStack matrix, float x, float y, int color, float maxWidth, Component component) {
        int length = mc.font.width(component);
        if (length <= maxWidth) {
            mc.font.drawShadow(matrix, component, x, y, color);
        } else {
            float scale = maxWidth / length;
            float reverse = 1 / scale;
            float yAdd = 4 - (scale * 8) / 2F;
            matrix.pushPose();
            matrix.scale(scale, scale, scale);
            mc.font.drawShadow(matrix, component, (int) (x * reverse), (int) ((y * reverse) + yAdd), color);
            matrix.popPose();
        }
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
