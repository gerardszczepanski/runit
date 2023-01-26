package szczepanski.gerard.runit.view.component;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import javafx.scene.input.KeyCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
public class KeyboardSettings {

    public static List<KeyCode> getAllowedKeyCodes() {
        List<KeyCode> allowedKeyCodes = new ObjectArrayList<>();

        allowedKeyCodes.add(KeyCode.DIGIT0);
        allowedKeyCodes.add(KeyCode.DIGIT1);
        allowedKeyCodes.add(KeyCode.DIGIT2);
        allowedKeyCodes.add(KeyCode.DIGIT3);
        allowedKeyCodes.add(KeyCode.DIGIT4);
        allowedKeyCodes.add(KeyCode.DIGIT5);
        allowedKeyCodes.add(KeyCode.DIGIT6);
        allowedKeyCodes.add(KeyCode.DIGIT7);
        allowedKeyCodes.add(KeyCode.DIGIT8);
        allowedKeyCodes.add(KeyCode.DIGIT9);
        allowedKeyCodes.add(KeyCode.Q);
        allowedKeyCodes.add(KeyCode.W);
        allowedKeyCodes.add(KeyCode.E);
        allowedKeyCodes.add(KeyCode.R);
        allowedKeyCodes.add(KeyCode.T);
        allowedKeyCodes.add(KeyCode.Y);
        allowedKeyCodes.add(KeyCode.U);
        allowedKeyCodes.add(KeyCode.I);
        allowedKeyCodes.add(KeyCode.O);
        allowedKeyCodes.add(KeyCode.P);
        allowedKeyCodes.add(KeyCode.A);
        allowedKeyCodes.add(KeyCode.S);
        allowedKeyCodes.add(KeyCode.D);
        allowedKeyCodes.add(KeyCode.F);
        allowedKeyCodes.add(KeyCode.G);
        allowedKeyCodes.add(KeyCode.H);
        allowedKeyCodes.add(KeyCode.J);
        allowedKeyCodes.add(KeyCode.K);
        allowedKeyCodes.add(KeyCode.L);
        allowedKeyCodes.add(KeyCode.Z);
        allowedKeyCodes.add(KeyCode.X);
        allowedKeyCodes.add(KeyCode.C);
        allowedKeyCodes.add(KeyCode.V);
        allowedKeyCodes.add(KeyCode.B);
        allowedKeyCodes.add(KeyCode.N);
        allowedKeyCodes.add(KeyCode.M);
        allowedKeyCodes.add(KeyCode.SPACE);
        allowedKeyCodes.add(KeyCode.BACK_SPACE);

        return allowedKeyCodes;
    }

    public static List<KeyCode> getKeycodesIneligibleForOptionsClear() {
        List<KeyCode> allowedKeyCodes = new ObjectArrayList<>();

        allowedKeyCodes.add(KeyCode.ENTER);
        allowedKeyCodes.add(KeyCode.UP);
        allowedKeyCodes.add(KeyCode.DOWN);

        return allowedKeyCodes;
    }

}
