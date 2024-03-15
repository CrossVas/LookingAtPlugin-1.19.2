package dev.crossvas.lookingatplugin.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import ic2.core.utils.math.ColorUtils;
import mcp.mobius.waila.api.WailaConstants;
import mcp.mobius.waila.api.WailaHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;
import java.util.function.Function;

public class GuiHelper {

    public static int getColorForFluid(FluidStack fluid) {
        return fluid.getFluid() == Fluids.LAVA ? -29925 : IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor() | -16777216;
    }

    public static void renderTank(PoseStack matrixStack, int x, int y, int width, int height, FluidStack fluid, int max, Component text) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        Function<ResourceLocation, TextureAtlasSprite> map = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
        width -= 2;
        int start = 1;
        Matrix4f matrix = matrixStack.last().pose();
        int lvl = (int)(fluid == null ? 0.0 : (double)fluid.getAmount() / (double)max * (double)width);
        int backColor = ColorUtils.doubleDarker(getColorForFluid(fluid));
        GuiComponent.fill(matrixStack, x + 1, y + 1, x + width + 1, y + height - 1, backColor);
        if (lvl > 0) {
            ResourceLocation stillTexture = IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture(fluid);
            TextureAtlasSprite liquidIcon = map.apply(stillTexture);
            if (!Objects.equals(liquidIcon, map.apply(MissingTextureAtlasSprite.getLocation()))) {
                int color = IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor(fluid);
                RenderSystem.setShaderColor((float)(color >> 16 & 255) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, (float)(color >> 24 & 255) / 255.0F);
                while(lvl > 0) {
                    int maxX = Math.min(16, lvl);
                    lvl -= maxX;
                    drawTexturedModalRect(matrix, x + start, y + 1, liquidIcon, maxX, height - 2);
                    start += maxX;
                }
            }

            TextHelper.renderScaledText(mc, matrixStack, x + 2, y + 2, ChatFormatting.WHITE.getColor(), mc.font.width(text.getString()), text);
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void drawTexturedModalRect(Matrix4f matrix, int x, int y, TextureAtlasSprite sprite, int width, int height) {
        float zLevel = 0.01F;
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        float u1 = sprite.getU0();
        float v1 = sprite.getV0();
        float u2 = sprite.getU1();
        float v2 = sprite.getV1();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(matrix, (float)x, (float)(y + height), zLevel).uv(u1, v1).endVertex();
        buffer.vertex(matrix, (float)(x + width), (float)(y + height), zLevel).uv(u1, v2).endVertex();
        buffer.vertex(matrix, (float)(x + width), (float)y, zLevel).uv(u2, v2).endVertex();
        buffer.vertex(matrix, (float)x, (float)y, zLevel).uv(u2, v1).endVertex();
        tessellator.end();
    }

    public static void renderBar(PoseStack matrices, int x, int y, float w, float h, float v0, float u1, float v1, int tint) {
        matrices.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, WailaConstants.COMPONENT_TEXTURE);
        int a = WailaHelper.getAlpha(tint);
        int r = WailaHelper.getRed(tint);
        int g = WailaHelper.getGreen(tint);
        int b = WailaHelper.getBlue(tint);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        buffer.vertex(matrices.last().pose(), (float)x, (float)(y + h + 1), 0.0F).color(r, g, b, a).uv(0.0859375F, v1).endVertex();
        buffer.vertex(matrices.last().pose(), (float)x + w, (float)(y + h + 1), 0.0F).color(r, g, b, a).uv(u1, v1).endVertex();
        buffer.vertex(matrices.last().pose(), (float)x + w, (float)y, 0.0F).color(r, g, b, a).uv(u1, v0).endVertex();
        buffer.vertex(matrices.last().pose(), (float)x, (float)y, 0.0F).color(r, g, b, a).uv(0.0859375F, v0).endVertex();
        tessellator.end();
        RenderSystem.disableBlend();
        matrices.popPose();
    }

    public static void renderText(PoseStack matrices, Component text, int x, int y, int w) {
        Font font = Minecraft.getInstance().font;
        int textWidth = font.width(text);
        float textX = (float)x + Math.max((float)(w - textWidth) / 2.0F, 0.0F);
        float textY = (float)(y + 3);
        MultiBufferSource.BufferSource textBuf = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        font.drawInBatch8xOutline(text.getVisualOrderText(), textX, textY, 11184810, 2697513, matrices.last().pose(), textBuf, 15728880);
        textBuf.endBatch();
    }
}
