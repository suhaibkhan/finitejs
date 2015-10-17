'use strict';

// finite js core module
// core module is loaded by module.js

var JSEngine = Java.type('com.finitejs.system.JSEngine');

// JSEngine singleton instance
var jsEngine = JSEngine.getInstance();

// get core modules and set as global variables to be 
// shared across contexts.

var Map = require('map');
jsEngine.addGlobalVariable('Map', Map);

var util = require('util');
jsEngine.addGlobalVariable('util', util);

var assert = require('assert');
jsEngine.addGlobalVariable('assert', assert);

var table = require('table');
jsEngine.addGlobalVariable('table', table);

var read = require('read');
jsEngine.addGlobalVariable('read', read);

var math = require('math');
jsEngine.addGlobalVariable('cbrt', math.cbrt);

var stat = require('stat');
jsEngine.addGlobalVariable('mean', stat.mean);
jsEngine.addGlobalVariable('std', stat.std);

if (!__shell){
	// load main file passed as cmd line argument
	require(__mainfile, true);
}else{
	// start shell mode
	require('shell').start();
}
