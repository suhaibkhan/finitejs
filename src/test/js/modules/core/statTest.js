'use strict';

var stat = require('stat');

exports.test = {
		
	testMean: function(){
		assert.equals(3.5, stat.mean(1,2,3,4,5,6));
		assert.equals(4.286, Math.round(stat.mean([1,2,3,4.2,5.6,6.9,7.3]) * 1000)/1000);
		assert.checkTrue(isNaN(stat.mean('dff',2,3,4,5,6)));
	},

	testStd: function(){
		assert.equals(1.871, Math.round(stat.std(1,2,3,4,5,6) * 1000)/1000);
		assert.equals(2.427, Math.round(stat.std([1,2,3,4.2,5.6,6.9,7.3]) * 1000)/1000);
		assert.checkTrue(isNaN(stat.std('dff',2,3,4,5,6)));
	}
};