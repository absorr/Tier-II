package com.absorr.tier2.base;

import com.absorr.tier2.materials.BlockFurnaceII;
import com.absorr.tier2.materials.TileEntityFurnaceII;
import com.absorr.tier2.ui.GuiHandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.world.World;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid="TierII", name="Tier II", version="Build 001")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class TierII 
{
	@SidedProxy(clientSide = "absorr.tier2.base.ClientProxy", serverSide = "absorr.tier2.base.CommonProxy", bukkitSide = "absorr.tier2.base.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance
	public static TierII instance;
	
	//Crafting Materials
	public static Block upgradeBlock;
	public static Item blankUpgrade;
	
	//Upgrades
	public static Item slotUpgrade;
	public static Item speedUpgrade;
	public static Item strengthUpgrade;
	
	//Adapters
	public static Item adapterFuel;
	
	//Tier II Tile Entities
	public static Block furnaceTwo;
	public static Block brewingTwo;
	public static Block dispenserTwo;
	public static Block jukeboxTwo;
	public static Block noteBlockTwo;
	public static Block enderChestTwo;
	public static Block enchantingTwo;
	
	//Creative Tab
	public static CreativeTabs tabTierII;
	
	public TierII(){}
	
	@Init
    public void load(FMLInitializationEvent event)
    {
		Config.loadConfiguration();
		proxy.registerRenderers();
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		GameRegistry.registerTileEntity(TileEntityFurnaceII.class, "Furnace II");
		
		tabTierII = new TabTierII(20,"Tier II");
		
		upgradeBlock = new Block(Config.baseBlockID, 0, Material.iron).setBlockName("upgradeBlock").setTextureFile(proxy.blockPic).setCreativeTab(tabTierII);
		blankUpgrade = new Item(Config.baseItemID).setItemName("blankUpgrade").setIconIndex(0).setTextureFile(proxy.itemPic).setCreativeTab(tabTierII);
		furnaceTwo = new BlockFurnaceII(Config.furnace2ID, 0).setBlockName("furnaceII").setTextureFile(proxy.blockPic).setCreativeTab(tabTierII);
		slotUpgrade = new Item(Config.slotUpgradeID).setItemName("slotUpgrade").setIconIndex(1).setTextureFile(proxy.itemPic).setCreativeTab(tabTierII);
		speedUpgrade = new Item(Config.speedUpgradeID).setItemName("speedUpgrade").setIconIndex(2).setTextureFile(proxy.itemPic).setCreativeTab(tabTierII);
		strengthUpgrade = new Item(Config.strengthUpgradeID).setItemName("strengthUpgrade").setIconIndex(3).setTextureFile(proxy.itemPic).setCreativeTab(tabTierII);
		adapterFuel = new Item(Config.fuelAdapterID).setItemName("fuelAdapter").setIconIndex(4).setTextureFile(proxy.itemPic).setCreativeTab(tabTierII);
		
		loadMaterials();
    }
	
	/*
	 * Registers the names and recipes for the blocks and items
	 */
	public void loadMaterials()
	{
		//Upgrading Block
		LanguageRegistry.addName(upgradeBlock, "Upgrading Block");
		ModLoader.registerBlock(upgradeBlock);
		GameRegistry.addRecipe(new ItemStack(upgradeBlock), new Object[] {"RLR", "LGL", "RLR", 'R', Item.redstone, 'L', new ItemStack(Item.dyePowder, 1, 4), 'G', Block.blockGold});
		
		//Furnace II
		LanguageRegistry.addName(furnaceTwo, "Furnace II");
		ModLoader.registerBlock(furnaceTwo);
		GameRegistry.addRecipe(new ItemStack(furnaceTwo), new Object[] {"RGR", "SFS", "SUS", 'R', Item.redstone, 'S', Block.stone, 'G', Item.lightStoneDust, 'F', Block.stoneOvenIdle, 'U', upgradeBlock});
		
		//Brewing Stand II
		
		//Dispenser II
		
		//Jukebox II
		
		//Noteblock II
		
		//Ender Chest II
		
		//Enchanting Table II
		
		//Blank Upgrade
		LanguageRegistry.addName(blankUpgrade, "Blank Upgrade");
		GameRegistry.addRecipe(new ItemStack(blankUpgrade), new Object[] {"SSS", "SIS", "SSS", 'S', Block.stone, 'I', Item.ingotIron});
		
		//Slot Upgrade
		LanguageRegistry.addName(slotUpgrade, "Slot Upgrade");

		//Speed Upgrade
		LanguageRegistry.addName(speedUpgrade, "Speed Upgrade");

		//Strength Upgrade
		LanguageRegistry.addName(strengthUpgrade, "Strength Upgrade");

		//Fuel Adapter
		LanguageRegistry.addName(adapterFuel, "Fuel Adapter");
	}
	
	public String getVersion()
    {
        return "0.0.0.01";
    }
}
