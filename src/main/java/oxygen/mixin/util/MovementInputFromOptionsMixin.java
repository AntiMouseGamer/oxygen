package oxygen.mixin.util;

import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oxygen.Oxygen;
import oxygen.event.events.PostInputEvent;

@Mixin(MovementInputFromOptions.class)
public class MovementInputFromOptionsMixin {

	@Inject(
			method = "updatePlayerMoveState",
			at = @At("TAIL")
	)
	public void updatePlayerMoveState_callPostInputEvent(CallbackInfo ci) {
		Oxygen.EVENT_BUS.post(new PostInputEvent());
	}
}
