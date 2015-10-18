'use strict';

/**
 * Read module for reading different file formats.
 * @module read
 */
var read = {};

/** @constant */
var DELIMITER = {
	CSV : ',',
	TSV : '\t'
};

// java imports
var PlainReader = Java.type("com.finitejs.modules.read.PlainReader");
var InputFormatter = Java.type('com.finitejs.modules.read.InputFormatter');
var InputValidator = Java.type('com.finitejs.modules.read.InputValidator');

/**
 * @class
 */
function Reader(){
	this._reader = PlainReader.get();
}

Reader.prototype.type =  function(columnIndex, typeString){
	try{
		this._reader.setType(columnIndex, typeString);
	}catch(ex){
		throw "Illegal arguments";
	}
	return this;
};

Reader.prototype.types = function(){
	
	var typeArray = null, i;
	
	if (arguments.length > 1){
		// expecting type strings as arguments
		if (util.isSingleArray(arguments)){
			typeArray = util.toStringWithArray(arguments);
		}
	}else if(arguments.length == 1){
		// expecting an array as argument
		if (util.isSingleArray(arguments[0])){
			typeArray = util.toStringWithArray(arguments[0]);
		}
	}
	
	if (typeArray){
		this._reader.setType(typeArray);
	}else{
		throw "Illegal arguments";
	}
	
	return this;
};

Reader.prototype.name = function(columnIndex, name){
	try{
		this._reader.setName(columnIndex, name);
	}catch(ex){
		throw "Illegal arguments";
	}
	return this;
};

Reader.prototype.names = function(){
	
	var nameArray = null, i;
	
	if (arguments.length > 1){
		// expecting type strings as arguments
		if (util.isSingleArray(arguments)){
			nameArray = util.toStringWithArray(arguments);
		}
	}else if(arguments.length == 1){
		// expecting an array as argument
		if (util.isSingleArray(arguments[0])){
			nameArray = util.toStringWithArray(arguments[0]);
		}
	}
	
	if (nameArray){
		this._reader.setName(nameArray);
	}else{
		throw "Illegal arguments";
	}
	
	return this;
};

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
 * @param {string} commentString - comment string
 */
Reader.prototype.comment = function(commentString){
	
	if (commentString && typeof commentString === 'string'){
		this._reader.setCommentString(commentString);
	}else{
		throw "Illegal argument";
	}
	
};

Reader.prototype.delim = function(file, settings){
	var isHeaderPresent = true, delimiter = DELIMITER.CSV;
	
	if (settings && settings.delimiter){
		delimiter = settings.delimiter;
	}
	
	if (settings && settings.header != null && settings.header === false){
		isHeaderPresent = false;
	}
	
	return table(this._reader.read(file, delimiter, isHeaderPresent));
};

Reader.prototype.csv = function(file, settings){
	if (!settings){
		settings = {};
	}
	settings.delimiter = DELIMITER.CSV;
	return this.delim(file, settings);
};

Reader.prototype.tsv = function(file, settings){
	if (!settings){
		settings = {};
	}
	settings.delimiter = DELIMITER.TSV;
	return this.delim(file, settings);
};

read = function(){
	return new Reader();
};

/**
 * Reads the specified CSV file and returns a table instance.
 * 
 * @param {string} file - file path/URL
 * @param {object} [settings] - optional settings object
 * @param {boolean} [settings.header=true] true if first non-comment row is header row, else false
 * @param {Array} [settings.types] string representations of column types
 * @param {Array} [settings.names] column names
 * @returns {Table} table instance of CSV
 */
read.csv = function(file, settings){
	var isHeaderPresent = true, types = null, names= null;
	if (settings && settings.header != null){
		isHeaderPresent = settings.header;
	}
	
	if (settings && settings.types && util.isSingleArray(settings.types)){
		types = util.toStringWithArray(settings.types);
	}
	
	if (settings && settings.names && util.isSingleArray(settings.names)){
		names = util.toStringWithArray(settings.names);
	}

	var reader = PlainReader.get(Java.to(types, 'String[]'), Java.to(names, 'String[]'));
	var dt = reader.read(file, DELIMITER.CSV, isHeaderPresent);
	return table(dt);
};

module.exports = read;