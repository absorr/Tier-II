package absorr.tier2.base;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid="TierII", name="Tier II", version="Build 001")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class TierII 
{
	@SidedProxy(clientSide = "absorr.tier2.base.ClientProxy", serverSide = "absorr.morecrafts.base.CommonProxy", bukkitSide = "absorr.morecrafts.base.CommonProxy")
	public static CommonProxy proxy;
	
	//Crafting Materials
	public static Block upgradeBlock;
	public static Item blankUpgrade;
	
	//Upgrades
	public static Item slotUpgrade;
	public static Item speedUpgrade;
	public static Item strengthUpgrade;
	
	//Tier II Tile Entities
	public static Block furnaceTwo;
	public static Block brewingTwo;
	public static Block dispenserTwo;
	public static Block jukeboxTwo;
	public static Block noteBlockTwo;
	public static Block enderChestTwo;
	public static Block enchantingTwo;
	
	@Init
    public void load(FMLInitializationEvent event)
    {
		
    }
	
	public void loadMaterials()
	{
		
	}
	
	public String getVersion()
    {
        return "0.1.0.01";
    }
}
