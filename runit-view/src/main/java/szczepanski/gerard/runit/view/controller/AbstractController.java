package szczepanski.gerard.runit.view.controller;

import javafx.scene.Node;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import szczepanski.gerard.runit.common.exception.RunitValidationException;
import szczepanski.gerard.runit.view.util.DialogDisplayer;

public abstract class AbstractController {
    private static final Logger LOG = Logger.getLogger(AbstractController.class);

    protected Stage getStage(Node node) {
        return (Stage) node.getScene().getWindow();
    }

    protected void handleValidationException(RunitValidationException e) {
        LOG.debug(e.getDisplayMessage());
        DialogDisplayer.showValidationMessageDialog(e.getDisplayMessage());
    }

    protected boolean isSelectedIndexValid(int index) {
        return -1 < index;
    }

}
