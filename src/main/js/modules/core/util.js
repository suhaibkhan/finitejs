'use strict';

/**
 * Module that contains several utility functions used by other modules.
 * 
 * @exports util
 */
var util = {};

// java imports
var String = Java.type('java.lang.String');

/**
 * Converts the argument to its string representation. If argument is an array,
 * then array with string representation is returned.
 * 
 * @param {*} o - argument to be converted
 * @returns {String|Array} converted string or string array
 */
util.toStringWithArray = function (o){
	var i, string;
	
	if (Array.isArray(o)){
		
		string = [];
		
		for (i = 0; i < o.length; i++){
			// recursively resolve to string
			string.push(util.toStringWithArray(o[i]));
		}
		
	}else{
		
		if (typeof o === 'number' && isNaN(o)){
			o = 'NaN';
		}
		
		if (o && typeof o.toString === 'function' && 
				o.toString() !== '[object Object]'){
			string = o.toString();
		}else{
			string = JSON.stringify(o);
		}
	}
	
	return string;
};

/**
 * Check whether given argument is a valid single dimensional array.
 * 
 * @param {*} o - argument to be checked
 * @returns {Boolean} true if valid, else false
 */
util.isSingleArray = function (o){
	var i, valid = true;
	
	if (Array.isArray(o)){
		// checks whether any of the child is an array
		for (i = 0; i < o.length; i++){
			if (Array.isArray(o[i])){
				valid = false;
				break;
			}
		}
	}else{
		valid = false;
	}
	
	return valid;
};

/**
 * Checks whether given argument is a valid two level array which contains tabular data.
 * 
 * @param {*} o - argument to be checked
 * @returns {Boolean} true if valid, else false
 */
util.isTwoLevelArray = function (o){
	var i, valid = true;
	
	if (Array.isArray(o)){
		// checks whether each element is a valid single array
		for (i = 0; i < o.length; i++){
			if (!util.isSingleArray(o[i])){
				valid = false;
				break;
			}
		}
	}else{
		valid = false;
	}
	
	return valid;
};

/**
 * Recursively checks for equality of two objects of any type.
 * 
 * @param {*} a - first object
 * @param {*} b - second object
 * @returns {Boolean} true if equal, else false
 */
util.equals = function(a, b){
	
	var prop, aPropCount = 0, bPropCount = 0;
	
	if (a == null || b == null){
		// two nulls are not equal
		return false;
	}
	
	// type check
	if (a.constructor.name !== b.constructor.name){
		return false;
	}
	
	if (typeof a === 'object' && typeof b === 'object'){
		for (prop in a){
			if (a.hasOwnProperty(prop)){
				if (b[prop] == null || !util.equals(a[prop], b[prop])){
					return false;
				}
				aPropCount++;
			}
		}
		
		for (prop in b){
			if (b.hasOwnProperty(prop)){
				bPropCount++;
			}
		}
		
		if (aPropCount != bPropCount){
			return false;
		}
		
		return true;
		
	}else{
		return a === b;
	}
};

/**
 * Returns a formatted string using the specified format string and arguments.
 * 
 * @param {String} format - format string
 * @param {...*} args - arguments referenced by the format specifiers in the format string
 */
util.sprintf = String.format;

module.exports = util;
