package dev.crossvas.lookingatplugin.mods.top;

import dev.crossvas.lookingatplugin.ILookingAtHelper;
import dev.crossvas.lookingatplugin.helpers.Formatter;
import dev.crossvas.lookingatplugin.helpers.GuiHelper;
import dev.crossvas.lookingatplugin.mods.top.style.TOPBarStyles;
import ic2.probeplugin.styles.IC2Styles;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.api.TankReference;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.apiimpl.elements.ElementText;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.apiimpl.styles.TextStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

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
    public void addTextElement(Component text, boolean append) {
        info.element(new ElementText(text, new TextStyle()));
    }

    @Override
    public void addEnergyElement(int currentEnergy, int maxEnergy, Component text) {
        info.element(new ElementProgress(currentEnergy, maxEnergy, IC2Styles.EU_BAR.copy().numberFormat(NumberFormat.NONE).prefix(text)));
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
    public void addPaddingElement(int x, int y) {
        info.padding(x, y);
    }

    @Override
    public void transferData(CompoundTag serverData) {}
}
