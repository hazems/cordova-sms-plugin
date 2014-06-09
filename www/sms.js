var smsExport = {};

smsExport.sendMessage = function(messageInfo, successCallback, errorCallback) {
	var phoneNumber = messageInfo.phoneNumber;
	var textMessage = messageInfo.textMessage || "Default Text from SMS plugin";

	if (! phoneNumber) {
		console.log("Missing Phone Number");
	
		errorCallback({
			code: "MISSING_PHONE_NUMBER",
			message: "Missing Phone number"
		});
	
		return;
	}

	cordova.exec(successCallback, errorCallback, "Sms", "sendMessage", [phoneNumber, textMessage]);
};

module.exports = smsExport;
