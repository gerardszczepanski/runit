package szczepanski.gerard.runit.program.util;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runnit.view.tray.ProgramTrayManager;

/**
 * TrayKeyListener is being installed on key type action in main scene to listen
 * for program tray show/hide shortcut.
 * 
 * Shortcut is: ALT + Q.
 * 
 * I do not predict to make it changeable by user.
 * 
 * @author Gerard Szczepanski
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgramHotKeyListener implements NativeKeyListener {

	private static ProgramHotKeyListener instance;

	private static final int FIRST_HOT_KEY_MASK = NativeKeyEvent.ALT_MASK;
	private static final int SECOND_HOT_KEY = NativeKeyEvent.VC_Q;

	public static ProgramHotKeyListener getInstance() {
		if (instance == null) {
			instance = new ProgramHotKeyListener();
		}

		return instance;
	}

	public void registerProgramHotKey() {
		disableLogger();
		tryToRegisterGlobalScreen();
		GlobalScreen.addNativeKeyListener(this);
	}

	private void disableLogger() {
		LogManager.getLogManager().reset();
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
	}

	private void tryToRegisterGlobalScreen() {
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			throw new RunitRuntimeException(ExceptionCode.R_001);
		}
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if (clientPressedShortcut(e)) {
			ProgramTrayManager.triggerTrayAction();
		}
	}
	
	private boolean clientPressedShortcut(NativeKeyEvent e) {
		return e.getKeyCode() == SECOND_HOT_KEY && ((e.getModifiers() & FIRST_HOT_KEY_MASK) != 0);
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		// Empty impl
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		// Empty impl
	}

}
