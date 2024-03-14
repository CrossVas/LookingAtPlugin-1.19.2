package dev.crossvas.lookingatplugin.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.crossvas.lookingatplugin.helpers.TextHelper;
import dev.crossvas.lookingatplugin.mods.jade.style.ColorStyle;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;

public abstract class AbstractBarElement implements ILookingAtElement {

    int current, max;
    final ColorStyle mainColor;
    Component text;

    public AbstractBarElement(int current, int max, ColorStyle mainColor) {
        this.current = current;
        this.max = max;
        this.mainColor = mainColor;
        this.text = Component.empty();
    }

    @Override
    public void renderElement(PoseStack matrix, int x, int y) {
        int width = getWidth();
        int height = getHeight();
        createBarBox(matrix, x, y, width, height);
        fillBackground(matrix, x, y, width, height);
        fillBar(getCurrent(), getMax(), matrix, x, y, width, height);
        TextHelper.renderScaledText(Minecraft.getInstance(), matrix, x + 2, y + 2, ColorStyle.BLUE.aColor, width, getText());
    }

    public void createBarBox(PoseStack matrix, int x, int y, int width, int height) {
        GuiComponent.fill(matrix, x, y, x + width - 1, y + 1, ColorUtils.WHITE);
        GuiComponent.fill(matrix, x, y, x + 1, y + height - 1, ColorUtils.WHITE);
        GuiComponent.fill(matrix, x + width - 1, y, x + width, y + height - 1, ColorUtils.WHITE);
        GuiComponent.fill(matrix, x, y + height - 1, x + width, y + height, ColorUtils.WHITE);
    }

    public void fillBackground(PoseStack matrix, int x, int y, int width, int height) {
        GuiComponent.fill(matrix, x + 1, y + 1, x + width - 1, y + height - 1, this.mainColor.back);
    }

    public void fillBar(long current, long max, PoseStack matrixStack, int x, int y, int w, int h) {
        if (current > 0L && max > 0L) {
            int dx = (int) Math.min(current * (long)(w - 2) / max, (long)(w - 2));
            for(int xx = x + 1; xx < x + dx + 1; ++xx) {
                int color = (xx & 1) == 0 ? this.mainColor.aColor : this.mainColor.bColor;
                GuiComponent.fill(matrixStack, xx, y + 1, xx + 1, y + h - 1, color);
            }
        }
    }

    public AbstractBarElement setText(Component text) {
        this.text = text;
        return this;
    }

    public int getWidth() {
        return Minecraft.getInstance().font.width(getText().getString()) + 5;
    }

    public int getHeight() {
        return Minecraft.getInstance().font.lineHeight + 3;
    }

    @Override
    public Vec2 getSize() {
        return new Vec2(getHeight(), getWidth());
    }

    public int getCurrent() {
        return this.current;
    }

    public int getMax() {
        return this.max;
    }

    public Component getText() {
        return this.text;
    }
}
