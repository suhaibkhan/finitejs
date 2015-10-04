
// read main file

// sub files in read folder

var read = {};
var PlainReader = Java.type("com.finitejs.modules.read.PlainReader");

read.csv = function(file){
	var df = PlainReader.get().read(file);
	return df;
};

module.exports = read;