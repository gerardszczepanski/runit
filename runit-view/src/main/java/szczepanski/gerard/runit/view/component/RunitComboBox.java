package szczepanski.gerard.runit.view.component;

import java.lang.reflect.Field;
import java.util.List;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.Skin;
import lombok.SneakyThrows;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.common.util.StringUtils;

public class RunitComboBox<T> extends ComboBox<T> {

    public String text() {
        return getEditor().getText();
    }

    public void applyNewOptions(List<T> options) {
        clearCurrentOptions();
        setNewOptions(options);
    }

    public void clearCurrentOptions() {
        this.getSelectionModel().clearSelection();
        this.reset();
        this.getItems().removeAll(this.getItems());
        this.itemsProperty().get().clear();
        this.valueProperty().set(null);
    }

    @SneakyThrows
    private void setNewOptions(List<T> options) {
        ObservableList<T> observableListOptions = FXCollections.observableArrayList(options);
        this.setItems(observableListOptions);
    }

    public void clearEditorAndOptions() {
        clearCurrentOptions();
        this.getEditor().clear();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    /**
     * lets do some dirty things here with reflection
     */
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

            if (this.getButtonCell() != null) {
                this.getButtonCell().setText("");
                this.getButtonCell().setItem(null);
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RunitRuntimeException(ExceptionCode.UNEXPECTED_ERROR, e, new Object[]{});
        }
    }
}