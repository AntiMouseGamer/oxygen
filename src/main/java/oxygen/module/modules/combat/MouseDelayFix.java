package oxygen.module.modules.combat;

import org.lwjgl.input.Keyboard;
import oxygen.module.Module;
import oxygen.module.ModuleCategory;
import oxygen.setting.settings.ListSetting;

public class MouseDelayFix extends Module {
	public final ListSetting mode = new ListSetting(
			"Mode",
			"prplz",
			"prplz",
			"Mojang"
	);

	public MouseDelayFix() {
		super("MouseDelayFix", ModuleCategory.COMBAT, Keyboard.KEY_NONE);
	}
}
