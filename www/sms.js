cordova.define("com.jsmobile.plugins.sms.sms", function(require, exports, module) {
var smsExport = {};

smsExport.sendMessage = function(messageInfo, successCallback, errorCallback) {
    if (messageInfo == null || typeof messageInfo !== 'object') {
    
        if (errorCallback) {
            errorCallback({
                code: "INVALID_INPUT",
                message: "Invalid Input"
            });
        }
       
        return;
    }
          
    var phoneNumber = messageInfo.number;
    var textMessage = messageInfo.message || "";
    var index = messageInfo.index || "";
           
    if (! phoneNumber) {
        console.log("Missing Phone Number");
    
        if (errorCallback) {
            errorCallback({
                code: "MISSING_PHONE_NUMBER",
                message: "Missing Phone number"
            });
        }
           
        return;
    }
           
    cordova.exec(successCallback, errorCallback, "Sms", "sendMessage", [phoneNumber, textMessage, index]);
};

module.exports = smsExport;

});
