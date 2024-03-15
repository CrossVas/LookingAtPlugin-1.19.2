package dev.crossvas.lookingatplugin.mods.top;

import dev.crossvas.lookingatplugin.ILookingAtHelper;
import dev.crossvas.lookingatplugin.helpers.ColorStyle;
import dev.crossvas.lookingatplugin.helpers.Formatter;
import dev.crossvas.lookingatplugin.helpers.GuiHelper;
import dev.crossvas.lookingatplugin.mods.top.style.TOPBarStyles;
import ic2.probeplugin.override.components.GridElement;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.TankReference;
import mcjty.theoneprobe.apiimpl.elements.ElementPadding;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.apiimpl.elements.ElementText;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.apiimpl.styles.TextStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class LookingAtTOPHelper implements ILookingAtHelper {

    private final IProbeInfo info;

    public LookingAtTOPHelper(IProbeInfo info) {
        this.info = info;
    }

    @Override
    public void addItemElement(ItemStack stack, Component text, boolean removeHarvestInfo) {
        IElement harvestInfoElement = info.getElements().get(1);
        if (removeHarvestInfo) {
            info.getElements().remove(harvestInfoElement); // harvest tool
        }
        info.horizontal().item(stack, new ItemStyle().width(15)).element(new ElementText(text, new TextStyle().topPadding(5)));
        info.element(harvestInfoElement);
    }

    @Override
    public void addItemGridElement(List<ItemStack> stacks, Component component, ChatFormatting formatting) {
        if (!stacks.isEmpty()) {
            info.element(new GridElement(Object2ObjectMaps.singleton(component.copy().withStyle(formatting), stacks)));
        }
    }

    @Override
    public void addTextElement(Component text, ChatFormatting formatting, boolean append, boolean centered) {
        IElement textElement = new ElementText(text.copy().withStyle(formatting), new TextStyle());
        if (append) {
            info.horizontal().element(info.getElements().get(info.getElements().size() - 1)).element(textElement);
        } else {
            if (centered) {
                int center = (118 - textElement.getWidth()) / 2;
                info.horizontal(new LayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).element(new ElementPadding(center, 0)).element(textElement).element(new ElementPadding(center, 0));
            } else {
                info.element(textElement);
            }
        }
    }

    @Override
    public void addEnergyElement(int currentEnergy, int maxEnergy, Component text) {
        info.element(new ElementProgress(currentEnergy, maxEnergy, TOPBarStyles.bar(ColorStyle.RED.aColor, text)));
    }

    @Override
    public void addBarElement(int current, int max, Component text, int color) {
        info.element(new ElementProgress(current, max, TOPBarStyles.bar(color, text)));
    }

    @Override
    public void addFluidElement(FluidStack fluid, int maxCapacity) {
        Component fluidComp = Component.translatable("ic2.barrel.info.fluid", fluid.getDisplayName(), Formatter.formatNumber(fluid.getAmount(), String.valueOf(fluid.getAmount()).length() - 1), Formatter.formatNumber(maxCapacity, String.valueOf(maxCapacity).length() - 1)).withStyle(ChatFormatting.WHITE);
        info.tank(new TankReference(maxCapacity, fluid.getAmount(), fluid), TOPBarStyles.tank(GuiHelper.getColorForFluid(fluid), fluidComp));
    }

    @Override
    public void addFluidGridElement(List<FluidStack> fluids, Component component, ChatFormatting formatting) {

    }

    @Override
    public void addPaddingElement(int x, int y) {
        info.padding(x, y);
    }

    @Override
    public void transferData(CompoundTag serverData) {}
}
