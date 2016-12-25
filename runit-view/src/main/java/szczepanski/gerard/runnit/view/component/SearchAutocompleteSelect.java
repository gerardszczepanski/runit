package szczepanski.gerard.runnit.view.component;

import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import lombok.Setter;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.service.result.SearchResult;
import szczepanski.gerard.runit.service.search.algorithm.SearchService;

/**
 * This is Autocomplete Select component responsible for handle typed text,
 * and display typed text results.
 * 
 * This object requires {@code SearchService} type object, to fire search and 
 * return result.
 * 
 * @author Gerard Szczepanski
 * 
 */
public class SearchAutocompleteSelect {
	
	private final TextField innerTextField;
	
	@Setter
	private SearchService searchService;
	
	/**
	 * Factory method for create SearchAutocomplete Select. 
	 * @param currentPane - pane for pin SearchAutocompleteSelect object after create.
	 * @param dimenstion - dimension for SearchAutocompleteSelect object.
	 * @param position - position for SearchAutocompleteSelect object. 
	 */
	public static SearchAutocompleteSelect createSearchAutocompleteSelect(Pane currentPane, Dimension dimenstion, Position position) {
		return new SearchAutocompleteSelect(currentPane, dimenstion, position);
	}
	
	private SearchAutocompleteSelect(Pane currentPane, Dimension dimenstion, Position position) {
		this.innerTextField = new TextField();
		initInnerTextField(currentPane, dimenstion, position);
	}
	
	private void initInnerTextField(Pane pane, Dimension dimenstion, Position position) {
		innerTextField.setPrefWidth(dimenstion.width);
		innerTextField.setPrefHeight(dimenstion.height);
		innerTextField.setLayoutX(position.x);
		innerTextField.setLayoutY(position.y);
		innerTextField.setFont(new Font("System", 14.0));
		
		innerTextField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
            	handleKeyType();
            }});
		pane.getChildren().add(innerTextField);
	}
	
	private void handleKeyType() {
		checkSearchService();
    	List<SearchResult> searchResults = fireSearch();
    	//TODO display search results as autocomplete
	}
	
	private List<SearchResult> fireSearch() {
		String searchTerm = innerTextField.getText();
		return searchService.searchFor(searchTerm);
	}
	
	private void checkSearchService() {
		if (searchService == null) {
			throw new RunitRuntimeException("Set SearchService before use SearchAutocompleteSelect object!");
		}
	}
	
}
