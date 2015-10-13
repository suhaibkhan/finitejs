/**
 * Read module for reading different file formats.
 * @module read
 */

var read = {};
var PlainReader = Java.type("com.finitejs.modules.read.PlainReader");

/** @constant */
var CSV_DELIMITER = ',';

/**
 * Reads the specified CSV file and returns a table instance.
 * 
 * @param {string} file - file path
 * @param {Object} [settings] - Optional settings object
 * @param {boolean} [settings.header=true] true if first non-comment row is header row, else false
 * @returns {Table} table instance of CSV
 */
read.csv = function(file, settings){
	var isHeaderPresent = true;
	if (settings && settings.header != null){
		isHeaderPresent = settings.header;
	}
	
	var reader = PlainReader.get();
	var dt = reader.read(file, CSV_DELIMITER, isHeaderPresent);
	return table(dt);
};

module.exports = read;