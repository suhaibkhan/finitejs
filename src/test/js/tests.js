'use strict';

/**
 * Test runner for finite.js modules.
 * All js test suites inside src/test/js will be 
 * execute by this runner. This test runner can be executed only with
 * help of finite.js.
 */

var File = Java.type('java.io.File');
var ArrayList = Java.type('java.util.ArrayList');
var console = Java.type("com.finitejs.modules.core.ConsoleUtils");

/**
 * Recursively returns all files in a specified directory.
 * 
 * @param {string} dir - directory path
 * @returns {Array} paths of all files
 */
function getAllFiles(dir){
	var directory = new File(dir), file, files, filePaths = new ArrayList;
	
	if (directory && directory.exists() && directory.isDirectory()){
		files = directory.listFiles();
		for each (file in files){
			if (file.isFile() && file.getAbsolutePath() !== __filename && 
					file.getAbsolutePath().endsWith('.js')) {
				// js of this test runner will be discarded
				filePaths.add(file.getAbsolutePath());
	        } else if (file.isDirectory()) {
	        	// recursively fetch file paths
	        	filePaths.addAll(getAllFiles(file.getAbsolutePath()));
	        }
		}
	}
	
	return filePaths;
}

// execute all js test suites
(function runTests(){
	
	var key, filePath, relativePath, testObj, testMethod, 
		filePaths = Java.from(getAllFiles(__dirname)),
		startTime, timeTaken, completedCount = 0, failedCount = 0;

	for (key in filePaths){
		filePath = filePaths[key];
		// print file names relative to src/test/js
		relativePath = filePath.replace(__dirname + '/', '');
		console.printf('%n%s%n', relativePath);
		// get test suite object with test cases as functions
		testObj = require(filePath).test;
		// execute each test case
		for (testMethod in testObj){
			try{
				
				startTime = +new Date;
				// execute 
				testObj[testMethod].call();
				timeTaken = (+new Date - startTime)/1000;
				// print test case name with time taken to execute
				console.printf('\t%s (%s s) %n', testMethod, timeTaken);
				completedCount++;
			}catch(ex){
				console.errorf('%s > %s : %s %n', relativePath, testMethod, ex);
				failedCount++;
			}
		}
	}
	
	if (failedCount > 0){
		console.error(sprintf('%n%d %s completed, %d failed', completedCount.intValue(), 
				completedCount > 1 ? 'tests' : 'test', failedCount.intValue()).toUpperCase());
	}else{
		console.print(sprintf('%n%d %s completed', completedCount.intValue(), 
				completedCount > 1 ? 'tests' : 'test').toUpperCase());
	}
	
})();
