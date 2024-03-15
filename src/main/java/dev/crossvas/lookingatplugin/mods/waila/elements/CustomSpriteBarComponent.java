package dev.crossvas.lookingatplugin.mods.waila.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.crossvas.lookingatplugin.helpers.GuiHelper;
import mcp.mobius.waila.api.ITooltipComponent;
import mcp.mobius.waila.api.WailaHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CustomSpriteBarComponent extends GuiComponent implements ITooltipComponent, ITooltipComponent.HorizontalGrowing {

    public int width;
    private final float ratio;
    private final ResourceLocation texture;
    private final float u0;
    private final float u1;
    private final float v0;
    private final float v1;
    private final int spriteTint;
    private final int regionWidth;
    private final int regionHeight;
    private final Component text;

    public CustomSpriteBarComponent(float ratio, ResourceLocation texture, float u0, float u1, float v0, float v1, int regionWidth, int regionHeight, int tint, Component text) {
        this.ratio = ratio;
        this.texture = texture;
        this.u0 = u0;
        this.u1 = u1;
        this.v0 = v0;
        this.v1 = v1;
        this.spriteTint = tint;
        this.regionWidth = regionWidth;
        this.regionHeight = regionHeight;
        this.text = text;
    }

    public CustomSpriteBarComponent(float ratio, TextureAtlasSprite sprite, int regionWidth, int regionHeight, int tint, Component text) {
        this(ratio, sprite.atlas().location(), sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(), regionWidth, regionHeight, tint, text);
        this.width = Math.max(Minecraft.getInstance().font.width(this.text.getString()) + 5, 100);
    }

    public int getWidth() {
        return this.width;
    }

    @Override
    public int getMinimalWidth() {
        return this.width;
    }

    @Override
    public void setGrownWidth(int grownWidth) {
        this.width = grownWidth;
    }

    @Override
    public int getHeight() {
        return Minecraft.getInstance().font.lineHeight + 3;
    }

    @Override
    public void render(PoseStack matrices, int x, int y, float delta) {
        GuiHelper.renderBar(matrices, x, y, getWidth(), getHeight(), 0.0F, 0.4765625F, 0.04296875F, -5592406);
        matrices.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, this.texture);
        int mx = (int)((float)x + getWidth() * this.ratio);
        int my = y + getHeight() + 1;
        enableScissor(x + 1, y + 1, mx - 1, my - 1);
        int a = WailaHelper.getAlpha(this.spriteTint);
        int r = WailaHelper.getRed(this.spriteTint);
        int g = WailaHelper.getGreen(this.spriteTint);
        int b = WailaHelper.getBlue(this.spriteTint);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);

        for(int px1 = x; px1 < mx; px1 += this.regionWidth) {
            int px2 = px1 + this.regionWidth;

            for(int py1 = y; py1 < my; py1 += this.regionHeight) {
                int py2 = py1 + this.regionHeight;
                buffer.vertex(matrices.last().pose(), (float)px1, (float)py2, 0.0F).color(r, g, b, a).uv(this.u0, this.v1).endVertex();
                buffer.vertex(matrices.last().pose(), (float)px2, (float)py2, 0.0F).color(r, g, b, a).uv(this.u1, this.v1).endVertex();
                buffer.vertex(matrices.last().pose(), (float)px2, (float)py1, 0.0F).color(r, g, b, a).uv(this.u1, this.v0).endVertex();
                buffer.vertex(matrices.last().pose(), (float)px1, (float)py1, 0.0F).color(r, g, b, a).uv(this.u0, this.v0).endVertex();
            }
        }

        tessellator.end();
        RenderSystem.disableBlend();
        matrices.popPose();
        disableScissor();
        GuiHelper.renderText(matrices, this.text, x, y, getWidth());
    }
}
