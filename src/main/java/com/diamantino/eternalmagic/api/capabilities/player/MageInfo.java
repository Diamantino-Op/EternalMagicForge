package com.diamantino.eternalmagic.api.capabilities.player;

import com.diamantino.eternalmagic.networking.s2c.MageInfoSyncPacket;
import com.diamantino.eternalmagic.registration.ModMessages;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.Random;

@AutoRegisterCapability
public class MageInfo {
    private MageLevel mageLevel;
    private Element mageElement;
    private long mana;
    private long maxMana;
    private final Player player;

    public MageInfo(Player player) {
        this.player = player;
        this.mageLevel = MageLevel.novice;
        this.mageElement = chooseRandom();
        this.mana = 0;
        this.maxMana = getMaxMana();
    }

    public void setMageLevel(MageLevel level) {
        this.mageLevel = level;

        updateMaxMana();

        sendUpdatePacket();
    }

    public boolean incrementMageLevel() {
        if (mageLevel.ordinal() < 3) {
            mageLevel = MageLevel.fromId(mageLevel.ordinal() + 1);

            sendUpdatePacket();

            return true;
        }

        return false;
    }

    public void setMageElement(Element element) {
        this.mageElement = element;

        updateMaxMana();

        sendUpdatePacket();
    }

    public void removeMana(long amount) {
        this.mana = Math.max(0, this.mana - amount);

        sendUpdatePacket();
    }

    public void addMana(long amount) {
        this.mana = Math.min(this.maxMana, this.mana + amount);

        sendUpdatePacket();
    }

    public void setMana(long amount) {
        this.mana = Math.max(0, Math.min(this.maxMana, amount));

        sendUpdatePacket();
    }

    public long getMana() {
        return this.mana;
    }

    public boolean hasEnoughMana(long required) {
        return this.mana >= required;
    }

    public void updateMaxMana() {
        this.maxMana = getMaxMana();
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putInt("mageLevel", mageLevel.ordinal());
        tag.putInt("mageElement", mageElement.ordinal());
        tag.putLong("mana", this.mana);
        tag.putLong("maxMana", this.maxMana);

        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        this.mageLevel = MageLevel.fromId(tag.getInt("mageLevel"));
        this.mageElement = Element.fromId(tag.getInt("mageElement"));
        this.mana = tag.getLong("mana");
        this.maxMana = tag.getLong("maxMana");
    }

    private long getMaxMana() {
        long baseMaxMana = 10000L;

        return switch (mageElement) {
            case none -> 0L;
            case earth, air, water, fire -> baseMaxMana * (mageLevel.ordinal() + 1);
            case electricity, ice, magma -> (baseMaxMana * 2) * (mageLevel.ordinal() + 1);
            case darkness, light -> (baseMaxMana * 4) * (mageLevel.ordinal() + 1);
            case infinity -> (baseMaxMana * 8) * (mageLevel.ordinal() + 1);
        };
    }

    private Element chooseRandom() {
        Random random = new Random();
        int randomNumber = random.nextInt(100) + 1;

        if (randomNumber <= 3) {
            return Element.infinity;
        } else if (randomNumber <= 15) {
            int rand = random.nextInt(2) + 1;

            return rand == 1 ? Element.darkness : Element.light;
        } else if (randomNumber <= 45) {
            int rand = random.nextInt(3) + 1;

            return rand == 1 ? Element.electricity : (rand == 2 ? Element.ice : Element.magma);
        } else {
            int rand = random.nextInt(4) + 1;

            return rand == 1 ? Element.earth : (rand == 2 ? Element.air : (rand == 3 ? Element.water : Element.fire));
        }
    }

    public void sendUpdatePacket() {
        if (!player.level().isClientSide()) {
            ModMessages.sendToPlayer(new MageInfoSyncPacket(this.serializeNBT()), (ServerPlayer) player);
        }
    }

    public enum MageLevel {
        novice,
        intermediate,
        advanced,
        master;

        public static MageLevel fromId(int levelId) {
            for (MageLevel level : MageLevel.values()) {
                if (level.ordinal() == levelId)
                    return level;
            }

            return novice;
        }
    }
}
