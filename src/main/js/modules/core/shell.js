'use strict';

// Console I/O utils
var console = Java.type("com.finitejs.modules.core.ConsoleUtils");

// JSEngine for executing JavaScript.
var JSEngine = Java.type('com.finitejs.system.JSEngine');

// Get a BufferedReader to System.in
var consoleReader = console.getInputReader();

// make these variables available globally in this context.
// these are available only inside the anonymous function
// executed by module loader.

var sharedObjects = {};
sharedObjects.require = require;
sharedObjects.__filename = module.filename = '<shell>';
sharedObjects.__dirname = '<shell>';

JSEngine.getInstance().addAllLocalVariables(sharedObjects, module.context);

function start(){
	
	var input, output;
	
	// REPL - Read Eval Print Loop
	while(true){
		console.printf("%n%s ",">");
		try{
			input = consoleReader.readLine();
			
			// parse input to enable mutiline expressions
			
			// use shell context to parse all input
			output = JSEngine.getInstance().evalInContext(input, module.context);
			
			// output result
			if (output && typeof output.toString === 'function'){
				console.print(output.toString());
			}else{
				console.print(output);
			}
		}catch(ex){
			ex.printStackTrace();
			console.error(ex);
		}
	}
}

// start method is made available to other modules
exports.start = start;