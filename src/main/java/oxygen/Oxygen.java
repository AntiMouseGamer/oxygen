package oxygen;

import oxygen.event.IEvent;
import oxygen.manager.ModuleManager;
import io.github.nevalackin.homoBus.bus.impl.EventBus;

public class Oxygen {
	public static final EventBus<IEvent> EVENT_BUS = new EventBus<>();

	public static void init() {
		ModuleManager.init();
	}
}
