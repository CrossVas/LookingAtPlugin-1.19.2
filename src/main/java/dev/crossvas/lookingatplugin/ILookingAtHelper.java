package dev.crossvas.lookingatplugin;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface ILookingAtHelper {

    void addItemElement(ItemStack stack, Component component, boolean removeHarvestInfo);
    void addTextElement(Component text, ChatFormatting formatting, boolean append, boolean centered);
    void addEnergyElement(int currentEnergy, int maxEnergy, Component text);
    void addBarElement(int current, int max, Component text, int color);
    void addFluidElement(FluidStack stored, int maxCapacity);
    void addPaddingElement(int x, int y);
    void transferData(CompoundTag serverData);
}
