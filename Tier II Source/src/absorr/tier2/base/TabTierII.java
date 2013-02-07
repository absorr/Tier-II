package absorr.tier2.base;

import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TabTierII extends CreativeTabs {
	public TabTierII(int par1, String par2Str)
	{
		super(par1, par2Str);
	}
	@SideOnly(Side.CLIENT)
	public int getTabIconItemIndex()
	{
		return TierII.furnaceTwo.blockID;
	}

	public String getTranslatedTabLabel()
	{
		return "TierII";
	}
}