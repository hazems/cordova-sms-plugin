using System;
using Microsoft.Phone.Tasks;
using WPCordovaClassLib.Cordova;
using WPCordovaClassLib.Cordova.Commands;
using WPCordovaClassLib.Cordova.JSON;

namespace WPCordovaClassLib.Cordova.Commands
{
  public class Sms : BaseCommand
  {
    public void coolMethod(string options)
    {
      string[] optValues = JsonHelper.Deserialize<string[]>(options);
      String message = optValues[0];
      
	  DispatchCommandResult(new PluginResult(PluginResult.Status.OK, message));
    }
  }
}