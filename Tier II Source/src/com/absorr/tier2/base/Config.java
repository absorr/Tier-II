package com.absorr.tier2.base;

import java.io.File;

import net.minecraftforge.common.Configuration;

public class Config 
{
	static Configuration configuration = new Configuration(new File(new File(".").getAbsolutePath(), "config/TierII.cfg"));
	public static int baseBlockID;
	public static int furnace2ID;
	public static int baseItemID;
	public static int slotUpgradeID;
	public static int speedUpgradeID;
	public static int strengthUpgradeID;
	public static int fuelAdapterID;
	
    public static void loadConfiguration()
    {
            configuration.load();
            baseBlockID = Integer.parseInt(configuration.getBlock(Configuration.CATEGORY_BLOCK, "Upgrade_Block", 850).value);
            furnace2ID = Integer.parseInt(configuration.getBlock(Configuration.CATEGORY_BLOCK, "FurnaceII", 851).value);
            baseItemID = Integer.parseInt(configuration.getItem(Configuration.CATEGORY_ITEM, "Blank_Upgrade", 5200).value);
            slotUpgradeID = Integer.parseInt(configuration.getItem(Configuration.CATEGORY_ITEM, "Slot_Upgrade", 5201).value);
            speedUpgradeID = Integer.parseInt(configuration.getItem(Configuration.CATEGORY_ITEM, "Speed_Upgrade", 5202).value);
            strengthUpgradeID = Integer.parseInt(configuration.getItem(Configuration.CATEGORY_ITEM, "Strength_Upgrade", 5203).value);
            fuelAdapterID = Integer.parseInt(configuration.getItem(Configuration.CATEGORY_ITEM, "Fuel_Adapter", 5204).value);
            configuration.save();
    }
}
