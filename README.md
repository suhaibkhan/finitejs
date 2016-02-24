
### finite.js
**finite.js** is a JavaScript based data analysis tool inspired from R. One of the 
main limitation of R is its steep learning curve. The main purpose of this project 
is to overcome that limitation by providing an easy to use data analysis tool for 
anyone with basic JavaScript knowledge.

The project is at budding stage. Project wiki and detailed documentation will be
updated with the initial release.
#### Build
```
cd <to finitejs folder>
./gradlew build
```
#### Start
```
cd build/distributions/finitejs-0.1.0
./finitejs
```
#### Test
```js
read.csv('samples/sample.csv');
quit();
```
#### Disclaimer
finite.js uses JxBrowser http://www.teamdev.com/jxbrowser, which is a proprietary software. The use of JxBrowser is governed by JxBrowser Product Licence Agreement http://www.teamdev.com/jxbrowser-licence-agreement. If you would like to use JxBrowser in your development, please contact TeamDev.
#### Contribute
finite.js uses Nashorn JavaScript engine which is a part of Java 8, to execute JavaScript 
and it follows CommonJS module system similar to Node.js. So any JavaScript developer can
write a new module for finite.js. Modules that include native functionality should be 
implemented in Java and can be executed from JavaScript with the help of Nashorn API.

If you are interested to know about development plans, contact me at suhaibklm@gmail.com.
