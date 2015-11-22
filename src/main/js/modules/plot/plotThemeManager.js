'use strict';

// Module used by plot module for loading themes

// java imports
var PlotThemeManager = Java.type('com.finitejs.modules.plot.PlotThemeManager');

var curTheme, plotThemeManager;

// initialize plot theme manager
PlotThemeManager.init(__dirname);
//get instance of plot theme manager
plotThemeManager = PlotThemeManager.getInstance();

// load a theme
function loadTheme(theme){
	
	var themeContents, themeMap;
	
	// check whether theme is already loaded
	if (curTheme === theme){
		return;
	}
	
	themeContents = plotThemeManager.readTheme(theme);
	themeMap = JSON.parse(themeContents);
	// convert every value in themeMap to string
	for (var key in themeMap){
		if (themeMap[key] !== null && themeMap.hasOwnProperty(key)) {
			themeMap[key] = '' + themeMap[key];
		}
	}
	plotThemeManager.setTheme(themeMap);
	
	// set as current theme
	curTheme = theme;
}

module.exports = {
	
	/**
	 * Initializes plot theme manager by loading current configured theme.
	 */
	init : function(){
		// default theme is loaded before current theme
		loadTheme(PlotThemeManager.DEFAULT_THEME);
		
		// load current theme
		if (module.parent && module.parent.info && 
				module.parent.info.config && module.parent.info.config.theme){
			loadTheme(module.parent.info.config.theme);
		}
	},
	
	/**
	 * Loads the specified theme in plot theme manager. Will skip
	 * the operation if theme already loaded.
	 * 
	 * @param {String} theme - theme name 
	 */
	loadTheme : loadTheme
};