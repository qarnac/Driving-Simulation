/*
	Copyright (c) 2004-2009, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/


window.YAHOO=window.YAHOO||{};YAHOO.namespace=function(ns){if(!ns||!ns.length){return null;}var _2=ns.split(".");var _3=YAHOO;for(var i=(_2[0]=="YAHOO")?1:0;i<_2.length;++i){_3[_2[i]]=_3[_2[i]]||{};_3=_3[_2[i]];}return _3;};YAHOO.log=function(_5,_6,_7){var l=YAHOO.widget.Logger;if(l&&l.log){return l.log(_5,_6,_7);}else{return false;}};YAHOO.extend=function(_9,_a){var f=function(){};f.prototype=_a.prototype;_9.prototype=new f();_9.prototype.constructor=_9;_9.superclass=_a.prototype;if(_a.prototype.constructor==Object.prototype.constructor){_a.prototype.constructor=_a;}};YAHOO.namespace("util");YAHOO.namespace("widget");YAHOO.namespace("example");function yahooIsOk(){};