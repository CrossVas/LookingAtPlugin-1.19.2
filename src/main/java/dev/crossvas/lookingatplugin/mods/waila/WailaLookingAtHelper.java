package dev.crossvas.lookingatplugin.mods.waila;

import dev.crossvas.lookingatplugin.ILookingAtHelper;
import dev.crossvas.lookingatplugin.TagRefs;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class WailaLookingAtHelper implements ILookingAtHelper {

    private final ListTag data = new ListTag();

    @Override
    public void addItemElement(ItemStack stack, Component text, boolean removeHarvestInfo) {
        CompoundTag stackData = new CompoundTag();
        CompoundTag stackTag = stack.save(new CompoundTag());
        stackTag.putString("stackText", Component.Serializer.toJson(text));
        stackData.put(TagRefs.TAG_ITEM, stackTag);
        data.add(stackData);
    }

    @Override
    public void addItemGridElement(List<ItemStack> stacks, Component component, ChatFormatting formatting) {
        CompoundTag stackListTag = new CompoundTag();
        ListTag stackList = new ListTag();
        for (ItemStack stack : stacks) {
            CompoundTag stackTag = new CompoundTag();
            stackTag.put("stack", stack.save(new CompoundTag()));
            stackTag.putInt("count", stack.getCount());
            stackList.add(stackTag);
        }
        if (!stackList.isEmpty()) {
            stackListTag.put(TagRefs.TAG_INVENTORY, stackList);
        }
        stackListTag.putString("stacksText", Component.Serializer.toJson(component));
        stackListTag.putInt("stackTextFormat", formatting.getId());
        data.add(stackListTag);
    }

    @Override
    public void addTextElement(Component text, ChatFormatting formatting, boolean append, boolean centered) {
        CompoundTag tag = new CompoundTag();
        tag.putString(TagRefs.TAG_TEXT, Component.Serializer.toJson(text));
        tag.putInt("formatting", formatting.getId());
        tag.putBoolean("append", append);
        tag.putBoolean("center", centered);
        data.add(tag);
    }

    @Override
    public void addEnergyElement(int currentEnergy, int maxEnergy, Component text) {
        CompoundTag energyData = new CompoundTag();
        energyData.putInt(TagRefs.TAG_ENERGY, currentEnergy);
        energyData.putInt(TagRefs.TAG_MAX, maxEnergy);
        energyData.putString("energyText", Component.Serializer.toJson(text));
        data.add(energyData);
    }

    @Override
    public void addBarElement(int current, int max, Component text, int color) {
        CompoundTag barTag = new CompoundTag();
        barTag.putInt(TagRefs.TAG_BAR, current);
        barTag.putInt(TagRefs.TAG_MAX, max);
        barTag.putString("barText", Component.Serializer.toJson(text));
        barTag.putInt(TagRefs.TAG_BAR_COLOR, color);
        data.add(barTag);
    }

    @Override
    public void addFluidElement(FluidStack stored, int maxCapacity) {
        CompoundTag fluidData = new CompoundTag();
        fluidData.put(TagRefs.TAG_FLUID, stored.writeToNBT(new CompoundTag()));
        fluidData.putInt(TagRefs.TAG_MAX, maxCapacity);
//        fluidData.putString(TagRefs.TAG_TEXT, Component.Serializer.toJson(text));
        data.add(fluidData);
    }

    @Override
    public void addFluidGridElement(List<FluidStack> fluids, Component component, ChatFormatting formatting) {
        ListTag fluidList = new ListTag();
        CompoundTag stackListTag = new CompoundTag();
        for (FluidStack stack : fluids) {
            CompoundTag stackTag = new CompoundTag();
            stackTag.put("fluid", stack.writeToNBT(new CompoundTag()));
            fluidList.add(stackTag);
        }
        if (!fluidList.isEmpty()) {
            stackListTag.put(TagRefs.TAG_INVENTORY_FLUID, fluidList);
        }
        stackListTag.putString("fluidsText", Component.Serializer.toJson(component));
        stackListTag.putInt("fluidsTextFormat", formatting.getId());
        data.add(stackListTag);
    }

    @Override
    public void addPaddingElement(int x, int y) {
        CompoundTag paddingTag = new CompoundTag();
        paddingTag.putInt(TagRefs.TAG_PADDING, x);
        paddingTag.putInt(TagRefs.TAG_PADDING_Y, y);
        data.add(paddingTag);
    }

    @Override
    public void transferData(CompoundTag serverData) {
        if (!this.data.isEmpty()) {
            serverData.put(TagRefs.TAG_DATA, this.data);
        }
    }
}
