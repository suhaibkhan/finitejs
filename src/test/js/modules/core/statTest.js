'use strict';

exports.test = {
		
	testMean: function(){
		assert.equals(3.5, mean(1,2,3,4,5,6));
		assert.equals(4.286, Math.round(mean([1,2,3,4.2,5.6,6.9,7.3]) * 1000)/1000);
		assert.checkTrue(isNaN(mean('dff',2,3,4,5,6)));
	},

	testMode: function(){
		assert.deepEquals([1,2,3,4,5,6], mode(1,2,3,4,5,6));
		assert.deepEquals([2,5], mode(1,2,2,4,5,5));
	},
	
	testMax: function(){
		assert.equals(6, max(1,2,3,4,5,6));
	},
	
	testMin: function(){
		assert.equals(1, min(1,2,3,4,5,6));
	},
	
	testSum: function(){
		assert.equals(10, sum(1,2,3,4));
	},
	
	testSumLog: function(){
		var expected = Math.log(1) + Math.log(2) + Math.log(3) + Math.log(4);
		assert.equals(expected, sumLog(1,2,3,4));
	},
	
	testSumSq: function(){
		assert.equals(30, sumSq(1,2,3,4));
	},
	
	testProduct: function(){
		assert.equals(24, product(1,2,3,4));
	},
	
	testVariance: function(){
		assert.equals(3.5, variance(1,2,3,4,5,6));
	},
	
	testSd: function(){
		assert.equals(1.871, Math.round(sd(1,2,3,4,5,6) * 1000)/1000);
		assert.equals(2.427, Math.round(sd([1,2,3,4.2,5.6,6.9,7.3]) * 1000)/1000);
		assert.checkTrue(isNaN(sd('dff',2,3,4,5,6)));
	},
	
	testNormalize: function(){
		var meanOfNormalized = mean(normalize(1,2,3,4,5,6));
		var sdOfNormalized = sd(normalize(1,2,3,4,5,6));
		assert.equals(0, meanOfNormalized);
		assert.equals(1, sdOfNormalized);
	},
	
	testPercentile: function(){
		assert.equals(3, percentile([1,2,3,4,5], 50));
		assert.equals(5, percentile([1,2,3,4,5], 100));
	},
};