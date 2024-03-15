package dev.crossvas.lookingatplugin.mods.waila.info;

import dev.crossvas.lookingatplugin.LookingAtCommon;
import dev.crossvas.lookingatplugin.TagRefs;
import dev.crossvas.lookingatplugin.elements.AbstractItemStackElement;
import dev.crossvas.lookingatplugin.helpers.ColorStyle;
import dev.crossvas.lookingatplugin.helpers.Formatter;
import dev.crossvas.lookingatplugin.mods.waila.WailaHelper;
import dev.crossvas.lookingatplugin.mods.waila.elements.CustomBarComponent;
import dev.crossvas.lookingatplugin.mods.waila.elements.WailaStackComponent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mcp.mobius.waila.api.*;
import mcp.mobius.waila.api.component.*;
import net.minecraft.ChatFormatting;
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

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
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
        CompoundTag serverData = accessor.getData().raw();
        if (serverData.contains(TagRefs.TAG_DATA, Tag.TAG_LIST)) {
            ListTag list = serverData.getList(TagRefs.TAG_DATA, Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag serverTag = list.getCompound(i);
                if (serverTag.contains(TagRefs.TAG_TEXT, Tag.TAG_STRING)) {
                    boolean append = serverTag.getBoolean("append");
                    boolean center = serverTag.getBoolean("center");
                    ChatFormatting formatting = ChatFormatting.getById(serverTag.getInt("formatting"));
                    ChatFormatting textFormatting = formatting == ChatFormatting.WHITE ? ChatFormatting.GRAY : formatting;
                    Component text = Component.Serializer.fromJson(serverTag.getString(TagRefs.TAG_TEXT)).withStyle(textFormatting);
                    if (append) {
                        tooltip.getLine(tooltip.getLineCount() - 1).with(text);
                    } else {
                        if (center) {
                            tooltip.addLine().with(GrowingComponent.INSTANCE).with(text).with(GrowingComponent.INSTANCE);
                        } else {
                            tooltip.addLine(text);
                        }
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
                    tooltip.addLine(new PairComponent(new WrappedComponent(WailaHelper.getWailaComp(text)), new BarComponent((float) current / max, ColorStyle.RED.aColor, Component.literal(Formatter.formatInt(current, 4) + " / " + Formatter.formatInt(max, 4)).withStyle(ChatFormatting.WHITE))));
                    tooltip.addLine(new SpacingComponent(0, 1));
                }
                if (serverTag.contains(TagRefs.TAG_FLUID)) {
                    LookingAtCommon.TANK_REMOVAL.add(accessor.getBlock());
                    FluidStack fluid = FluidStack.loadFluidStackFromNBT(serverTag.getCompound(TagRefs.TAG_FLUID));
                    int max = serverTag.getInt(TagRefs.TAG_MAX);
                    if (!fluid.isEmpty()) {
                        Function<ResourceLocation, TextureAtlasSprite> map = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
                        ResourceLocation stillTexture = IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture(fluid);
                        TextureAtlasSprite liquidIcon = map.apply(stillTexture);
                        int color = IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor(fluid);
                        tooltip.addLine(new PairComponent(
                                new WrappedComponent(fluid.getDisplayName()),
                                new SpriteBarComponent((float) fluid.getAmount() / max, liquidIcon, 16, 16, color,
                                        Component.literal(Formatter.formatNumber(fluid.getAmount(), String.valueOf(fluid.getAmount()).length() - 1) + " / " + Formatter.formatNumber(max, String.valueOf(max).length() - 1)).withStyle(ChatFormatting.WHITE))));
                    }
                }

                if (serverTag.contains(TagRefs.TAG_BAR)) {
                    int color = serverTag.getInt(TagRefs.TAG_BAR_COLOR);
                    int current = serverTag.getInt(TagRefs.TAG_BAR);
                    int max = serverTag.getInt(TagRefs.TAG_MAX);
                    Component label = Component.Serializer.fromJson(serverTag.getString("barText"));
                    Component newLabel = WailaHelper.getWailaComp(label);
                    Component args = Component.literal(Formatter.formatInt(current, 4) + " / " + Formatter.formatInt(max, 4)).withStyle(ChatFormatting.WHITE);
                    if (Objects.equals(newLabel, Component.empty())) {
                        args = label.copy().withStyle(ChatFormatting.WHITE);
                        tooltip.addLine(new CustomBarComponent((float) current / max, color, args));
                    } else if (WailaHelper.SPEED_COMP.contains(label)) {
                        args = Component.literal(new DecimalFormat().format(((float) current / max) * 100.0) + "%").withStyle(ChatFormatting.WHITE);
                        tooltip.addLine(new PairComponent(new WrappedComponent(WailaHelper.getWailaComp(label)), new BarComponent((float) current / max, color, args)));
                    } else {
                        tooltip.addLine(new PairComponent(new WrappedComponent(WailaHelper.getWailaComp(label)), new BarComponent((float) current / max, color, args)));
                    }
                    tooltip.addLine(new SpacingComponent(0, 1));
                }
                if (serverTag.contains(TagRefs.TAG_PADDING)) {
                    int paddingX = serverTag.getInt(TagRefs.TAG_PADDING);
                    int paddingY = serverTag.getInt(TagRefs.TAG_PADDING_Y);
                    tooltip.addLine(new SpacingComponent(paddingX, paddingY));
                }
                if (serverTag.contains(TagRefs.TAG_INVENTORY)) {
                    Component label = Component.Serializer.fromJson(serverTag.getString("stacksText"));
                    ChatFormatting formatting = ChatFormatting.getById(serverTag.getInt("stackTextFormat"));
                    ListTag stackListTag = serverTag.getList(TagRefs.TAG_INVENTORY, Tag.TAG_COMPOUND);
                    List<ItemStack> stackList = new ObjectArrayList<>();
                    stackListTag.forEach(tag -> {
                        CompoundTag stackTag = (CompoundTag) tag;
                        ItemStack stack = ItemStack.of(stackTag.getCompound("stack"));
                        stack.setCount(stackTag.getInt("count"));
                        stackList.add(stack);
                    });

                    int counter = 0;
                    if (!stackList.isEmpty()) {
                        tooltip.addLine(new SpacingComponent(0, 5));
                        tooltip.addLine(label.copy().withStyle(formatting));
                        tooltip.addLine(new SpacingComponent(0, 2));
                        for (ItemStack stack : stackList) {
                            if (counter < 7) {
                                tooltip.getLine(tooltip.getLineCount() - 1).with(new ItemComponent(stack));
                                counter++;
                                if (counter == 6) {
                                    counter = 0;
                                    tooltip.addLine(new SpacingComponent(0, 0));
                                }
                            }
                        }
                        tooltip.addLine(new SpacingComponent(0, 2));
                    }
                }
            }
        }
    }
}
