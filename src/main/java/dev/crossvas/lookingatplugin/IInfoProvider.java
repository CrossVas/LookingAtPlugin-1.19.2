package dev.crossvas.lookingatplugin;

import ic2.core.inventory.filter.IFilter;
import ic2.core.utils.helpers.StackUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IInfoProvider {

    void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player);

    IFilter getFilter();

    default boolean canHandle(Player player) {
        return StackUtil.hasHotbarItems(player, getFilter());
    }
}
