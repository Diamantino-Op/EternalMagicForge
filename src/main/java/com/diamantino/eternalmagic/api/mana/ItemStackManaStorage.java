package com.diamantino.eternalmagic.api.mana;

import com.diamantino.eternalmagic.registration.ModCapabilities;
import com.diamantino.eternalmagic.storage.mana.ModManaStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemStackManaStorage  implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final ItemStack stack;
    private final LazyOptional<IManaStorage> lazyManaHandler;

    private final ModManaStorage manaStorage = new ModManaStorage(1000, 1024) {
        @Override
        public void onManaChanged() {
            //ModMessages.sendToClients(new ManaSyncS2CPacket(this.mana, getBlockPos()));
        }
    };

    public ItemStackManaStorage(ItemStack stack) {
        this.stack = stack;

        lazyManaHandler = LazyOptional.of(() -> manaStorage);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ModCapabilities.mana) {
            return ModCapabilities.mana.orEmpty(cap, lazyManaHandler);
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.put("manaStorage", manaStorage.serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        manaStorage.deserializeNBT(nbt.get("manaStorage"));
    }

    public void onCapabilityInvalidated()
    {
        this.lazyManaHandler.invalidate();
    }
}
