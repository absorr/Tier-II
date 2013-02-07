package absorr.tier2.materials;

import absorr.tier2.base.TierII;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileEntityFurnaceII extends TileEntity implements IInventory, ISidedInventory
{
    /**
     * The ItemStacks that hold the items currently being used in the furnace
     * 0 = Adapter
     * 1 = Fuel Input
     * 2-4 = Input
     * 5-7 = Output
     * 8-10 = Upgrades
     */
    private ItemStack[] furnaceItemStacks = new ItemStack[11];

    /** The number of ticks that the furnace will keep burning */
    public int furnaceBurnTime = 0;

    /**
     * The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for
     */
    public int currentItemBurnTime = 0;

    /** The number of ticks that the current item has been cooking for */
    public int furnaceCookTime = 0;

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.furnaceItemStacks.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1)
    {
        return this.furnaceItemStacks[par1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.furnaceItemStacks[par1] != null)
        {
            ItemStack var3;

            if (this.furnaceItemStacks[par1].stackSize <= par2)
            {
                var3 = this.furnaceItemStacks[par1];
                this.furnaceItemStacks[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.furnaceItemStacks[par1].splitStack(par2);

                if (this.furnaceItemStacks[par1].stackSize == 0)
                {
                    this.furnaceItemStacks[par1] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.furnaceItemStacks[par1] != null)
        {
            ItemStack var2 = this.furnaceItemStacks[par1];
            this.furnaceItemStacks[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.furnaceItemStacks[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "container.furnace" + " II";
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.furnaceItemStacks.length)
            {
                this.furnaceItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        this.furnaceBurnTime = par1NBTTagCompound.getShort("BurnTime");
        this.furnaceCookTime = par1NBTTagCompound.getShort("CookTime");
        this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("BurnTime", (short)this.furnaceBurnTime);
        par1NBTTagCompound.setShort("CookTime", (short)this.furnaceCookTime);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.furnaceItemStacks.length; ++var3)
        {
            if (this.furnaceItemStacks[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.furnaceItemStacks[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", var2);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Returns an integer between 0 and the passed value representing how close the current item is to being completely
     * cooked
     */
    public int getCookProgressScaled(int par1)
    {
        return this.furnaceCookTime* par1 / 200;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Returns an integer between 0 and the passed value representing how much burn time is left on the current fuel
     * item, where 0 means that the item is exhausted and the passed value means that the item is fresh
     */
    public int getBurnTimeRemainingScaled(int par1)
    {
        if (this.currentItemBurnTime == 0)
        {
            this.currentItemBurnTime = 200;
        }

        return this.furnaceBurnTime * par1 / this.currentItemBurnTime;
    }

    /**
     * Returns true if the furnace is currently burning
     */
    public boolean isBurning()
    {
        return this.furnaceBurnTime > 0;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        boolean var1 = this.furnaceBurnTime > 0;
        boolean var2 = false;

        if (this.furnaceBurnTime > 0)
        {
            if (this.getTimeUpgradeBonus() == 0)
            	-- this.furnaceBurnTime;
            else
            {
            	this.furnaceBurnTime = this.furnaceBurnTime - (this.getTimeUpgradeBonus() * 2);
            }
        }

        if (!this.worldObj.isRemote)
        {
            if (this.furnaceBurnTime == 0 && this.canSmelt())
            {
                this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);

                if (this.furnaceBurnTime > 0)
                {
                    var2 = true;

                    if (this.furnaceItemStacks[1] != null)
                    {
                        --this.furnaceItemStacks[1].stackSize;

                        if (this.furnaceItemStacks[1].stackSize == 0)
                        {
                            this.furnaceItemStacks[1] = this.furnaceItemStacks[1].getItem().getContainerItemStack(furnaceItemStacks[1]);
                        }
                    }
                }
            }

            if (this.isBurning() && this.canSmelt())
            {
                this.furnaceCookTime = this.furnaceCookTime + 1 + this.getTimeUpgradeBonus();

                if (this.furnaceCookTime == 200)
                {
                    this.furnaceCookTime = 0;
                    this.smeltItem();
                    var2 = true;
                }
            }
            else
            {
                this.furnaceCookTime = 0;
            }

            if (var1 != this.furnaceBurnTime > 0)
            {
                var2 = true;
            }
        }

        if (var2)
        {
            this.onInventoryChanged();
        }
    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt()
    {
    	if (this.furnaceItemStacks[0] == null)
    		return false;
    	if (this.furnaceItemStacks[0].getItem() != TierII.adapterFuel)
            return false;
    	if (this.furnaceItemStacks[1] == null)
    	{
    		if (furnaceBurnTime > 0) return true;
    		else return false;
    	}
    	
    	boolean slot1;
    	boolean slot2;
    	boolean slot3;
        if (this.furnaceItemStacks[2] == null)
        {
            slot1 = false;
        }
        else
        {
            ItemStack var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[2]);
            if (var1 == null) return false;
            if (this.furnaceItemStacks[5] == null) return true;
            if (!this.furnaceItemStacks[5].isItemEqual(var1)) return false;
            int result = furnaceItemStacks[5].stackSize + var1.stackSize;
            slot1 =  (result <= getInventoryStackLimit() && result <= var1.getMaxStackSize());
        }
        
        if (this.furnaceItemStacks[3] == null)
        {
            slot2 = false;
        }
        else
        {
            ItemStack var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[3]);
            if (var1 == null) return false;
            if (this.furnaceItemStacks[6] == null) return true;
            if (!this.furnaceItemStacks[6].isItemEqual(var1)) return false;
            int result = furnaceItemStacks[6].stackSize + var1.stackSize;
            slot2 =  (result <= getInventoryStackLimit() && result <= var1.getMaxStackSize());
        }
        
        if (this.furnaceItemStacks[4] == null)
        {
            slot3 = false;
        }
        else
        {
            ItemStack var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[4]);
            if (var1 == null) return false;
            if (this.furnaceItemStacks[7] == null) return true;
            if (!this.furnaceItemStacks[7].isItemEqual(var1)) return false;
            int result = furnaceItemStacks[7].stackSize + var1.stackSize;
            slot3 =  (result <= getInventoryStackLimit() && result <= var1.getMaxStackSize());
        }
        
        return (slot1 || slot2 || slot3);
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack var1;
            var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[2]);

            if (this.furnaceItemStacks[5] == null)
            {
                this.furnaceItemStacks[5] = var1.copy();

                --this.furnaceItemStacks[2].stackSize;

                if (this.furnaceItemStacks[2].stackSize <= 0)
                {
                    this.furnaceItemStacks[2] = null;
                }
            }
            else if (this.furnaceItemStacks[5].isItemEqual(var1))
            {
                furnaceItemStacks[5].stackSize += var1.stackSize;

                --this.furnaceItemStacks[2].stackSize;

                if (this.furnaceItemStacks[2].stackSize <= 0)
                {
                    this.furnaceItemStacks[2] = null;
                }
            }
            
            if (this.getSlotUpgradeBonus() > 0)
            {
                var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[3]);

                if (this.furnaceItemStacks[6] == null)
                {
                    this.furnaceItemStacks[6] = var1.copy();

                    --this.furnaceItemStacks[3].stackSize;

                    if (this.furnaceItemStacks[3].stackSize <= 0)
                    {
                        this.furnaceItemStacks[3] = null;
                    }
                }
                else if (this.furnaceItemStacks[6].isItemEqual(var1))
                {
                    furnaceItemStacks[6].stackSize += var1.stackSize;

                    --this.furnaceItemStacks[3].stackSize;

                    if (this.furnaceItemStacks[3].stackSize <= 0)
                    {
                        this.furnaceItemStacks[3] = null;
                    }
                }
            }
            
            if (this.getSlotUpgradeBonus() > 1)
            {
                var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[4]);

                if (this.furnaceItemStacks[7] == null)
                {
                    this.furnaceItemStacks[7] = var1.copy();

                    --this.furnaceItemStacks[4].stackSize;

                    if (this.furnaceItemStacks[4].stackSize <= 0)
                    {
                        this.furnaceItemStacks[4] = null;
                    }
                }
                else if (this.furnaceItemStacks[7].isItemEqual(var1))
                {
                    furnaceItemStacks[7].stackSize += var1.stackSize;

                    --this.furnaceItemStacks[4].stackSize;

                    if (this.furnaceItemStacks[4].stackSize <= 0)
                    {
                        this.furnaceItemStacks[4] = null;
                    }
                }
            }
        }
    }

    /**
     * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
     * fuel
     */
    public int getItemBurnTime(ItemStack par0ItemStack)
    {
        if (par0ItemStack == null)
        {
            return 0;
        }
        else
        {
        	int returner = 0;
        	
            int var1 = par0ItemStack.getItem().shiftedIndex;
            Item var2 = par0ItemStack.getItem();

            if (par0ItemStack.getItem() instanceof ItemBlock && Block.blocksList[var1] != null)
            {
                Block var3 = Block.blocksList[var1];

                if (var3 == Block.woodSingleSlab)
                {
                    returner += 150;
                }

                if (var3.blockMaterial == Material.wood)
                {
                    returner += 300;
                }
            }

            if (var2 instanceof ItemTool && ((ItemTool) var2).getToolMaterialName().equals("WOOD")) returner += 200;
            if (var2 instanceof ItemSword && ((ItemSword) var2).func_77825_f().equals("WOOD")) returner += 200;
            if (var2 instanceof ItemHoe && ((ItemHoe) var2).func_77842_f().equals("WOOD")) returner += 200;
            if (var1 == Item.stick.shiftedIndex) returner += 100;
            if (var1 == Item.coal.shiftedIndex) returner += 1600;
            if (var1 == Item.bucketLava.shiftedIndex) returner += 20000;
            if (var1 == Block.sapling.blockID) returner += 100;
            if (var1 == Item.blazeRod.shiftedIndex) returner += 2400;
            returner += GameRegistry.getFuelValue(par0ItemStack);
            return returner + this.getFuelUpgradeBonus();
        }
    }

    /**
     * Return true if item is a fuel source (getItemBurnTime() > 0).
     */
    public boolean isItemFuel(ItemStack par0ItemStack)
    {
        return (getItemBurnTime(par0ItemStack) - this.getFuelUpgradeBonus()) > 0;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openChest() {}

    public void closeChest() {}

    @Override
    public int getStartInventorySide(ForgeDirection side)
    {
        if (side == ForgeDirection.DOWN) return 1;
        if (side == ForgeDirection.UP) return 2; 
        return 2;
    }

    @Override
    public int getSizeInventorySide(ForgeDirection side)
    {
    	if (side != ForgeDirection.DOWN) return 3;
    	else return 1;
    }

	@Override
	public void onInventoryChanged() {
		// TODO Auto-generated method stub
		
	}
	
	public int getFuelUpgradeBonus()
	{
		ItemStack slot1 = this.getStackInSlot(8);
		ItemStack slot2 = this.getStackInSlot(9);
		ItemStack slot3 = this.getStackInSlot(10);
		if (slot1 != null)
		if (slot1.getItem() == TierII.strengthUpgrade)
		{
			return slot1.stackSize * 200;
		}
		if (slot2 != null)
		if (slot2.getItem() == TierII.strengthUpgrade)
		{
			return slot2.stackSize * 200;
		}
		if (slot3 != null)
		if (slot3.getItem() == TierII.strengthUpgrade)
		{
			return slot3.stackSize * 200;
		}
		return 0;
	}
	
	public int getTimeUpgradeBonus()
	{
		ItemStack slot1 = this.getStackInSlot(8);
		ItemStack slot2 = this.getStackInSlot(9);
		ItemStack slot3 = this.getStackInSlot(10);
		if (slot1 != null)
		if (slot1.getItem() == TierII.speedUpgrade)
		{
			return slot1.stackSize;
		}
		if (slot2 != null)
		if (slot2.getItem() == TierII.speedUpgrade)
		{
			return slot2.stackSize;
		}
		if (slot3 != null)
		if (slot3.getItem() == TierII.speedUpgrade)
		{
			return slot3.stackSize;
		}
		return 0;
	}
	
	public int getSlotUpgradeBonus()
	{
		ItemStack slot1 = this.getStackInSlot(8);
		ItemStack slot2 = this.getStackInSlot(9);
		ItemStack slot3 = this.getStackInSlot(10);
		if (slot1 != null)
		if (slot1.getItem() == TierII.slotUpgrade)
		{
			return slot1.stackSize;
		}
		if (slot2 != null)
		if (slot2.getItem() == TierII.slotUpgrade)
		{
			return slot2.stackSize;
		}
		if (slot3 != null)
		if (slot3.getItem() == TierII.slotUpgrade)
		{
			return slot3.stackSize;
		}
		return 0;
	}
}