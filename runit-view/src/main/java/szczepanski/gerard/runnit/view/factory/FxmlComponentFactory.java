package szczepanski.gerard.runnit.view.factory;

import org.apache.log4j.Logger;

import javafx.fxml.FXMLLoader;

public abstract class FxmlComponentFactory<T> {
	private static final Logger LOG = Logger.getLogger(FxmlComponentFactory.class);

	private final String fxmlPath;

	public FxmlComponentFactory(String fxmlPath) {
		this.fxmlPath = fxmlPath;
	}

	public final T createComponent() {
		LOG.debug("Create FxmlComponent");
		FXMLLoader loader = getLoader();
		return generateFxmlComponent(loader);
	}

	private final FXMLLoader getLoader() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(FxmlComponentFactory.class.getResource(fxmlPath));
		return loader;
	}

	protected abstract T generateFxmlComponent(FXMLLoader loader);

}