package com.mcfire.autoplay.menu;

import com.mcfire.autoplay.entity.blockentity.RefinedLubricatingOilPurifierEntity;
import com.mcfire.autoplay.registry.ModBlocks;
import com.mcfire.autoplay.registry.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jline.utils.Log;

public class RefinedLubricatingOilPurifierMenu extends AbstractContainerMenu {
    public final RefinedLubricatingOilPurifierEntity entity;
    private final Level level;
    private final ContainerData data;
    public RefinedLubricatingOilPurifierMenu(int id, Inventory inventory, FriendlyByteBuf extraData){
        this(id, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(5)); // this count of var
    }

    public RefinedLubricatingOilPurifierMenu(int id, Inventory inventory, BlockEntity entity, ContainerData data){
        super(ModMenus.REFINED_LUBRICATING_OIL_PURIFIER_MENU.get(), id);
        checkContainerSize(inventory, 2);
        this.entity=(RefinedLubricatingOilPurifierEntity) entity;
        this.level=inventory.player.level();
        this.data=data;
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
        this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent((I)->{
            this.addSlot(new SlotItemHandler(I,0,12,15));
            this.addSlot(new SlotItemHandler(I,1,152,62));
        });
        addDataSlots(data);
    }

    public boolean isCrafting(){
        return this.data.get(0)>0; // get progress
    }

    public int getScaledProgress() { // to render arrow
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 33;

        return maxProgress!=0&&progress!=0?progress*progressArrowSize/maxProgress:0;
    }

    public int getScaledEnergy(int id){ // to render water, lava, milk energy
        return switch (id){
            case 0 -> this.data.get(2) * 61 / 640;
            case 1 -> this.data.get(3) * 61/ 640;
            default -> this.data.get(4) * 61 / 640;
        };
    }

    private static final int HOTBAR_SLOT_COUNT=9;
    private static final int PLAYER_INVENTORY_ROW_COUNT=3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT=9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT=PLAYER_INVENTORY_COLUMN_COUNT*PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT=HOTBAR_SLOT_COUNT+PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX=0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX=VANILLA_FIRST_SLOT_INDEX+VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT=2;
    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) { // shift+lmb to quick stack
        Slot sourceSlot=slots.get(p_38942_);
        if(sourceSlot==null||!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack=sourceSlot.getItem();
        ItemStack copyFromSourceStack=sourceStack.copy();
        if(p_38942_<VANILLA_FIRST_SLOT_INDEX+VANILLA_SLOT_COUNT){
            if(!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX+TE_INVENTORY_SLOT_COUNT,false)){
                return ItemStack.EMPTY;
            }
        }else if(p_38942_<TE_INVENTORY_FIRST_SLOT_INDEX+TE_INVENTORY_SLOT_COUNT){
            if(!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX+VANILLA_SLOT_COUNT, false)){
                return ItemStack.EMPTY;
            }
        }else {
            Log.warn("[AutoPlay] invalid slotIndex: " + p_38942_);
            return ItemStack.EMPTY;
        }
        if(sourceStack.getCount()==0){
            sourceSlot.set(ItemStack.EMPTY);
        }else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(p_38941_, sourceStack);
        return copyFromSourceStack;
    }

    @Override
    public boolean stillValid(Player p_38874_) { // close menu when player is out
        return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()), p_38874_,
                ModBlocks.REFINED_LUBRICATING_OIL_PURIFIER_BLOCK.get());
    }

    private void addPlayerInventory(Inventory inventory) {
        for (int y=0;y<3;y++){
            for (int x=0;x<9;x++){
                this.addSlot(new Slot(inventory, x+y*9+9, 8+x*18, 86+y*18));
            }
        }
    }
    private void addPlayerHotbar(Inventory inventory){
        for (int k=0;k<9;k++) {
            this.addSlot(new Slot(inventory,k,8+k*18,144));
        }
    }
}
