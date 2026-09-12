package oxygen.module.modules.combat;

import org.lwjgl.input.Keyboard;
import oxygen.module.Module;
import oxygen.module.ModuleCategory;

public class NoMissClick extends Module {
	public NoMissClick() {
		super("NoMissClick", ModuleCategory.COMBAT, Keyboard.KEY_NONE);
	}
}
