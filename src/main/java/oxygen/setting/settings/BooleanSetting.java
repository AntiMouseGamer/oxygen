package oxygen.setting.settings;

import oxygen.setting.Setting;
import oxygen.util.BooleanFunction;

public class BooleanSetting extends Setting<Boolean> {
	public BooleanSetting(String name, Boolean value) {
		super(name, value);
	}

	public BooleanSetting(
			String name,
			Boolean value,
			BooleanFunction showIf
	) {
		super(name, value, showIf);
	}
}
