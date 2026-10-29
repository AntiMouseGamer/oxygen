package oxygen.module.modules.render;

import org.lwjgl.input.Keyboard;
import oxygen.module.Module;
import oxygen.module.ModuleCategory;
import oxygen.setting.settings.BooleanSetting;
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

	public final DoubleSetting ratioX = new DoubleSetting(
			"Ratio X",
			1.2,
			0.01,
			5.0,
			0.01,
			() -> aspectType.value.equals("Ratio")
	);

	public final DoubleSetting ratioY = new DoubleSetting(
			"Ratio Y",
			1.0,
			0.01,
			5.0,
			0.01,
			() -> aspectType.value.equals("Ratio")
	);

	public final IntegerSetting widthX = new IntegerSetting(
			"Width X",
			1280,
			1,
			2000,
			1,
			() -> aspectType.value.equals("Resolution")
	);

	public final IntegerSetting heightX = new IntegerSetting(
			"Height X",
			1024,
			1,
			2000,
			1,
			() -> aspectType.value.equals("Resolution")
	);

	public final IntegerSetting widthY = new IntegerSetting(
			"Width Y",
			1,
			1,
			2000,
			1,
			() -> aspectType.value.equals("Resolution")
	);

	public final IntegerSetting heightY = new IntegerSetting(
			"Height Y",
			1,
			1,
			2000,
			1,
			() -> aspectType.value.equals("Resolution")
	);

	public final BooleanSetting dontAffectHand = new BooleanSetting(
			"Dont Affect Hand",
			false
	);

	public AspectRatio() {
		super("AspectRatio", ModuleCategory.RENDER, Keyboard.KEY_NONE);
	}
}
