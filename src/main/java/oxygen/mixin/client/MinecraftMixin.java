package oxygen.mixin.client;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oxygen.Oxygen;
import oxygen.event.events.PreTickEvent;
import oxygen.manager.ModuleManager;
import oxygen.module.Module;
import oxygen.module.modules.combat.NoHitDelay;
import oxygen.module.modules.combat.NoMissClick;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Shadow
	private int leftClickCounter;

	@Shadow
	public EntityPlayerSP thePlayer;

	@Shadow
	@Final
	private static Logger logger;

	@Shadow
	public MovingObjectPosition objectMouseOver;

	@Shadow
	public PlayerControllerMP playerController;

	@Shadow
	public WorldClient theWorld;

	@Inject(
			method = "startGame",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;",
					shift = At.Shift.AFTER,
					opcode = Opcodes.PUTFIELD
			)
	)
	public void startGame_startOxygen(CallbackInfo ci) {
		Oxygen.init();
	}

	@Inject(
			method = "runTick",
			at = @At("HEAD")
	)
	public void runTick_callPreTickEvent(CallbackInfo ci) {
		Oxygen.EVENT_BUS.post(new PreTickEvent());
	}

	@Inject(
			method = "runTick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V",
					shift = At.Shift.AFTER
			)
	)
	public void runTick_keyListener(CallbackInfo ci) {
		int key = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();

		if (Keyboard.getEventKeyState()) {
			for (Module module : ModuleManager.MODULES) {
				if (key == module.keybind) {
					module.toggle();
				}
			}
		}
	}

	@Inject(
			method = "clickMouse",
			at = @At("HEAD"),
			cancellable = true
	)
	public void clickMouse_noHitDelay(CallbackInfo ci) {
		NoHitDelay noHitDelay = ModuleManager.getModule(NoHitDelay.class);

		if (ModuleManager.getModule(NoMissClick.class).isEnabled()) {
			if (objectMouseOver != null) {
				if (objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.MISS) {
					leftClickCounter = 0;
				}
			}
		}

		if (noHitDelay.isEnabled()) {
			if (noHitDelay.mode.value.equals("Partial")) {
				leftClickCounter = 0;
			} else {
				if (leftClickCounter <= 0) {
					thePlayer.swingItem();

					if (objectMouseOver == null) {
						logger.error("Null returned as 'hitResult', this shouldn't happen!");
					} else {
						switch (this.objectMouseOver.typeOfHit) {
							case ENTITY:
								playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);

								break;
							case BLOCK:
								BlockPos blockpos = this.objectMouseOver.getBlockPos();

								if (theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
									playerController.clickBlock(blockpos, this.objectMouseOver.sideHit);
									break;
								}
						}
					}
				}

				ci.cancel();
			}
		}
	}
}
