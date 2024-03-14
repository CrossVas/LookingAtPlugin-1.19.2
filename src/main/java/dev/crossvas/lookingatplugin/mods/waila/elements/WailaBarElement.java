package dev.crossvas.lookingatplugin.mods.waila.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.crossvas.lookingatplugin.elements.AbstractBarElement;
import mcp.mobius.waila.api.ITooltipComponent;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WailaBarElement extends GuiComponent implements ITooltipComponent {

    AbstractBarElement element;

    public WailaBarElement(AbstractBarElement element, Component text) {
        this.element = element;
        this.element.setText(text);
    }

    @Override
    public int getWidth() {
        return this.element.getWidth();
    }

    @Override
    public int getHeight() {
        return this.element.getHeight();
    }

    @Override
    public void render(PoseStack poseStack, int x, int y, float delta) {
        this.element.renderElement(poseStack, x, y);
    }
}
