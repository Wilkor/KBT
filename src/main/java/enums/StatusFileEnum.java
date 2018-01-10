package enums;

/**
 * @author Keila Lacerda
 *
 */
public enum StatusFileEnum {
	
	TO_PROCESS("toProcess", "C/kb/"),
	PROCESSING("processing", "C:/kb/Processando/"),
	PROCESSED("processed", "C:/kb/Processados/"),
	REJECTED("rejected", "C:/kb/Rejeitados/");

	private String status;
	private String pathFile;
	
	/**
	 * @param status
	 * @param pathFile
	 */
	StatusFileEnum(String status, String pathFile){
		this.status = status;
		this.pathFile = pathFile;
	}

	public String getStatus() {
		return status;
	}

	public String getPathFile() {
		return pathFile;
	}
	
}
