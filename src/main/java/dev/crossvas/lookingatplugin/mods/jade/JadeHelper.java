package dev.crossvas.lookingatplugin.mods.jade;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.crossvas.lookingatplugin.helpers.ColorStyle;
import dev.crossvas.lookingatplugin.mods.jade.style.SpecialBoxStyle;
import dev.crossvas.lookingatplugin.mods.jade.style.SpecialProgressStyle;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import snownee.jade.Jade;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IProgressStyle;
import snownee.jade.impl.ui.ProgressStyle;
import snownee.jade.overlay.OverlayRenderer;

public class JadeHelper {

    public static void drawItem(PoseStack matrixStack, float x, float y, ItemStack stack, float scale) {
        if (!(OverlayRenderer.alpha < 0.5F)) {
            RenderSystem.enableDepthTest();
            PoseStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushPose();
            modelViewStack.mulPoseMatrix(matrixStack.last().pose());
            float o = 8.0F * scale;
            modelViewStack.translate((double)(x + o), (double)(y + o), 0.0);
            scale *= Math.min(1.0F, OverlayRenderer.alpha + 0.2F);
            modelViewStack.scale(scale, scale, scale);
            modelViewStack.translate(-8.0, -8.0, 0.0);
            Minecraft.getInstance().getItemRenderer().renderGuiItem(stack, 0, 0);
            modelViewStack.popPose();
            RenderSystem.applyModelViewMatrix();
            RenderSystem.disableDepthTest();
        }
    }

    public static boolean forceTopStyle() {
        return Jade.CONFIG.get().getPlugin().get(LookingAtJadePlugin.TOP_STYLE);
    }

    public static BoxStyle getStyle(ColorStyle color) {
        return forceTopStyle() ? new SpecialBoxStyle(color.back) : BoxStyle.DEFAULT;
    }

    public static BoxStyle getStyle(int color) {
        return forceTopStyle() ? new SpecialBoxStyle(ColorUtils.doubleDarker(color)) : BoxStyle.DEFAULT;
    }

    public static IProgressStyle getProgressStyle(ColorStyle color) {
        return forceTopStyle() ? new SpecialProgressStyle().color(color.aColor, color.bColor) : new ProgressStyle().color(color.aColor, color.bColor);
    }

    public static IProgressStyle getProgressStyle(int color) {
        return forceTopStyle() ? new SpecialProgressStyle().color(color, ColorUtils.darker(color)) : new ProgressStyle().color(color, ColorUtils.darker(color));
    }

    public static ChatFormatting getFormattingStyle() {
        return forceTopStyle() ? ChatFormatting.WHITE : ChatFormatting.GRAY;
    }
}
