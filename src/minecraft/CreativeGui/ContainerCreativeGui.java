package CreativeGui;

import java.util.ArrayList;

import CreativeGui.tabs.CreativeTabs;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerCreativeGui extends Container{
	private EntityPlayer player;

	public ContainerCreativeGui(EntityPlayer player) {
		this.player = player;

        this.scrollTo(0.0F);
	}

	public void scrollTo(float par1)
    {
		this.inventorySlots = new ArrayList();
        InventoryPlayer inventory = player.inventory;
        if(GuiContainerCreativeGui.getSelectedIndex()!=-2) {
	        for (int i = 0; i < 5; ++i)
	        {
	            for (int l = 0; l < 9; ++l)
	            {
	                this.addSlot(new Slot(GuiContainerCreativeGui.getInventory(), l + (i * 9), 9 + l * 18, 48 + i * 18));
	            }
	        }
		}else {
	        for (int y = 0; y < 3; ++y)
	        {
	            for (int x = 0; x < 9; ++x)
	            {
	                this.addSlot(new Slot(inventory, x + y * 9 + 9, 9 + x * 18, 48 + (y+2) * 18));
	            }
	        }
            this.addSlot(new SlotArmor(inventory, 39, 9, 36, 0));
            this.addSlot(new SlotArmor(inventory, 38, 9, 63, 1));
            this.addSlot(new SlotArmor(inventory, 37, 63, 36, 2));
            this.addSlot(new SlotArmor(inventory, 36, 63, 63, 3));
            this.addSlot(new Slot(GuiContainerCreativeGui.inventoryRecycle, 0, 9 + 9 * 18 + 2, 142));
		}

        for (int i = 0; i < 9; ++i)
        {
            this.addSlot(new Slot(inventory, i, 9 + i * 18, 142));
        }
		if(GuiContainerCreativeGui.getSelectedIndex()==-2) {
			return;
		}
		CreativeTabs tab = GuiContainerCreativeGui.getSelectedIndex()==-1?CreativeTabs.tabSearch:CreativeTabs.tabs.get(GuiContainerCreativeGui.getSelectedIndex());
        int var2 = tab.items.size() / 9 - 4;
        int var3 = (int)((double)(par1 * (float)var2) + 0.5D);

        if (var3 < 0)
        {
            var3 = 0;
        }

        for (int var4 = 0; var4 < 5; ++var4)
        {
            for (int var5 = 0; var5 < 9; ++var5)
            {
                int var6 = var5 + (var4 + var3) * 9;

                if (var6 >= 0 && var6 < tab.items.size())
                {
                    if(tab.items.get(var6)==null) GuiContainerCreativeGui.getInventory().setInventorySlotContents(var5 + var4 * 9, null);
                    else GuiContainerCreativeGui.getInventory().setInventorySlotContents(var5 + var4 * 9, tab.items.get(var6));
                }
                else
                {
                	GuiContainerCreativeGui.getInventory().setInventorySlotContents(var5 + var4 * 9, null);
                }
            }
        }
    }

	public void transferItemStack(ItemStack stack, int from, int to) {
		this.mergeItemStack(stack, from, to, false);
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}


}
