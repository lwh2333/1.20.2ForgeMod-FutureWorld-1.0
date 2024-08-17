package com.mcfire.autoplay.cap;

import com.mcfire.autoplay.registry.ModCapabilityRegistryHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PowerfulItemInfoValueProvider implements ICapabilitySerializable<CompoundTag> {
    private final PowerfulItemInfoValue instance;
    public PowerfulItemInfoValueProvider(){
        this.instance=new PowerfulItemInfoValue();
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap==ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP?LazyOptional.of(()->this.instance).cast():LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.instance.deserializeNBT(nbt);
    }
}
