using System;
using Microsoft.Phone.Tasks;
using WPCordovaClassLib.Cordova;
using WPCordovaClassLib.Cordova.Commands;
using WPCordovaClassLib.Cordova.JSON;

namespace WPCordovaClassLib.Cordova.Commands
{
  public class Sms : BaseCommand
  {

    public void sendMessage(string options)
    {
      string[] optValues = JsonHelper.Deserialize<string[]>(options);
      String number = optValues[0];
      String message = optValues[1];

      SmsComposeTask sms = new SmsComposeTask();

      sms.To = number;
      sms.Body = message;

      sms.Show();

      //Since there is no way to track SMS application events in WP8, always send Ok status.
      DispatchCommandResult(new PluginResult(PluginResult.Status.OK, "Success"));
    }
  }
}