package tornaco.apps.shortx.core.context

val allKeysWithDefaultValue by lazy {
    allKeys.associateWith { "" }
}

val allKeys: List<String> = listOf(
    enumValues<ContextDataMapping.AppHasNotification>().map { it.name },
    enumValues<ContextDataMapping.Notification>().map { it.name },
    enumValues<ContextDataMapping.ExecuteMVEL>().map { it.name },
    enumValues<ContextDataMapping.ExecuteJS>().map { it.name },
    enumValues<ContextDataMapping.HttpRequest>().map { it.name },
    enumValues<ContextDataMapping.ActivityStarted>().map { it.name },
    enumValues<ContextDataMapping.ActivityStopped>().map { it.name },
    enumValues<ContextDataMapping.ActivityDestroyed>().map { it.name },
    enumValues<ContextDataMapping.AppBecomeFg>().map { it.name },
    enumValues<ContextDataMapping.AppBecomeBg>().map { it.name },
    enumValues<ContextDataMapping.TaskRemoved>().map { it.name },
    enumValues<ContextDataMapping.AppProcessStarted>().map { it.name },
    enumValues<ContextDataMapping.AppAdded>().map { it.name },
    enumValues<ContextDataMapping.AppRemoved>().map { it.name },
    enumValues<ContextDataMapping.AppUpdated>().map { it.name },
    enumValues<ContextDataMapping.PkgStopRunning>().map { it.name },
    enumValues<ContextDataMapping.AudioFocusChanged>().map { it.name },
    enumValues<ContextDataMapping.ReadClipboard>().map { it.name },
    enumValues<ContextDataMapping.GetTextFromScreenNode>().map { it.name },
    enumValues<ContextDataMapping.ShellCommand>().map { it.name },
    enumValues<ContextDataMapping.WifiConnectedTo>().map { it.name },
    enumValues<ContextDataMapping.ConnectedWifiSignalLevelChanged>().map { it.name },
    enumValues<ContextDataMapping.WifiDisconnectedFrom>().map { it.name },
    enumValues<ContextDataMapping.BatteryTemperatureChanged>().map { it.name },
    enumValues<ContextDataMapping.BatteryLevelChanged>().map { it.name },
    enumValues<ContextDataMapping.MediaStoreInsert>().map { it.name },
    enumValues<ContextDataMapping.AdvancedKeyEvent>().map { it.name },
    enumValues<ContextDataMapping.BTDeviceConnected>().map { it.name },
    enumValues<ContextDataMapping.BTDeviceDisConnected>().map { it.name },
    enumValues<ContextDataMapping.OnStartOp>().map { it.name },
    enumValues<ContextDataMapping.OnFinishOp>().map { it.name },
    enumValues<ContextDataMapping.ShowTextFieldDialog>().map { it.name },
    enumValues<ContextDataMapping.SystemSettingsChanged>().map { it.name },
    enumValues<ContextDataMapping.AppGainWindowFocus>().map { it.name },
    enumValues<ContextDataMapping.AppLostWindowFocus>().map { it.name },
    enumValues<ContextDataMapping.CallStateChanged>().map { it.name },
    enumValues<ContextDataMapping.ClipboardContentChanged>().map { it.name },
    enumValues<ContextDataMapping.HasFoundNodeOnScreen>().map { it.name },
    enumValues<ContextDataMapping.Logcat>().map { it.name },
    enumValues<ContextDataMapping.ForEachPkgSet>().map { it.name },
    enumValues<ContextDataMapping.DownloadFile>().map { it.name },
    enumValues<ContextDataMapping.MatchRegex>().map { it.name },
    enumValues<ContextDataMapping.ReplaceRegex>().map { it.name },
    enumValues<ContextDataMapping.TextProcessing>().map { it.name },
    enumValues<ContextDataMapping.ExportBackup>().map { it.name },
).flatten().distinct().sorted()