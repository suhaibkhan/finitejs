package com.finitejs.modules.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.finitejs.system.FiniteJS;

/**
 * Utility class for file related operations.
 * 
 */
public class FileUtils {
	
	/**
	 * Reads a text file, given path as string.
	 * 
	 * @param filePath  path of the file to read
	 * @return file contents
	 * @throws IOException
	 */
	public static String readTextFile(String filePath) throws IOException{
		byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
		return new String(fileBytes, StandardCharsets.UTF_8);
	}
	
	/**
	 * Reads a text file, given {@code File} as argument.
	 * 
	 * @param file  file to read
	 * @return file contents
	 * @throws IOException
	 */
	public static String readTextFile(File file) throws IOException{
		byte[] fileBytes = Files.readAllBytes(Paths.get(file.toURI()));
		return new String(fileBytes, StandardCharsets.UTF_8);
	}

	/**
	 * Read JSON file.
	 * 
	 * @param file  JSON file to read
	 * @return JSON file contents
	 * @throws IOException
	 */
	public static String readJSONFile(File file) throws IOException{
		String jsonFileContents = readTextFile(file);
		// strip comments from JSON file
		jsonFileContents = jsonFileContents.replaceAll(
				"(?:\\/\\*(?:[^\\*]|(?:\\*+[^\\*\\/]))*\\*+\\/)|(?:\\/\\/.*)", "");
		return jsonFileContents;
	}
	
	/**
	 * Get current working directory.
	 * 
	 * @return absolute path of working directory 
	 */
	public static String getWorkingDir(){
		Path dir = Paths.get("");
		return dir.toAbsolutePath().toString();
	}

	/**
	 * Get path of finite.js jar. 
	 * All modules will be loaded in relative to this path.
	 * 
	 * Based on the implementation from 
	 * {@link http://stackoverflow.com/questions/15359702/get-location-of-jar-file }
	 * 
	 * @return root path
	 */
	public static String getRootPath() {
		
		// main class
		Class<FiniteJS> aclass = FiniteJS.class;
		
		URL url;
		// get the URL
		try {
			url = aclass.getProtectionDomain().getCodeSource().getLocation();
		} catch (SecurityException ex) {
			url = aclass.getResource(aclass.getSimpleName() + ".class");
		}

		// convert to external form
		String extURL = url.toExternalForm();

		// prune for various cases
		if (extURL.endsWith(".jar"))   
			// from getCodeSource
			extURL = extURL.substring(0, extURL.lastIndexOf("/"));
		else {  
			// from getResource
			String suffix = "/"+(aclass.getName()).replace(".", "/")+".class";
			extURL = extURL.replace(suffix, "");
			if (extURL.startsWith("jar:") && extURL.endsWith(".jar!")){
				extURL = extURL.substring(4, extURL.lastIndexOf("/"));
			}
		}

		// convert back to URL
		try {
			url = new URL(extURL);
		} catch (MalformedURLException mux) {
			// leave URL unchanged; probably does not happen
		}

		File file;
		// convert URL to File
		try {
			file = new File(url.toURI());
		} catch(URISyntaxException ex) {
			file = new File(url.getPath());
		}
		
		return file.getAbsolutePath();
	}

}
