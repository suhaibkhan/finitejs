'use strict';

// Module:read for reading different file formats.

/** 
 * Enum for delimiters.
 * 
 * @readonly
 * @enum {String}
 * @ignore
 */
var DELIMITER = {
	CSV : ',',
	TSV : '\t'
};

// java imports
var PlainReader = Java.type('com.finitejs.modules.read.PlainReader');
var InputFormatter = Java.type('com.finitejs.modules.read.InputFormatter');
var InputValidator = Java.type('com.finitejs.modules.read.InputValidator');

/**
 * Represents a file reader.
 * 
 * @class
 * @protected 
 */
function Reader(){
	this._reader = PlainReader.get();
}

/**
 * Set a predefined type for a column with specified index. If a column 
 * type is predefined, then dynamic type checking is skipped for that column.
 * 
 * @param {Number} columnIndex - index of the column to which the type is set
 * @param {String} typeString - string representation of the type
 * @returns {Reader} current reader instance, can be used for method chaining
 */
Reader.prototype.type =  function(columnIndex, typeString){
	try{
		this._reader.setType(columnIndex, typeString);
	}catch(ex){
		throw "Illegal arguments";
	}
	return this;
};

/**
 * Set a predefined type for columns. If a column type is predefined, then dynamic 
 * type checking is skipped for that column. Table column index is matched with 
 * column type index in this list.
 * 
 * @param {String[]|...String} arguments - string representation of types in 
 * column order as an array or as arguments
 * @returns {Reader} current reader instance, can be used for method chaining
 */
Reader.prototype.types = function(){
	
	var typeArray = null, i, args;
	
	// converts arguments object to array
	args = Array.prototype.slice.call(arguments).sort();
	
	if (args.length > 1){
		// expecting type strings as arguments
		if (util.isSingleArray(args)){
			typeArray = util.toStringWithArray(args);
		}
	}else if(args.length == 1){
		// expecting an array as argument
		if (util.isSingleArray(args[0])){
			typeArray = util.toStringWithArray(args[0]);
		}
	}
	
	if (typeArray){
		this._reader.setType(typeArray);
	}else{
		throw "Illegal arguments";
	}
	
	return this;
};

/**
 * Set a predefined name for a column with specified index. If a column name 
 * is predefined, then name/header from file is discarded.
 * 
 * @param {Number} columnIndex - index of the column to which the name is set
 * @param {String} name - column name
 * @returns {Reader} current reader instance, can be used for method chaining
 */
Reader.prototype.name = function(columnIndex, name){
	try{
		this._reader.setName(columnIndex, name);
	}catch(ex){
		throw "Illegal arguments";
	}
	return this;
};

/**
 * Set predefined name for columns. If a column name is predefined, then header/name 
 * from file is discarded.  Table column index is matched with column name 
 * index in this list.
 * 
 * @param {String[]|...String} arguments - column names in column order as 
 * an array or as arguments
 * @returns {Reader} current reader instance, can be used for method chaining
 */
Reader.prototype.names = function(){
	
	var nameArray = null, i, args;
	
	// converts arguments object to array
	args = Array.prototype.slice.call(arguments).sort();
	
	if (args.length > 1){
		// expecting type strings as arguments
		if (util.isSingleArray(args)){
			nameArray = util.toStringWithArray(args);
		}
	}else if(args.length == 1){
		// expecting an array as argument
		if (util.isSingleArray(args[0])){
			nameArray = util.toStringWithArray(args[0]);
		}
	}
	
	if (nameArray){
		this._reader.setName(nameArray);
	}else{
		throw "Illegal arguments";
	}
	
	return this;
};

/**
 * Set an input formatter for a column with specified index. Only result of the
 * formatter will be stored.
 * 
 * @param {Number} columnIndex - index of the column to which the formatter is set
 * @param {Function} formatter - formatter to use, should be a function that has 
 * a string argument representing the input value and must return a string value 
 * representing the formatted output
 * @returns {Reader} current reader instance, can be used for method chaining
 */
Reader.prototype.format = function(columnIndex, formatter){
	
	var inputFormatter = new InputFormatter(function(input){
		return formatter(input);
	});
	
	try{
		this._reader.setFormatter(columnIndex, inputFormatter);
	}catch(ex){
		throw "Illegal arguments";
	}
	return this;
};

/**
 * Set an input validator for a column with specified index. If validator returns
 * false for a value, then the whole row will be discarded.
 * 
 * @param {Number} columnIndex - index of the column to which the formatter is set
 * @param {Function} validator - validator to use, should be a function that has 
 * a string argument representing the input value and must return a boolean value 
 * representing valid value or not
 * @returns {Reader} current reader instance, can be used for method chaining
 */
Reader.prototype.validate = function(columnIndex, validator){
	
	var inputValidator = new InputValidator(function(input){
		return validator(input);
	});
	
	try{
		this._reader.setValidator(columnIndex, inputValidator);
	}catch(ex){
		throw "Illegal arguments";
	}
	return this;
};

/**
 * Set a comment string to use while reading. Lines starting with comment 
 * string will be ignored.
 * 
 * @param {String} commentString - comment string
 * @returns {Reader} current reader instance, can be used for method chaining
 */
Reader.prototype.comment = function(commentString){
	
	if (commentString && typeof commentString === 'string'){
		this._reader.setCommentString(commentString);
	}else{
		throw "Illegal argument";
	}
	
};

/**
 * Read the specified file and returns the whole data as a {@link Table}.
 * Specified custom delimiter will be used to separate columns.
 * 
 * @param {String} path - path to the file, it can also be a URL
 * @param {Object} [settings] - optional settings object
 * @param {Boolean} [settings.header=true] - true if first non-comment row is header row, else false
 * @param {String} [settings.delimiter=DELIMITER.CSV] - delimiter to separate columns in a row
 * @returns {Table}
 */
Reader.prototype.delim = function(path, settings){
	var isHeaderPresent = true, delimiter = DELIMITER.CSV;
	
	if (settings && settings.delimiter){
		delimiter = settings.delimiter;
	}
	
	if (settings && settings.header != null && settings.header === false){
		isHeaderPresent = false;
	}
	
	return table(this._reader.read(path, delimiter, isHeaderPresent));
};

/**
 * Reads a CSV file and returns the whole data as a {@link Table}.
 * 
 * @param {String} path - path to the file, it can also be a URL
 * @param {Object} [settings] - optional settings object
 * @param {Boolean} [settings.header=true] - true if first non-comment row is header row, else false
 * @returns {Table}
 */
Reader.prototype.csv = function(path, settings){
	if (!settings){
		settings = {};
	}
	settings.delimiter = DELIMITER.CSV;
	return this.delim(path, settings);
};

/**
 * Reads a TSV file and returns the whole data as a {@link Table}.
 * 
 * @param {String} path - path to the file, it can also be a URL
 * @param {Object} [settings] - optional settings object
 * @param {Boolean} [settings.header=true] - true if first non-comment row is header row, else false
 * @returns {Table}
 */
Reader.prototype.tsv = function(path, settings){
	if (!settings){
		settings = {};
	}
	settings.delimiter = DELIMITER.TSV;
	return this.delim(path, settings);
};

/**
 * Module for reading different file formats.
 * Module returns a function, which can be used to create an instance of {@link Reader}.
 * 
 * @exports read
 * @returns {Reader}
 */
var read = function(){
	return new Reader();
};

/**
 * Reads the specified file and returns the whole data as a {@link Table}.
 * Specified custom delimiter will be used to separate columns.
 * 
 * @param {String} path - path to the file, it can also be a URL
 * @param {Object} [settings] - optional settings object
 * @param {Boolean} [settings.header=true] - true if first non-comment row is header row, else false
 * @param {Array} [settings.types] - string representations of column types
 * @param {Array} [settings.names] - column names
 * @param {String} [settings.delimiter=DELIMITER.CSV] - delimiter to separate columns in a row
 * @returns {Table}
 * @static
 */
read.delim = function(path, settings){
	var isHeaderPresent = true, types = null, names= null, delimiter = DELIMITER.CSV;
	
	if (settings && settings.delimiter){
		delimiter = settings.delimiter;
	}
	
	if (settings && settings.header != null){
		isHeaderPresent = settings.header;
	}
	
	if (settings && settings.types && util.isSingleArray(settings.types)){
		types = util.toStringWithArray(settings.types);
	}
	
	if (settings && settings.names && util.isSingleArray(settings.names)){
		names = util.toStringWithArray(settings.names);
	}

	var reader = PlainReader.get(types, names);
	var dt = reader.read(path, delimiter, isHeaderPresent);
	return table(dt);
};


/**
 * Reads the specified CSV file and returns the whole data as a {@link Table}.
 * 
 * @param {String} path - path to the file, it can also be a URL
 * @param {Object} [settings] - optional settings object
 * @param {Boolean} [settings.header=true] - true if first non-comment row is header row, else false
 * @param {Array} [settings.types] - string representations of column types
 * @param {Array} [settings.names] - column names
 * @returns {Table}
 * @static
 */
read.csv = function(path, settings){
	if (!settings){
		settings = {};
	}
	settings.delimiter = DELIMITER.CSV;
	
	return read.delim(path, settings);
};

/**
 * Reads the specified TSV file and returns the whole data as a {@link Table}.
 * 
 * @param {String} path - path to the file, it can also be a URL
 * @param {Object} [settings] - optional settings object
 * @param {Boolean} [settings.header=true] - true if first non-comment row is header row, else false
 * @param {Array} [settings.types] - string representations of column types
 * @param {Array} [settings.names] - column names
 * @returns {Table}
 * @static
 */
read.tsv = function(path, settings){
	if (!settings){
		settings = {};
	}
	settings.delimiter = DELIMITER.TSV;
	
	return read.delim(path, settings);
};

module.exports = read;