/********* Sms.m Cordova Plugin Implementation *******/

#import "Sms.h"

@implementation Sms

- (void)sendMessage:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* phoneNumber = [command.arguments objectAtIndex:0];
    NSString* textMessage = [command.arguments objectAtIndex:1];
    
    self.callbackID = command.callbackId;
    
    if (![MFMessageComposeViewController canSendText]) {
        NSMutableDictionary* returnInfo = [NSMutableDictionary dictionaryWithCapacity:2];
        
        [returnInfo setObject:@"SMS_FEATURE_NOT_SUPPORTED" forKey:@"code"];
        [returnInfo setObject:@"SMS feature is not supported on this device" forKey:@"message"];
        
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:returnInfo];
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        
        return;
    }
    
    MFMessageComposeViewController *composeViewController = [[MFMessageComposeViewController alloc] init];
    composeViewController.messageComposeDelegate = self;
    
    NSMutableArray *recipients = [[NSMutableArray alloc] init];
    
    [recipients addObject:phoneNumber];
    
    [composeViewController setBody:textMessage];
    [composeViewController setRecipients:recipients];
    
    [self.viewController presentViewController:composeViewController animated:YES completion:nil];
}

// Handle the different situations of MFMessageComposeViewControllerDelegate
- (void)messageComposeViewController:(MFMessageComposeViewController *)controller didFinishWithResult:(MessageComposeResult)result {
    BOOL succeeded = NO;
    NSString* errorCode = @"";
    NSString* message = @"";
    
    switch(result) {
        case MessageComposeResultSent:
            succeeded = YES;
            message = @"Message sent";
            break;
        case MessageComposeResultCancelled:
            message = @"Message cancelled";
            errorCode = @"SMS_MESSAGE_CANCELLED";
            break;
        case MessageComposeResultFailed:
            message = @"Message Compose Result failed";
            errorCode = @"SMS_MESSAGE_COMPOSE_FAILED";
            break;
        default:
            message = @"Sms General error";
            errorCode = @"SMS_GENERAL_ERROR";
            break;
    }
    
    [self.viewController dismissViewControllerAnimated:YES completion:nil];
    
    if (succeeded == YES) {
        [super writeJavascript:[[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message]
                                toSuccessCallbackString:self.callbackID]];
    } else {
        NSMutableDictionary* returnInfo = [NSMutableDictionary dictionaryWithCapacity:2];
        
        [returnInfo setObject:errorCode forKey:@"code"];
        [returnInfo setObject:message forKey:@"message"];
        
        [super writeJavascript:[[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:returnInfo]
                                toErrorCallbackString:self.callbackID]];
    }
}

@end
