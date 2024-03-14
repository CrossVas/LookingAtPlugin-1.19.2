package dev.crossvas.lookingatplugin.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.phys.Vec2;

public interface ILookingAtElement {

    void renderElement(PoseStack matrix, int x, int y);

    Vec2 getSize();
}
