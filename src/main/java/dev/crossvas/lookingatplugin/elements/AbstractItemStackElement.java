package dev.crossvas.lookingatplugin.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.crossvas.lookingatplugin.mods.jade.JadeHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;

public class AbstractItemStackElement implements ILookingAtElement {
    private final ItemStack stack;
    private final float scale;
    private final String text;
    private Vec2 translation;

    public AbstractItemStackElement(ItemStack stack, float scale, @Nullable String text) {
        this.stack = stack;
        this.scale = scale == 0.0F ? 1.0F : scale;
        this.text = text;
    }

    public static AbstractItemStackElement of(ItemStack stack) {
        return new AbstractItemStackElement(stack, 1.0F, null);
    }

    @Override
    public void renderElement(PoseStack matrix, int x, int y) {
        if (!this.stack.isEmpty()) {
            JadeHelper.drawItem(matrix, x + this.translation.x, y + this.translation.y, this.stack, this.scale);
        }
    }

    public Vec2 getSize() {
        int size = Mth.floor(18.0F * this.scale);
        return new Vec2((float)size, (float)size);
    }

    public AbstractItemStackElement translate(Vec2 translation) {
        this.translation = translation;
        return this;
    }
}
