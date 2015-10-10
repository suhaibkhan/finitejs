
// Read Module

// sub files in read folder

var read = {};
var PlainReader = Java.type("com.finitejs.modules.read.PlainReader");

read.csv = function(file){
	var dt = PlainReader.get().read(file);
	return table(dt);
};

module.exports = read;