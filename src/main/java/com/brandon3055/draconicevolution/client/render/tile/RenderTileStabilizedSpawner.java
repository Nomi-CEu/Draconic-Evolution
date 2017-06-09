package com.brandon3055.draconicevolution.client.render.tile;import codechicken.lib.texture.TextureUtils;import com.brandon3055.brandonscore.client.render.TESRBase;import com.brandon3055.brandonscore.utils.ModelUtils;import com.brandon3055.draconicevolution.DEFeatures;import com.brandon3055.draconicevolution.blocks.tileentity.TileStabilizedSpawner;import com.brandon3055.draconicevolution.helpers.ResourceHelperDE;import net.minecraft.client.Minecraft;import net.minecraft.client.renderer.GlStateManager;import net.minecraft.client.renderer.block.model.BakedQuad;import net.minecraft.client.renderer.block.model.IBakedModel;import net.minecraft.entity.Entity;import net.minecraft.item.ItemStack;import java.util.List;/** * Created by brandon3055 on 20/05/2016. */public class RenderTileStabilizedSpawner extends TESRBase<TileStabilizedSpawner> {    private static ItemStack[] CORE_RENDER_ITEMS = new ItemStack[] {new ItemStack(DEFeatures.draconicCore), new ItemStack(DEFeatures.wyvernCore), new ItemStack(DEFeatures.awakenedCore), new ItemStack(DEFeatures.chaoticCore)};    @Override    public void renderTileEntityAt(TileStabilizedSpawner te, double x, double y, double z, float partialTicks, int destroyStage) {        Entity entity = te.getRenderEntity();        GlStateManager.pushMatrix();        GlStateManager.translate((float)x + 0.5F, (float)y, (float)z + 0.5F);        if (entity != null)        {            GlStateManager.pushMatrix();            float f = 0.53125F;            float f1 = Math.max(entity.width, entity.height);            if ((double)f1 > 1.0D)            {                f /= f1;            }            GlStateManager.translate(0.0F, 0.4F, 0.0F);            GlStateManager.rotate((float)(te.mobRotation + (partialTicks * te.getRotationSpeed())) * 10.0F, 0.0F, 1.0F, 0.0F);            GlStateManager.translate(0.0F, -0.2F, 0.0F);            GlStateManager.rotate(-30.0F, 1.0F, 0.0F, 0.0F);            GlStateManager.scale(f, f, f);            entity.setLocationAndAngles(x, y, z, 0.0F, 0.0F);            Minecraft.getMinecraft().getRenderManager().doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);            GlStateManager.popMatrix();        }        ItemStack stack = CORE_RENDER_ITEMS[te.spawnerTier.value.ordinal()];        IBakedModel bakedModel = getStackModel(stack);        List<BakedQuad> quads = bakedModel.getQuads(null, null, 0);        GlStateManager.translate(-0.25, 1.225, -0.25);        GlStateManager.scale(0.5, 0.5, 0.5);        GlStateManager.rotate(90, 1, 0, 0);        TextureUtils.bindBlockTexture();        ModelUtils.renderQuads(quads);        setLighting(200);        renderEffect(quads);        resetLighting();        GlStateManager.translate(0, 0, 1.9);        ModelUtils.renderQuads(quads);        setLighting(200);        renderEffect(quads);        resetLighting();        GlStateManager.popMatrix();    }    public static void renderEffect(List<BakedQuad> quads) {        GlStateManager.enableBlend();        GlStateManager.depthMask(false);        GlStateManager.depthFunc(514);        GlStateManager.disableLighting();        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);        ResourceHelperDE.bindTexture(ResourceHelperDE.getResourceRAW("textures/misc/enchanted_item_glint.png"));        GlStateManager.matrixMode(5890);        GlStateManager.pushMatrix();        GlStateManager.scale(8.0F, 8.0F, 8.0F);        float f = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;        GlStateManager.translate(f, 0.0F, 0.0F);        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);        ModelUtils.renderQuadsARGB(quads, 0xFFAE9020);        GlStateManager.popMatrix();        GlStateManager.pushMatrix();        GlStateManager.scale(8.0F, 8.0F, 8.0F);        float f1 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;        GlStateManager.translate(-f1, 0.0F, 0.0F);        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);        ModelUtils.renderQuadsARGB(quads, 0xFFAE9020);        GlStateManager.popMatrix();        GlStateManager.matrixMode(5888);        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);        GlStateManager.enableLighting();        GlStateManager.depthFunc(515);        GlStateManager.depthMask(true);        TextureUtils.bindBlockTexture();        GlStateManager.disableBlend();    }}