package dev.crossvas.lookingatplugin.mods.waila.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import mcp.mobius.waila.api.WailaConstants;
import mcp.mobius.waila.api.WailaHelper;
import mcp.mobius.waila.api.component.BarComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

public class CustomBarComponent extends BarComponent {

    Component text;
    float ratio;
    int color;

    public CustomBarComponent(float ratio, int color, Component text) {
        super(ratio, color, text);
        this.text = text;
        this.ratio = ratio;
        this.color = color;

    }

    @Override
    public void render(PoseStack matrices, int x, int y, float delta) {
        renderBar(matrices, x, y, getWidth(), 0.0F, 0.4765625F, 0.04296875F, this.color);
        renderBar(matrices, x, y, getWidth() * this.ratio, 0.04296875F, 0.0859375F + 0.390625F * this.ratio, 0.0859375F, this.color);
        renderText(matrices, this.text, x, y);
    }

    private void renderBar(PoseStack matrices, int x, int y, float w, float v0, float u1, float v1, int tint) {
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
        buffer.vertex(matrices.last().pose(), (float)x, (float)(y + getHeight() + 1), 0.0F).color(r, g, b, a).uv(0.0859375F, v1).endVertex();
        buffer.vertex(matrices.last().pose(), (float)x + w, (float)(y + getHeight() + 1), 0.0F).color(r, g, b, a).uv(u1, v1).endVertex();
        buffer.vertex(matrices.last().pose(), (float)x + w, (float)y, 0.0F).color(r, g, b, a).uv(u1, v0).endVertex();
        buffer.vertex(matrices.last().pose(), (float)x, (float)y, 0.0F).color(r, g, b, a).uv(0.0859375F, v0).endVertex();
        tessellator.end();
        RenderSystem.disableBlend();
        matrices.popPose();
    }

    private void renderText(PoseStack matrices, Component text, int x, int y) {
        Font font = Minecraft.getInstance().font;
        int textWidth = font.width(text);
        float textX = (float)x + Math.max((float)(getWidth() - textWidth) / 2.0F, 0.0F);
        float textY = (float)(y + 3);
        MultiBufferSource.BufferSource textBuf = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        font.drawInBatch8xOutline(text.getVisualOrderText(), textX, textY, 11184810, 2697513, matrices.last().pose(), textBuf, 15728880);
        textBuf.endBatch();
    }

    @Override
    public int getWidth() {
        return Math.max(Minecraft.getInstance().font.width(this.text.getString()) + 5, 118);
    }

    @Override
    public int getHeight() {
        return Minecraft.getInstance().font.lineHeight + 3;
    }
}
