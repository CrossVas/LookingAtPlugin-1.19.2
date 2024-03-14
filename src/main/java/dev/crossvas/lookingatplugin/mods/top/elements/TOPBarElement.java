package dev.crossvas.lookingatplugin.mods.top.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.crossvas.lookingatplugin.LookingAt;
import dev.crossvas.lookingatplugin.elements.AbstractBarElement;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IProgressStyle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TOPBarElement implements IElement {

    AbstractBarElement element;
    IProgressStyle style;

    public TOPBarElement(AbstractBarElement element, Component text, IProgressStyle style) {
        this.element = element;
        this.element.setText(text);
        this.style = style;
    }

    public TOPBarElement(FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public void render(PoseStack poseStack, int x, int y) {
        this.element.renderElement(poseStack, x, y);
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
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.element.getCurrent());
        buf.writeInt(this.element.getMax());
        buf.writeInt(this.style.getWidth());
        buf.writeInt(this.style.getHeight());
        buf.writeComponent(this.style.getPrefixComp());
        buf.writeComponent(this.style.getSuffixComp());
        buf.writeInt(this.style.getBorderColor());
        buf.writeInt(this.style.getFilledColor());
        buf.writeInt(this.style.getAlternatefilledColor());
        buf.writeInt(this.style.getBackgroundColor());
        buf.writeBoolean(this.style.isShowText());
        buf.writeByte(this.style.getNumberFormat().ordinal());
        buf.writeBoolean(this.style.isLifeBar());
        buf.writeBoolean(this.style.isArmorBar());
        buf.writeEnum(this.style.getAlignment());
    }

    @Override
    public ResourceLocation getID() {
        return LookingAt.rl("bar");
    }
}
