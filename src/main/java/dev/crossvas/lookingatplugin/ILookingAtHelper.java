package dev.crossvas.lookingatplugin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface ILookingAtHelper {

    void addItem(ItemStack stack, Component component, boolean removeHarvestInfo);
    void addText(Component text, boolean append);
    void addEnergyElement(int currentEnergy, int maxEnergy, Component text);
    void addBarElement(int current, int max, Component text, int color);
    void addFluidElement(FluidStack stored, int maxCapacity);
    void transferData(CompoundTag serverData);
}
