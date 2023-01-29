package szczepanski.gerard.runit.view.component;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Region;

public class RunitComboBox<T> extends ComboBox<T> {

    Region arrowBtn;

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        if (arrowBtn == null) {
            arrowBtn = (Region) lookup(".arrow-button");
            arrowBtn.setMaxSize(0, 0);
            arrowBtn.setMinSize(0, 0);
            arrowBtn.setPadding(new Insets(0));

            Region arrow = (Region) lookup(".arrow");
            arrow.setMaxSize(0, 0);
            arrow.setMinSize(0, 0);
            arrow.setPadding(new Insets(0));

            // Call again the super method to relayout with the new bounds.
            super.layoutChildren();
        }
    }
}