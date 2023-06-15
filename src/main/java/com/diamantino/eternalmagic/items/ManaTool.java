package com.diamantino.eternalmagic.items;

import com.diamantino.eternalmagic.blockentities.ManaBlockEntityBase;
import com.diamantino.eternalmagic.blockentities.ManaTransceiverBlockEntityBase;
import com.diamantino.eternalmagic.utils.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManaTool extends Item {
    public ManaTool(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.mana_tool.eternalmagic.mode", getModeName(pStack.getOrCreateTag().getInt("mode"))));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (pPlayer.isShiftKeyDown()) {
            stack.getOrCreateTag().remove("savedTarget");

            pPlayer.displayClientMessage(Component.translatable("message.eternalmagic.clear_saved_position").withStyle(ChatFormatting.RED), true);
        } else {
            changeMode(stack.getOrCreateTag());

            pPlayer.displayClientMessage(Component.translatable("mode.manatool.eternalmagic.change", getModeName(stack.getOrCreateTag().getInt("mode"))), true);
        }

        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide());
    }

    private String getModeName(int mode) {
        return switch (mode) {
            case 0 -> Component.translatable("mode.manatool.eternalmagic.link").getString();
            case 1 -> Component.translatable("mode.manatool.eternalmagic.info").getString();
            default -> "";
        };
    }

    private void changeMode(CompoundTag tag) {
        int currMode = tag.getInt("mode");

        currMode++;

        if (currMode > 1)
            currMode = 0;

        tag.putInt("mode", currMode);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext pContext) {
        CompoundTag tag = pContext.getItemInHand().getOrCreateTag();
        int mode = tag.getInt("mode");
        BlockEntity ent = pContext.getLevel().getBlockEntity(pContext.getClickedPos());
        Player player = pContext.getPlayer();

        if (player != null) {
            if (mode == 1) {
                if (ent instanceof ManaBlockEntityBase manaBlockEntityBase) {
                    player.displayClientMessage(Component.translatable("message.eternalmagic.mana_info", TextUtils.formatNumberWithPrefix(manaBlockEntityBase.getManaStorage().getManaStored()), TextUtils.formatNumberWithPrefix(manaBlockEntityBase.getManaStorage().getMaxManaStored())), true);
                }
            } else if (mode == 0) {
                if (player.isShiftKeyDown() && ent instanceof ManaTransceiverBlockEntityBase) {
                    CompoundTag targetTag = new CompoundTag();

                    BlockPos pos = pContext.getClickedPos();

                    targetTag.putInt("x", pos.getX());
                    targetTag.putInt("y", pos.getY());
                    targetTag.putInt("z", pos.getZ());

                    targetTag.putString("level", pContext.getLevel().dimension().location().toString());

                    tag.put("savedTarget", targetTag);

                    player.displayClientMessage(Component.translatable("message.eternalmagic.save_position").withStyle(ChatFormatting.GREEN), true);
                }
            }
        }

        return InteractionResult.sidedSuccess(pContext.getLevel().isClientSide());
    }
}
