package oxygen.manager;

import oxygen.module.Module;
import oxygen.module.ModuleCategory;
import oxygen.module.modules.combat.MouseDelayFix;
import oxygen.module.modules.combat.NoHitDelay;
import oxygen.module.modules.combat.NoMissClick;
import oxygen.module.modules.movement.Sprint;
import oxygen.module.modules.render.AspectRatio;
import oxygen.module.modules.render.ClickGUIModule;
import oxygen.setting.Setting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ModuleManager {
	public static final ArrayList<Module> MODULES = new ArrayList<>();

	public static void init() {
		MODULES.addAll(Arrays.asList(
				// combat modules
				new NoHitDelay(),
				new NoMissClick(),
				new MouseDelayFix(),

				// movement modules
				new Sprint(),

				// render modules
				new ClickGUIModule(),
				new AspectRatio()
		));

		MODULES.sort((m1, m2) -> m1.name.compareToIgnoreCase(m2.name));

		for (Module module : MODULES) {
			for (Field field : module.getClass().getDeclaredFields()) {
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}

				try {
					Object object = field.get(module);

					if (object instanceof Setting<?>) {
						module.registerSettings((Setting<?>) object);
					}
				} catch (Exception ignored) {
				}
			}
		}
	}

	public static ArrayList<Module> getModulesFromCategory(ModuleCategory moduleCategory) {
		return (ArrayList<Module>) MODULES.parallelStream()
				.filter(module -> module.category == moduleCategory)
				.collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	public static <T extends Module> T getModule(Class<T> moduleClass) {
		for (Module module : MODULES) {
			if (module.getClass() == moduleClass) {
				return (T) module;
			}
		}

		return (T) new Module(null, null, 0);
	}
}
