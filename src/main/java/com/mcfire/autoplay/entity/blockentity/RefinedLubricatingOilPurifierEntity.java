package com.mcfire.autoplay.entity.blockentity;

import com.mcfire.autoplay.blocks.RefinedLubricatingOilPurifierBlock;
import com.mcfire.autoplay.energy.ModEnergyStorage;
import com.mcfire.autoplay.menu.RefinedLubricatingOilPurifierMenu;
import com.mcfire.autoplay.registry.ModBlockEntities;
import com.mcfire.autoplay.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class RefinedLubricatingOilPurifierEntity extends BlockEntity implements MenuProvider {
    // 64 water, 64 lava, 64 milk -> 1 lubricating oil
    private final ItemStackHandler itemStackHandler=new ItemStackHandler(2){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged(); // update
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot){
                case 0 -> stack.getItem()==Items.WATER_BUCKET||stack.getItem()==Items.LAVA_BUCKET||stack.getItem()==Items.MILK_BUCKET;
                case 1 -> false;
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    protected final ContainerData data;

    private int progress=0;
    private int maxProgress=78;
    private LazyOptional<IItemHandler> lazyItemHandler=LazyOptional.empty();
    private final ModEnergyStorage WATER_STORAGE = new ModEnergyStorage(640, 10) {
        @Override
        public void onEnergyChange() {
            setChanged();
        }
    };
    private final ModEnergyStorage LAVA_STORAGE = new ModEnergyStorage(640, 10) {
        @Override
        public void onEnergyChange() {
            setChanged();
        }
    };
    private final ModEnergyStorage MILK_STORAGE = new ModEnergyStorage(640, 10) {
        @Override
        public void onEnergyChange() {
            setChanged();
        }
    };

    private LazyOptional<IEnergyStorage> lazyWaterEnergyHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> lazyLavaEnergyHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> lazyMilkEnergyHandler = LazyOptional.empty();
    // io put with hopper
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, (i) -> !itemStackHandler.isItemValid(i, itemStackHandler.getStackInSlot(i)), (i, s) -> false)),
                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, (i) -> false, itemStackHandler::isItemValid)),
                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, (i) -> false, itemStackHandler::isItemValid)),
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, (i) -> false, itemStackHandler::isItemValid)),
                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, (i) -> false, itemStackHandler::isItemValid)),
                    Direction.UP, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, (i) -> false, itemStackHandler::isItemValid)));
    public RefinedLubricatingOilPurifierEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.REFINED_LUBRICATING_OIL_PURIFIER.get(), p_155229_, p_155230_);
        this.data=new ContainerData() {
            @Override
            public int get(int p_39284_) {
                return switch (p_39284_){
                    case 0 -> RefinedLubricatingOilPurifierEntity.this.progress;
                    case 1 -> RefinedLubricatingOilPurifierEntity.this.maxProgress;
                    case 2 -> RefinedLubricatingOilPurifierEntity.this.WATER_STORAGE.getEnergyStored();
                    case 3 -> RefinedLubricatingOilPurifierEntity.this.LAVA_STORAGE.getEnergyStored();
                    case 4 -> RefinedLubricatingOilPurifierEntity.this.MILK_STORAGE.getEnergyStored();
                    default -> 0;
                };
            }

            @Override
            public void set(int p_39285_, int p_39286_) {
                switch (p_39285_){
                    case 0 -> RefinedLubricatingOilPurifierEntity.this.progress=p_39286_;
                    case 1 -> RefinedLubricatingOilPurifierEntity.this.maxProgress=p_39286_;
                    case 2 -> RefinedLubricatingOilPurifierEntity.this.WATER_STORAGE.setEnergy(p_39286_);
                    case 3 -> RefinedLubricatingOilPurifierEntity.this.LAVA_STORAGE.setEnergy(p_39286_);
                    case 4 -> RefinedLubricatingOilPurifierEntity.this.MILK_STORAGE.setEnergy(p_39286_);

                }
            }

            @Override
            public int getCount() {
                return 5; // the count of var
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("name.autoplay.refined_lubricating_oil_purifier");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new RefinedLubricatingOilPurifierMenu(p_39954_, p_39955_,this,this.data);
    }

    public IEnergyStorage getEnergyStorage(int id){
        return switch (id){
            case 0 -> WATER_STORAGE;
            case 1 -> LAVA_STORAGE;
            case 2 -> MILK_STORAGE;
            default -> null;
        };
    }

    public void setEnergyLevel(int energy, int id){
        switch (id){
            case 0 -> this.WATER_STORAGE.setEnergy(energy);
            case 1 -> this.LAVA_STORAGE.setEnergy(energy);
            case 2 -> this.MILK_STORAGE.setEnergy(energy);
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction direction) {
        if(cap==ForgeCapabilities.ITEM_HANDLER){
            if(direction==null){
                return lazyItemHandler.cast();
            }
            if(directionWrappedHandlerMap.containsKey(direction)){
                Direction localDirection=this.getBlockState().getValue(RefinedLubricatingOilPurifierBlock.FACING);
                if(direction==Direction.UP||direction==Direction.DOWN){
                    return directionWrappedHandlerMap.get(direction).cast();
                }
                return switch (localDirection){
                    case EAST -> directionWrappedHandlerMap.get(direction.getClockWise()).cast();
                    case SOUTH -> directionWrappedHandlerMap.get(direction).cast();
                    case WEST -> directionWrappedHandlerMap.get(direction.getCounterClockWise()).cast();
                    default -> directionWrappedHandlerMap.get(direction.getOpposite()).cast();
                };
            }
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler=LazyOptional.of(()->itemStackHandler);
        lazyWaterEnergyHandler=LazyOptional.of(()->WATER_STORAGE);
        lazyLavaEnergyHandler=LazyOptional.of(()->LAVA_STORAGE);
        lazyMilkEnergyHandler=LazyOptional.of(()->MILK_STORAGE);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyWaterEnergyHandler.invalidate();
        lazyLavaEnergyHandler.invalidate();
        lazyMilkEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        p_187471_.put("inventory", itemStackHandler.serializeNBT());
        p_187471_.putInt("refined_lubricating_oil_purifier.progress", this.progress);
        p_187471_.putInt("refined_lubricating_oil_purifier_water.energy", WATER_STORAGE.getEnergyStored());
        p_187471_.putInt("refined_lubricating_oil_purifier_lava.energy", LAVA_STORAGE.getEnergyStored());
        p_187471_.putInt("refined_lubricating_oil_purifier_milk.energy", MILK_STORAGE.getEnergyStored());
        super.saveAdditional(p_187471_);
    }

    @Override
    public void load(CompoundTag p_155245_) {
        itemStackHandler.deserializeNBT(p_155245_.getCompound("inventory"));
        progress=p_155245_.getInt("refined_lubricating_oil_purifier.progress");
        WATER_STORAGE.setEnergy(p_155245_.getInt("refined_lubricating_oil_purifier_water.energy"));
        LAVA_STORAGE.setEnergy(p_155245_.getInt("refined_lubricating_oil_purifier_lava.energy"));
        MILK_STORAGE.setEnergy(p_155245_.getInt("refined_lubricating_oil_purifier_milk.energy"));
        super.load(p_155245_);
    }

    public void drops(){ // drops item when block is destroyed
        SimpleContainer inventory=new SimpleContainer(itemStackHandler.getSlots());
        for(int i=0;i<itemStackHandler.getSlots();i++){
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, RefinedLubricatingOilPurifierEntity entity){
        // only in server
        if(level.isClientSide()) return;
        SimpleContainer inventory=new SimpleContainer(entity.itemStackHandler.getSlots());
        for(int i=0;i<entity.itemStackHandler.getSlots();i++){
            inventory.setItem(i, entity.itemStackHandler.getStackInSlot(i));
        }
        if(hasRecipe(entity)){
            entity.progress++;
            setChanged(level,blockPos,state);
            if(entity.progress>=entity.maxProgress){
                craftItem(entity);
            }
        }else {
            entity.resetProgress();
            setChanged(level,blockPos,state);
        }
        if(hasEnoughEnergy(entity, 0, 640)&&hasEnoughEnergy(entity, 1, 640)&&hasEnoughEnergy(entity, 2, 640)&&canInsertItem(inventory, new ItemStack(ModItems.LUBRICATING_OIL.get()))&canInsertAmount(inventory)){
            entity.itemStackHandler.setStackInSlot(1, new ItemStack(ModItems.LUBRICATING_OIL.get(),
                    entity.itemStackHandler.getStackInSlot(1).getCount()+1));
            entity.WATER_STORAGE.setEnergy(0);
            entity.LAVA_STORAGE.setEnergy(0);
            entity.MILK_STORAGE.setEnergy(0);
        }
    }

    private void resetProgress(){
        progress=0;
    }

    private static void craftItem(RefinedLubricatingOilPurifierEntity entity){
        if(hasRecipe(entity)){
            switch (getRecipe(entity)){ // only w, l, m can increase energy
                case 0 -> entity.WATER_STORAGE.receiveEnergy(10, false);
                case 1 -> entity.LAVA_STORAGE.receiveEnergy(10, false);
                case 2 -> entity.MILK_STORAGE.receiveEnergy(10, false);
            }
            entity.itemStackHandler.setStackInSlot(0,new ItemStack(Items.BUCKET));
            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(RefinedLubricatingOilPurifierEntity entity){
        return entity.itemStackHandler.getStackInSlot(0).is(Items.WATER_BUCKET)||
        entity.itemStackHandler.getStackInSlot(0).is(Items.LAVA_BUCKET) ||
        entity.itemStackHandler.getStackInSlot(0).is(Items.MILK_BUCKET);
    }
    private static int getRecipe(RefinedLubricatingOilPurifierEntity entity){
        if(entity.itemStackHandler.getStackInSlot(0).is(Items.WATER_BUCKET)) return 0;
        if(entity.itemStackHandler.getStackInSlot(0).is(Items.LAVA_BUCKET)) return 1;
        if(entity.itemStackHandler.getStackInSlot(0).is(Items.MILK_BUCKET)) return 2;
        return 0;
    }

    private static boolean hasEnoughEnergy(RefinedLubricatingOilPurifierEntity entity, int id, int energy) {
        return switch (id){
            case 0 -> entity.WATER_STORAGE.getEnergyStored()>=energy;
            case 1 -> entity.LAVA_STORAGE.getEnergyStored()>=energy;
            case 2 -> entity.MILK_STORAGE.getEnergyStored()>=energy;
            default -> false;
        };
    }

    private static boolean canInsertAmount(SimpleContainer inventory){
        return inventory.getItem(1).getMaxStackSize()>inventory.getItem(1).getCount()||inventory.getItem(1).isEmpty();
    }

    private static boolean canInsertItem(SimpleContainer inventory, ItemStack itemStack){
        return inventory.getItem(1).is(itemStack.getItem())||inventory.getItem(1).isEmpty();
    }
}
