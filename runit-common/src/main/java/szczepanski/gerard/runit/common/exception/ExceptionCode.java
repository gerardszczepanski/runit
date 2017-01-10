package szczepanski.gerard.runit.common.exception;

import lombok.Getter;

/**
 * This enum represents all RunitExceptions that can occur in Application.
 * Each exception registered here is being displayed for user.
 * 
 *  For error raporting.
 *  
 *  R_XXX -> for {@code RunitRuntimeExceptions}
 *  B_XXX -> for {@code RunitBusinessExceptions}
 *  
 * @author Gerard Szczepanski
 */
@Getter
public enum ExceptionCode {
	
	R_001("R-001", "Unable to register hot key listening at this machine"),
	R_002("R-002", "Unable to install tray icon at this machine"),
	R_003("R-003", "Unable to load tray image"),
	R_004("R-004", "SearchService is not configured at program runtime"),
	R_005("R-005", "App graphics can not be loaded"),
	R_006("R-006", "URI %s can not be browsed"),
	R_007("R-007", "SearchResult creation failure. Input File is empty"),
	R_008("R-008", "Can not read Settings"),
	R_009("R-009", "Not allowed Cache operation! Trying to add searchTerm: [%s] results that actually are stored in Cache!"),
	R_010("R-010", "App components are not ready to work"),
	R_011("R-011", "Can not save Settings"),
	
	B_001("B-001", "SearchResult creation failure. Alias is broken: alias [%s], value [%s]"),
	B_002("B-002", "URI can not be created from Web Address: %s. URI is broken."),
	B_003("B-003", "Cannot read files from root path %s. Maybe path is invaid?"),
	B_004("B-004", "This file can not be executed.");
	
	private String exceptionCode;
	private String exceptionTemplateMessage;
	
	private ExceptionCode(String exceptionCode, String exceptionTemplateMessage) {
		this.exceptionCode = exceptionCode;
		this.exceptionTemplateMessage = exceptionTemplateMessage;
	}
	
}
