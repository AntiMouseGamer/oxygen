package oxygen.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import oxygen.manager.ModuleManager;
import oxygen.module.modules.movement.Sprint;

@Mixin(KeyBinding.class)
public class KeybindingMixin {

	@Inject(
			method = "isKeyDown",
			at = @At("HEAD"),
			cancellable = true
	)
	public void isKeyDown_overwriteSprint(CallbackInfoReturnable<Boolean> cir) {
		Minecraft mc = Minecraft.getMinecraft();

		if (mc.theWorld != null && mc.thePlayer != null) {
			if ((Object) this == Minecraft.getMinecraft().gameSettings.keyBindSprint) {
				Sprint sprint = ModuleManager.getModule(Sprint.class);

				if (sprint.isEnabled()) {
					if (sprint.type.value.equals("Overwrite")) {
						cir.setReturnValue(
								!sprint.forwardOnly.value ||
								mc.thePlayer.movementInput.moveForward > 0
						);
					}
				}
			}
		}
	}
}
