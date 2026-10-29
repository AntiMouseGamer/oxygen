package oxygen.module.modules.movement;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import oxygen.event.events.PostInputEvent;
import oxygen.event.events.PreTickEvent;
import oxygen.event.events.RenderTickEvent;
import oxygen.module.Module;
import oxygen.module.ModuleCategory;
import oxygen.setting.settings.BooleanSetting;
import oxygen.setting.settings.ListSetting;

public class Sprint extends Module {
	public final ListSetting type = new ListSetting(
			"Type",
			"Keybind",
			"Keybind",
			"Overwrite",
			"Direct"
	);

	private final ListSetting eventType = new ListSetting(
			"Event Type",
			"RenderTick",
			() -> type.value.equals("Keybind"),
			"Tick",
			"RenderTick"
	);

	public final BooleanSetting forwardOnly = new BooleanSetting(
			"Forward Only",
			false
	);

	public final BooleanSetting disableWTap = new BooleanSetting(
			"Disable WTap",
			false
	);

	public Sprint() {
		super("Sprint", ModuleCategory.MOVEMENT, Keyboard.KEY_NONE);
	}

	@Override
	public void onDisable() {
		if (mc.thePlayer.isSprinting()) {
			mc.thePlayer.setSprinting(false);
		}

		if (mc.gameSettings.keyBindSprint.isKeyDown()) {
			KeyBinding.setKeyBindState(
					mc.gameSettings.keyBindSprint.getKeyCode(),
					false
			);
		}
	}

	@EventLink
	private final Listener<PostInputEvent> onPostInput = (event) -> {
		if (type.value.equals("Direct")) {
			mc.thePlayer.setSprinting(
					!forwardOnly.value || mc.thePlayer.movementInput.moveForward > 0
			);
		}
	};

	@EventLink
	private final Listener<RenderTickEvent> onRenderTick = (event) -> {
		if (type.value.equals("Keybind")) {
			if (mc.theWorld != null && mc.thePlayer != null) {
				if (eventType.value.equals("RenderTick")) {
					KeyBinding.setKeyBindState(
							mc.gameSettings.keyBindSprint.getKeyCode(),
							!forwardOnly.value || mc.thePlayer.movementInput.moveForward > 0
					);
				}
			}
		}
	};

	@EventLink
	private final Listener<PreTickEvent> onPreTick = (event) -> {
		if (type.value.equals("Keybind")) {
			if (eventType.value.equals("Tick")) {
				if (mc.theWorld != null && mc.thePlayer != null) {
					KeyBinding.setKeyBindState(
							mc.gameSettings.keyBindSprint.getKeyCode(),
							!forwardOnly.value || mc.thePlayer.movementInput.moveForward > 0
					);
				}
			}
		}
	};
}
