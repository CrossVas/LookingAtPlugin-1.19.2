package dev.crossvas.lookingatplugin.mods.waila.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.crossvas.lookingatplugin.helpers.GuiHelper;
import mcp.mobius.waila.api.ITooltipComponent;
import mcp.mobius.waila.api.component.BarComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class CustomBarComponent extends BarComponent implements ITooltipComponent.HorizontalGrowing {

    Component text;
    float ratio;
    int color;
    int width;

    public CustomBarComponent(float ratio, int color, Component text) {
        super(ratio, color, text);
        this.text = text;
        this.ratio = ratio;
        this.color = color;
        this.width = Minecraft.getInstance().font.width(this.text.getString()) + 5;
    }

    @Override
    public void render(PoseStack matrices, int x, int y, float delta) {
        GuiHelper.renderBar(matrices, x, y, getWidth(), getHeight(), 0.0F, 0.4765625F, 0.04296875F, this.color);
        GuiHelper.renderBar(matrices, x, y, getWidth() * this.ratio, getHeight(), 0.04296875F, 0.0859375F + 0.390625F * this.ratio, 0.0859375F, this.color);
        GuiHelper.renderText(matrices, this.text, x, y, getWidth());
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getMinimalWidth() {
        return getWidth();
    }

    @Override
    public void setGrownWidth(int grownWidth) {
        this.width = grownWidth;
    }

    @Override
    public int getHeight() {
        return Minecraft.getInstance().font.lineHeight + 3;
    }
}
