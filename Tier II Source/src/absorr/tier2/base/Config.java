package absorr.tier2.base;

import java.io.File;

import net.minecraftforge.common.Configuration;

public class Config 
{
	static Configuration configuration = new Configuration(new File(new File(".").getAbsolutePath(), "config/MoreCrafts.cfg"));
	public static int baseBlockID;
	public static int baseItemID;
    public static void loadConfiguration()
    {
            configuration.load();
            baseBlockID = Integer.parseInt(configuration.getBlock(Configuration.CATEGORY_BLOCK, "Upgrade_Block", 700).value);
            baseItemID = Integer.parseInt(configuration.getItem(Configuration.CATEGORY_ITEM, "Blank_Upgrade", 5200).value);
            configuration.save();
    }
}
