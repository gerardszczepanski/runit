package szczepanski.gerard.runit.view.factory;

import javafx.fxml.FXMLLoader;
import org.apache.log4j.Logger;

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