package dev.crossvas.lookingatplugin.mods.waila.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.crossvas.lookingatplugin.elements.FluidElement;
import mcp.mobius.waila.api.ITooltipComponent;
import net.minecraft.client.gui.GuiComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WailaFluidBarElement extends GuiComponent implements ITooltipComponent {

    FluidElement element;

    public WailaFluidBarElement(FluidElement element) {
        this.element = element;
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
