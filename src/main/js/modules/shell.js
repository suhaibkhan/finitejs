'use strict';

/**
 * Module that starts the shell/REPL mode.
 * 
 * @module shell
 */

// File utils
var FileUtils = Java.type("com.finitejs.modules.core.FileUtils");

// Console I/O utils
var console = Java.type("com.finitejs.modules.core.ConsoleUtils");

// JSEngine for executing JavaScript.
var JSEngine = Java.type('com.finitejs.system.JSEngine');

//JSEngine singleton instance
var jsEngine = JSEngine.getInstance();

// Get a BufferedReader to System.in
var consoleReader = console.getInputReader();

// set module filename to current working directory 
// so that relative modules loaded from shell 
// will be relative to this path
module.filename = FileUtils.getWorkingDir();

// clear shell module parent
// no need of that
module.parent = null;

// make these variables available globally in this context
// if not set these variables will be available only inside 
// the anonymous function executed by module loader.
var sharedObjects = {
	exports : exports,
	require : require,
	module : module,
	__filename : module.filename,
	__dirname : module.filename
};
jsEngine.addGlobalVariableMap(sharedObjects);

function start(){
	
	var input, output, promptFormat = '%s ', promptChar = '>>';
	
	// print application info
	console.printAppInfo();
	
	// REPL - Read Eval Print Loop
	while(true){
		
		console.printf(promptFormat, promptChar);
		
		try{
			input = consoleReader.readLine();
			
			if (input == null || input == ''){
				continue;
			}
			
			// parse input to enable mutiline expressions
			// TODO
			
			// evaluate input in global scope
			output = jsEngine.eval(input);
			
			// print if a variable or output not null
			if (output != null || 
				/^[a-zA-Z_$][a-zA-Z_$0-9]*(?:\.[a-zA-Z_$][a-zA-Z_$0-9]*)*$/.test(input.trim())){
				
				// show NaN in console instead of null
				if (typeof output === 'number' && isNaN(output)){
					output = 'NaN';
				}
				
				if (output && !Array.isArray(output) && 
						typeof output.toString === 'function' && 
						output.toString() !== '[object Object]'){
					console.println(output.toString());
				}else{
					// try to print JSON representation of object instead of [object Object]
					// JSON representation of array is printed instead of Array.toString
					console.println(JSON.stringify(output, null, 2));
				}
			}
			
		}catch(ex){
			if (__debug){
				ex.printStackTrace();
			}
			console.errorf("%s%n", ex);
		}
	}
}

// start method is made available to other modules
exports.start = start;