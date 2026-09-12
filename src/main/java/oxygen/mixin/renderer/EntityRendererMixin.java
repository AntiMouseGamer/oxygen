package oxygen.mixin.renderer;

import net.minecraft.client.renderer.EntityRenderer;
import org.lwjgl.util.glu.Project;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oxygen.Oxygen;
import oxygen.event.events.RenderTickEvent;
import oxygen.manager.ModuleManager;
import oxygen.module.modules.render.AspectRatio;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

	@Inject(
			method = "updateCameraAndRender",
			at = @At("TAIL")
	)
	public void updateCameraAndRender_callRenderTickEvent(
			float partialTicks,
			long nanoTime,
			CallbackInfo ci
	) {
		Oxygen.EVENT_BUS.post(new RenderTickEvent());
	}

	@Dynamic("OptiFine moment")
	@Redirect(
			method = {
					"renderHand(FI)V",
					"renderHand(FIZZZ)V"
			},
			at = @At(
					value = "INVOKE",
					target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"
			),
			remap = false
	)
	public void gluPerspective_changeHandAspect(float fovY, float aspect, float zNear, float zFar) {
		AspectRatio aspectRatio = ModuleManager.getModule(AspectRatio.class);

		if (aspectRatio.isEnabled()) {
			if (!aspectRatio.dontAffectHand.value) {
				if (aspectRatio.aspectType.value.equals("Ratio")) {
					aspect = aspectRatio.ratio.value.floatValue();
				} else {
					aspect = aspectRatio.width.value.floatValue() / aspectRatio.height.value.floatValue();
				}
			}
		}

		Project.gluPerspective(fovY, aspect, zNear, zFar);
	}

	@Redirect(
			method = {
					"setupCameraTransform",
					"renderCloudsCheck",
					"renderWorldPass"
			},
			at = @At(
					value = "INVOKE",
					target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"
			)
	)
	public void gluPerspective_changeAspect(float fovY, float aspect, float zNear, float zFar) {
		AspectRatio aspectRatio = ModuleManager.getModule(AspectRatio.class);

		if (aspectRatio.isEnabled()) {
			if (aspectRatio.aspectType.value.equals("Ratio")) {
				aspect = aspectRatio.ratio.value.floatValue();
			} else {
				aspect = aspectRatio.width.value.floatValue() / aspectRatio.height.value.floatValue();
			}
		}

		Project.gluPerspective(fovY, aspect, zNear, zFar);
	}
}
