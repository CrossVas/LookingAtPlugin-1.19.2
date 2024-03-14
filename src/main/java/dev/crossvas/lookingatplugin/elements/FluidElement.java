package dev.crossvas.lookingatplugin.elements;

import dev.crossvas.lookingatplugin.helpers.Formatter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;

public class FluidElement extends AbstractFluidBarElement {

    public FluidElement(FluidStack stored, int capacity) {
        super(stored, capacity);
        this.text = Component.empty();
    }

    @Override
    public Component getText() {
        int amount = stored.getAmount();
        if (amount == Integer.MAX_VALUE) {
            return Component.translatable("generic.info.fluid", stored.getDisplayName(), "Infinite").withStyle(ChatFormatting.WHITE);
        }
        return Component.translatable("ic2.barrel.info.fluid", stored.getDisplayName(), Formatter.formatNumber(stored.getAmount(), String.valueOf(stored.getAmount()).length() - 1), Formatter.formatNumber(this.max, String.valueOf(this.max).length() - 1));
    }

    public FluidElement setText(Component text) {
        this.text = text;
        return this;
    }
}
