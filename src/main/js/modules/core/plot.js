


function plot(data, aes, geom, transform){
	
	var _plot = {};
	
	_plot._data = data || null;
	_plot._aes = aes || null;
	_plot._geom = geom || null;
	_plot._transform = transform || null;
	_plot._layers = [];
	
	_plot.data = function(data){
		if (data){
			_plot._data = data;
		}
		return _plot;
	};
	
	_plot.aes = function(aes){
		if (aes){
			_plot._aes = aes;
		}
		return _plot;
	};
	
	_plot.geom = function(geom){
		if (geom){
			_plot._geom = geom;
		}
		return _plot;
	};
	
	_plot.transform = function(transform){
		if (transform){
			_plot._transform = transform;
		}
		return _plot;
	};
	
	_plot.layer = function(layer){
		if (layer){
			_plot._layers.push(layer);
		}
		return _plot;
	};
	
	_plot.render = function(){
		renderPlot(_plot);
		return _plot;
	};
	
	return _plot;
}

function renderPlot(plot){
	
	// check for data
	if (plot._data )
	
	// create scale
	
}

module.exports = plot;