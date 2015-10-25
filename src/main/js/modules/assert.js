'use strict';

/**
 * Assert module used for testing other js modules.
 * @exports assert
 */
var assert = {};

/**
 * Asserts if expected value not equals to actual value.
 * Equality checked using {@code === } operator.
 * 
 * @param {*} expected - expected value
 * @param {*} actual - actual value
 * @param {String} [msg] - message to print on assert
 */
assert.equals = function(expected, actual, msg){
	
	var expStr, actStr;
	
	if (!msg){
		expStr = typeof expected === 'string' ? expected : JSON.stringify(expected);
		actStr = typeof actual === 'string' ? actual : JSON.stringify(actual);

		msg = 'expected: <' + expStr + '> but was: <' + actStr + '>';
	}
	
	if (expected !== actual){
		throw 'Assertion error : ' + msg;
	}
};

/**
 * Asserts if expected value equals to actual value.
 * Equality checked using {@code === } operator.
 * 
 * @param {*} expected - expected value
 * @param {*} actual - actual value
 * @param {String} [msg] - message to print on assert
 */
assert.notEquals = function(expected, actual, msg){
	
	var expStr, actStr;
	
	if (!msg){
		expStr = typeof expected === 'string' ? expected : JSON.stringify(expected);
		actStr = typeof actual === 'string' ? actual : JSON.stringify(actual);

		msg = 'expected: not <' + expStr + '> but was: <' + actStr + '>';
	}
	
	if (expected === actual){
		throw 'Assertion error : ' + msg;
	}
};

/**
 * Asserts if expected value not equals to actual value.
 * Equality checked recursively with each property.
 * 
 * @param {*} expected - expected value
 * @param {*} actual - actual value
 * @param {String} [msg] - message to print on assert
 */
assert.deepEquals = function(expected, actual, msg){
	
	var expStr, actStr;
	
	if (!msg){
		expStr = typeof expected === 'string' ? expected : JSON.stringify(expected);
		actStr = typeof actual === 'string' ? actual : JSON.stringify(actual);

		msg = 'expected: <' + expStr + '> but was: <' + actStr + '>';
	}
	
	if (!util.equals(expected,actual)){
		throw 'Assertion error : ' + msg;
	}
};

/**
 * Asserts if expected value equals to actual value.
 * Equality checked recursively with each property.
 * 
 * @param {*} expected - expected value
 * @param {*} actual - actual value
 * @param {String} [msg] - message to print on assert
 */
assert.deepNotEquals = function(expected, actual, msg){
	
	var expStr, actStr;
	
	if (!msg){
		expStr = typeof expected === 'string' ? expected : JSON.stringify(expected);
		actStr = typeof actual === 'string' ? actual : JSON.stringify(actual);

		msg = 'expected: not <' + expStr + '> but was: <' + actStr + '>';
	}
	
	if (util.equals(expected,actual)){
		throw 'Assertion error : ' + msg;
	}
};

/**
 * Asserts if actual value is not true.
 * 
 * @param {*} actual - actual value
 * @param {String} [msg] - message to print on assert 
 */
assert.checkTrue = function(actual, msg){
	
	var actStr;
	
	if (!msg){
		actStr = JSON.stringify(actual);

		msg = 'expected: <true> but was: <' + actStr + '>';
	}
	
	if (actual !== true){
		throw 'Assertion error : ' + msg;
	}
};

/**
 * Asserts if actual value is not false.
 * 
 * @param {*} actual - actual value
 * @param {String} [msg] - message to print on assert 
 */
assert.checkFalse = function(actual, msg){
	
	var actStr;
	
	if (!msg){
		actStr = JSON.stringify(actual);

		msg = 'expected: <false> but was: <' + actStr + '>';
	}
	
	if (actual !== false){
		throw 'Assertion error : ' + msg;
	}
};

/**
 * Asserts if actual value is not null.
 * 
 * @param {*} actual - actual value
 * @param {String} [msg] - message to print on assert 
 */
assert.checkNull = function(actual, msg){
	
	var actStr;
	
	if (!msg){
		actStr = JSON.stringify(actual);

		msg = 'expected: <null> but was: <' + actStr + '>';
	}
	
	if (actual !== null){
		throw 'Assertion error : ' + msg;
	}
};

/**
 * Asserts if actual value is null.
 * 
 * @param {*} actual - actual value
 * @param {String} [msg] - message to print on assert 
 */
assert.checkNotNull = function(actual, msg){
	
	var actStr;
	
	if (!msg){
		actStr = JSON.stringify(actual);

		msg = 'expected: <not null> but was: <' + actStr + '>';
	}
	
	if (actual === null){
		throw 'Assertion error : ' + msg;
	}
};

module.exports = assert;