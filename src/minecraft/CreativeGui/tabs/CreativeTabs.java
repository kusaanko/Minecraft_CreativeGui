package CreativeGui.tabs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import CreativeGui.mod_CreativeGui;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.src.BaseMod;
import net.minecraft.src.Block;
import net.minecraft.src.EntityList;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class CreativeTabs {
	public static Vector<CreativeTabs> tabs = new Vector<CreativeTabs>();

	public static CreativeTabs tabBuilding = new CreativeTabs("itemGroup.buildingBlocks", new ItemStack(Block.brick));
	public static CreativeTabs tabDecoration = new CreativeTabs("itemGroup.decorations", new ItemStack(Block.plantRed));
	public static CreativeTabs tabRedstone = new CreativeTabs("itemGroup.redstone", new ItemStack(Item.redstone));
	public static CreativeTabs tabTransport = new CreativeTabs("itemGroup.transportation", new ItemStack(Block.railPowered));
	public static CreativeTabs tabMisc = new CreativeTabs("itemGroup.misc", new ItemStack(Item.bucketLava));
	public static CreativeTabs tabFood = new CreativeTabs("itemGroup.food", new ItemStack(Item.appleRed));
	public static CreativeTabs tabTool = new CreativeTabs("itemGroup.tools", new ItemStack(Item.axeSteel));
	public static CreativeTabs tabCombat = new CreativeTabs("itemGroup.combat", new ItemStack(Item.swordGold));
	public static CreativeTabs tabBrewing = new CreativeTabs("itemGroup.brewing", new ItemStack(Item.potion));
	public static CreativeTabs tabMaterial = new CreativeTabs("itemGroup.materials", new ItemStack(Item.stick));
	public static CreativeTabs tabSearch = new CreativeTabs("itemGroup.search", new ItemStack(Item.compass), false);
	private static CreativeTabs tabMods = new CreativeTabs("Mods", new ItemStack(Block.workbench));
	public static HashMap<String, String> modsParent = new HashMap<String, String>();
	public static ArrayList<String> IdDictionary = new ArrayList<String>();

	public String tabName;
	public ItemStack tabIcon;
	public int tabId;

	public List<ItemStack> items = new ArrayList<ItemStack>();

	public CreativeTabs(String tabName, ItemStack tabIcon, boolean addList) {
		this.tabName = tabName;
		this.tabIcon = tabIcon;
		if(addList) {
			tabId = tabs.size();
			tabs.addElement(this);
		}
	}

	public CreativeTabs(String tabName, ItemStack tabIcon) {
		this(tabName, tabIcon, true);
	}

	public static void init() {
		Block[] building = new Block[] {Block.stone, Block.grass, Block.dirt, Block.cobblestone, Block.planks, Block.bedrock, Block.sand, Block.gravel, Block.oreGold, Block.oreIron, Block.oreCoal, Block.wood, Block.glass, Block.oreLapis, Block.blockLapis, Block.sandStone, Block.cloth, Block.blockGold, Block.blockSteel, Block.stairSingle, Block.brick, Block.bookShelf, Block.cobblestoneMossy, Block.obsidian, Block.stairCompactPlanks, Block.oreDiamond, Block.blockDiamond, Block.stairCompactCobblestone, Block.oreRedstone, Block.ice, Block.blockSnow, Block.blockClay, Block.pumpkin, Block.netherrack, Block.slowSand, Block.glowStone, Block.pumpkinLantern, Block.stoneBrick, Block.stairsBrick, Block.stairsStoneBrickSmooth, Block.mycelium, Block.netherBrick, Block.stairsNetherBrick, Block.whiteStone};
		for(Block b : building) {
			if(Item.itemsList[b.blockID]==null) continue;
			if(b.blockID==5) {
				for(int i = 0;i < 4;i++) {
					tabBuilding.items.add(new ItemStack(b,1,i));
				}
				continue;
			}
			if(b.blockID==17) {
				for(int i = 0;i < 4;i++) {
					tabBuilding.items.add(new ItemStack(b,1,i));
				}
				continue;
			}
			if(b.blockID==Block.sandStone.blockID) {
				for(int i = 0;i < 3;i++) {
					tabBuilding.items.add(new ItemStack(b,1,i));
				}
				continue;
			}
			if(b.blockID==35) {
				for(int i = 0;i < 16;i++) {
					tabBuilding.items.add(new ItemStack(b,1,i));
				}
				continue;
			}
			if(b.blockID==44) {
				for(int i = 0;i < 6;i++) {
					tabBuilding.items.add(new ItemStack(b,1,i));
				}
				continue;
			}
			if(b.blockID==98) {
				for(int i = 0;i < 4;i++) {
					tabBuilding.items.add(new ItemStack(b,1,i));
				}
				continue;
			}
			tabBuilding.items.add(new ItemStack(b));
		}
		int[] decoration = new int[] {Block.sapling.blockID, Block.leaves.blockID, Block.web.blockID, Block.tallGrass.blockID, Block.deadBush.blockID, Block.plantYellow.blockID, Block.plantRed.blockID, Block.mushroomBrown.blockID, Block.mushroomRed.blockID, Block.torchWood.blockID, Block.chest.blockID, Block.workbench.blockID, Block.stoneOvenIdle.blockID, Block.ladder.blockID, Block.snow.blockID, Block.cactus.blockID, Block.jukebox.blockID, Block.fence.blockID, Block.silverfish.blockID, Block.fenceIron.blockID, Block.thinGlass.blockID, Block.vine.blockID, Block.waterlily.blockID, Block.netherFence.blockID, Block.enchantmentTable.blockID, Block.endPortalFrame.blockID, Item.painting.shiftedIndex, Item.sign.shiftedIndex, Item.bed.shiftedIndex};
		for(int i : decoration) {
			if(Item.itemsList[i]==null) continue;
			if(i==6) {
				for(int l = 0;l < 4;l++) {
					tabDecoration.items.add(new ItemStack(i, 1, l));
				}
				continue;
			}
			if(i==18) {
				for(int l = 0;l < 4;l++) {
					tabDecoration.items.add(new ItemStack(i, 1, l));
				}
				continue;
			}
			if(i==97) {
				for(int l = 0;l < 3;l++) {
					tabDecoration.items.add(new ItemStack(i, 1, l));
				}
				continue;
			}
			if(i==Block.tallGrass.blockID) {
				tabDecoration.items.add(new ItemStack(i, 1, 1));
				tabDecoration.items.add(new ItemStack(i, 1, 2));
				continue;
			}
			tabDecoration.items.add(new ItemStack(i, 1, 0));
		}
		int[] redstone = new int[] {Block.dispenser.blockID, Block.music.blockID, Block.pistonStickyBase.blockID, Block.pistonBase.blockID, Block.tnt.blockID, Block.lever.blockID, Block.pressurePlateStone.blockID, Block.pressurePlatePlanks.blockID, Block.torchRedstoneActive.blockID, Block.button.blockID, Block.trapdoor.blockID, Block.fenceGate.blockID, Block.redstoneLampIdle.blockID, Item.doorWood.shiftedIndex, Item.doorSteel.shiftedIndex, Item.redstone.shiftedIndex, Item.redstoneRepeater.shiftedIndex};
		for(int i : redstone) {
			if(Item.itemsList[i]==null) continue;
			tabRedstone.items.add(new ItemStack(i, 1, 0));
		}
		int[] transport = new int[] {Block.railPowered.blockID, Block.railDetector.blockID, Block.rail.blockID, Item.minecartEmpty.shiftedIndex, Item.boat.shiftedIndex, Item.minecartCrate.shiftedIndex, Item.minecartPowered.shiftedIndex};
		for(int i : transport) {
			if(Item.itemsList[i]==null) continue;
			tabTransport.items.add(new ItemStack(i, 1, 0));
		}
		int[] misc = new int[] {Item.bucketEmpty.shiftedIndex, Item.bucketWater.shiftedIndex, Item.bucketLava.shiftedIndex, Item.snowball.shiftedIndex, Item.bucketMilk.shiftedIndex, Item.paper.shiftedIndex, Item.book.shiftedIndex, Item.slimeBall.shiftedIndex, Item.bone.shiftedIndex, Item.enderPearl.shiftedIndex, Item.eyeOfEnder.shiftedIndex, Item.monsterPlacer.shiftedIndex, Item.expBottle.shiftedIndex, Item.fireballCharge.shiftedIndex, Item.map.shiftedIndex, Item.record13.shiftedIndex, Item.recordCat.shiftedIndex, Item.recordBlocks.shiftedIndex, Item.recordChirp.shiftedIndex, Item.recordFar.shiftedIndex, Item.recordMall.shiftedIndex, Item.recordMellohi.shiftedIndex, Item.recordStal.shiftedIndex, Item.recordStrad.shiftedIndex, Item.recordWard.shiftedIndex, Item.record11.shiftedIndex};
		for(int i : misc) {
			if(Item.itemsList[i]==null) continue;
			if(i==Item.monsterPlacer.shiftedIndex) {
				Iterator var15 = EntityList.entityEggs.keySet().iterator();

		        while (var15.hasNext())
		        {
		            Integer var17 = (Integer)var15.next();
		            tabMisc.items.add(new ItemStack(Item.monsterPlacer.shiftedIndex, 1, var17.intValue()));
		        }
		        continue;
			}
			tabMisc.items.add(new ItemStack(i, 1, 0));
		}
		Item[] food = new Item[] {Item.appleRed, Item.bowlSoup, Item.bread, Item.porkRaw, Item.porkCooked, Item.appleGold, Item.fishRaw, Item.fishCooked, Item.cake, Item.cookie, Item.melon, Item.beefRaw, Item.beefCooked, Item.chickenRaw, Item.chickenCooked, Item.rottenFlesh, Item.spiderEye};
		for(Item item : food) {
			if(Item.itemsList[item.shiftedIndex]==null) continue;
			tabFood.items.add(new ItemStack(item, 1, 0));
		}
		Item[] tool = new Item[] {Item.shovelSteel, Item.pickaxeSteel, Item.axeSteel, Item.flintAndSteel, Item.shovelWood, Item.pickaxeWood, Item.axeWood, Item.shovelStone, Item.pickaxeStone, Item.axeStone, Item.shovelDiamond, Item.pickaxeDiamond, Item.axeDiamond, Item.shovelGold, Item.pickaxeGold, Item.axeGold, Item.hoeWood, Item.hoeStone, Item.hoeSteel, Item.hoeDiamond, Item.hoeGold, Item.compass, Item.fishingRod, Item.pocketSundial, Item.shears};
		for(Item item : tool) {
			if(Item.itemsList[item.shiftedIndex]==null) continue;
			tabTool.items.add(new ItemStack(item, 1, 0));
		}
		Item[] combat = new Item[] {Item.bow, Item.arrow, Item.swordSteel, Item.swordWood, Item.swordStone, Item.swordDiamond, Item.swordGold, Item.helmetLeather, Item.plateLeather, Item.legsLeather, Item.bootsLeather, Item.helmetChain, Item.plateChain, Item.legsChain, Item.bootsChain, Item.helmetSteel, Item.plateSteel, Item.legsSteel, Item.bootsSteel, Item.helmetDiamond, Item.plateDiamond, Item.legsDiamond, Item.bootsDiamond, Item.helmetGold, Item.plateGold, Item.legsGold, Item.bootsGold};
		for(Item item : combat) {
			if(Item.itemsList[item.shiftedIndex]==null) continue;
			tabCombat.items.add(new ItemStack(item, 1, 0));
		}
		Item[] material = new Item[] {Item.coal, Item.diamond, Item.ingotIron, Item.ingotGold, Item.stick, Item.bowlEmpty, Item.silk, Item.feather, Item.gunpowder, Item.seeds, Item.wheat, Item.flint, Item.leather, Item.brick, Item.clay, Item.reed, Item.egg, Item.lightStoneDust, Item.dyePowder, Item.sugar, Item.pumpkinSeeds, Item.melonSeeds, Item.blazeRod, Item.goldNugget, Item.netherStalkSeeds};
		for(Item item : material) {
			if(Item.itemsList[item.shiftedIndex]==null) continue;
			if(item.shiftedIndex==Item.coal.shiftedIndex) {
				tabMaterial.items.add(new ItemStack(item, 1, 0));
				tabMaterial.items.add(new ItemStack(item, 1, 1));
				continue;
			}
			if(item.shiftedIndex==351) {
				for(int i = 0;i < 16;i++) {
					tabMaterial.items.add(new ItemStack(item, 1, i));
				}
				continue;
			}
			tabMaterial.items.add(new ItemStack(item, 1, 0));
		}
		Item[] brewing = new Item[] {Item.ghastTear, Item.potion, Item.glassBottle, Item.fermentedSpiderEye, Item.blazePowder, Item.magmaCream, Item.brewingStand, Item.cauldron, Item.speckledMelon};
		for(Item item : brewing) {
			if(Item.itemsList[item.shiftedIndex]==null) continue;
			if(item.shiftedIndex==Item.potion.shiftedIndex) {
				tabBrewing.items.add(new ItemStack(item, 1, 0));
				int[] meta = new int[] {8193,8194,81950,8196,819700,82000,8201,82020,820400};
				for(int i : meta) {
					boolean lv2 = true;
					boolean time = true;
					if(i>100000) {i/=100;time=false;}
					if(i>80000) {i/=10;lv2=false;}
					tabBrewing.items.add(new ItemStack(item, 1, i));
					if(lv2) tabBrewing.items.add(new ItemStack(item, 1, i+32));
					if(time) tabBrewing.items.add(new ItemStack(item, 1, i+64));
					tabBrewing.items.add(new ItemStack(item, 1, i+8192));
					if(lv2) tabBrewing.items.add(new ItemStack(item, 1, i+8192+32));
					if(time) tabBrewing.items.add(new ItemStack(item, 1, i+8192+64));
				}
				continue;
			}
			tabBrewing.items.add(new ItemStack(item, 1, 0));
		}

		//Other mods' items add to CreativeTab.
		HashMap<String, String> modsSources = new HashMap<String, String>();
		HashMap<String, Integer> modsTab = new HashMap<String, Integer>();
		for(ModContainer mc : Loader.getModList()) {
			modsSources.put(mc.getName().toLowerCase(), mc.getSource().getName());
			if(mc.getMetadata()!=null&&mc.getMetadata().parent!=null) {
				if(!mc.getMetadata().parent.equals("")) modsParent.put(mc.getName().toLowerCase(), mc.getMetadata().parent);
			}else {
				String file = "";
				String modClazz = "";
				for(String prop : ((BaseMod)mc.getMod()).getPriorities().split(";")) {
					if(prop.startsWith("after:")) {
						String modclazz = modsSources.get(prop.substring(6).toLowerCase());
						if(modclazz==null) continue;
						if(!file.equals("")&&!modclazz.equals(file)) {
							file = "";
							modClazz = "";
							break;
						}
						file = modclazz;
						modClazz = prop.substring(6);
					}
				}
				if(!file.equals("")&&!modsParent.containsKey(mc.getName().toLowerCase())) modsParent.put(mc.getName().toLowerCase(), modClazz);
			}
		}
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
		for(Block block : Block.blocksList) {
			if(block!=null) {
				ArrayList<ItemStack> fake = new ArrayList<ItemStack>();
				block.addCreativeItems(fake);
				if(fake.size() == 0 && mod_CreativeGui.addAllBlocks) {
					int maxMeta = 0;
					Item item = Item.itemsList[block.blockID];
					System.out.println(item);
					if(item == null) continue;
					String firstName = item.getItemNameIS(new ItemStack(item, 1, 0));
					String prevName = null;
					for(int i = 0;i < 16;i++) {
						ItemStack stack = new ItemStack(item, 1, i);
						try {
							String displayName = item.getItemDisplayName(stack);
							if(displayName == null || displayName.isEmpty() || displayName.equals("Unnamed")) {
								continue;
							}
						}catch(ArrayIndexOutOfBoundsException ignore) {}

						String name = null;
						try {
							name = item.getItemNameIS(stack);
						}catch(ArrayIndexOutOfBoundsException ex) {
							break;
						}
						if(name == null) {
							break;
						}
						if(name.equals(prevName) || (i > 0 && firstName.equals(name))) {
							break;
						}
						prevName = name;
						fake.add(stack);
					}
				}
				stacks.addAll(fake);
			}
		}
		for(Item item : Item.itemsList) {
			if(item!=null&&!(item instanceof ItemBlock)) item.addCreativeItems(stacks);
		}
		mods:for(ItemStack item : stacks) {
			if(item==null) continue;
			String modName = mod_CreativeGui.itemMods.get(item.itemID);
			if(!modName.equals("Minecraft")) {
				if(mod_CreativeGui.addonMode) {
					String modparent = modsParent.get(modName.toLowerCase());
					if(modparent!=null) while(modsParent.get(modparent.toLowerCase())!=null) {
						modparent = modsParent.get(modparent.toLowerCase());
					}
					if(modparent!=null&&!modparent.equals("")) modName = modparent;
				}
				for(String line : IdDictionary) {
					CreativeTabs tab = null;
					String key = line.substring(0, line.indexOf("="));
					String value = line.substring(line.indexOf("=")+1);
					int tabId = 0;
					boolean dontAddTab = false;
					try {
						tabId = Integer.parseInt(value);
					}catch(NumberFormatException ignore) {continue;}
					if(tabId == -1) {
						tabId = 0;
						dontAddTab = true;
					}
					try {
						tab = tabs.get(tabId);
					}catch(Exception ignore) {
						continue;
					}
					if(key.endsWith("(start)")) {
						if(item.getItem().getItemNameIS(item)!=null&&item.getItem().getItemNameIS(item).startsWith(key.substring(0, key.length()-7))) {
							if(!dontAddTab) {
								tab.items.add(item);
							}
							continue mods;
						}
					}
					try {
						int id = Integer.parseInt(key);
						if(item.itemID==id) {
							if(!dontAddTab) {
								tab.items.add(item);
							}
							continue mods;
						}
					}catch(NumberFormatException e) {
						if(item.getItem().getItemNameIS(item)!=null&&item.getItem().getItemNameIS(item).equals(key)) {
							if(!dontAddTab) {
								tab.items.add(item);
							}
							continue mods;
						}
					}
				}
				if(!modName.startsWith("mod_")) modName = "mod_"+modName;
				if(modName!=null&&modsTab.get(modName)==null) {
					int id = tabs.size();
					new CreativeTabs(modName.substring(4), item);
					modsTab.put(modName, id);
				}
				if(modsTab.get(modName)!=null){
					tabs.get(modsTab.get(modName)).items.add(item);
					continue;
				}
			}
		}
		if(mod_CreativeGui.uniteTabs) {
			boolean CreateTab = false;
			Vector<CreativeTabs> removeTabs = new Vector<CreativeTabs>();
			for(CreativeTabs tab : tabs) {
				if(tab.tabId>10&&tab.items.size()<=mod_CreativeGui.uniteSize) {
					 for(ItemStack stack : tab.items) {
						 tabMods.items.add(stack.copy());
					 }
					removeTabs.add(tab);
				}
			}
			for(CreativeTabs tab : removeTabs) {
				tabs.remove(tab);
			}
		}else {
			tabs.remove(tabMods);
		}
		for(int i = 0;i < 10;i++) {
			Collections.sort(tabs.get(i).items, new Comparator<ItemStack>() {
				@Override
				public int compare(ItemStack o1, ItemStack o2) {
					return o1.itemID - o2.itemID;
				}

			});
		}
	}
}
