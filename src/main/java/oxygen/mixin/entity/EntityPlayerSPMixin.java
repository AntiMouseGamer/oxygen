package oxygen.mixin.entity;

import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oxygen.Oxygen;
import oxygen.event.events.PreUpdateEvent;

@Mixin(EntityPlayerSP.class)
public class EntityPlayerSPMixin {

	@Inject(
			method = "onUpdate",
			at = @At("HEAD")
	)
	public void onUpdate_callPreUpdateEvent(CallbackInfo ci) {
		Oxygen.EVENT_BUS.post(new PreUpdateEvent());
	}
}
