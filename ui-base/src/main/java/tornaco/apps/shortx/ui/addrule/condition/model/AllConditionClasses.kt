package tornaco.apps.shortx.ui.addrule.condition.model

val allConditionClasses = listOf(
    Condition.CurrentForegroundApp::class,
    Condition.CurrentForegroundAppByPkg::class,
    Condition.MVEL::class,
    Condition.JS::class,
    Condition.TRUE::class,
    Condition.EvaluateGlobalVar::class,
    Condition.EvaluateContextVar::class,
    Condition.EvaluateLocalVar::class,
    Condition.BatteryPercent::class,
    Condition.AppIsRunning::class,
    Condition.AppIsNotRunning::class,
    Condition.AppHasAudioFocus::class,
    Condition.AppHasWindowFocus::class,
    Condition.ServiceIsRunning::class,
    Condition.ScreenIsOn::class,
    Condition.VPNIsConnected::class,
    Condition.TimeInRange::class,
    Condition.ChargeState::class,
    Condition.PlugState::class,
    Condition.RequireWifiConnected::class,
    Condition.RequireWifiDisconnected::class,
    Condition.RequireBTConnected::class,
    Condition.RequireBTDisconnected::class,
    Condition.RequireBTDeviceFound::class,
    Condition.RequireMobileDataEnabled::class,
    Condition.AppHasNotification::class,
    Condition.KeyguardIsLocked::class,
    Condition.ScreenOrientationIsPort::class,
    Condition.IsInCall::class,
    Condition.IsRinging::class,
    Condition.HasNodeOnScreen::class,
    Condition.IsRuleEnabled::class,
    Condition.IsHeadsetPlug::class,
    Condition.RequireScreenRotate::class,
    Condition.RequireWindowRotation::class,
    Condition.RequireDelay::class,
    Condition.RequireSensorOff::class,
    Condition.CurrentActivity::class,
    Condition.RequireTileState::class,
    Condition.RequireFactTag::class,
    Condition.AppHasTask::class,
    Condition.RequireAPMMode::class,
    Condition.RequireIMEVisibility::class,
    Condition.ConnectedWifiSignal::class,
    Condition.TheXXTimeToday::class,
)