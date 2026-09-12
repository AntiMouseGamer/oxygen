package oxygen.module;

public enum ModuleCategory {
	COMBAT("Combat"),
	MOVEMENT("Movement"),
	RENDER("Render");

	public final String categoryName;

	ModuleCategory(String categoryName) {
		this.categoryName = categoryName;
	}
}
