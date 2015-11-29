'use strict';

// finite js core module
// core module is loaded by module.js

var JSEngine = Java.type('com.finitejs.system.JSEngine');

// JSEngine singleton instance
var jsEngine = JSEngine.getInstance();

// get core modules and set as global variables to be 
// shared across contexts.

var util = require('util');
jsEngine.addGlobalVariable('util', util);
jsEngine.addGlobalVariable('sprintf', util.sprintf);

var assert = require('assert');
jsEngine.addGlobalVariable('assert', assert);

var Map = require('map');
jsEngine.addGlobalVariable('Map', Map);

var table = require('table');
jsEngine.addGlobalVariable('table', table);

var read = require('read');
jsEngine.addGlobalVariable('read', read);

var math = require('math');
jsEngine.addGlobalVariable('cbrt', math.cbrt);

var stat = require('stat');
// add all functions in stat module as global functions
jsEngine.addGlobalVariableMap(stat);

var Complex = require('complex');
jsEngine.addGlobalVariable('Complex', Complex);

var plot = require('plot');
jsEngine.addGlobalVariable('plot', plot);

if (!__shell){
	// load main file passed as cmd line argument
	require(__mainfile, true);
}else{
	// start shell mode
	require('shell').start();
}
