package com.diamantino.eternalmagic.api.capabilities.player;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerMageInfo implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<MageInfo> mageInfoCapability = CapabilityManager.get(new CapabilityToken<>(){});
    private final LazyOptional<MageInfo> lazyMageInfo;

    private final MageInfo mageInfo;

    public PlayerMageInfo() {
        mageInfo = new MageInfo();

        lazyMageInfo = LazyOptional.of(() -> mageInfo);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == mageInfoCapability)
            return lazyMageInfo.cast();

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.put("mageInfo", mageInfo.serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.mageInfo.deserializeNBT(nbt.getCompound("mageInfo"));
    }
}
