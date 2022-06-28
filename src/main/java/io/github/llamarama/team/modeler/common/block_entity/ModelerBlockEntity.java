package io.github.llamarama.team.modeler.common.block_entity;

import io.github.llamarama.team.modeler.Modeler;
import io.github.llamarama.team.modeler.common.screen_handler.ModelerScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class ModelerBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, Inventory {

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public ModelerBlockEntity(BlockPos pos, BlockState state) {
        super(Modeler.MODELER_BE, pos, state);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("modeler.modeler_name");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ModelerScreenHandler(syncId, inv, this);
    }

    @Override
    public int size() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(this.items, slot, amount);

        if (!itemStack.isEmpty())
            this.markDirty();

        return itemStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.items, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.items.set(slot, stack);

        if (!stack.isEmpty() && stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

        this.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return player.squaredDistanceTo(Vec3d.ofCenter(this.pos)) < 64;
    }

    @Override
    public void clear() {
        this.items.clear();
        this.markDirty();
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.items);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.items);
    }

}
