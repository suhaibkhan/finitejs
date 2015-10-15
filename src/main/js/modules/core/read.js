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

var PlainReader = Java.type("com.finitejs.modules.read.PlainReader");
var InputFormatter = Java.type('com.finitejs.modules.read.InputFormatter');


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
		if (isValidArray(arguments)){
			typeArray = toStringArray(arguments);
		}
	}else if(arguments.length == 1){
		// expecting an array as argument
		if (isValidArray(arguments[0])){
			typeArray = toStringArray(arguments[0]);
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
		if (isValidArray(arguments)){
			nameArray = toStringArray(arguments);
		}
	}else if(arguments.length == 1){
		// expecting an array as argument
		if (isValidArray(arguments[0])){
			nameArray = toStringArray(arguments[0]);
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

//converts an array to another array with string representation of elements
//supports array of arrays also
function toStringArray(array){
	var i, stringArray = new ArrayList;
	for (i = 0; i < array.length; i++){
		if (Array.isArray(array[i])){
			// recursively resolve to string
			stringArray.add(toStringArray(array[i]));
		}else{
			if (array[i] && typeof array[i].toString === 'function'){
				stringArray.add(array[i].toString());
			}else{
				stringArray.add('' + array[i]);
			}
		}
	}
	return stringArray;
}

//check whether a valid array which contains only data for one row
function isValidArray(array){
	var i, valid = true;
	for (i = 0; i < array.length; i++){
		if (Array.isArray(array[i])){
			valid = false;
			break;
		}
	}
	
	return valid;
}

read = function(){
	return new Reader();
};

/**
 * Reads the specified CSV file and returns a table instance.
 * 
 * @param {string} file - file path
 * @param {Object} [settings] - optional settings object
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
	
	if (settings.types && Array.isArray(settings.types) && 
			isValidArray(settings.types)){
		types = toStringArray(settings.types);
	}
	
	if (settings.names && Array.isArray(settings.names) && 
			isValidArray(settings.names)){
		names = toStringArray(settings.names);
	}

	var reader = PlainReader.get(Java.to(types, 'String[]'), Java.to(names, 'String[]'));
	var dt = reader.read(file, CSV_DELIMITER, isHeaderPresent);
	return table(dt);
};

module.exports = read;