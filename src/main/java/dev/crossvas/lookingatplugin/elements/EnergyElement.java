package dev.crossvas.lookingatplugin.elements;

import dev.crossvas.lookingatplugin.helpers.ColorStyle;
import net.minecraft.network.chat.Component;

public class EnergyElement extends AbstractBarElement {

    public EnergyElement(int current, int maxCapacity, ColorStyle mainColor) {
        super(current, maxCapacity, mainColor);
        this.current = current;
        this.max = maxCapacity;
        this.text = Component.empty();
    }

    @Override
    public Component getText() {
        return this.text;
    }

    public EnergyElement setText(Component text) {
        this.text = text;
        return this;
    }
}
