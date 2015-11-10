'use strict';

//Module:plot for visualizing data.

// java imports
var LinePlot = Java.type('com.finitejs.modules.plot.LinePlot');
var PlotContainer = Java.type('com.finitejs.modules.plot.PlotContainer');

var plotStyleManager = require('./plotStyleManager');
// initialize plot style manager
plotStyleManager.init();

/**
 * Represents a plot/visualization.
 * 
 * @class
 * @protected 
 */
function Plot(table){
	this._table = table;
	this._aes = null;
	this._geom = null;
	this._scale = null;
	this._label = null;
	this._container = null;
}

/**
 * Module for visualizing data.
 * Module returns a function, which can be used to create an instance of {@link Plot}.
 * 
 * @exports plot
 * @returns {Plot}
 */
var plot = function(table){
	return new Plot(table);
};

/**
 * Loads the specified theme.
 * 
 * @param {String} theme - theme name
 * @static
 */
plot.theme = function(theme){
	plotStyleManager.loadTheme('' + theme);
};

Plot.prototype.aes = function(aes){
	this._aes = aes;
	return this;
};

Plot.prototype.scale = function(scale){
	this._scale = scale;
	return this;
};

Plot.prototype.label = function(label){
	this._label = label;
	return this;
};

Plot.prototype.geom = function(geom){
	this._geom = geom;
	return this;
};

Plot.prototype.line = function(){
	this.geom('line');
	return this;
};

Plot.prototype.point = function(){
	this.geom('point');
	return this;
};

Plot.prototype.render = function(){
	var nativePlot;
	
	if (this._geom === 'line'){
		nativePlot = new LinePlot('Sample Line Plot');
	}
	
	this._container = new PlotContainer(640, 480, null);
	this._container.init();
	this._container.addPlot(nativePlot);
	this._container.show();
};

module.exports = plot;