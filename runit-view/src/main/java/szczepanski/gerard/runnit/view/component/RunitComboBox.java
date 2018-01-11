package szczepanski.gerard.runnit.view.component;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.Skin;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.common.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

public class RunitComboBox<T> extends ComboBox<T> {

    public void applyNewOptions(List<T> options) {
        clearCurrentOptions();
        setNewOptions(options);
    }

    public void clearCurrentOptions() {
        this.getItems().removeAll(this.getItems());
        this.itemsProperty().get().clear();
        this.reset();
    }

    private void setNewOptions(List<T> options) {
        ObservableList<T> observableListOptions = FXCollections.observableArrayList(options);
        this.setItems(observableListOptions);
    }

    public void clear() {
        clearCurrentOptions();
        this.getEditor().clear();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void reset() {
        Skin<?> skin = this.getSkin();
        if (skin == null) {
            return;
        }

        this.setValue(null);
        Field buttonCellField;
        try {
            buttonCellField = skin.getClass().getDeclaredField("buttonCell");
            buttonCellField.setAccessible(true);
            ListCell buttonCell = (ListCell) buttonCellField.get(skin);
            if (buttonCell != null) {
                StringProperty text = buttonCell.textProperty();
                text.set(StringUtils.EMPTY);
                buttonCell.setItem(null);
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RunitRuntimeException(ExceptionCode.UNEXPECTED_ERROR, e, new Object[]{});
        }
    }
}