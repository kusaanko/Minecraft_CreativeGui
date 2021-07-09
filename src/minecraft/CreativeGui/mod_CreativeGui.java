package CreativeGui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import CreativeGui.tabs.CreativeTabs;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import minecraftAPI.MCAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.src.BaseMod;
import net.minecraft.src.Block;
import net.minecraft.src.GuiContainerCreative;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Item;
import net.minecraft.src.MLProp;
import net.minecraft.src.ModLoader;

public class mod_CreativeGui extends BaseMod {
    public static HashMap<Integer, String> itemMods = new HashMap<Integer, String>();
    @MLProp
    public static boolean addonMode = true;
    @MLProp
    public static boolean uniteTabs = false;
    @MLProp(min = 0, max = Integer.MAX_VALUE)
    public static int uniteSize = 10;
    @MLProp
    public static boolean unlocalizedNameSearch = true;
    @MLProp
    public static boolean modNameSearch = true;
    @MLProp
    public static boolean idSearch = true;
    @MLProp
    public static boolean loreSearch = true;
    @MLProp
    public static boolean addAllBlocks = true;

    private boolean initialized = false;

    public mod_CreativeGui() {
        Loader.log.addHandler(new Handler() {
            public String modName = "Minecraft";

            @Override
            public void publish(LogRecord record) {
                String message = record.getMessage();
                String modName = "";
                if (message.contains(" "))
                    modName = message.split(" ")[1];
                if (modName.endsWith(".modsLoaded()"))
                    modName = modName.substring(0, modName.length() - 13);
                if (modName.endsWith(".load()"))
                    modName = modName.substring(0, modName.length() - 7);
                if (message.startsWith("Pre-initializing ") && message.split(" ").length > 2) {
                    update();
                    if (message.contains(".zip ")) {
                        modName = message.substring(message.indexOf(".zip ") + 5);
                    } else if (message.contains(".jar ")) {
                        modName = message.substring(message.indexOf(".jar ") + 5);
                    } else {
                        modName = message.split(" ")[2];
                    }
                    this.modName = modName;
                }
                if (message.startsWith("Mod pre-initialization complete")) {
                    update();
                    this.modName = "Minecraft";
                }
                if (message.startsWith("Initializing ")) {
                    update();
                    this.modName = modName;
                }
                if (message.startsWith("Post-initializing ")) {
                    update();
                    this.modName = modName;
                }
                if (message.equals("Mod post-initialization complete")) {
                    update();
                    File dir = new File(FMLCommonHandler.instance().getMinecraftRootDirectory(), "/CreativeGui");
                    dir.mkdir();
                    for (File f : dir.listFiles()) {
                        if (f.getName().endsWith(".cfg")) {
                            try {
                                BufferedReader br = new BufferedReader(
                                        new InputStreamReader(new FileInputStream(f), "UTF-8"));
                                String line;
                                String type = "AddonDictionary";
                                while ((line = br.readLine()) != null) {
                                    if (line.startsWith("#"))
                                        continue;
                                    if (!line.contains("=")) {
                                        type = line;
                                        continue;
                                    }
                                    if (type.equals("AddonDictionary")) {
                                        String key = line.substring(0, line.indexOf("=")).toLowerCase();
                                        String value = line.substring(line.indexOf("=") + 1);
                                        CreativeTabs.modsParent.put(key, value);
                                    }
                                    if (type.equals("IdDictionary")) {
                                        CreativeTabs.IdDictionary.add(line);
                                    }
                                }
                                br.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            public void update() {
                for (Item item : Item.itemsList) {
                    if (item != null && !itemMods.containsKey(item.shiftedIndex)) {
                        itemMods.put(item.shiftedIndex, this.modName);
                        String modclazz = this.modName;
                        if (modclazz.startsWith("mod_"))
                            modclazz = this.modName.substring(4);
                        if (item.shiftedIndex < Block.blocksList.length
                                && Block.blocksList[item.shiftedIndex] != null) {
                            Block b = Block.blocksList[item.shiftedIndex];
                            String name = MCAPI.getModName(b);
                            if (MCAPI.getModName(b).equals(""))
                                name = modclazz;
                            MCAPI.setModName(b, name);
                        }
                        Item i = Item.itemsList[item.shiftedIndex];
                        if (MCAPI.getModName(i).equals(""))
                            MCAPI.setModName(i, modclazz);
                    }
                }
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        });
    }

    @Override
    public String getVersion() {
        return "1.2.1";
    }

    @Override
    public void load() {
        ModLoader.setInGUIHook(this, true, true);
    }

    public void addLocalizeEn(String key, String value) {
        ModLoader.addLocalization(key, value);
    }

    public void addLocalizeJp(String key, String value) {
        ModLoader.addLocalization(key, "ja_JP", value);
    }

    @Override
    public void modsLoaded() {
        addLocalizeEn("inventory.binSlot", "Destroy Item");
        addLocalizeEn("itemGroup.buildingBlocks", "Building Blocks");
        addLocalizeEn("itemGroup.decorations", "Decoration Blocks");
        addLocalizeEn("itemGroup.redstone", "Redstone");
        addLocalizeEn("itemGroup.transportation", "Transportation");
        addLocalizeEn("itemGroup.misc", "Miscellaneous");
        addLocalizeEn("itemGroup.search", "Search Items");
        addLocalizeEn("itemGroup.food", "Foodstuffs");
        addLocalizeEn("itemGroup.tools", "Tools");
        addLocalizeEn("itemGroup.combat", "Combat");
        addLocalizeEn("itemGroup.brewing", "Brewing");
        addLocalizeEn("itemGroup.materials", "Materials");
        addLocalizeEn("itemGroup.inventory", "Survival Inventory");

        addLocalizeJp("inventory.binSlot", "アイテム削除");
        addLocalizeJp("itemGroup.brewing", "醸造");
        addLocalizeJp("itemGroup.buildingBlocks", "建築ブロック");
        addLocalizeJp("itemGroup.combat", "戦闘");
        addLocalizeJp("itemGroup.decorations", "装飾ブロック");
        addLocalizeJp("itemGroup.food", "食料");
        addLocalizeJp("itemGroup.inventory", "サバイバルインベントリ");
        addLocalizeJp("itemGroup.materials", "材料");
        addLocalizeJp("itemGroup.misc", "その他");
        addLocalizeJp("itemGroup.redstone", "レッドストーン");
        addLocalizeJp("itemGroup.search", "アイテム検索");
        addLocalizeJp("itemGroup.tools", "道具");
        addLocalizeJp("itemGroup.transportation", "移動");
    }

    @Override
    public boolean onTickInGUI(float tick, Minecraft game, GuiScreen gui) {
        if (gui instanceof GuiContainerCreative) {
            if (!initialized) {
                CreativeTabs.init();
                initialized = true;
            }
            ModLoader.openGUI(game.thePlayer, new GuiContainerCreativeGui(game.thePlayer));
        }
        return true;
    }

}
