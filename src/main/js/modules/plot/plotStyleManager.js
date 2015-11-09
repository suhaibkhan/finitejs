'use strict';

// Module used by plot module for loading themes

// java imports
var PlotStyleManager = Java.type('com.finitejs.modules.plot.PlotStyleManager');

var curTheme, plotStyleManager;

// initialize plot style manager
PlotStyleManager.init(__dirname);
//get instance of plot style manager
plotStyleManager = PlotStyleManager.getInstance();

// load a theme
function loadTheme(theme){
	
	var themeContents, themeMap;
	
	// check whether theme is already loaded
	if (curTheme === theme){
		return;
	}
	
	themeContents = plotStyleManager.readTheme(theme);
	themeMap = JSON.parse(themeContents);
	// convert every value in themeMap to string
	for (var key in themeMap){
		if (themeMap[key] !== null && themeMap.hasOwnProperty(key)) {
			themeMap[key] = '' + themeMap[key];
		}
	}
	plotStyleManager.setTheme(themeMap);
	
	// set as current theme
	curTheme = theme;
}

module.exports = {
	
	/**
	 * Initializes plot style manager by loading current configured theme.
	 */
	init : function(){
		// default theme is loaded before current theme
		loadTheme(PlotStyleManager.DEFAULT_THEME);
		
		// load current theme
		if (module.parent && module.parent.info && 
				module.parent.info.config && module.parent.info.config.theme){
			loadTheme(module.parent.info.config.theme);
		}
	},
	
	/**
	 * Loads the specified theme in plot style manager. Will skip
	 * the operation if theme already loaded.
	 * 
	 * @param {String} theme - theme name 
	 */
	loadTheme : loadTheme,
	
	/**
	 * Returns specified plot style property.
	 * 
	 * @param {String} key - style property key
	 * @returns {String} property value
	 */
	get : plotStyleManager.get
};