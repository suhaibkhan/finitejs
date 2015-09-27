
var p = require('multiLineParser');

var FrameManager = Java.type("com.finitejs.modules.gui.FrameManager").getInstance();

function createFrame(title, width, height){
	var frameId = FrameManager.create(width, height, title);
	return frameId;
}

function resizeFrame(frameId, width, height){
	FrameManager.resize(frameId, width, height);
}

function disposeFrame(frameId){
	FrameManager.dispose(frameId);
}

var guiModule = {
	createFrame : createFrame,
	resizeFrame : resizeFrame,
	disposeFrame : disposeFrame
};

module.exports = guiModule;