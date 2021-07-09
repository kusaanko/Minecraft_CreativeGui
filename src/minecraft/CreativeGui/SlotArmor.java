package CreativeGui;

import net.minecraft.src.Block;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class SlotArmor extends Slot {
    final int armorType;

    SlotArmor(IInventory par2IInventory, int par3, int par4, int par5, int par6) {
        super(par2IInventory, par3, par4, par5);
        this.armorType = par6;
    }

    public int getSlotStackLimit() {
        return 1;
    }

    public boolean isItemValid(ItemStack par1ItemStack) {
        return par1ItemStack.getItem() instanceof ItemArmor
                ? ((ItemArmor) par1ItemStack.getItem()).armorType == this.armorType
                : (par1ItemStack.getItem().shiftedIndex == Block.pumpkin.blockID ? this.armorType == 0 : false);
    }

    public int getBackgroundIconIndex() {
        return 15 + this.armorType * 16;
    }
}
