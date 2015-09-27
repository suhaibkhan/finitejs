
function Map(){
	var HashMap = Java.type('java.util.HashMap');
	this._hashMap = new HashMap;
}

Map.prototype = {
	set: function(k, v) { this._hashMap.put(k, v); return this; },
	put: function(k, v) { this._hashMap.put(k, v); return this; },
	get: function(k) { return this._hashMap.get(k)},
	has: function(k) { return this._hashMap.containsKey(k); },
	contains: function(k) { return this._hashMap.containsKey(k); },
	delete: function(k) { return this._hashMap.remove(k) !== null; },
	remove: function(k) { return this._hashMap.remove(k) !== null; },
	clear: function() { this._hashMap.clear(); },
	size: function() { return this._hashMap.size(); },
	forEach: function(c) { for each (var k in this._hashMap.keySet()){ c(this._hashMap.get(k), k, this); } }
};

module.exports = Map;