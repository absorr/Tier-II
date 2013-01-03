package absorr.tier2.materials;

import java.util.Random;

import absorr.tier2.base.TierII;

import net.minecraft.block.BlockFurnace;

public class BlockFurnaceII extends BlockFurnace
{

	public BlockFurnaceII(int par1, int par2)
	{
		super(par1, false);
		this.blockIndexInTexture = par2;
	}
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
    {
		return this.blockID;
    }

}
