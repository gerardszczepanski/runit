package szczepanski.gerard.runnit.view.scene.factory;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.common.config.ProgramConfig;

@RequiredArgsConstructor
public class SettingsStagePresenter {
	
	private final SettingsSceneFactory settingsSceneFactory;
	
	private Stage settingsStage;
	
	public void showSettingsStageInNewWindow() {
		if (!isSettingsStageShowing()) {
			showSettingsStage();
		}
	}
	
	private boolean isSettingsStageShowing() {
		return settingsStage != null && settingsStage.isShowing();
	}
	
	private void showSettingsStage() {
		settingsStage = new Stage();
		Scene scene = settingsSceneFactory.createComponent();
		
		settingsStage.initModality(Modality.APPLICATION_MODAL);
		settingsStage.setScene(scene);
		settingsStage.setResizable(false);
		settingsStage.setTitle(ProgramConfig.PROGRAM_SETTINGS_TITLE);
		settingsStage.getIcons().add(new Image(SettingsStagePresenter.class.getResourceAsStream(ProgramConfig.PROGRAM_ICON_PATH)));
		
		settingsStage.show();
	}
	
}
