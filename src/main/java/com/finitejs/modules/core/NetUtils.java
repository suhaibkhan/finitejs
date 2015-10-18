package com.finitejs.modules.core;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Utility class for network/Internet related operations.
 */
public class NetUtils {

	/**
	 * Checks whether the given string is a valid URL.
	 * 
	 * @param urlStr  URL string to check
	 * @return true if valid URL, else false
	 */
	public static boolean isValidURL(String urlStr) {
		
		if (urlStr == null){
			return false;
		}
		
		try {
			URI uri = new URI(urlStr);
			return uri.getScheme().equals("http") || uri.getScheme().equals("https");
		} catch (URISyntaxException e) {
			return false;
		}
	}

}
