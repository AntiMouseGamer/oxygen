package oxygen.module.modules.render;

import org.lwjgl.input.Keyboard;
import oxygen.module.Module;
import oxygen.module.ModuleCategory;
import oxygen.setting.settings.DoubleSetting;
import oxygen.setting.settings.IntegerSetting;
import oxygen.setting.settings.ListSetting;

public class AspectRatio extends Module {
	public final ListSetting aspectType = new ListSetting(
			"Aspect Type",
			"Ratio",
			"Ratio",
			"Resolution"
	);

	public final DoubleSetting ratio = new DoubleSetting(
			"Ratio",
			1.2,
			0.01,
			5.0,
			0.01,
			() -> aspectType.value.equals("Ratio")
	);

	public final IntegerSetting width = new IntegerSetting(
			"Width",
			1280,
			1,
			2000,
			1,
			() -> aspectType.value.equals("Resolution")
	);

	public final IntegerSetting height = new IntegerSetting(
			"Height",
			1024,
			1,
			2000,
			1,
			() -> aspectType.value.equals("Resolution")
	);

	public AspectRatio() {
		super("AspectRatio", ModuleCategory.RENDER, Keyboard.KEY_NONE);
	}
}
