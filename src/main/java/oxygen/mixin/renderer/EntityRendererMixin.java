package oxygen.mixin.renderer;

import net.minecraft.client.renderer.EntityRenderer;
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
import oxygen.util.GLUtil;

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
		float aspectX = aspect;
		float aspectY = 1;

		if (aspectRatio.isEnabled()) {
			if (!aspectRatio.dontAffectHand.value) {
				if (aspectRatio.aspectType.value.equals("Ratio")) {
					aspectX = aspectRatio.ratioX.value.floatValue();
					aspectY = aspectRatio.ratioY.value.floatValue();
				} else {
					aspectX = aspectRatio.widthX.value.floatValue() / aspectRatio.heightX.value.floatValue();
					aspectY = aspectRatio.widthY.value.floatValue() / aspectRatio.heightY.value.floatValue();
				}
			}
		}

		GLUtil.perspective(fovY, aspectX, aspectY, zNear, zFar);
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
	public void gluPerspective_changeWorldAspect(float fovY, float aspect, float zNear, float zFar) {
		AspectRatio aspectRatio = ModuleManager.getModule(AspectRatio.class);
		float aspectX = aspect;
		float aspectY = 1;

		// nice IntelliJ duplicated code solution
		if (aspectRatio.isEnabled()) {
			if (!aspectRatio.aspectType.value.equals("Ratio")) {
				aspectX = aspectRatio.widthX.value.floatValue() / aspectRatio.heightX.value.floatValue();
				aspectY = aspectRatio.widthY.value.floatValue() / aspectRatio.heightY.value.floatValue();
			} else {
				aspectX = aspectRatio.ratioX.value.floatValue();
				aspectY = aspectRatio.ratioY.value.floatValue();
			}
		}

		GLUtil.perspective(fovY, aspectX, aspectY, zNear, zFar);
	}
}
