cordova.define("com.jsmobile.plugins.sms.sms", function(require, exports, module) { 
    
    var exec = require('cordova/exec');

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
        
        exec(successCallback, errorCallback, "Sms", "sendMessage", [phoneNumber, textMessage]);
    };
    
    module.exports = smsExport;
});
