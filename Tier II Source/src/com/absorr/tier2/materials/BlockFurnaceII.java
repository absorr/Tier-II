package com.absorr.tier2.materials;

import java.util.Random;

import com.absorr.tier2.base.TierII;


import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

public class BlockFurnaceII extends BlockContainer
{

	public BlockFurnaceII(int par1, int par2)
	{
		super(par1, Material.iron);
		this.blockIndexInTexture = par2;
	}
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
    {
		return this.blockID;
    }
	
	/**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityFurnaceII();
    }
    
    @Override
    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
    	if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityFurnaceII var10 = (TileEntityFurnaceII)par1World.getBlockTileEntity(par2, par3, par4);

            if (var10 != null)
            {
                par5EntityPlayer.openGui(TierII.instance, 0, par1World, par2, par3, par4);
            }

            return true;
        }
    }
    
    //TODO: Break block and keep contents
}
