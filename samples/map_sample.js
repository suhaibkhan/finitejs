

var m = new Map;

m.put('s', 123);
m.put('k', 321);

print(m.size());
print(m.get('s'));

m.delete('s');

print(m.size());
print(m.get('s'));
print(m.get('k'));

print(m);

