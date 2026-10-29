package oxygen.mixin.entity;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import oxygen.manager.ModuleManager;
import oxygen.module.modules.combat.MouseDelayFix;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity {
	public EntityLivingBaseMixin(World worldIn) {
		super(worldIn);
	}

	@Inject(
			method = "getLook",
			at = @At("HEAD"),
			cancellable = true
	)
	public void getLook_fixMouseDelay(float partialTicks, CallbackInfoReturnable<Vec3> cir) {
		MouseDelayFix mouseDelayFix = ModuleManager.getModule(MouseDelayFix.class);

		if ((Object) this instanceof EntityPlayerSP) {
			if (mouseDelayFix.isEnabled()) {
				if (mouseDelayFix.mode.value.equals("prplz")) {
					cir.setReturnValue(super.getLook(partialTicks));
				} else {
					cir.setReturnValue(getVectorForRotation(rotationPitch, rotationYaw));
				}
			}
		}
	}
}
