
var DataTable = Java.type('com.finitejs.modules.read.DataTable');
var ArrayList = Java.type('java.util.ArrayList');

// table constructor can have argument of type DataTable or arrays
function Table(args){
	var i, stringArray, lastArg, typeArray = null, headerArray = null, 
		validSettingsArgPresent = false, stringArrayList = new ArrayList;
	
	// internal DataTable
	this._table = null;
		
	// check for settings argument
	lastArg = args[args.length - 1];
	if (args.length > 1 && lastArg && !Array.isArray(lastArg)){
		
		if (lastArg.type && Array.isArray(lastArg.type)){
			typeArray = Java.to(lastArg.type, 'java.util.List');
			validSettingsArgPresent = true;
		}
		
		if (lastArg.header && Array.isArray(lastArg.header)){
			headerArray = Java.to(lastArg.header, 'java.util.List');
			validSettingsArgPresent = true;
		}
		
		if (validSettingsArgPresent){
			// remove settings arguments from arguments list
			args.pop();
		}
	}
	
	if (args.length == 1){
		
		// if constructor contains only one argument, then it can
		// a DataTable instance, or an array or a 2-d array containing tabular data
		
		if (args[0] instanceof DataTable){
			this._table = args[0];
		}else if (Array.isArray(args[0])){
			
			// convert array to string array
			if (isValidArray(args[0])){
				stringArray = toStringArray(args[0]);
				stringArrayList.add(stringArray);
			}else if (isValidMultiArray(args[0])){
				stringArrayList = toStringArray(args[0]);
			}else{
				throw "Illegal arguments";
			}
			
			// create table
			this._table = DataTable.getTableWithTypeStrings(stringArrayList, typeArray, headerArray);
			
		}else{
			throw "Illegal arguments";
		}
	}else if(args.length > 1){
		
		// if contains multiple arguments then it can be 
		// only array representing each row
		
		for (i = 0; i < args.length; i++){
			// convert array to string array
			if (Array.isArray(args[i]) && isValidArray(args[i])){
				stringArrayList.add(toStringArray(args[i]));
			}else{
				throw "Illegal arguments";
			}
		}
		
		// create table
		this._table = DataTable.getTableWithTypeStrings(stringArrayList, typeArray, headerArray);
	}else{
		throw "Illegal arguments";
	}
	
}

Table.prototype.size = function(){
	return this._table.getRowCount();
};

Table.prototype.toString = function(start, limit){
	
	if (typeof start !== 'undefined' && start !== null && 
			typeof limit !== 'undefined' && limit !== null){
		return this._table.toString(start, limit);
	}
	
	if (typeof start !== 'undefined' && start !== null){
		return this._table.toString(start);
	}
	
	return this._table.toString();
};

// checks whether a valid 2-d array which contains tabular data
function isValidMultiArray(array){
	var i, valid = true;
	for (i = 0; i < array.length; i++){
		if (!Array.isArray(array[i])){
			valid = false;
			break;
		}else{
			if (!isValidArray(array[i])){
				valid = false;
				break;
			}
		}
	}
	
	return valid;
}

// check whether a valid array which contains only data for one row
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

// converts an array to another array with string representation of elements
// supports array of arrays also
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

/**
 * Example:
 * 
 * var t = table([1,2,3],[4,5,6], {type: ['number', 'number', 'string'], header:['NUM1', 'NUM2', 'STR']});
 * var t = table([1,2,3],[4,5,6]);
 * var t = table([[1,2,3],[4,5,6]]);
 * 
 */
var table = function(){
	var i, args = [];
	for (i = 0; i < arguments.length; i++){
		args.push(arguments[i]);
	}
	return new Table(args);
};

module.exports = table;