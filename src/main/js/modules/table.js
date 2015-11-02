'use strict';

// Module:table for tabular data manipulations.

var DataTable = Java.type('com.finitejs.modules.read.DataTable');
var ArrayList = Java.type('java.util.ArrayList');

/**
 * Creates a table instance for storing tabular data.
 * 
 * @class
 * @protected
 * @param {...Array|com.finitejs.modules.read.DataTable} args - table constructor 
 * can have argument of type DataTable or arrays
 */
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
			if (util.isSingleArray(args[0])){
				stringArray = util.toStringWithArray(args[0]);
				stringArrayList.add(stringArray);
			}else if (util.isTwoLevelArray(args[0])){
				stringArrayList = util.toStringWithArray(args[0]);
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
			if (Array.isArray(args[i]) && util.isSingleArray(args[i])){
				stringArrayList.add(util.toStringWithArray(args[i]));
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

/**
 * Returns row count of the table.
 * 
 * @returns {Number} row count
 */
Table.prototype.size = function(){
	return this._table.getRowCount();
};

/**
 * Returns string representation of the tabular data, starting at specified 
 * row index and number of rows limited to specified limit.
 * 
 * @param {Number} [start] - index of the first row in the string representation
 * @param {Number} [limit] - number of rows in the string representation
 * @return {String} string representation
 */
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

/**
 * Returns underlying {@code com.finitejs.modules.read.DataTable}.
 * 
 * @returns {com.finitejs.modules.read.DataTable}
 */
Table.prototype.getDataTable = function(){
	return this._table;
};

/**
 * Module for tabular data operations.
 * Module returns function that creates a {@link Table} instance based on the arguments.
 * 
 * @exports table
 * @param {...Array|com.finitejs.modules.read.DataTable} - table constructor 
 * can have argument of type DataTable or arrays
 * @returns {Table}
 * @example
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