package tornaco.apps.shortx.ui.addrule.action.model


val allActionClasses = listOf(
    Action.TakeScreenshot::class,
    Action.ShellCommand::class,
    Action.InputText::class,
    Action.Delay::class,
    Action.ShowToast::class,
    Action.ShowDanmu::class,
    Action.ExecuteJS::class,
    Action.MVEL::class,
    Action.RemoteMVEL::class,
    Action.ShowOverlay::class,
    Action.HideAllOverlay::class,
    Action.HideOverlay::class,
    Action.LaunchApp::class,
    Action.LaunchAppByPkg::class,
    Action.StopApp::class,
    Action.StopAppByPkg::class,
    Action.StartActivity::class,
    Action.StartActivityIntent::class,
    Action.StartActivityUrlSchema::class,
    Action.StartActivityIntentUri::class,
    Action.ExpandNotification::class,
    Action.PostNotification::class,
    Action.RemoveNotification::class,
    Action.StartAppProcess::class,
    Action.SetAppInactive::class,
    Action.StartAppProcessByPkg::class,
    Action.SetAppInactiveByPkg::class,
    Action.EnableApp::class,
    Action.DisableApp::class,
    Action.EnableAppByPkg::class,
    Action.DisableAppByPkg::class,
    Action.SuspendApp::class,
    Action.SuspendAppByPkg::class,
    Action.UnSuspendApp::class,
    Action.UnSuspendAppByPkg::class,
    Action.EnableBT::class,
    Action.EnableWifi::class,
    Action.EnableSensorsOff::class,
    Action.EnableHotSpot::class,
    Action.EnableData::class,
    Action.EnableNFC::class,
    Action.EnableFlashLight::class,
    Action.EnableDND::class,
    Action.EnableLocation::class,
    Action.EnableDarkMode::class,
    Action.DisableBT::class,
    Action.DisableWifi::class,
    Action.DisableSensorsOff::class,
    Action.DisableHotSpot::class,
    Action.DisableData::class,
    Action.DisableNFC::class,
    Action.DisableFlashLight::class,
    Action.DisableDND::class,
    Action.DisableLocation::class,
    Action.DisableDarkMode::class,
    Action.Brk::class,
    Action.WhileLoop::class,
    Action.ShowAlertDialog::class,
    Action.ShowMenuDialog::class,
    Action.WriteGV::class,
    Action.CreateGV::class,
    Action.DeleteGV::class,
    Action.MapNav::class,
    Action.MapQueryBus::class,
    Action.FromDA::class,
    Action.FindAndClickViewByText::class,
    Action.FindAndClickViewById::class,
    Action.FindAndClickMatchedView::class,
    Action.InputTap::class,
    Action.InputSwipe::class,
    Action.WaitForIdle::class,
    Action.IfThenElse::class,
    Action.SwitchCase::class,
    Action.ReadClipboard::class,
    Action.WriteClipboard::class,
    Action.GetTextFromScreenNode::class,
    Action.CreatePkgSet::class,
    Action.SetRingerMode::class,
    Action.EnableAutoBrightness::class,
    Action.DisableAutoBrightness::class,
    Action.SetBrightness::class,
    Action.StopService::class,
    Action.StartService::class,
    Action.RemoveTasks::class,
    Action.RemoveTasksByPkg::class,
    Action.KillProcessByName::class,
    Action.RemoveNotificationForPackage::class,
    Action.RemoveNotificationForPackageByPkg::class,
    Action.HttpRequest::class,
    Action.InjectKeyCode::class,
    Action.InjectCombineKeyCode::class,
    Action.WakeupScreen::class,
    Action.SleepScreen::class,
    Action.StartLastApp::class,
    Action.StopCurrentApp::class,
    Action.AdjustVolume::class,
    Action.Vibrate::class,
    Action.AudioRecording::class,
    Action.StopAudioRecording::class,
    Action.SwitchMobileDataSlot::class,
    Action.CreateLocalVar::class,
    Action.WriteLocalVar::class,
    Action.StartGestureRecording::class,
    Action.StopGestureRecording::class,
    Action.ToggleGestureRecording::class,
    Action.InjectGestureRecording::class,
    Action.EnableUniversalCopy::class,
    Action.EnableViewIdViewer::class,
    Action.ShowTextFieldDialog::class,
    Action.TTS::class,
    Action.SetWallpaper::class,
    Action.SetRuleEnabled::class,
    Action.ClickTile::class,
    Action.MediaPlayback::class,
    Action.SetVolume::class,
    Action.BiometricVerify::class,
    Action.AppShortcut::class,
    Action.SetAPMModeEnabled::class,
    Action.SetScreenTimeout::class,
    Action.SetScreenRotate::class,
    Action.ScrollViewTo::class,
    Action.WaitUtilConditionMatch::class,
    Action.PerformContextMenuAction::class,
    Action.ShowDrawBoard::class,
    Action.ConnectWifi::class,
    Action.DisconnectCurrentWifi::class,
    Action.StayAwake::class,
    Action.ForEachPkgSet::class,
    Action.LockDeviceNow::class,
    Action.SetMasterSync::class,
    Action.ClickNotification::class,
    Action.ClickNotificationActionButton::class,
    Action.ShowClipboardView::class,
    Action.PlayRingtone::class,
    Action.Toggle5G::class,
    Action.DownloadFile::class,
    Action.SendSMS::class,
    Action.StopAllActions::class,
    Action.MatchRegex::class,
    Action.NoAction::class,
)