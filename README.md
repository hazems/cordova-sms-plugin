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
<code>
cordova plugin add https://github.com/hazems/cordova-sms-plugin.git
</code>
or
<code>
cordova plugin add com.jsmobile.plugins.sms
</code>

Licence
---
It is Apache License, Version 2.0. Feel free to fork the project and send pull requests if you have any.
 
