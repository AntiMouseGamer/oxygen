package oxygen.module.modules.render;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import org.lwjgl.input.Keyboard;
import oxygen.event.events.PreUpdateEvent;
import oxygen.module.Module;
import oxygen.module.ModuleCategory;
import oxygen.ui.ClickGUI;

public class ClickGUIModule extends Module {
	private ClickGUI clickGUI;

	public ClickGUIModule() {
		super("ClickGUI", ModuleCategory.RENDER, Keyboard.KEY_RSHIFT);
	}

	@EventLink
	private final Listener<PreUpdateEvent> onPreUpdate = (event) -> {
		if (clickGUI == null) {
			clickGUI = new ClickGUI();
		}

		mc.displayGuiScreen(clickGUI);

		setEnabled(false);
	};
}
