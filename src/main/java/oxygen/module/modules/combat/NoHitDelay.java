package oxygen.module.modules.combat;

import org.lwjgl.input.Keyboard;
import oxygen.module.Module;
import oxygen.module.ModuleCategory;
import oxygen.setting.settings.ListSetting;

public class NoHitDelay extends Module {
	public final ListSetting mode = new ListSetting(
			"Mode",
			"Partial",
			"Partial",
			"Full"
	);

	public NoHitDelay() {
		super("NoHitDelay", ModuleCategory.COMBAT, Keyboard.KEY_NONE);
	}
}
