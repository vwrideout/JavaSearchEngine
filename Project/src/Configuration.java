import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The Configuration class parses a JSON file containing configuration information and provides wrapper methods
 * to access the configuration data in the JSON object.
 * @author Vincent Rideout
 *
 */
public class Configuration {

	/**
	 * Constants that store the keys used in the configuration file.
	 */
	public static final String INPUT_PATH = "inputPath";
	public static final String OUTPUT_PATH = "outputPath";
	public static final String DIGIT_DELIMITER = "digitDelimiter";	
	public static final String SEARCH_PATH = "searchPath";
	public static final String SEARCH_OUTPUT_PATH = "searchOutputPath";
	

	/**
	 * Instance variables to store shared information.
	 */
	private String inputPath;
	private String outputPath;
	private boolean digitDelimiter;
	private Path configPath;
	private String searchPath;
	private String searchOutputPath;
	
	/**
	 * Instantiates a Configuration object.
	 * @param path - the location of the file 			
	 */
	public Configuration(Path path) {
		this.configPath = path;
	}
	
	/**
	 * Initializes a Configuration object. Uses a JSONParser to parse the contents of the file. Hint:
	 * I used a helper method to validate the contents of the file once it was parsed. Note: you will need
	 * to implement your own exception class called InitializationException.
	 * @throws InitializationException - thrown in the following cases: (1) an IOException is generated when 
	 * 				accessing the file; (2) a ParseException is thrown when parsing the JSON contents of the file;
	 * 				(3) the file does not contain the inputPath key; (4) the file does not contain the digitDelimiter 
	 * 				key; (5) the digitDelimiter value is not a boolean.
	 */
	public void init() throws InitializationException {
		JSONObject jsonobject = null;
		JSONParser parser = new JSONParser();
		try (BufferedReader in = Files.newBufferedReader(configPath, Charset.forName("UTF-8"));){
			jsonobject = (JSONObject) parser.parse(in);
		}
		catch(IOException ioe){
			throw new InitializationException("Unable to open file");
		}
		catch(ParseException e){
			throw new InitializationException("Unable to parse file");
		}
		validate(jsonobject);
		inputPath = (String)jsonobject.get(INPUT_PATH);
		digitDelimiter = (boolean)jsonobject.get(DIGIT_DELIMITER);
		if(jsonobject.containsKey(OUTPUT_PATH)){
			outputPath = (String)jsonobject.get(OUTPUT_PATH);
		}
		else{
			outputPath = null;
		}
		if(jsonobject.containsKey(SEARCH_PATH)){
			searchPath = (String)jsonobject.get(SEARCH_PATH);
		}
		else{
			searchPath = null;
		}
		if(jsonobject.containsKey(SEARCH_OUTPUT_PATH)){
			searchOutputPath = (String)jsonobject.get(SEARCH_OUTPUT_PATH);
		}
		else{
			searchOutputPath = null;
		}
	}
	
	/**
	 * Helper method to confirm good values in the JSON file.
	 * @param jsonobject
	 * @throws InitializationException
	 */
	private void validate(JSONObject jsonobject) throws InitializationException{
		if(!jsonobject.containsKey(INPUT_PATH))
			throw new InitializationException("inputPath not specified");
		if(!jsonobject.containsKey(DIGIT_DELIMITER))
			throw new InitializationException("digitDelimiter not specified");
		if(!(jsonobject.get(DIGIT_DELIMITER) instanceof Boolean))
			throw new InitializationException("digitDelimiter not a boolean");
	}
	

	/**
	 * Returns the value of associated with the inputPath key in the JSON configuration file.
	 * @return - value associated with key inputPath
	 */
	public String getInputPath() {
		return inputPath;
	}
	
	/**
	 * Returns the value of associated with the outputPath key in the JSON configuration file.
	 * @return - value associated with key outputPath - null if no outputPath specified
	 */
	public String getOutputPath() {
		return outputPath;
	}
	
	public String getSearchPath() {
		return searchPath;
	}
	
	public String getSearchOutputPath() {
		return searchOutputPath;
	}

	/**
	 * Returns the value of associated with the digitDelimiter key in the JSON configuration file.
	 * @return - value associated with key digitDelimiter
	 */
	public boolean useDigitDelimiter() {
		return digitDelimiter;
	}
	
	public String toString(){
		return (inputPath + " " + outputPath + " " + searchPath + " " + searchOutputPath + " "+ digitDelimiter + " " + configPath);
	}
	
	/**
	 * Simple main method used for in-progress testing of Configuration class only.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		(new Configuration(FileSystems.getDefault().getPath("config.json"))).init();
	}
	
}
