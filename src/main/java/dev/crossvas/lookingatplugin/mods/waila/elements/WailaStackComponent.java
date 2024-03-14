package dev.crossvas.lookingatplugin.mods.waila.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.crossvas.lookingatplugin.elements.AbstractItemStackElement;
import mcp.mobius.waila.api.ITooltipComponent;
import net.minecraft.world.phys.Vec2;

public class WailaStackComponent implements ITooltipComponent {

    private final AbstractItemStackElement element;

    public WailaStackComponent(AbstractItemStackElement element) {
        this.element = element;
    }

    @Override
    public int getWidth() {
        return (int) element.getSize().x;
    }

    @Override
    public int getHeight() {
        return (int) this.element.getSize().y;
    }

    @Override
    public void render(PoseStack poseStack, int x, int y, float delta) {
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        element.renderElement(poseStack, 0, 1);
        poseStack.popPose();
    }

    public WailaStackComponent translation(Vec2 translation) {
        element.translate(translation);
        return this;
    }
}
