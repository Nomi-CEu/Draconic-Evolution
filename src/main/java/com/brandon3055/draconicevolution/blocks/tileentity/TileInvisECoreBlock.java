package com.brandon3055.draconicevolution.blocks.tileentity;


import com.brandon3055.brandonscore.blocks.TileBCBase;
import com.brandon3055.brandonscore.lib.Vec3I;
import com.brandon3055.brandonscore.lib.datamanager.ManagedVec3I;
import com.brandon3055.draconicevolution.DEFeatures;
import com.brandon3055.draconicevolution.blocks.ParticleGenerator;
import com.brandon3055.draconicevolution.integration.funkylocomotion.IMovableStructure;
import com.brandon3055.draconicevolution.world.EnergyCoreStructure;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import scala.Int;

import java.util.Collections;

/**
 * Created by brandon3055 on 13/4/2016.
 */
public class TileInvisECoreBlock extends TileBCBase implements IMultiBlockPart, IMovableStructure {

    public final ManagedVec3I coreOffset = register("coreOffset", new ManagedVec3I(new Vec3I(0, -1, 0))).syncViaContainer().saveToTile().trigerUpdate().finish();
    public String blockName = "";
    public int metaData = 0;
    public boolean isDefault = true;


    //region IMultiBlock

    @Override
    public boolean isStructureValid() {
        return getController() != null && getController().isStructureValid();
    }

    @Override
    public IMultiBlockPart getController() {
        TileEntity tile = world.getTileEntity(getCorePos());
        if (tile instanceof IMultiBlockPart) {
            return (IMultiBlockPart) tile;
        }
        else {
            revert();
        }

        return null;
    }

    @Override
    public boolean validateStructure() {
        IMultiBlockPart master = getController();
        if (master == null) {
            revert();
            return false;
        }
        else return master.validateStructure();
    }

    //endregion

    public boolean onTileClicked(EntityPlayer player, IBlockState state) {
        IMultiBlockPart controller = getController();

        if (controller instanceof TileEnergyCoreStabilizer) {
            ((TileEnergyCoreStabilizer) controller).onTileClicked(world, pos, state, player);
        }
        else if (controller instanceof TileEnergyStorageCore) {
            ((TileEnergyStorageCore) controller).onStructureClicked(world, pos, state, player);
        }
        else if (controller instanceof TileEnergyPylon) {
            ((TileEnergyPylon) controller).isOutputMode.value = !((TileEnergyPylon) controller).isOutputMode.value;
        }

        return true;
    }

    public void revert() {
        if (blockName.equals("draconicevolution:particle_generator")) {
            world.setBlockState(pos, DEFeatures.particleGenerator.getDefaultState().withProperty(ParticleGenerator.TYPE, "stabilizer"));
            return;
        }
        Block block = Block.REGISTRY.getObject(new ResourceLocation(blockName));
        IBlockState state;
        if (!block.equals(Blocks.AIR)){
            if (!isDefault)
                state = block.getStateFromMeta(metaData);
            else
                state = block.getDefaultState();

            world.setBlockState(pos, state);
        }
        else {
            world.setBlockToAir(pos);
        }
    }

    public void setController(IMultiBlockPart controller) {
        coreOffset.vec = new Vec3I(pos.subtract(((TileEntity) controller).getPos()));
    }

    private BlockPos getCorePos() {
        return pos.add(-coreOffset.vec.x, -coreOffset.vec.y, -coreOffset.vec.z);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound compound = new NBTTagCompound();
        if (isDefault)
            compound.setString("BlockName", blockName);
        else
            compound.setString("BlockName", blockName + " " + metaData);

        coreOffset.toNBT(compound);
        return new SPacketUpdateTileEntity(pos, 0, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        String[] input = pkt.getNbtCompound().getString("BlockName").split(" ");
        if (input.length != 2){
            blockName = input[0];
            metaData = 0;
            isDefault = true;
        }
        else{
            blockName = input[0];
            metaData = Integer.parseInt(input[1]);
            isDefault = false;
        }
        coreOffset.fromNBT(pkt.getNbtCompound());
    }

    @Override
    public void writeExtraNBT(NBTTagCompound compound) {
        if (isDefault)
            compound.setString("BlockName", blockName);
        else
            compound.setString("BlockName", blockName + " " + metaData);
    }

    @Override
    public void readExtraNBT(NBTTagCompound compound) {
        String[] input = compound.getString("BlockName").split(" ");
        if (input.length != 2){
            blockName = input[0];
            metaData = 0;
            isDefault = true;
        }
        else{
            blockName = input[0];
            metaData = Integer.parseInt(input[1]);
            isDefault = false;
        }
    }

    @Override
    public Iterable<BlockPos> getBlocksForFrameMove() {
        IMultiBlockPart controller = getController();
        if (controller instanceof TileEnergyCoreStabilizer) {
            return ((TileEnergyCoreStabilizer) controller).getBlocksForFrameMove();
        }
        return Collections.emptyList();
    }

    @Override
    public EnumActionResult canMove() {
        IMultiBlockPart controller = getController();
        if (controller instanceof TileEnergyCoreStabilizer) {
            return ((TileEnergyCoreStabilizer) controller).canMove();
        }
        else if (blockName.equals("draconicevolution:particle_generator")) {
            return EnumActionResult.FAIL;
        }
        return EnumActionResult.SUCCESS;
    }
}
