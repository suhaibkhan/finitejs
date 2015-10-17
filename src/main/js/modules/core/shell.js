'use strict';

// Console I/O utils
var console = Java.type("com.finitejs.modules.core.ConsoleUtils");

// JSEngine for executing JavaScript.
var JSEngine = Java.type('com.finitejs.system.JSEngine');

//JSEngine singleton instance
var jsEngine = JSEngine.getInstance();

// Get a BufferedReader to System.in
var consoleReader = console.getInputReader();

// make these variables available globally in this context.
// these are available only inside the anonymous function
// executed by module loader.

var sharedObjects = {};
sharedObjects.exports = exports;
sharedObjects.require = require;
sharedObjects.__filename = module.filename = '<shell>';
sharedObjects.__dirname = '<shell>';

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
			
			// use shell context to parse all input
			output = jsEngine.eval(input);
			
			// print if a variable or output not null
			if (output != null || 
				/^[a-zA-Z_$][a-zA-Z_$0-9]*(?:\.[a-zA-Z_$][a-zA-Z_$0-9]*)*$/.test(input.trim())){
				
				if (output && !Array.isArray(output) && 
						typeof output.toString === 'function' && 
						output.toString() !== '[object Object]'){
					console.println(output.toString());
				}else{
					// try to print JSON representation of object instead of [object Object]
					// JSON representation of array is printed instead of Array.toString
					console.println(JSON.stringify(output));
				}
			}
			
		}catch(ex){
			// ex.printStackTrace();
			console.errorf("%s%n", ex);
		}
	}
}

// start method is made available to other modules
exports.start = start;