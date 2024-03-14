package dev.crossvas.lookingatplugin.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.crossvas.lookingatplugin.helpers.Formatter;
import dev.crossvas.lookingatplugin.helpers.GuiHelper;
import dev.crossvas.lookingatplugin.helpers.ColorStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;

public class AbstractFluidBarElement extends AbstractBarElement {

    FluidStack stored;

    public AbstractFluidBarElement(FluidStack fluid, int max) {
        super(fluid.getAmount(), max, ColorStyle.BROWN);
        this.stored = fluid;
    }

    @Override
    public int getWidth() {
        return Math.max(Minecraft.getInstance().font.width(getText().getString() + 5), 118);
    }

    @Override
    public Component getText() {
        int amount = stored.getAmount();
        if (amount == Integer.MAX_VALUE) {
            return Component.translatable("generic.info.fluid", stored.getDisplayName(), "Infinite").withStyle(ChatFormatting.WHITE);
        }
        return Component.translatable("ic2.barrel.info.fluid", stored.getDisplayName(), Formatter.formatNumber(this.getCurrent(), String.valueOf(this.getCurrent()).length() - 1), Formatter.formatNumber(this.getMax(), String.valueOf(this.getMax()).length() - 1));
    }

    @Override
    public void renderElement(PoseStack matrix, int x, int y) {
        int width = getWidth();
        int height = getHeight();
        createBarBox(matrix, x, y, width, height);
        GuiHelper.renderTank(matrix, x, y, width, height, stored, this.getMax(), getText());
    }
}
