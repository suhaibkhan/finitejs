
/**
 * Module that contains several utility functions used by other modules.
 * @module util
 */

var util = {};

/**
 * Converts the argument to its string representation. If argument is an array,
 * then array with string representation is returned.
 * 
 * @param {object} o - argument to be converted
 * @returns {string|Array} converted string or string array
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
 * @param {object} o - argument to be checked
 * @returns {boolean} true if valid, else false
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
 * @param {object} o - argument to be checked
 * @returns {boolean} true if valid, else false
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
 * Recursively checks for equality of two objects.
 * 
 * @param {object} a - first object
 * @param {object} b - second object
 * @returns {boolean} true if equal, else false
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

module.exports = util;
