package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModReferences;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
    public static final DeferredRegister<Block> blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, ModReferences.modId);

    public static void registerBlocks(IEventBus bus) {
        blocks.register(bus);
    }
}
