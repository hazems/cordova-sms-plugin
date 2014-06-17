Sms Custom Cordova Plugin:
====================
This plugin allows you to send SMS message to a specific phone number for Android, iOS and Windows Platform 8. Here is an example below:


	var messageInfo = {
		phoneNumber: "xxxxxxxxxx",
		textMessage: "This is a test message"
	};
	
	sms.sendMessage(messageInfo, function(message) {
		console.log("success: " + message);
	}, function(error) {
		console.log("code: " + error.code + ", message: " + error.message);
	});
	
As you notice you just need to call *sms.sendMessage(messageInfo, successCallback, failureCallback)*:

 * *messageInfo* is a JSON object which contains phoneNumber and textMessage attributes.
 * *sucessCallback* is a callback function which will be called if the send SMS operation succeeds.
 * *errorCallback* is a callback function which will be called if the send SMS operation fails.

Installing the plugin
---
In order to install the plugin you can simply use the following Cordova CLI command: 
	
	cordova plugin add https://github.com/hazems/cordova-sms-plugin.git
	
or

	cordova plugin add com.jsmobile.plugins.sms


Important Note
---
For iOS and Windows Phone platforms, it is not possible to send SMS directly without opening the default device SMS application. This means that the API will only open and track the SMS application events (cancel, successful sending, un-successful sending, ...etc) in iOS platform. For Windows Phone 8, the plugin will only open the SMS application without tracking its events because tracking SMS application events is not currently applicable in Windows 8 platform.

Test Client
---
You can reach the test client of this plugin here:
 [https://github.com/hazems/cordova-sms-plugin-test/ ](https://github.com/hazems/cordova-sms-plugin-test/)

Used Resources
---
Special Thanks to [appcoda.com]() for helping me in implementing the iOS part of this plugin:

[http://www.appcoda.com/ios-programming-send-sms-text-message/ ](http://www.appcoda.com/ios-programming-send-sms-text-message/)

Licence
---
It is Apache License, Version 2.0. Feel free to fork the project and send pull requests if you have any.
 