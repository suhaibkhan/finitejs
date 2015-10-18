
var util = require('util');

exports.test = {
	
	testToStringWithArray : function(){
		assert.deepEquals(['1', '2', '3'], util.toStringWithArray([1, 2, 3]));
		assert.equals('{"s":2}', util.toStringWithArray({s : 2}));
		assert.equals('2', util.toStringWithArray(2));
	},
	
	testIsSingleArray : function(){
		assert.checkTrue(util.isSingleArray([1, 2, 3]));
		assert.checkFalse(util.isSingleArray({s : 2}));
		assert.checkFalse(util.isSingleArray([1, [2], 3]));
	},
	
	testIsTwoLevelArray : function(){
		assert.checkFalse(util.isTwoLevelArray([1, 2, 3]));
		assert.checkFalse(util.isTwoLevelArray({s : 2}));
		assert.checkFalse(util.isTwoLevelArray([1, [2], 3]));
		assert.checkTrue(util.isTwoLevelArray([[1,2], [2,5], [3,4]]));
		assert.checkFalse(util.isTwoLevelArray([[1,[2]], [2,[5]], [3,[4]]]));
	},
	
	testEquals : function(){
		assert.checkFalse(util.equals([1,2,3,4], [1, 2, 3]));
		assert.checkTrue(util.equals({s : {d:2}}, {s: {d: 2}}));
		assert.checkFalse(util.equals({s : {d:2}}, {s: {d: 3}}));
		assert.checkFalse(util.equals(1, 2));
		assert.checkTrue(util.equals([[1,[2]], [2,[5]], [3,[4]]], [[1,[2]], [2,[5]], [3,[4]]]));
		assert.checkFalse(util.equals([[1,[2]], [2,[5]], [3,[5]]], [[1,[2]], [2,[5]], [3,[4]]]));

	},
	
	testSprintf: function(){
		assert.equals('1-test', util.sprintf('%d-%s', 1, 'test'));
	}
	
};