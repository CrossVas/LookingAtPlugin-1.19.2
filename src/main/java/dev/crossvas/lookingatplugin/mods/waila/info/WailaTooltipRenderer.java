package dev.crossvas.lookingatplugin.mods.waila.info;

import dev.crossvas.lookingatplugin.TagRefs;
import dev.crossvas.lookingatplugin.elements.AbstractItemStackElement;
import dev.crossvas.lookingatplugin.helpers.Formatter;
import dev.crossvas.lookingatplugin.mods.jade.style.ColorStyle;
import dev.crossvas.lookingatplugin.mods.waila.elements.CustomBarComponent;
import dev.crossvas.lookingatplugin.mods.waila.elements.WailaStackComponent;
import mcp.mobius.waila.api.*;
import mcp.mobius.waila.api.component.PairComponent;
import mcp.mobius.waila.api.component.SpacingComponent;
import mcp.mobius.waila.api.component.SpriteBarComponent;
import mcp.mobius.waila.api.component.WrappedComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Function;

public class WailaTooltipRenderer implements IBlockComponentProvider, IEntityComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        append(tooltip, accessor);
    }

    @Override
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        append(tooltip, (IBlockAccessor) accessor);
    }

    private void append(ITooltip tooltip, IBlockAccessor accessor) {
        CompoundTag serverData = accessor.getServerData();
        if (serverData.contains(TagRefs.TAG_DATA, Tag.TAG_LIST)) {
            ListTag list = serverData.getList(TagRefs.TAG_DATA, Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag serverTag = list.getCompound(i);
                if (serverTag.contains(TagRefs.TAG_TEXT, Tag.TAG_STRING)) {
                    Component text = Component.Serializer.fromJson(serverTag.getString(TagRefs.TAG_TEXT));
                    boolean append = serverTag.getBoolean("append");
                    if (append) {
                        tooltip.getLine(tooltip.getLineCount() - 1).with(text);
                    } else {
                        tooltip.addLine(text);
                    }
                }
                if (serverTag.contains(TagRefs.TAG_ITEM)) {
                    ItemStack stack = ItemStack.of(serverTag.getCompound(TagRefs.TAG_ITEM));
                    Component text = Component.Serializer.fromJson(serverTag.getCompound(TagRefs.TAG_ITEM).getString("stackText"));
                    tooltip.addLine().with(new WailaStackComponent(AbstractItemStackElement.of(stack)).translation(new Vec2(0, -2)));
                    if (text != null) {
                        tooltip.getLine(tooltip.getLineCount() - 1).with(text);
                    }
                }
                if (serverTag.contains(TagRefs.TAG_ENERGY)) {
                    int current = serverTag.getInt(TagRefs.TAG_ENERGY);
                    int max = serverTag.getInt(TagRefs.TAG_MAX);
                    Component text = Component.Serializer.fromJson(serverTag.getString("energyText"));
                    tooltip.addLine(new CustomBarComponent((float) current / max, ColorStyle.RED.aColor, text));
                    tooltip.addLine(new SpacingComponent(0, 1));
                }
                if (serverTag.contains(TagRefs.TAG_FLUID)) {
                    WailaTankInfoRemover.TANK_REMOVAL.add(accessor.getBlock());
                    FluidStack fluid = FluidStack.loadFluidStackFromNBT(serverTag.getCompound(TagRefs.TAG_FLUID));
                    int max = serverTag.getInt(TagRefs.TAG_MAX);
                    if (!fluid.isEmpty()) {
                        Function<ResourceLocation, TextureAtlasSprite> map = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
                        ResourceLocation stillTexture = IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture(fluid);
                        TextureAtlasSprite liquidIcon = map.apply(stillTexture);
                        int color = IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor(fluid);
                        tooltip.addLine(new PairComponent(new WrappedComponent(fluid.getDisplayName()), new SpriteBarComponent((float) fluid.getAmount() / max, liquidIcon, 16, 16, color, Component.literal(Formatter.formatNumber(fluid.getAmount(), String.valueOf(fluid.getAmount()).length() - 1) + " / " + Formatter.formatNumber(max, String.valueOf(max).length() - 1)))));
                    }
                }

                if (serverTag.contains(TagRefs.TAG_BAR)) {
                    int color = serverTag.getInt(TagRefs.TAG_BAR_COLOR);
                    int current = serverTag.getInt(TagRefs.TAG_BAR);
                    int max = serverTag.getInt(TagRefs.TAG_MAX);
                    Component label = Component.Serializer.fromJson(serverTag.getString("barText"));
                    tooltip.addLine(new CustomBarComponent((float) current / max, color, label));
                    tooltip.addLine(new SpacingComponent(0, 1));
                }
                if (serverTag.contains(TagRefs.TAG_PADDING)) {
                    int paddingX = serverTag.getInt(TagRefs.TAG_PADDING);
                    int paddingY = serverTag.getInt(TagRefs.TAG_PADDING_Y);
                    tooltip.addLine(new SpacingComponent(paddingX, paddingY));
                }
            }
        }
    }
}
