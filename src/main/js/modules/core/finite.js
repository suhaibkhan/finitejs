'use strict';

// finite js core module
// core module is loaded by module.js

var JSEngine = Java.type('com.finitejs.system.JSEngine');

// get core modules and set as global variables to be 
// shared across contexts.
var Map = require('map');
JSEngine.getInstance().addGlobalVariable('Map', Map);
var table = require('table');
JSEngine.getInstance().addGlobalVariable('table', table);
var read = require('read');
JSEngine.getInstance().addGlobalVariable('read', read);

var math = require('math');
JSEngine.getInstance().addGlobalVariable('cbrt', math.cbrt);

if (!__shell){
	// load main file passed as cmd line argument
	require(__mainfile, true);
}else{
	// start shell mode
	require('shell').start();
}
