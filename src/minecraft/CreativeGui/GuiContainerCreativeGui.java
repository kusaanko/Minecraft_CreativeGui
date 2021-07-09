package CreativeGui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import CreativeGui.tabs.CreativeTabs;
import minecraftAPI.MCAPI;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiTextField;
import net.minecraft.src.InventoryBasic;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderManager;
import net.minecraft.src.Slot;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StringTranslate;

public class GuiContainerCreativeGui extends GuiContainer {
    private static InventoryBasic inventory = new InventoryBasic("tmp", 45);
    public static InventoryBasic inventoryRecycle = new InventoryBasic("tmp", 1);
    public static String search;

    private boolean isScrolling = false;
    private float currentScroll = 0.0F;
    private boolean wasClicking;
    private static int page;
    private static int selectedIndex;
    private float xSize_lo;
    private float ySize_lo;
    private GuiTextField searchField;

    public GuiContainerCreativeGui(EntityPlayer player) {
        super(new ContainerCreativeGui(player));
        this.xSize = 195;
        this.ySize = 196;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.controlList.add(new GuiButton(1, this.guiLeft, this.guiTop - 20, 20, 20, "<"));
        this.controlList.add(new GuiButton(2, this.guiLeft + this.xSize - 20, this.guiTop - 20, 20, 20, ">"));
        Keyboard.enableRepeatEvents(true);
        this.searchField = new GuiTextField(this.fontRenderer, this.guiLeft + 81, this.guiTop + 35, 89,
                this.fontRenderer.FONT_HEIGHT);
        this.searchField.setMaxStringLength(15);
        this.searchField.setEnableBackgroundDrawing(false);
        if (selectedIndex == -1)
            this.updateSearch(true);
    }

    protected void keyTyped(char par1, int par2) {
        if (selectedIndex != -1) {
            super.keyTyped(par1, par2);
            return;
        }
        if (this.searchField.textboxKeyTyped(par1, par2)) {
            search = this.searchField.getText();
            this.updateSearch(true);
        } else {
            super.keyTyped(par1, par2);
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    public void updateSearch(boolean scrollUpdate) {
        CreativeTabs.tabSearch.items = new ArrayList<ItemStack>();
        for (CreativeTabs tab : CreativeTabs.tabs) {
            for (ItemStack stack : tab.items) {
                if (this.searchField.getText().equals("")) {
                    Collections.addAll(CreativeTabs.tabSearch.items, stack);
                } else if (mod_CreativeGui.modNameSearch && this.searchField.getText().startsWith("@")) {
                    if (MCAPI.getModName(stack.getItem()).toLowerCase()
                            .contains(this.searchField.getText().substring(1).toLowerCase()))
                        Collections.addAll(CreativeTabs.tabSearch.items, stack);
                } else {
                    List<String> list = new ArrayList<String>();
                    stack.getItem().addInformation(stack, list);
                    String lore = "";
                    for (String s : list) {
                        lore += s;
                    }
                    if ((mod_CreativeGui.unlocalizedNameSearch && stack.getItem().getItemName() != null
                            && (stack.getItem().getItemName().toLowerCase()
                                    .contains(this.searchField.getText().toLowerCase())))
                            || (mod_CreativeGui.idSearch
                                    && (stack.itemID + "").contains(this.searchField.getText().toLowerCase()))
                            || (mod_CreativeGui.loreSearch
                                    && lore.toLowerCase().contains(this.searchField.getText().toLowerCase()))
                            || stack.getItem().getItemDisplayName(stack).toLowerCase()
                                    .contains(this.searchField.getText().toLowerCase())) {
                        Collections.addAll(CreativeTabs.tabSearch.items, stack);
                    }
                }
            }
        }
        Collections.sort(CreativeTabs.tabSearch.items, new Comparator<ItemStack>() {
            @Override
            public int compare(ItemStack o1, ItemStack o2) {
                return o1.itemID - o2.itemID;
            }

        });
        if (scrollUpdate)
            ((ContainerCreativeGui) this.inventorySlots).scrollTo(0.0f);
    }

    public void updateScreen() {
        if (!this.mc.playerController.isInCreativeMode()) {
            this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
        }
    }

    protected void drawGuiContainerForegroundLayer() {
        this.fontRenderer
                .drawString(
                        StatCollector
                                .translateToLocal(
                                        selectedIndex == -2 ? ""
                                                : selectedIndex == -1 ? CreativeTabs.tabSearch.tabName
                                                        : CreativeTabs.tabs.get(selectedIndex).tabName),
                        8, 36, 4210752);
        String draw = (page + 1) + "/" + (int) Math.ceil(CreativeTabs.tabs.size() / 10d);
        this.fontRenderer.drawString(draw, this.ySize / 2 - this.fontRenderer.getStringWidth(draw), -18,
                Color.WHITE.getRGB());
    }

    protected void handleMouseClick(Slot par1Slot, int par2, int par3, boolean par4) {
        InventoryPlayer var5;
        ItemStack var6;

        if (par1Slot != null) {
            if (par1Slot.inventory == inventory) {
                var5 = this.mc.thePlayer.inventory;
                var6 = var5.getItemStack();
                ItemStack var7 = par1Slot.getStack();

                if (var6 != null && var7 != null && var6.itemID == var7.itemID) {
                    if (par3 == 0) {
                        if (par4) {
                            var6.stackSize = var6.getMaxStackSize();
                        } else if (var6.stackSize < var6.getMaxStackSize()
                                && var6.getItemDamage() == var7.getItemDamage()) {
                            ++var6.stackSize;
                        } else {
                            var5.setItemStack(null);
                        }
                    } else if (var6.stackSize <= 1) {
                        var5.setItemStack((ItemStack) null);
                    } else {
                        --var6.stackSize;
                    }
                } else if (var6 != null) {
                    var5.setItemStack((ItemStack) null);
                } else if (var7 == null) {
                    var5.setItemStack((ItemStack) null);
                } else if (var6 == null || var6.itemID != var7.itemID) {
                    var5.setItemStack(ItemStack.copyItemStack(var7));
                    var6 = var5.getItemStack();

                    if (par4) {
                        var6.stackSize = var6.getMaxStackSize();
                    }
                }
            } else if (par1Slot.inventory == this.mc.thePlayer.inventory && selectedIndex == -2) {
                if (par4 && par1Slot != null && par1Slot.getHasStack()) {
                    if (par1Slot.slotNumber >= 27) {
                        if (par1Slot != null && par1Slot.getStack() != null) {
                            ((ContainerCreativeGui) this.inventorySlots).transferItemStack(par1Slot.getStack(), 0, 26);
                            par1Slot.putStack(null);
                        }
                    }
                    if (par1Slot.slotNumber < 27) {
                        if (par1Slot != null && par1Slot.getStack() != null) {
                            ((ContainerCreativeGui) this.inventorySlots).transferItemStack(par1Slot.getStack(), 27, 35);
                            par1Slot.putStack(null);
                        }
                    }
                }
                if (par1Slot.slotNumber > 26 && par1Slot.slotNumber < 31) {
                    if (this.mc.thePlayer.inventory.getItemStack() == null
                            || this.mc.thePlayer.inventory.getItemStack().getItem() instanceof ItemArmor) {
                        if (this.mc.thePlayer.inventory.getItemStack() == null || par1Slot.slotNumber
                                - 27 == ((ItemArmor) this.mc.thePlayer.inventory.getItemStack().getItem()).armorType) {
                            this.inventorySlots.slotClick(par1Slot.slotNumber, par3, par4, this.mc.thePlayer);
                            ItemStack var8 = this.inventorySlots.getSlot(par1Slot.slotNumber).getStack();
                            this.mc.playerController.sendSlotPacket(var8,
                                    par1Slot.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
                        }
                    }
                } else {
                    this.inventorySlots.slotClick(par1Slot.slotNumber, par3, par4, this.mc.thePlayer);
                    ItemStack var8 = this.inventorySlots.getSlot(par1Slot.slotNumber).getStack();
                    this.mc.playerController.sendSlotPacket(var8,
                            par1Slot.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
                }
            } else if (par1Slot.inventory == inventoryRecycle) {
                if (par4) {
                    for (int i = 0; i < 4; i++) {
                        for (int l = 0; l < 9; l++) {
                            this.mc.thePlayer.inventory.setInventorySlotContents(l + i * 9, null);
                        }
                    }
                    for (int i = 0; i < 4; i++) {
                        this.mc.thePlayer.inventory.setInventorySlotContents(i + 36, null);
                    }
                } else {
                    this.mc.thePlayer.inventory.setItemStack(null);
                }
            } else {

                if (par4) {
                    par1Slot.putStack(null);
                } else {
                    this.inventorySlots.slotClick(par1Slot.slotNumber, par3, par4, this.mc.thePlayer);
                    ItemStack var8 = this.inventorySlots.getSlot(par1Slot.slotNumber).getStack();
                    this.mc.playerController.sendSlotPacket(var8,
                            par1Slot.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
                }
            }
        } else {
            var5 = this.mc.thePlayer.inventory;

            if (var5.getItemStack() != null) {
                if (par3 == 0) {
                    this.mc.thePlayer.dropPlayerItem(var5.getItemStack());
                    this.mc.playerController.func_35639_a(var5.getItemStack());
                    var5.setItemStack((ItemStack) null);
                }

                if (par3 == 1) {
                    var6 = var5.getItemStack().splitStack(1);
                    this.mc.thePlayer.dropPlayerItem(var6);
                    this.mc.playerController.func_35639_a(var6);

                    if (var5.getItemStack().stackSize == 0) {
                        var5.setItemStack((ItemStack) null);
                    }
                }
            }
        }
    }

    public void handleMouseInput() {
        super.handleMouseInput();
        int var1 = Mouse.getEventDWheel();

        if (selectedIndex != -2 && (selectedIndex == -1 ? CreativeTabs.tabSearch.items.size()
                : CreativeTabs.tabs.get(selectedIndex).items.size() / 9 - 4) > 0) {
            CreativeTabs tab = selectedIndex == -1 ? CreativeTabs.tabSearch : CreativeTabs.tabs.get(selectedIndex);
            if (var1 != 0) {
                int var2 = tab.items.size() / 9 - 4;

                if (var1 > 0) {
                    var1 = 1;
                }

                if (var1 < 0) {
                    var1 = -1;
                }

                this.currentScroll = (float) ((double) this.currentScroll - (double) var1 / (double) var2);

                if (this.currentScroll < 0.0F) {
                    this.currentScroll = 0.0F;
                }

                if (this.currentScroll > 1.0F) {
                    this.currentScroll = 1.0F;
                }

                ((ContainerCreativeGui) this.inventorySlots).scrollTo(this.currentScroll);
            }
        }
    }

    @Override
    protected void mouseMovedOrUp(int par1, int par2, int par3) {
        if (par3 == 0) {
            int l = par1 - this.guiLeft;
            int i1 = par2 - this.guiTop;
            List<CreativeTabs> acreativetabs = CreativeTabs.tabs;
            int j1 = acreativetabs.size();

            this.searchField.setCanLoseFocus(true);
            this.searchField.setFocused(false);
            if (selectedIndex == -1 && 81 < l && 169 >= l && 35 < i1 && 45 >= i1) {
                this.searchField.setFocused(true);
            }
            if ((this.xSize - 28 < l && this.xSize > l) && (0 < i1 && 28 > i1)) {
                selectedIndex = -1;
                this.searchField.setCanLoseFocus(true);
                this.searchField.setFocused(true);
                this.searchField.setText("");
                this.updateSearch(true);
                this.currentScroll = 0.0F;
                ((ContainerCreativeGui) this.inventorySlots).scrollTo(this.currentScroll);
                super.mouseMovedOrUp(par1, par2, par3);
                return;
            }
            if ((this.xSize - 28 < l && this.xSize > l) && (this.ySize - 30 < i1 && this.ySize > i1)) {
                selectedIndex = -2;
                this.currentScroll = 0.0F;
                ((ContainerCreativeGui) this.inventorySlots).scrollTo(this.currentScroll);
                super.mouseMovedOrUp(par1, par2, par3);
                return;
            }
            if (selectedIndex != -1)
                this.updateSearch(false);

            int i = 0;
            int prevI = 0;
            for (CreativeTabs tab : CreativeTabs.tabs) {
                if (i < 10 * page) {
                    i++;
                    continue;
                }
                if (i > 10 * page + 9) {
                    break;
                }
                boolean isSelected = i == selectedIndex;
                prevI = i;
                i = i - (10 * page);
                int dx = i * 28 + i;
                int dy = 4;
                if (i > 4) {
                    dx = (i - 5) * 28 + (i - 5);
                    dy = 166;
                }
                if (isSelected) {
                    if (i > 4)
                        dy -= 4;
                    else
                        dy = 2;
                }
                if (this.guiLeft + dx <= par1 && this.guiLeft + dx + 28 >= par1 && this.guiTop + dy <= par2
                        && this.guiTop + dy + 28 >= par2) {
                    selectedIndex = i + (10 * page);
                    this.currentScroll = 0.0F;
                    ((ContainerCreativeGui) this.inventorySlots).scrollTo(this.currentScroll);
                    return;
                }
                if (this.guiLeft + 166 <= par1 && this.guiLeft + 166 + 28 >= par1 && this.guiTop <= par2
                        && this.guiTop + 28 >= par2) {
                    selectedIndex = -1;
                    this.currentScroll = 0.0F;
                    ((ContainerCreativeGui) this.inventorySlots).scrollTo(this.currentScroll);
                    return;
                }
                if (this.guiLeft + 166 <= par1 && this.guiLeft + 166 + 28 >= par1 && this.guiTop + 166 <= par2
                        && this.guiTop + 166 + 28 >= par2) {
                    selectedIndex = -2;
                    this.currentScroll = 0.0F;
                    ((ContainerCreativeGui) this.inventorySlots).scrollTo(this.currentScroll);
                    return;
                }
                i = prevI;
                i++;
            }
        }
        super.mouseMovedOrUp(par1, par2, par3);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        {
            int texture = this.mc.renderEngine.getTexture("/CreativeGui/gui/tabs.png");
            int i = 0;
            int prevI = 0;
            for (CreativeTabs tab : CreativeTabs.tabs) {
                if (i < 10 * page) {
                    i++;
                    continue;
                }
                if (i > 10 * page + 9) {
                    break;
                }
                boolean isSelected = i == selectedIndex;
                prevI = i;
                i = i - 10 * page;
                int x = i * 28;
                int y = 2;
                int sy = 32;
                int dx = i * 28 + i;
                int dy = 4;
                if (i > 4) {
                    x = (i - 5) * 28;
                    y = 64;
                    sy = 28;
                    dx = (i - 5) * 28 + (i - 5);
                    dy = 162;
                }
                this.zLevel = 0.0F;
                if (isSelected) {
                    if (i > 4) {
                        // dy -= 4;
                        sy = 32;
                        y += 32;
                    } else {
                        dy = 2;
                        y += 30;
                    }
                    this.zLevel = 50.0F;
                }
                this.mc.renderEngine.bindTexture(texture);
                GL11.glColor3f(1, 1, 1);
                this.drawTexturedModalRect(this.guiLeft + dx, this.guiTop + dy, x, y, 28, sy);
                ItemStack itemstack = tab.tabIcon;
                if (i > 4) {
                    dx = (i - 5) * 28 + (i - 5);
                    dy = 160;
                } else {
                    dx = i * 28 + i;
                    dy = 4;
                }
                this.zLevel = 0.0F;
                if (selectedIndex == -1) {
                    this.zLevel = 50.0F;
                    this.drawTexturedModalRect(this.guiLeft + this.xSize - 28, this.guiTop + 2, 140, 32, 28, 32);
                } else
                    this.drawTexturedModalRect(this.guiLeft + this.xSize - 28, this.guiTop + 4, 140, 2, 28, 30);
                this.zLevel = 0.0F;
                if (selectedIndex == -2) {
                    this.zLevel = 50.0F;
                    this.drawTexturedModalRect(this.guiLeft + this.xSize - 28, this.guiTop + this.ySize - 34, 140, 96,
                            28, 32);
                } else
                    this.drawTexturedModalRect(this.guiLeft + this.xSize - 28, this.guiTop + this.ySize - 32, 140, 66,
                            28, 28);
                this.zLevel = 100.0F;
                RenderHelper.enableGUIStandardItemLighting();
                itemRenderer.zLevel = 100.0F;
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, itemstack,
                        this.guiLeft + dx + 6, this.guiTop + dy + 8);
                itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, itemstack,
                        this.guiLeft + dx + 6, this.guiTop + dy + 8);
                itemRenderer.zLevel = 0.0F;
                this.zLevel = 0.0F;
                RenderHelper.disableStandardItemLighting();
                i = prevI;
                i++;
            }
            {
                int texturea = this.mc.renderEngine.getTexture("/CreativeGui/gui/tab_items.png");
                if (selectedIndex == -1)
                    texturea = this.mc.renderEngine.getTexture("/CreativeGui/gui/tab_item_search.png");
                if (selectedIndex == -2)
                    texturea = this.mc.renderEngine.getTexture("/CreativeGui/gui/tab_inventory.png");
                GL11.glColor3f(1, 1, 1);
                this.mc.renderEngine.bindTexture(texturea);
                int var5 = this.guiLeft;
                int var6 = this.guiTop + 30;
                this.drawTexturedModalRect(var5, var6, 0, 0, 195, 136);
            }
            this.displayDebuffEffects();
            RenderHelper.enableGUIStandardItemLighting();
            this.mc.renderEngine.bindTexture(texture);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glColor3f(1F, 1F, 1F);
            this.zLevel = 100.0F;
            itemRenderer.zLevel = 100.0F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, new ItemStack(Item.compass),
                    this.guiLeft + this.xSize - 28 + 6, this.guiTop + 2 + 8);
            itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, new ItemStack(Item.compass),
                    this.guiLeft + this.xSize - 28 + 6, this.guiTop + 2 + 8);
            itemRenderer.zLevel = 0.0F;
            this.zLevel = 0.0F;
            this.zLevel = 100.0F;
            itemRenderer.zLevel = 100.0F;
            itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, new ItemStack(Block.chest),
                    this.guiLeft + this.xSize - 28 + 6, this.guiTop + this.ySize - 28 - 6 + 8);
            itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, new ItemStack(Block.chest),
                    this.guiLeft + this.xSize - 28 + 6, this.guiTop + this.ySize - 28 - 6 + 8);
            itemRenderer.zLevel = 0.0F;
            this.zLevel = 0.0F;
            this.mc.renderEngine.bindTexture(texture);
            int var8 = this.guiTop + 30 + 17;
            int var9 = var8 + 110 + 2;
            this.zLevel = 20.0F;
            RenderHelper.disableStandardItemLighting();
            if (selectedIndex != -2)
                this.drawTexturedModalRect(this.guiLeft + 175,
                        var8 + 1 + (int) ((float) (var9 - var8 - 17) * this.currentScroll),
                        (selectedIndex == -1 ? CreativeTabs.tabSearch.items.size()
                                : CreativeTabs.tabs.get(selectedIndex).items.size() / 9 - 4) > 0 ? 232 : 244,
                        0, 12, 16);
            else {
                int var5 = this.guiLeft;
                int var6 = this.guiTop;
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glEnable(GL11.GL_COLOR_MATERIAL);
                GL11.glPushMatrix();
                GL11.glTranslatef((float) (var5 + 45), (float) (var6 + 75), 50.0F);
                float var7 = 20F;
                GL11.glScalef(-var7, var7, var7);
                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                float var81 = this.mc.thePlayer.renderYawOffset;
                float var91 = this.mc.thePlayer.rotationYaw;
                float var10 = this.mc.thePlayer.rotationPitch;
                float var11 = (float) (var5 + 45) - this.xSize_lo;
                float var12 = (float) (var6 + 75 - 33) - this.ySize_lo;
                GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
                RenderHelper.enableStandardItemLighting();
                GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-((float) Math.atan((double) (var12 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
                this.mc.thePlayer.renderYawOffset = (float) Math.atan((double) (var11 / 40.0F)) * 20.0F;
                this.mc.thePlayer.rotationYaw = (float) Math.atan((double) (var11 / 40.0F)) * 40.0F;
                this.mc.thePlayer.rotationPitch = -((float) Math.atan((double) (var12 / 40.0F))) * 20.0F;
                this.mc.thePlayer.rotationYawHead = this.mc.thePlayer.rotationYaw;
                GL11.glTranslatef(0.0F, this.mc.thePlayer.yOffset, 0.0F);
                RenderManager.instance.playerViewY = 180.0F;
                RenderManager.instance.renderEntityWithPosYaw(this.mc.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
                this.mc.thePlayer.renderYawOffset = var81;
                this.mc.thePlayer.rotationYaw = var91;
                this.mc.thePlayer.rotationPitch = var10;
                GL11.glPopMatrix();
                RenderHelper.disableStandardItemLighting();
                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            }
            if (selectedIndex == -1)
                this.searchField.drawTextBox();
        }
        RenderHelper.disableStandardItemLighting();
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    public void drawScreen(int par1, int par2, float par3) {
        boolean click = Mouse.isButtonDown(0);
        int left = this.guiLeft;
        int top = this.guiTop;
        int scrollLeft = left + 175;
        int scrollTop = top + 47;
        int var9 = scrollLeft + 14;
        int var10 = scrollTop + 110 + 2;

        if (selectedIndex != -2 && (selectedIndex == -1 ? CreativeTabs.tabSearch.items.size()
                : CreativeTabs.tabs.get(selectedIndex).items.size() / 9 - 4) > 0) {
            if (!this.wasClicking && click && par1 >= scrollLeft && par2 >= scrollTop && par1 < var9 && par2 < var10) {
                this.isScrolling = true;
            }

            if (!click) {
                this.isScrolling = false;
            }

            this.wasClicking = click;

            if (this.isScrolling) {
                this.currentScroll = (float) (par2 - (scrollTop + 8)) / ((float) (var10 - scrollTop) - 16.0F);

                if (this.currentScroll < 0.0F) {
                    this.currentScroll = 0.0F;
                }

                if (this.currentScroll > 1.0F) {
                    this.currentScroll = 1.0F;
                }

                ((ContainerCreativeGui) this.inventorySlots).scrollTo(this.currentScroll);
            }
        }

        super.drawScreen(par1, par2, par3);

        if (selectedIndex == -2 && this.guiLeft + 173 <= par1 && this.guiLeft + 189 >= par1 && this.guiTop + 142 <= par2
                && this.guiTop + 158 >= par2) {
            this.drawHoveringText(Arrays.asList(StatCollector.translateToLocal("inventory.binSlot")), par1, par2);
        }
        int i = 0;
        int prevI = 0;
        for (CreativeTabs tab : CreativeTabs.tabs) {
            if (i < 10 * page) {
                i++;
                continue;
            }
            if (i > 10 * page + 9) {
                break;
            }
            boolean isSelected = i == selectedIndex;
            prevI = i;
            i = i - (10 * page);
            int dx = i * 28 + i;
            int dy = 4;
            if (i > 4) {
                dx = (i - 5) * 28 + (i - 5);
                dy = 166;
            }
            if (isSelected) {
                if (i > 4)
                    dy -= 4;
                else
                    dy = 2;
            }
            if (this.guiLeft + dx <= par1 && this.guiLeft + dx + 28 >= par1 && this.guiTop + dy <= par2
                    && this.guiTop + dy + 28 >= par2) {
                this.drawHoveringText(Arrays.asList(StringTranslate.getInstance().translateKey(tab.tabName)), par1,
                        par2);
                break;
            }
            if (this.guiLeft + 166 <= par1 && this.guiLeft + 166 + 28 >= par1 && this.guiTop <= par2
                    && this.guiTop + 28 >= par2) {
                this.drawHoveringText(Arrays.asList(StatCollector.translateToLocal("itemGroup.search")), par1, par2);
                break;
            }
            if (this.guiLeft + 166 <= par1 && this.guiLeft + 166 + 28 >= par1 && this.guiTop + 166 <= par2
                    && this.guiTop + 166 + 28 >= par2) {
                this.drawHoveringText(Arrays.asList(StatCollector.translateToLocal("itemGroup.inventory")), par1, par2);
                break;
            }
            i = prevI;
            i++;
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.xSize_lo = (float) par1;
        this.ySize_lo = (float) par2;
    }

    public void drawHoveringText(List list, int x, int y) {
        if (!list.isEmpty()) {
            FontRenderer font = this.fontRenderer;
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                int l = font.getStringWidth(s);

                if (l > k) {
                    k = l;
                }
            }

            int i1 = x + 12;
            int j1 = y - 12;
            int k1 = 8;

            if (list.size() > 1) {
                k1 += 2 + (list.size() - 1) * 10;
            }

            if (i1 + k > this.width) {
                i1 -= 28 + k;
            }

            if (j1 + k1 + 6 > this.height) {
                j1 = this.height - k1 - 6;
            }

            this.zLevel = 300.0F;
            itemRenderer.zLevel = 300.0F;
            int l1 = -267386864;
            this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
            this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
            this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
            this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
            this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
            int i2 = 1347420415;
            int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
            this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
            this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
            this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
            this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

            for (int k2 = 0; k2 < list.size(); ++k2) {
                String s1 = (String) list.get(k2);
                font.drawStringWithShadow(s1, i1, j1, -1);

                if (k2 == 0) {
                    j1 += 2;
                }

                j1 += 10;
            }

            this.zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);
        if (button.id == 1) {
            if (page > 0)
                page--;
        } else if (button.id == 2) {
            if (page + 1 < (int) Math.ceil(CreativeTabs.tabs.size() / 10d))
                page++;
        }
    }

    private void displayDebuffEffects() {
        int var1 = this.guiLeft - 124;
        int var2 = this.guiTop + 30;
        int var3 = this.mc.renderEngine.getTexture("/gui/inventory.png");
        Collection var4 = this.mc.thePlayer.getActivePotionEffects();

        if (!var4.isEmpty()) {
            int var5 = 33;

            if (var4.size() > 5) {
                var5 = 132 / (var4.size() - 1);
            }

            for (Iterator var6 = this.mc.thePlayer.getActivePotionEffects().iterator(); var6.hasNext(); var2 += var5) {
                PotionEffect var7 = (PotionEffect) var6.next();
                Potion var8 = Potion.potionTypes[var7.getPotionID()];
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.mc.renderEngine.bindTexture(var3);
                this.drawTexturedModalRect(var1, var2, 0, 166, 140, 32);

                if (var8.hasStatusIcon()) {
                    int var9 = var8.getStatusIconIndex();
                    this.drawTexturedModalRect(var1 + 6, var2 + 7, 0 + var9 % 8 * 18, 166 + 32 + var9 / 8 * 18, 18, 18);
                }

                String var11 = StatCollector.translateToLocal(var8.getName());

                if (var7.getAmplifier() == 1) {
                    var11 = var11 + " II";
                } else if (var7.getAmplifier() == 2) {
                    var11 = var11 + " III";
                } else if (var7.getAmplifier() == 3) {
                    var11 = var11 + " IV";
                }

                this.fontRenderer.drawStringWithShadow(var11, var1 + 10 + 18, var2 + 6, 16777215);
                String var10 = Potion.getDurationString(var7);
                this.fontRenderer.drawStringWithShadow(var10, var1 + 10 + 18, var2 + 6 + 10, 8355711);
            }
        }
    }

    static InventoryBasic getInventory() {
        return inventory;
    }

    static int getSelectedIndex() {
        return selectedIndex;
    }

}
