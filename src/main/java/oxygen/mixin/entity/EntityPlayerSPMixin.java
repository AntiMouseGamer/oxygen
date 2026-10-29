package oxygen.mixin.entity;

import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oxygen.Oxygen;
import oxygen.event.events.PreUpdateEvent;
import oxygen.manager.ModuleManager;
import oxygen.module.modules.movement.Sprint;

@Mixin(EntityPlayerSP.class)
public class EntityPlayerSPMixin {

	@Shadow
	protected int sprintToggleTimer;

	@Inject(
			method = "onUpdate",
			at = @At("HEAD")
	)
	public void onUpdate_callPreUpdateEvent(CallbackInfo ci) {
		Oxygen.EVENT_BUS.post(new PreUpdateEvent());
	}

	@Inject(
			method = "onLivingUpdate",
			at = @At("HEAD")
	)
	public void onLivingUpdate_disableWTap(CallbackInfo ci) {
		Sprint sprint = ModuleManager.getModule(Sprint.class);

		if (sprint.isEnabled() && sprint.disableWTap.value) {
			sprintToggleTimer = 0;
		}
	}
}
