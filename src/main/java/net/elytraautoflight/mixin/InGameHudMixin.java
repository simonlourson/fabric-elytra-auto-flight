package net.elytraautoflight.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.elytraautoflight.ElytraAutoFlight;
import net.elytraautoflight.GraphDataPoint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {


	MinecraftClient minecraftClient;
	ElytraAutoFlight elytraAutoFlight;

	@Inject(at = @At(value = "RETURN"), method = "render")
	public void renderPost(MatrixStack matrixStack, float f, CallbackInfo ci) {
		if (!ci.isCancelled()) {

			if (minecraftClient == null) minecraftClient = MinecraftClient.getInstance();
			if (elytraAutoFlight == null) elytraAutoFlight = ElytraAutoFlight.instance;

			if (elytraAutoFlight.drownedList != null) {
				float drownedX = elytraAutoFlight.config.guiX;
				float drownedY = elytraAutoFlight.config.guiY;
				for (DrownedEntity drowned : elytraAutoFlight.drownedList) {

					int color = 0xFFFFFF;
					if ("trident".equals(drowned.getEquippedStack(EquipmentSlot.MAINHAND).getItem().toString())) {
						color = 0xFFFFFF;
						minecraftClient.textRenderer.drawWithShadow(matrixStack, elytraAutoFlight.getDrownedString(drowned), drownedX, drownedY, color);

						drownedY += minecraftClient.textRenderer.fontHeight + 1;
					}


				}
			}

			if (elytraAutoFlight.afkProtection) {
				minecraftClient.textRenderer.drawWithShadow(matrixStack, new TranslatableText("text.elytraautoflight.afkProtection"), elytraAutoFlight.config.guiX, elytraAutoFlight.config.guiY, 0xFFFFFF);
			}

			if (elytraAutoFlight.showHud) {

				if (elytraAutoFlight.hudString != null) {
					float stringX = elytraAutoFlight.config.guiX;
					float stringY = elytraAutoFlight.config.guiY + elytraAutoFlight.config.guiHeight + 2;

					for (int i = 0; i < elytraAutoFlight.hudString.length; i++) {
						minecraftClient.textRenderer.drawWithShadow(matrixStack, elytraAutoFlight.hudString[i], stringX, stringY, 0xFFFFFF);
						stringY += minecraftClient.textRenderer.fontHeight + 1;

					}
				}

				if (elytraAutoFlight.config.showGraph) {

					DrawableHelper.fill(matrixStack, elytraAutoFlight.config.guiX, elytraAutoFlight.config.guiY, elytraAutoFlight.config.guiX + elytraAutoFlight.config.guiWidth, elytraAutoFlight.config.guiY + elytraAutoFlight.config.guiHeight, 0x44FFFFFF);

					double maxAltitude = 0;
					double minAltitude = 999;
					for (GraphDataPoint p : elytraAutoFlight.graph) {
						if (p.realPosition.y > maxAltitude) maxAltitude = p.realPosition.y;
						if (p.realPosition.y < minAltitude) minAltitude = p.realPosition.y;
					}

					if (maxAltitude > 0) {

						maxAltitude += 5;
						minAltitude -= 40;

						beginDrawLineColor();

						double currentX = 0;
						double currentY;
						for (GraphDataPoint p : elytraAutoFlight.graph) {

							currentY = ((p.realPosition.y - minAltitude) * elytraAutoFlight.config.guiHeight / (maxAltitude - minAltitude));

							double screenX = elytraAutoFlight.config.guiX + currentX;
							double screenY = elytraAutoFlight.config.guiY + elytraAutoFlight.config.guiHeight - currentY;

							float speedRatio = (float) p.velocity / 3f;

							float r = 2 * (1 - speedRatio);
							float g = 2 * speedRatio;

							if (r > 1) r = 1;
							if (g > 1) g = 1;
							if (r < 0) r = 0;
							if (g < 0) g = 0;

							addLinePointColor(screenX, screenY, 0, 1, r, g, 0);

							currentX += p.horizontalDelta * (elytraAutoFlight.config.guiWidth - 1) / elytraAutoFlight.config.guiGraphRealWidth;
						}

						endDrawLine();


						beginDrawLine(0xFF000000);
						addLinePoint(elytraAutoFlight.config.guiX, elytraAutoFlight.config.guiY, 0);
						addLinePoint(elytraAutoFlight.config.guiX, elytraAutoFlight.config.guiY + elytraAutoFlight.config.guiHeight, 0);
						addLinePoint(elytraAutoFlight.config.guiX + elytraAutoFlight.config.guiWidth, elytraAutoFlight.config.guiY + elytraAutoFlight.config.guiHeight, 0);
						addLinePoint(elytraAutoFlight.config.guiX + elytraAutoFlight.config.guiWidth, elytraAutoFlight.config.guiY, 0);
						addLinePoint(elytraAutoFlight.config.guiX, elytraAutoFlight.config.guiY, 0);
						endDrawLine();


					}
				}
			}


		}
	}

	private Tessellator tessellator_1;
	private BufferBuilder bufferBuilder_1;
	private void beginDrawLine(int color)
	{
		float float_1 = (float)(color >> 24 & 255) / 255.0F;
		float float_2 = (float)(color >> 16 & 255) / 255.0F;
		float float_3 = (float)(color >> 8 & 255) / 255.0F;
		float float_4 = (float)(color & 255) / 255.0F;

		tessellator_1 = Tessellator.getInstance();
		//bufferBuilder_1 = tessellator_1.getBufferBuilder();
		bufferBuilder_1 = tessellator_1.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture();
		//GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.color4f(float_2, float_3, float_4, float_1);
		bufferBuilder_1.begin(3, VertexFormats.POSITION);
	}

	private void beginDrawLineColor()
	{
		tessellator_1 = Tessellator.getInstance();
		bufferBuilder_1 = tessellator_1.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture();
		//GlStateManager.blendFuncSeparate(GlStateManager.DestFactor.SRC.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.blendColor(1, 1, 1, 1);
		bufferBuilder_1.begin(3, VertexFormats.POSITION_COLOR);
	}

	private void addLinePoint(double x, double y, double z)
	{
		bufferBuilder_1.vertex(x, y, z).next();
	}

	private void addLinePointColor(double x, double y, double z, float a, float r, float g, float b)
	{
		bufferBuilder_1.vertex(x, y, z).color(r, g, b, a).next();
	}

	private void endDrawLine()
	{
		tessellator_1.draw();
		GlStateManager.enableTexture();
		GlStateManager.disableBlend();
	}

}
