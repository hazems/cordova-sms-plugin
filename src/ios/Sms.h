#import <Cordova/CDV.h>
#import <MessageUI/MFMessageComposeViewController.h>

@interface Sms : CDVPlugin <MFMessageComposeViewControllerDelegate> {
  // Member variables go here.
    
}

@property(strong) NSString* callbackID;
- (void)sendMessage:(CDVInvokedUrlCommand*)command;
@end