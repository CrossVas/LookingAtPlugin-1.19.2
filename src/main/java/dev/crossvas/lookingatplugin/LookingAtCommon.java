package dev.crossvas.lookingatplugin;

import dev.crossvas.lookingatplugin.info.BaseMachineInfoProvider;
import dev.crossvas.lookingatplugin.info.EUStorageInfoProvider;
import dev.crossvas.lookingatplugin.info.WrenchableInfoProvider;
import ic2.core.block.base.features.multiblock.IStructureListener;
import ic2.core.platform.events.StructureManager;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

public class LookingAtCommon {

    public static List<IInfoProvider> INFO_PROVIDERS = new ObjectArrayList<>();

    static {
        INFO_PROVIDERS.add(EUStorageInfoProvider.INSTANCE);
        INFO_PROVIDERS.add(BaseMachineInfoProvider.INSTANCE);

        INFO_PROVIDERS.add(WrenchableInfoProvider.INSTANCE); // keep last
    }

    public static BlockEntity getMultiBlockController(Level level, BlockPos pos) {
        IStructureListener structureListener = StructureManager.INSTANCE.getListener(level, pos);
        if (structureListener instanceof BlockEntity master) {
            return master;
        } else {
            return level.getBlockEntity(pos);
        }
    }

    public static void addInfo(ILookingAtHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity != null) {
            INFO_PROVIDERS.forEach(infoProvider -> {
                if (infoProvider.canHandle(player)) {
                    infoProvider.addInfo(helper, blockEntity, player);
                }
            });
        }
    }

    public static void addTankInfo(ILookingAtHelper helper, BlockEntity blockEntity) {
        if (blockEntity instanceof IFluidHandler fluidHandler) {
            loadTankData(helper, fluidHandler);
        } else {
            blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(handler -> loadTankData(helper, handler));
        }
    }

    public static void loadTankData(ILookingAtHelper helper, IFluidHandler fluidHandler) {
        for (int i = 0; i < fluidHandler.getTanks(); i++) {
            FluidStack fluid = fluidHandler.getFluidInTank(i);
            if (fluid.getAmount() > 0) {
                helper.addFluidElement(fluid, fluidHandler.getTankCapacity(i));
            }
        }
    }
}
