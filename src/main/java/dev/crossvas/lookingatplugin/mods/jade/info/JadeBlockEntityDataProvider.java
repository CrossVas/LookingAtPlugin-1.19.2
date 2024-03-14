package dev.crossvas.lookingatplugin.mods.jade.info;

import dev.crossvas.lookingatplugin.LookingAtCommon;
import dev.crossvas.lookingatplugin.mods.jade.JadeLookingAtHelper;
import dev.crossvas.lookingatplugin.mods.jade.JadeRefs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.IServerDataProvider;

public class JadeBlockEntityDataProvider implements IServerDataProvider<BlockEntity> {

    public static final JadeBlockEntityDataProvider INSTANCE = new JadeBlockEntityDataProvider();

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        JadeLookingAtHelper helper = new JadeLookingAtHelper();
        LookingAtCommon.addInfo(helper, blockEntity, serverPlayer);
        helper.transferData(compoundTag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeRefs.WRENCHABLE;
    }
}
