/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tornaco.apps.shortx.core.compat;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import android.annotation.ColorInt;
import android.annotation.IntDef;
import android.annotation.NonNull;
import android.annotation.Nullable;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.Gravity;
import android.widget.RemoteViews;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import tornaco.apps.shortx.core.util.ResUtils;


/**
 * Helper for accessing features in {@link Notification}.
 */
public class NotificationCompat {

    /**
     * Use all default values (where applicable).
     */
    public static final int DEFAULT_ALL = ~0;

    /**
     * Use the default notification sound. This will ignore any sound set using
     * {@link Builder#setSound}
     *
     * <p>
     * A notification that is noisy is more likely to be presented as a heads-up notification,
     * on some platforms.
     * </p>
     *
     * @see Builder#setDefaults
     */
    public static final int DEFAULT_SOUND = 1;

    /**
     * Use the default notification vibrate. This will ignore any vibrate set using
     * {@link Builder#setVibrate}. Using phone vibration requires the
     * {@link android.Manifest.permission#VIBRATE VIBRATE} permission.
     *
     * <p>
     * A notification that vibrates is more likely to be presented as a heads-up notification,
     * on some platforms.
     * </p>
     *
     * @see Builder#setDefaults
     */
    public static final int DEFAULT_VIBRATE = 2;

    /**
     * Use the default notification lights. This will ignore the
     * {@link #FLAG_SHOW_LIGHTS} bit, and values set with {@link Builder#setLights}.
     *
     * @see Builder#setDefaults
     */
    public static final int DEFAULT_LIGHTS = 4;

    /**
     * Use this constant as the value for audioStreamType to request that
     * the default stream type for notifications be used.  Currently the
     * default stream type is {@link AudioManager#STREAM_NOTIFICATION}.
     */
    public static final int STREAM_DEFAULT = -1;
    /**
     * Bit set in the Notification flags field when LEDs should be turned on
     * for this notification.
     */
    public static final int FLAG_SHOW_LIGHTS = 0x00000001;

    /**
     * Bit set in the Notification flags field if this notification is in
     * reference to something that is ongoing, like a phone call.  It should
     * not be set if this notification is in reference to something that
     * happened at a particular point in time, like a missed phone call.
     */
    public static final int FLAG_ONGOING_EVENT = 0x00000002;

    /**
     * Bit set in the Notification flags field if
     * the audio will be repeated until the notification is
     * cancelled or the notification window is opened.
     */
    public static final int FLAG_INSISTENT = 0x00000004;

    /**
     * Bit set in the Notification flags field if the notification's sound,
     * vibrate and ticker should only be played if the notification is not already showing.
     */
    public static final int FLAG_ONLY_ALERT_ONCE = 0x00000008;

    /**
     * Bit set in the Notification flags field if the notification should be canceled when
     * it is clicked by the user.
     */
    public static final int FLAG_AUTO_CANCEL = 0x00000010;

    /**
     * Bit set in the Notification flags field if the notification should not be canceled
     * when the user clicks the Clear all button.
     */
    public static final int FLAG_NO_CLEAR = 0x00000020;

    /**
     * Bit set in the Notification flags field if this notification represents a currently
     * running service.  This will normally be set for you by
     * {@link android.app.Service#startForeground}.
     */
    public static final int FLAG_FOREGROUND_SERVICE = 0x00000040;

    /**
     * Obsolete flag indicating high-priority notifications; use the priority field instead.
     *
     * @deprecated Use {@link Builder#setPriority(int)} with a positive value.
     */
    @Deprecated
    public static final int FLAG_HIGH_PRIORITY = 0x00000080;

    /**
     * Bit set in the Notification flags field if this notification is relevant to the current
     * device only and it is not recommended that it bridge to other devices.
     */
    public static final int FLAG_LOCAL_ONLY = 0x00000100;

    /**
     * Bit set in the Notification flags field if this notification is the group summary for a
     * group of notifications. Grouped notifications may display in a cluster or stack on devices
     * which support such rendering. Requires a group key also be set using
     * {@link Builder#setGroup}.
     */
    public static final int FLAG_GROUP_SUMMARY = 0x00000200;

    /**
     * Default notification priority for {@link Builder#setPriority(int)}.
     * If your application does not prioritize its own notifications,
     * use this value for all notifications.
     */
    public static final int PRIORITY_DEFAULT = 0;

    /**
     * Lower notification priority for {@link Builder#setPriority(int)},
     * for items that are less important. The UI may choose to show
     * these items smaller, or at a different position in the list,
     * compared with your app's {@link #PRIORITY_DEFAULT} items.
     */
    public static final int PRIORITY_LOW = -1;

    /**
     * Lowest notification priority for {@link Builder#setPriority(int)};
     * these items might not be shown to the user except under
     * special circumstances, such as detailed notification logs.
     */
    public static final int PRIORITY_MIN = -2;

    /**
     * Higher notification priority for {@link Builder#setPriority(int)},
     * for more important notifications or alerts. The UI may choose
     * to show these items larger, or at a different position in
     * notification lists, compared with your app's {@link #PRIORITY_DEFAULT} items.
     */
    public static final int PRIORITY_HIGH = 1;

    /**
     * Highest notification priority for {@link Builder#setPriority(int)},
     * for your application's most important items that require the user's
     * prompt attention or input.
     */
    public static final int PRIORITY_MAX = 2;

    /**
     * Notification extras key: this is the title of the notification,
     * as supplied to {@link Builder#setContentTitle(CharSequence)}.
     */
    public static final String EXTRA_TITLE = "android.title";

    /**
     * Notification extras key: this is the title of the notification when shown in expanded form,
     * e.g. as supplied to {@link BigTextStyle#setBigContentTitle(CharSequence)}.
     */
    public static final String EXTRA_TITLE_BIG = EXTRA_TITLE + ".big";

    /**
     * Notification extras key: this is the main text payload, as supplied to
     * {@link Builder#setContentText(CharSequence)}.
     */
    public static final String EXTRA_TEXT = "android.text";

    /**
     * Notification extras key: this is a third line of text, as supplied to
     * {@link Builder#setSubText(CharSequence)}.
     */
    public static final String EXTRA_SUB_TEXT = "android.subText";

    /**
     * Notification extras key: this is the remote input history, as supplied to
     * {@link Builder#setRemoteInputHistory(CharSequence[])}.
     * <p>
     * Apps can fill this through {@link Builder#setRemoteInputHistory(CharSequence[])}
     * with the most recent inputs that have been sent through a {@link tornaco.apps.shortx.core.compat.RemoteInput} of this
     * Notification and are expected to clear it once the it is no longer relevant (e.g. for chat
     * notifications once the other party has responded).
     * <p>
     * The extra with this key is of type CharSequence[] and contains the most recent entry at
     * the 0 index, the second most recent at the 1 index, etc.
     *
     * @see Builder#setRemoteInputHistory(CharSequence[])
     */
    public static final String EXTRA_REMOTE_INPUT_HISTORY = "android.remoteInputHistory";

    /**
     * Notification extras key: this is a small piece of additional text as supplied to
     * {@link Builder#setContentInfo(CharSequence)}.
     */
    public static final String EXTRA_INFO_TEXT = "android.infoText";

    /**
     * Notification extras key: this is a line of summary information intended to be shown
     * alongside expanded notifications, as supplied to (e.g.)
     * {@link BigTextStyle#setSummaryText(CharSequence)}.
     */
    public static final String EXTRA_SUMMARY_TEXT = "android.summaryText";

    /**
     * Notification extras key: this is the longer text shown in the big form of a
     * {@link BigTextStyle} notification, as supplied to
     * {@link BigTextStyle#bigText(CharSequence)}.
     */
    public static final String EXTRA_BIG_TEXT = "android.bigText";

    /**
     * Notification extras key: this is the resource ID of the notification's main small icon, as
     * supplied to {@link Builder#setSmallIcon(int)}.
     */
    public static final String EXTRA_SMALL_ICON = "android.icon";

    /**
     * Notification extras key: this is a bitmap to be used instead of the small icon when showing the
     * notification payload, as
     * supplied to {@link Builder#setLargeIcon(Bitmap)}.
     */
    public static final String EXTRA_LARGE_ICON = "android.largeIcon";

    /**
     * Notification extras key: this is a bitmap to be used instead of the one from
     * {@link Builder#setLargeIcon(Bitmap)} when the notification is
     * shown in its expanded form, as supplied to
     * {@link BigPictureStyle#bigLargeIcon(Bitmap)}.
     */
    public static final String EXTRA_LARGE_ICON_BIG = EXTRA_LARGE_ICON + ".big";

    /**
     * Notification extras key: this is the progress value supplied to
     * {@link Builder#setProgress(int, int, boolean)}.
     */
    public static final String EXTRA_PROGRESS = "android.progress";

    /**
     * Notification extras key: this is the maximum value supplied to
     * {@link Builder#setProgress(int, int, boolean)}.
     */
    public static final String EXTRA_PROGRESS_MAX = "android.progressMax";

    /**
     * Notification extras key: whether the progress bar is indeterminate, supplied to
     * {@link Builder#setProgress(int, int, boolean)}.
     */
    public static final String EXTRA_PROGRESS_INDETERMINATE = "android.progressIndeterminate";

    /**
     * Notification extras key: whether the when field set using {@link Builder#setWhen} should
     * be shown as a count-up timer (specifically a {@link android.widget.Chronometer}) instead
     * of a timestamp, as supplied to {@link Builder#setUsesChronometer(boolean)}.
     */
    public static final String EXTRA_SHOW_CHRONOMETER = "android.showChronometer";

    /**
     * Notification extras key: whether the when field set using {@link Builder#setWhen} should
     * be shown, as supplied to {@link Builder#setShowWhen(boolean)}.
     */
    public static final String EXTRA_SHOW_WHEN = "android.showWhen";

    /**
     * Notification extras key: this is a bitmap to be shown in {@link BigPictureStyle} expanded
     * notifications, supplied to {@link BigPictureStyle#bigPicture(Bitmap)}.
     */
    public static final String EXTRA_PICTURE = "android.picture";

    /**
     * Notification extras key: An array of CharSequences to show in {@link InboxStyle} expanded
     * notifications, each of which was supplied to {@link InboxStyle#addLine(CharSequence)}.
     */
    public static final String EXTRA_TEXT_LINES = "android.textLines";

    /**
     * Notification extras key: A string representing the name of the specific
     * {@link Notification.Style} used to create this notification.
     */
    public static final String EXTRA_TEMPLATE = "android.template";

    /**
     * Notification extras key: A String array containing the people that this
     * notification relates to, each of which was supplied to
     * {@link Builder#addPerson(String)}.
     */
    public static final String EXTRA_PEOPLE = "android.people";

    /**
     * Notification extras key: A
     * {@link android.content.ContentUris content URI} pointing to an image that can be displayed
     * in the background when the notification is selected. The URI must point to an image stream
     * suitable for passing into
     * {@link android.graphics.BitmapFactory#decodeStream(java.io.InputStream)
     * BitmapFactory.decodeStream}; all other content types will be ignored. The content provider
     * URI used for this purpose must require no permissions to read the image data.
     */
    public static final String EXTRA_BACKGROUND_IMAGE_URI = "android.backgroundImageUri";

    /**
     * Notification key: A
     * {@link android.media.session.MediaSession.Token} associated with a
     * {@link Notification.MediaStyle} notification.
     */
    public static final String EXTRA_MEDIA_SESSION = "android.mediaSession";


    /**
     * Keys into the {@link #getExtras} Bundle: the audio contents of this notification.
     * <p>
     * This is for use when rendering the notification on an audio-focused interface;
     * the audio contents are a complete sound sample that contains the contents/body of the
     * notification. This may be used in substitute of a Text-to-Speech reading of the
     * notification. For example if the notification represents a voice message this should point
     * to the audio of that message.
     * <p>
     * The data stored under this key should be a String representation of a Uri that contains the
     * audio contents in one of the following formats: WAV, PCM 16-bit, AMR-WB.
     * <p>
     * This extra is unnecessary if you are using {@code MessagingStyle} since each {@code Message}
     * has a field for holding data URI. That field can be used for audio.
     * See {@code Message#setData}.
     * <p>
     * Example usage:
     * <pre>
     * {@code
     * NotificationCompat.Builder myBuilder = (build your Notification as normal);
     * myBuilder.getExtras().putString(EXTRA_AUDIO_CONTENTS_URI, myAudioUri.toString());
     * }
     * </pre>
     */
    public static final String EXTRA_AUDIO_CONTENTS_URI = "android.audioContents";

    /**
     * Value of {@link Notification#color} equal to 0 (also known as
     * {@link Color#TRANSPARENT Color.TRANSPARENT}),
     * telling the system not to decorate this notification with any special color but instead use
     * default colors when presenting this notification.
     */
    @ColorInt
    public static final int COLOR_DEFAULT = Color.TRANSPARENT;

    /**
     *
     */
    // @RestrictTo(LIBRARY_GROUP)
    @IntDef({AudioManager.STREAM_VOICE_CALL, AudioManager.STREAM_SYSTEM, AudioManager.STREAM_RING,
            AudioManager.STREAM_MUSIC, AudioManager.STREAM_ALARM, AudioManager.STREAM_NOTIFICATION,
            AudioManager.STREAM_DTMF, AudioManager.STREAM_ACCESSIBILITY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StreamType {
    }

    /**
     *
     */
    // @RestrictTo(LIBRARY_GROUP)
    @Retention(SOURCE)
    @IntDef({VISIBILITY_PUBLIC, VISIBILITY_PRIVATE, VISIBILITY_SECRET})
    public @interface NotificationVisibility {
    }

    /**
     * Notification visibility: Show this notification in its entirety on all lockscreens.
     * <p>
     * {@see android.app.Notification#visibility}
     */
    public static final int VISIBILITY_PUBLIC = Notification.VISIBILITY_PUBLIC;

    /**
     * Notification visibility: Show this notification on all lockscreens, but conceal sensitive or
     * private information on secure lockscreens.
     * <p>
     * {@see android.app.Notification#visibility}
     */
    public static final int VISIBILITY_PRIVATE = Notification.VISIBILITY_PRIVATE;

    /**
     * Notification visibility: Do not reveal any part of this notification on a secure lockscreen.
     * <p>
     * {@see android.app.Notification#visibility}
     */
    public static final int VISIBILITY_SECRET = Notification.VISIBILITY_SECRET;

    /**
     * Notification category: incoming call (voice or video) or similar synchronous communication request.
     */
    public static final String CATEGORY_CALL = Notification.CATEGORY_CALL;

    /**
     * Notification category: incoming direct message (SMS, instant message, etc.).
     */
    public static final String CATEGORY_MESSAGE = Notification.CATEGORY_MESSAGE;

    /**
     * Notification category: asynchronous bulk message (email).
     */
    public static final String CATEGORY_EMAIL = Notification.CATEGORY_EMAIL;

    /**
     * Notification category: calendar event.
     */
    public static final String CATEGORY_EVENT = Notification.CATEGORY_EVENT;

    /**
     * Notification category: promotion or advertisement.
     */
    public static final String CATEGORY_PROMO = Notification.CATEGORY_PROMO;

    /**
     * Notification category: alarm or timer.
     */
    public static final String CATEGORY_ALARM = Notification.CATEGORY_ALARM;

    /**
     * Notification category: progress of a long-running background operation.
     */
    public static final String CATEGORY_PROGRESS = Notification.CATEGORY_PROGRESS;

    /**
     * Notification category: social network or sharing update.
     */
    public static final String CATEGORY_SOCIAL = Notification.CATEGORY_SOCIAL;

    /**
     * Notification category: error in background operation or authentication status.
     */
    public static final String CATEGORY_ERROR = Notification.CATEGORY_ERROR;

    /**
     * Notification category: media transport control for playback.
     */
    public static final String CATEGORY_TRANSPORT = Notification.CATEGORY_TRANSPORT;

    /**
     * Notification category: system or device status update.  Reserved for system use.
     */
    public static final String CATEGORY_SYSTEM = Notification.CATEGORY_SYSTEM;

    /**
     * Notification category: indication of running background service.
     */
    public static final String CATEGORY_SERVICE = Notification.CATEGORY_SERVICE;

    /**
     * Notification category: user-scheduled reminder.
     */
    public static final String CATEGORY_REMINDER = Notification.CATEGORY_REMINDER;

    /**
     * Notification category: a specific, timely recommendation for a single thing.
     * For example, a news app might want to recommend a news story it believes the user will
     * want to read next.
     */
    public static final String CATEGORY_RECOMMENDATION =
            Notification.CATEGORY_RECOMMENDATION;

    /**
     * Notification category: ongoing information about device or contextual status.
     */
    public static final String CATEGORY_STATUS = Notification.CATEGORY_STATUS;

    /**
     *
     */
    @Retention(RetentionPolicy.SOURCE)
    // @RestrictTo(LIBRARY_GROUP)
    @IntDef({BADGE_ICON_NONE, BADGE_ICON_SMALL, BADGE_ICON_LARGE})
    public @interface BadgeIconType {
    }

    /**
     * If this notification is being shown as a badge, always show as a number.
     */
    public static final int BADGE_ICON_NONE = Notification.BADGE_ICON_NONE;

    /**
     * If this notification is being shown as a badge, use the icon provided to
     * {@link Builder#setSmallIcon(int)} to represent this notification.
     */
    public static final int BADGE_ICON_SMALL = Notification.BADGE_ICON_SMALL;

    /**
     * If this notification is being shown as a badge, use the icon provided to
     * {@link Builder#setLargeIcon(Bitmap) to represent this notification.
     */
    public static final int BADGE_ICON_LARGE = Notification.BADGE_ICON_LARGE;

    /**
     *
     */
    @Retention(RetentionPolicy.SOURCE)
    // @RestrictTo(LIBRARY_GROUP)
    @IntDef({GROUP_ALERT_ALL, GROUP_ALERT_SUMMARY, GROUP_ALERT_CHILDREN})
    public @interface GroupAlertBehavior {
    }

    /**
     * Constant for {@link Builder#setGroupAlertBehavior(int)}, meaning that all notifications in a
     * group with sound or vibration ought to make sound or vibrate (respectively), so this
     * notification will not be muted when it is in a group.
     */
    public static final int GROUP_ALERT_ALL = Notification.GROUP_ALERT_ALL;

    /**
     * Constant for {@link Builder#setGroupAlertBehavior(int)}, meaning that all children
     * notification in a group should be silenced (no sound or vibration) even if they would
     * otherwise make sound or vibrate. Use this constant to mute this notification if this
     * notification is a group child. This must be applied to all children notifications you want
     * to mute.
     *
     * <p> For example, you might want to use this constant if you post a number of children
     * notifications at once (say, after a periodic sync), and only need to notify the user
     * audibly once.
     */
    public static final int GROUP_ALERT_SUMMARY = Notification.GROUP_ALERT_SUMMARY;

    /**
     * Constant for {@link Builder#setGroupAlertBehavior(int)}, meaning that the summary
     * notification in a group should be silenced (no sound or vibration) even if they would
     * otherwise make sound or vibrate. Use this constant
     * to mute this notification if this notification is a group summary.
     *
     * <p>For example, you might want to use this constant if only the children notifications
     * in your group have content and the summary is only used to visually group notifications
     * rather than to alert the user that new information is available.
     */
    public static final int GROUP_ALERT_CHILDREN = Notification.GROUP_ALERT_CHILDREN;

    /**
     * Builder class for {@link NotificationCompat} objects.  Allows easier control over
     * all the flags, as well as help constructing the typical notification layouts.
     * <p>
     * On platform versions that don't offer expanded notifications, methods that depend on
     * expanded notifications have no effect.
     * </p>
     * <p>
     * For example, action buttons won't appear on platforms prior to Android 4.1. Action
     * buttons depend on expanded notifications, which are only available in Android 4.1
     * and later.
     * <p>
     * For this reason, you should always ensure that UI controls in a notification are also
     * available in an {@link Activity} in your app, and you should always start that
     * {@link Activity} when users click the notification. To do this, use the
     * {@link Builder#setContentIntent setContentIntent()}
     * method.
     * </p>
     */
    public static class Builder {
        /**
         * Maximum length of CharSequences accepted by Builder and friends.
         *
         * <p>
         * Avoids spamming the system with overly large strings such as full e-mails.
         */
        private static final int MAX_CHARSEQUENCE_LENGTH = 5 * 1024;

        // All these variables are declared public/hidden so they can be accessed by a builder
        // extender.

        /**
         *
         */
        // @RestrictTo(LIBRARY_GROUP)
        public Context mContext;

        /**
         *
         */
        // @RestrictTo(LIBRARY_GROUP)
        public ArrayList<Action> mActions = new ArrayList<>();

        // Invisible actions are stored in the CarExtender bundle without actually being owned by
        // CarExtender. This is to comply with an optimization of the Android OS which removes
        // Actions from the Notification if there are no listeners for those Actions.
        ArrayList<Action> mInvisibleActions = new ArrayList<>();

        CharSequence mContentTitle;
        CharSequence mContentText;
        PendingIntent mContentIntent;
        PendingIntent mFullScreenIntent;
        RemoteViews mTickerView;
        Bitmap mLargeIcon;
        CharSequence mContentInfo;
        int mNumber;
        int mPriority;
        boolean mShowWhen = true;
        boolean mUseChronometer;
        Style mStyle;
        CharSequence mSubText;
        CharSequence[] mRemoteInputHistory;
        int mProgressMax;
        int mProgress;
        boolean mProgressIndeterminate;
        String mGroupKey;
        boolean mGroupSummary;
        String mSortKey;
        boolean mLocalOnly = false;
        boolean mColorized;
        boolean mColorizedSet;
        String mCategory;
        Bundle mExtras;
        int mColor = COLOR_DEFAULT;
        @NotificationVisibility
        int mVisibility = VISIBILITY_PRIVATE;
        Notification mPublicVersion;
        RemoteViews mContentView;
        RemoteViews mBigContentView;
        RemoteViews mHeadsUpContentView;
        String mChannelId;
        int mBadgeIcon = BADGE_ICON_NONE;
        String mShortcutId;
        long mTimeout;
        @GroupAlertBehavior
        int mGroupAlertBehavior = GROUP_ALERT_ALL;
        Notification mNotification = new Notification();

        /**
         * @deprecated This field was not meant to be public.
         */
        @Deprecated
        public ArrayList<String> mPeople;

        /**
         * Constructor.
         * <p>
         * Automatically sets the when field to {@link System#currentTimeMillis()
         * System.currentTimeMillis()} and the audio stream to the
         * {@link Notification#STREAM_DEFAULT}.
         *
         * @param context   A {@link Context} that will be used to construct the
         *                  RemoteViews. The Context will not be held past the lifetime of this
         *                  Builder object.
         * @param channelId The constructed Notification will be posted on this
         *                  NotificationChannel.
         */
        public Builder(@NonNull Context context, @NonNull String channelId) {
            mContext = context;
            mChannelId = channelId;

            // Set defaults to match the defaults of a Notification
            mNotification.when = System.currentTimeMillis();
            mNotification.audioStreamType = Notification.STREAM_DEFAULT;
            mPriority = PRIORITY_DEFAULT;
            mPeople = new ArrayList<String>();
        }

        @Deprecated
        public Builder(Context context) {
            this(context, null);
        }

        /**
         * Set the time that the event occurred.  Notifications in the panel are
         * sorted by this time.
         */
        public Builder setWhen(long when) {
            mNotification.when = when;
            return this;
        }

        /**
         * Control whether the timestamp set with {@link #setWhen(long) setWhen} is shown
         * in the content view.
         */
        public Builder setShowWhen(boolean show) {
            mShowWhen = show;
            return this;
        }

        /**
         * Show the {@link Notification#when} field as a stopwatch.
         * <p>
         * Instead of presenting <code>when</code> as a timestamp, the notification will show an
         * automatically updating display of the minutes and seconds since <code>when</code>.
         * <p>
         * Useful when showing an elapsed time (like an ongoing phone call).
         *
         * @see android.widget.Chronometer
         * @see Notification#when
         */
        public Builder setUsesChronometer(boolean b) {
            mUseChronometer = b;
            return this;
        }

        /**
         * Set the small icon to use in the notification layouts.  Different classes of devices
         * may return different sizes.  See the UX guidelines for more information on how to
         * design these icons.
         *
         * @param icon A resource ID in the application's package of the drawable to use.
         */
        public Builder setSmallIcon(int icon) {
            mNotification.icon = icon;
            return this;
        }

        /**
         * A variant of {@link #setSmallIcon(int) setSmallIcon(int)} that takes an additional
         * level parameter for when the icon is a {@link android.graphics.drawable.LevelListDrawable
         * LevelListDrawable}.
         *
         * @param icon  A resource ID in the application's package of the drawable to use.
         * @param level The level to use for the icon.
         * @see android.graphics.drawable.LevelListDrawable
         */
        public Builder setSmallIcon(int icon, int level) {
            mNotification.icon = icon;
            mNotification.iconLevel = level;
            return this;
        }

        /**
         * Set the title (first row) of the notification, in a standard notification.
         */
        public Builder setContentTitle(CharSequence title) {
            mContentTitle = limitCharSequenceLength(title);
            return this;
        }

        /**
         * Set the text (second row) of the notification, in a standard notification.
         */
        public Builder setContentText(CharSequence text) {
            mContentText = limitCharSequenceLength(text);
            return this;
        }

        /**
         * Set the third line of text in the platform notification template.
         * Don't use if you're also using {@link #setProgress(int, int, boolean)};
         * they occupy the same location in the standard template.
         * <br>
         * If the platform does not provide large-format notifications, this method has no effect.
         * The third line of text only appears in expanded view.
         * <br>
         */
        public Builder setSubText(CharSequence text) {
            mSubText = limitCharSequenceLength(text);
            return this;
        }

        /**
         * Set the remote input history.
         * <p>
         * This should be set to the most recent inputs that have been sent
         * through a {@link tornaco.apps.shortx.core.compat.RemoteInput} of this Notification and cleared once the it is no
         * longer relevant (e.g. for chat notifications once the other party has responded).
         * <p>
         * The most recent input must be stored at the 0 index, the second most recent at the
         * 1 index, etc. Note that the system will limit both how far back the inputs will be shown
         * and how much of each individual input is shown.
         *
         * <p>Note: The reply text will only be shown on notifications that have least one action
         * with a {@code RemoteInput}.</p>
         */
        public Builder setRemoteInputHistory(CharSequence[] text) {
            mRemoteInputHistory = text;
            return this;
        }

        /**
         * Set the large number at the right-hand side of the notification.  This is
         * equivalent to setContentInfo, although it might show the number in a different
         * font size for readability.
         */
        public Builder setNumber(int number) {
            mNumber = number;
            return this;
        }

        /**
         * Set the large text at the right-hand side of the notification.
         */
        public Builder setContentInfo(CharSequence info) {
            mContentInfo = limitCharSequenceLength(info);
            return this;
        }

        /**
         * Set the progress this notification represents, which may be
         * represented as a {@link android.widget.ProgressBar}.
         */
        public Builder setProgress(int max, int progress, boolean indeterminate) {
            mProgressMax = max;
            mProgress = progress;
            mProgressIndeterminate = indeterminate;
            return this;
        }

        /**
         * Supply a custom RemoteViews to use instead of the standard one.
         */
        public Builder setContent(RemoteViews views) {
            mNotification.contentView = views;
            return this;
        }

        /**
         * Supply a {@link PendingIntent} to send when the notification is clicked.
         * If you do not supply an intent, you can now add PendingIntents to individual
         * views to be launched when clicked by calling {@link RemoteViews#setOnClickPendingIntent
         * RemoteViews.setOnClickPendingIntent(int,PendingIntent)}.  Be sure to
         * read {@link Notification#contentIntent Notification.contentIntent} for
         * how to correctly use this.
         */
        public Builder setContentIntent(PendingIntent intent) {
            mContentIntent = intent;
            return this;
        }

        /**
         * Supply a {@link PendingIntent} to send when the notification is cleared by the user
         * directly from the notification panel.  For example, this intent is sent when the user
         * clicks the "Clear all" button, or the individual "X" buttons on notifications.  This
         * intent is not sent when the application calls
         * {@link android.app.NotificationManager#cancel NotificationManager.cancel(int)}.
         */
        public Builder setDeleteIntent(PendingIntent intent) {
            mNotification.deleteIntent = intent;
            return this;
        }

        /**
         * An intent to launch instead of posting the notification to the status bar.
         * Only for use with extremely high-priority notifications demanding the user's
         * <strong>immediate</strong> attention, such as an incoming phone call or
         * alarm clock that the user has explicitly set to a particular time.
         * If this facility is used for something else, please give the user an option
         * to turn it off and use a normal notification, as this can be extremely
         * disruptive.
         *
         * <p>
         * On some platforms, the system UI may choose to display a heads-up notification,
         * instead of launching this intent, while the user is using the device.
         * </p>
         *
         * @param intent       The pending intent to launch.
         * @param highPriority Passing true will cause this notification to be sent
         *                     even if other notifications are suppressed.
         */
        public Builder setFullScreenIntent(PendingIntent intent, boolean highPriority) {
            mFullScreenIntent = intent;
            setFlag(FLAG_HIGH_PRIORITY, highPriority);
            return this;
        }

        /**
         * Sets the "ticker" text which is sent to accessibility services. Prior to
         * {@link Build.VERSION_CODES#LOLLIPOP}, sets the text that is displayed in the status bar
         * when the notification first arrives.
         */
        public Builder setTicker(CharSequence tickerText) {
            mNotification.tickerText = limitCharSequenceLength(tickerText);
            return this;
        }

        /**
         * Sets the "ticker" text which is sent to accessibility services. Prior to
         * {@link Build.VERSION_CODES#LOLLIPOP}, sets the text that is displayed in the status bar
         * when the notification first arrives, and also a RemoteViews object that may be displayed
         * instead on some devices.
         */
        public Builder setTicker(CharSequence tickerText, RemoteViews views) {
            mNotification.tickerText = limitCharSequenceLength(tickerText);
            mTickerView = views;
            return this;
        }

        /**
         * Set the large icon that is shown in the ticker and notification.
         */
        public Builder setLargeIcon(Bitmap icon) {
            mLargeIcon = reduceLargeIconSize(icon);
            return this;
        }

        /**
         * Reduce the size of a notification icon if it's overly large. The framework does
         * this automatically starting from API 27.
         */
        private Bitmap reduceLargeIconSize(Bitmap icon) {
            if (icon == null || Build.VERSION.SDK_INT >= 27) {
                return icon;
            }

            Resources res = mContext.getResources();
            int maxWidth = (int) ResUtils.convertDpToPixel(320f, mContext);
            int maxHeight = (int) ResUtils.convertDpToPixel(320f, mContext);

            if (icon.getWidth() <= maxWidth && icon.getHeight() <= maxHeight) {
                return icon;
            }

            double scale = Math.min(
                    maxWidth / (double) Math.max(1, icon.getWidth()),
                    maxHeight / (double) Math.max(1, icon.getHeight()));
            return Bitmap.createScaledBitmap(
                    icon,
                    (int) Math.ceil(icon.getWidth() * scale),
                    (int) Math.ceil(icon.getHeight() * scale),
                    true /* filtered */);
        }

        /**
         * Set the sound to play.  It will play on the default stream.
         *
         * <p>
         * On some platforms, a notification that is noisy is more likely to be presented
         * as a heads-up notification.
         * </p>
         */
        public Builder setSound(Uri sound) {
            mNotification.sound = sound;
            mNotification.audioStreamType = Notification.STREAM_DEFAULT;
            if (Build.VERSION.SDK_INT >= 21) {
                mNotification.audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
            }
            return this;
        }

        /**
         * Set the sound to play.  It will play on the stream you supply.
         *
         * <p>
         * On some platforms, a notification that is noisy is more likely to be presented
         * as a heads-up notification.
         * </p>
         *
         * @see Notification#STREAM_DEFAULT
         * @see AudioManager for the <code>STREAM_</code> constants.
         */
        public Builder setSound(Uri sound, @StreamType int streamType) {
            mNotification.sound = sound;
            mNotification.audioStreamType = streamType;
            if (Build.VERSION.SDK_INT >= 21) {
                mNotification.audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setLegacyStreamType(streamType)
                        .build();
            }
            return this;
        }

        /**
         * Set the vibration pattern to use.
         *
         * <p>
         * On some platforms, a notification that vibrates is more likely to be presented
         * as a heads-up notification.
         * </p>
         *
         * @see android.os.Vibrator for a discussion of the <code>pattern</code>
         * parameter.
         */
        public Builder setVibrate(long[] pattern) {
            mNotification.vibrate = pattern;
            return this;
        }

        /**
         * Set the argb value that you would like the LED on the device to blink, as well as the
         * rate.  The rate is specified in terms of the number of milliseconds to be on
         * and then the number of milliseconds to be off.
         */
        public Builder setLights(@ColorInt int argb, int onMs, int offMs) {
            mNotification.ledARGB = argb;
            mNotification.ledOnMS = onMs;
            mNotification.ledOffMS = offMs;
            boolean showLights = mNotification.ledOnMS != 0 && mNotification.ledOffMS != 0;
            mNotification.flags = (mNotification.flags & ~Notification.FLAG_SHOW_LIGHTS) |
                    (showLights ? Notification.FLAG_SHOW_LIGHTS : 0);
            return this;
        }

        /**
         * Set whether this is an ongoing notification.
         *
         * <p>Ongoing notifications differ from regular notifications in the following ways:
         * <ul>
         * <li>Ongoing notifications are sorted above the regular notifications in the
         * notification panel.</li>
         * <li>Ongoing notifications do not have an 'X' close button, and are not affected
         * by the "Clear all" button.
         * </ul>
         */
        public Builder setOngoing(boolean ongoing) {
            setFlag(Notification.FLAG_ONGOING_EVENT, ongoing);
            return this;
        }

        /**
         * Set whether this notification should be colorized. When set, the color set with
         * {@link #setColor(int)} will be used as the background color of this notification.
         * <p>
         * This should only be used for high priority ongoing tasks like navigation, an ongoing
         * call, or other similarly high-priority events for the user.
         * <p>
         * For most styles, the coloring will only be applied if the notification is for a
         * foreground service notification.
         * <p>
         * However, for MediaStyle and DecoratedMediaCustomViewStyle notifications
         * that have a media session attached there is no such requirement.
         * <p>
         * Calling this method on any version prior to {@link Build.VERSION_CODES#O} will
         * not have an effect on the notification and it won't be colorized.
         *
         * @see #setColor(int)
         */
        public Builder setColorized(boolean colorize) {
            mColorized = colorize;
            mColorizedSet = true;
            return this;
        }

        /**
         * Set this flag if you would only like the sound, vibrate
         * and ticker to be played if the notification is not already showing.
         */
        public Builder setOnlyAlertOnce(boolean onlyAlertOnce) {
            setFlag(Notification.FLAG_ONLY_ALERT_ONCE, onlyAlertOnce);
            return this;
        }

        /**
         * Setting this flag will make it so the notification is automatically
         * canceled when the user clicks it in the panel.  The PendingIntent
         * set with {@link #setDeleteIntent} will be broadcast when the notification
         * is canceled.
         */
        public Builder setAutoCancel(boolean autoCancel) {
            setFlag(Notification.FLAG_AUTO_CANCEL, autoCancel);
            return this;
        }

        /**
         * Set whether or not this notification is only relevant to the current device.
         *
         * <p>Some notifications can be bridged to other devices for remote display.
         * This hint can be set to recommend this notification not be bridged.
         */
        public Builder setLocalOnly(boolean b) {
            mLocalOnly = b;
            return this;
        }

        /**
         * Set the notification category.
         *
         * <p>Must be one of the predefined notification categories (see the <code>CATEGORY_*</code>
         * constants in {@link Notification}) that best describes this notification.
         * May be used by the system for ranking and filtering.
         */
        public Builder setCategory(String category) {
            mCategory = category;
            return this;
        }

        /**
         * Set the default notification options that will be used.
         * <p>
         * The value should be one or more of the following fields combined with
         * bitwise-or:
         * {@link Notification#DEFAULT_SOUND}, {@link Notification#DEFAULT_VIBRATE},
         * {@link Notification#DEFAULT_LIGHTS}.
         * <p>
         * For all default values, use {@link Notification#DEFAULT_ALL}.
         */
        public Builder setDefaults(int defaults) {
            mNotification.defaults = defaults;
            if ((defaults & Notification.DEFAULT_LIGHTS) != 0) {
                mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
            }
            return this;
        }

        private void setFlag(int mask, boolean value) {
            if (value) {
                mNotification.flags |= mask;
            } else {
                mNotification.flags &= ~mask;
            }
        }

        /**
         * Set the relative priority for this notification.
         * <p>
         * Priority is an indication of how much of the user's
         * valuable attention should be consumed by this
         * notification. Low-priority notifications may be hidden from
         * the user in certain situations, while the user might be
         * interrupted for a higher-priority notification.
         * The system sets a notification's priority based on various factors including the
         * setPriority value. The effect may differ slightly on different platforms.
         *
         * @param pri Relative priority for this notification. Must be one of
         *            the priority constants defined by {@link NotificationCompat}.
         *            Acceptable values range from {@link
         *            NotificationCompat#PRIORITY_MIN} (-2) to {@link
         *            NotificationCompat#PRIORITY_MAX} (2).
         */
        public Builder setPriority(int pri) {
            mPriority = pri;
            return this;
        }

        /**
         * Add a person that is relevant to this notification.
         *
         * <p>
         * Depending on user preferences, this annotation may allow the notification to pass
         * through interruption filters, and to appear more prominently in the user interface.
         * </P>
         *
         * <p>
         * The person should be specified by the {@code String} representation of a
         * {@link android.provider.ContactsContract.Contacts#CONTENT_LOOKUP_URI}.
         * </P>
         *
         * <P>The system will also attempt to resolve {@code mailto:} and {@code tel:} schema
         * URIs.  The path part of these URIs must exist in the contacts database, in the
         * appropriate column, or the reference will be discarded as invalid. Telephone schema
         * URIs will be resolved by {@link android.provider.ContactsContract.PhoneLookup}.
         * </P>
         *
         * @param uri A URI for the person.
         * @see Notification#EXTRA_PEOPLE
         */
        public Builder addPerson(String uri) {
            mPeople.add(uri);
            return this;
        }

        /**
         * Set this notification to be part of a group of notifications sharing the same key.
         * Grouped notifications may display in a cluster or stack on devices which
         * support such rendering.
         *
         * <p>To make this notification the summary for its group, also call
         * {@link #setGroupSummary}. A sort order can be specified for group members by using
         * {@link #setSortKey}.
         *
         * @param groupKey The group key of the group.
         * @return this object for method chaining
         */
        public Builder setGroup(String groupKey) {
            mGroupKey = groupKey;
            return this;
        }

        /**
         * Set this notification to be the group summary for a group of notifications.
         * Grouped notifications may display in a cluster or stack on devices which
         * support such rendering. Requires a group key also be set using {@link #setGroup}.
         *
         * @param isGroupSummary Whether this notification should be a group summary.
         * @return this object for method chaining
         */
        public Builder setGroupSummary(boolean isGroupSummary) {
            mGroupSummary = isGroupSummary;
            return this;
        }

        /**
         * Set a sort key that orders this notification among other notifications from the
         * same package. This can be useful if an external sort was already applied and an app
         * would like to preserve this. Notifications will be sorted lexicographically using this
         * value, although providing different priorities in addition to providing sort key may
         * cause this value to be ignored.
         *
         * <p>This sort key can also be used to order members of a notification group. See
         * {@link Builder#setGroup}.
         *
         * @see String#compareTo(String)
         */
        public Builder setSortKey(String sortKey) {
            mSortKey = sortKey;
            return this;
        }

        /**
         * Merge additional metadata into this notification.
         *
         * <p>Values within the Bundle will replace existing extras values in this Builder.
         *
         * @see Notification#extras
         */
        public Builder addExtras(Bundle extras) {
            if (extras != null) {
                if (mExtras == null) {
                    mExtras = new Bundle(extras);
                } else {
                    mExtras.putAll(extras);
                }
            }
            return this;
        }

        /**
         * Set metadata for this notification.
         *
         * <p>A reference to the Bundle is held for the lifetime of this Builder, and the Bundle's
         * current contents are copied into the Notification each time {@link #build()} is
         * called.
         *
         * <p>Replaces any existing extras values with those from the provided Bundle.
         * Use {@link #addExtras} to merge in metadata instead.
         *
         * @see Notification#extras
         */
        public Builder setExtras(Bundle extras) {
            mExtras = extras;
            return this;
        }

        /**
         * Get the current metadata Bundle used by this notification Builder.
         *
         * <p>The returned Bundle is shared with this Builder.
         *
         * <p>The current contents of this Bundle are copied into the Notification each time
         * {@link #build()} is called.
         *
         * @see Notification#extras
         */
        public Bundle getExtras() {
            if (mExtras == null) {
                mExtras = new Bundle();
            }
            return mExtras;
        }

        /**
         * Add an action to this notification. Actions are typically displayed by
         * the system as a button adjacent to the notification content.
         * <br>
         * Action buttons won't appear on platforms prior to Android 4.1. Action
         * buttons depend on expanded notifications, which are only available in Android 4.1
         * and later. To ensure that an action button's functionality is always available, first
         * implement the functionality in the {@link Activity} that starts when a user
         * clicks the  notification (see {@link #setContentIntent setContentIntent()}), and then
         * enhance the notification by implementing the same functionality with
         * {@link #addAction addAction()}.
         *
         * @param icon   Resource ID of a drawable that represents the action.
         * @param title  Text describing the action.
         * @param intent {@link PendingIntent} to be fired when the action is invoked.
         */
        public Builder addAction(int icon, CharSequence title, PendingIntent intent) {
            mActions.add(new Action(icon, title, intent));
            return this;
        }

        /**
         * Add an action to this notification. Actions are typically displayed by
         * the system as a button adjacent to the notification content.
         * <br>
         * Action buttons won't appear on platforms prior to Android 4.1. Action
         * buttons depend on expanded notifications, which are only available in Android 4.1
         * and later. To ensure that an action button's functionality is always available, first
         * implement the functionality in the {@link Activity} that starts when a user
         * clicks the  notification (see {@link #setContentIntent setContentIntent()}), and then
         * enhance the notification by implementing the same functionality with
         * {@link #addAction addAction()}.
         *
         * @param action The action to add.
         */
        public Builder addAction(Action action) {
            mActions.add(action);
            return this;
        }

        /**
         * Add an invisible action to this notification. Invisible actions are never displayed by
         * the system, but can be retrieved and used by other application listening to
         * system notifications. Invisible actions are supported from Android 4.4.4 (API 20) and can
         * be retrieved using {@link NotificationCompat#getInvisibleActions(Notification)}.
         *
         * @param icon   Resource ID of a drawable that represents the action.
         * @param title  Text describing the action.
         * @param intent {@link PendingIntent} to be fired when the action is invoked.
         */
        // @RequiresApi(21)
        public Builder addInvisibleAction(int icon, CharSequence title, PendingIntent intent) {
            return addInvisibleAction(new Action(icon, title, intent));
        }

        /**
         * Add an invisible action to this notification. Invisible actions are never displayed by
         * the system, but can be retrieved and used by other application listening to
         * system notifications. Invisible actions are supported from Android 4.4.4 (API 20) and can
         * be retrieved using {@link NotificationCompat#getInvisibleActions(Notification)}.
         *
         * @param action The action to add.
         */
        // @RequiresApi(21)
        public Builder addInvisibleAction(Action action) {
            mInvisibleActions.add(action);
            return this;
        }

        /**
         * Add a rich notification style to be applied at build time.
         * <br>
         * If the platform does not provide rich notification styles, this method has no effect. The
         * user will always see the normal notification style.
         *
         * @param style Object responsible for modifying the notification style.
         */
        public Builder setStyle(Style style) {
            if (mStyle != style) {
                mStyle = style;
                if (mStyle != null) {
                    mStyle.setBuilder(this);
                }
            }
            return this;
        }

        /**
         * Sets {@link Notification#color}.
         *
         * @param argb The accent color to use
         * @return The same Builder.
         */
        public Builder setColor(@ColorInt int argb) {
            mColor = argb;
            return this;
        }

        /**
         * Sets {@link Notification#visibility}.
         *
         * @param visibility One of {@link Notification#VISIBILITY_PRIVATE} (the default),
         *                   {@link Notification#VISIBILITY_PUBLIC}, or
         *                   {@link Notification#VISIBILITY_SECRET}.
         */
        public Builder setVisibility(@NotificationVisibility int visibility) {
            mVisibility = visibility;
            return this;
        }

        /**
         * Supply a replacement Notification whose contents should be shown in insecure contexts
         * (i.e. atop the secure lockscreen). See {@link Notification#visibility} and
         * {@link #VISIBILITY_PUBLIC}.
         *
         * @param n A replacement notification, presumably with some or all info redacted.
         * @return The same Builder.
         */
        public Builder setPublicVersion(Notification n) {
            mPublicVersion = n;
            return this;
        }

        /**
         * Supply custom RemoteViews to use instead of the platform template.
         * <p>
         * This will override the layout that would otherwise be constructed by this Builder
         * object.
         */
        public Builder setCustomContentView(RemoteViews contentView) {
            mContentView = contentView;
            return this;
        }

        /**
         * Supply custom RemoteViews to use instead of the platform template in the expanded form.
         * <p>
         * This will override the expanded layout that would otherwise be constructed by this
         * Builder object.
         * <p>
         * No-op on versions prior to {@link Build.VERSION_CODES#JELLY_BEAN}.
         */
        public Builder setCustomBigContentView(RemoteViews contentView) {
            mBigContentView = contentView;
            return this;
        }

        /**
         * Supply custom RemoteViews to use instead of the platform template in the heads up dialog.
         * <p>
         * This will override the heads-up layout that would otherwise be constructed by this
         * Builder object.
         * <p>
         * No-op on versions prior to {@link Build.VERSION_CODES#LOLLIPOP}.
         */
        public Builder setCustomHeadsUpContentView(RemoteViews contentView) {
            mHeadsUpContentView = contentView;
            return this;
        }

        /**
         * Specifies the channel the notification should be delivered on.
         * <p>
         * No-op on versions prior to {@link Build.VERSION_CODES#O} .
         */
        public Builder setChannelId(@NonNull String channelId) {
            mChannelId = channelId;
            return this;
        }

        /**
         * Specifies the time at which this notification should be canceled, if it is not already
         * canceled.
         */
        public Builder setTimeoutAfter(long durationMs) {
            mTimeout = durationMs;
            return this;
        }

        public Builder setShortcutId(String shortcutId) {
            mShortcutId = shortcutId;
            return this;
        }

        /**
         * Sets which icon to display as a badge for this notification.
         *
         * <p>Must be one of {@link #BADGE_ICON_NONE}, {@link #BADGE_ICON_SMALL},
         * {@link #BADGE_ICON_LARGE}.
         *
         * <p><strong>Note:</strong> This value might be ignored, for launchers that don't support
         * badge icons.
         */
        public Builder setBadgeIconType(@BadgeIconType int icon) {
            mBadgeIcon = icon;
            return this;
        }

        /**
         * Sets the group alert behavior for this notification. Use this method to mute this
         * notification if alerts for this notification's group should be handled by a different
         * notification. This is only applicable for notifications that belong to a
         * {@link #setGroup(String) group}. This must be called on all notifications you want to
         * mute. For example, if you want only the summary of your group to make noise, all
         * children in the group should have the group alert behavior {@link #GROUP_ALERT_SUMMARY}.
         *
         * <p> The default value is {@link #GROUP_ALERT_ALL}.</p>
         */
        public Builder setGroupAlertBehavior(@GroupAlertBehavior int groupAlertBehavior) {
            mGroupAlertBehavior = groupAlertBehavior;
            return this;
        }

        /**
         * Apply an extender to this notification builder. Extenders may be used to add
         * metadata or change options on this builder.
         */
        public Builder extend(Extender extender) {
            extender.extend(this);
            return this;
        }

        /**
         * @deprecated Use {@link #build()} instead.
         */
        @Deprecated
        public Notification getNotification() {
            return build();
        }

        /**
         * Combine all of the options that have been set and return a new {@link Notification}
         * object.
         */
        public Notification build() {
            return new NotificationCompatBuilder(this).build();
        }

        protected static CharSequence limitCharSequenceLength(CharSequence cs) {
            if (cs == null) return cs;
            if (cs.length() > MAX_CHARSEQUENCE_LENGTH) {
                cs = cs.subSequence(0, MAX_CHARSEQUENCE_LENGTH);
            }
            return cs;
        }

        /**
         *
         */
        // @RestrictTo(LIBRARY_GROUP)
        public RemoteViews getContentView() {
            return mContentView;
        }

        /**
         *
         */
        // @RestrictTo(LIBRARY_GROUP)
        public RemoteViews getBigContentView() {
            return mBigContentView;
        }

        /**
         *
         */
        // @RestrictTo(LIBRARY_GROUP)
        public RemoteViews getHeadsUpContentView() {
            return mHeadsUpContentView;
        }

        /**
         * return when if it is showing or 0 otherwise
         */
        // @RestrictTo(LIBRARY_GROUP)
        public long getWhenIfShowing() {
            return mShowWhen ? mNotification.when : 0;
        }

        /**
         * @return the priority set on the notification
         */
        // @RestrictTo(LIBRARY_GROUP)
        public int getPriority() {
            return mPriority;
        }

        /**
         * @return the color of the notification
         */
        // @RestrictTo(LIBRARY_GROUP)
        public int getColor() {
            return mColor;
        }
    }

    /**
     * An object that can apply a rich notification style to a {@link Notification.Builder}
     * object.
     * <br>
     * If the platform does not provide rich notification styles, methods in this class have no
     * effect.
     */
    public static abstract class Style {
        /**
         *
         */
        // @RestrictTo(LIBRARY_GROUP)
        protected Builder mBuilder;
        CharSequence mBigContentTitle;
        CharSequence mSummaryText;
        boolean mSummaryTextSet = false;

        public void setBuilder(Builder builder) {
            if (mBuilder != builder) {
                mBuilder = builder;
                if (mBuilder != null) {
                    mBuilder.setStyle(this);
                }
            }
        }

        public Notification build() {
            Notification notification = null;
            if (mBuilder != null) {
                notification = mBuilder.build();
            }
            return notification;
        }

        /**
         * Applies the compat style data to the framework {@link Notification} in a backwards
         * compatible way. All other data should be stored within the Notification's extras.
         */
        // @RestrictTo(LIBRARY_GROUP)
        // TODO: implement for all styles
        public void apply(NotificationBuilderWithBuilderAccessor builder) {
        }

        /**
         *
         */
        // @RestrictTo(LIBRARY_GROUP)
        public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor builder) {
            return null;
        }

        /**
         *
         */
        // @RestrictTo(LIBRARY_GROUP)
        public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor builder) {
            return null;
        }

        /**
         *
         */
        // @RestrictTo(LIBRARY_GROUP)
        public RemoteViews makeHeadsUpContentView(NotificationBuilderWithBuilderAccessor builder) {
            return null;
        }

        /**
         *
         */
        // @RestrictTo(LIBRARY_GROUP)
        // TODO: implement for all styles
        public void addCompatExtras(Bundle extras) {
        }

        /**
         *
         */
        // @RestrictTo(LIBRARY_GROUP)
        // TODO: implement for all styles
        protected void restoreFromCompatExtras(Bundle extras) {
        }

        /**
         *
         */
        // @RestrictTo(LIBRARY_GROUP)
        public Bitmap createColoredBitmap(int iconId, int color) {
            return createColoredBitmap(iconId, color, 0);
        }

        private Bitmap createColoredBitmap(int iconId, int color, int size) {
            Drawable drawable = mBuilder.mContext.getResources().getDrawable(iconId);
            int width = size == 0 ? drawable.getIntrinsicWidth() : size;
            int height = size == 0 ? drawable.getIntrinsicHeight() : size;
            Bitmap resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            drawable.setBounds(0, 0, width, height);
            if (color != 0) {
                drawable.mutate().setColorFilter(
                        new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
            Canvas canvas = new Canvas(resultBitmap);
            drawable.draw(canvas);
            return resultBitmap;
        }
    }

    /**
     * Helper class for generating large-format notifications that include a large image attachment.
     * <br>
     * If the platform does not provide large-format notifications, this method has no effect. The
     * user will always see the normal notification view.
     * <br>
     * This class is a "rebuilder": It attaches to a Builder object and modifies its behavior, like so:
     * <pre class="prettyprint">
     * Notification notification = new Notification.Builder(mContext)
     *     .setContentTitle(&quot;New photo from &quot; + sender.toString())
     *     .setContentText(subject)
     *     .setSmallIcon(R.drawable.new_post)
     *     .setLargeIcon(aBitmap)
     *     .setStyle(new Notification.BigPictureStyle()
     *         .bigPicture(aBigBitmap))
     *     .build();
     * </pre>
     *
     * @see Notification#bigContentView
     */
    public static class BigPictureStyle extends Style {
        private Bitmap mPicture;
        private Bitmap mBigLargeIcon;
        private boolean mBigLargeIconSet;

        public BigPictureStyle() {
        }

        public BigPictureStyle(Builder builder) {
            setBuilder(builder);
        }

        /**
         * Overrides ContentTitle in the big form of the template.
         * This defaults to the value passed to setContentTitle().
         */
        public BigPictureStyle setBigContentTitle(CharSequence title) {
            mBigContentTitle = Builder.limitCharSequenceLength(title);
            return this;
        }

        /**
         * Set the first line of text after the detail section in the big form of the template.
         */
        public BigPictureStyle setSummaryText(CharSequence cs) {
            mSummaryText = Builder.limitCharSequenceLength(cs);
            mSummaryTextSet = true;
            return this;
        }

        /**
         * Provide the bitmap to be used as the payload for the BigPicture notification.
         */
        public BigPictureStyle bigPicture(Bitmap b) {
            mPicture = b;
            return this;
        }

        /**
         * Override the large icon when the big notification is shown.
         */
        public BigPictureStyle bigLargeIcon(Bitmap b) {
            mBigLargeIcon = b;
            mBigLargeIconSet = true;
            return this;
        }

        /**
         *
         */
        // @RestrictTo(LIBRARY_GROUP)
        @Override
        public void apply(NotificationBuilderWithBuilderAccessor builder) {
            if (Build.VERSION.SDK_INT >= 16) {
                Notification.BigPictureStyle style =
                        new Notification.BigPictureStyle(builder.getBuilder())
                                .setBigContentTitle(mBigContentTitle)
                                .bigPicture(mPicture);
                if (mBigLargeIconSet) {
                    style.bigLargeIcon(mBigLargeIcon);
                }
                if (mSummaryTextSet) {
                    style.setSummaryText(mSummaryText);
                }
            }
        }
    }

    /**
     * Helper class for generating large-format notifications that include a lot of text.
     *
     * <br>
     * If the platform does not provide large-format notifications, this method has no effect. The
     * user will always see the normal notification view.
     * <br>
     * This class is a "rebuilder": It attaches to a Builder object and modifies its behavior, like so:
     * <pre class="prettyprint">
     * Notification notification = new Notification.Builder(mContext)
     *     .setContentTitle(&quot;New mail from &quot; + sender.toString())
     *     .setContentText(subject)
     *     .setSmallIcon(R.drawable.new_mail)
     *     .setLargeIcon(aBitmap)
     *     .setStyle(new Notification.BigTextStyle()
     *         .bigText(aVeryLongString))
     *     .build();
     * </pre>
     *
     * @see Notification#bigContentView
     */
    public static class BigTextStyle extends Style {
        private CharSequence mBigText;

        public BigTextStyle() {
        }

        public BigTextStyle(Builder builder) {
            setBuilder(builder);
        }

        /**
         * Overrides ContentTitle in the big form of the template.
         * This defaults to the value passed to setContentTitle().
         */
        public BigTextStyle setBigContentTitle(CharSequence title) {
            mBigContentTitle = Builder.limitCharSequenceLength(title);
            return this;
        }

        /**
         * Set the first line of text after the detail section in the big form of the template.
         */
        public BigTextStyle setSummaryText(CharSequence cs) {
            mSummaryText = Builder.limitCharSequenceLength(cs);
            mSummaryTextSet = true;
            return this;
        }

        /**
         * Provide the longer text to be displayed in the big form of the
         * template in place of the content text.
         */
        public BigTextStyle bigText(CharSequence cs) {
            mBigText = Builder.limitCharSequenceLength(cs);
            return this;
        }

        /**
         *
         */
        // @RestrictTo(LIBRARY_GROUP)
        @Override
        public void apply(NotificationBuilderWithBuilderAccessor builder) {
            Notification.BigTextStyle style =
                    new Notification.BigTextStyle(builder.getBuilder())
                            .setBigContentTitle(mBigContentTitle)
                            .bigText(mBigText);
            if (mSummaryTextSet) {
                style.setSummaryText(mSummaryText);
            }
        }
    }


    /**
     * Helper class for generating large-format notifications that include a list of (up to 5) strings.
     *
     * <br>
     * If the platform does not provide large-format notifications, this method has no effect. The
     * user will always see the normal notification view.
     * <br>
     * This class is a "rebuilder": It attaches to a Builder object and modifies its behavior, like so:
     * <pre class="prettyprint">
     * Notification notification = new Notification.Builder()
     *     .setContentTitle(&quot;5 New mails from &quot; + sender.toString())
     *     .setContentText(subject)
     *     .setSmallIcon(R.drawable.new_mail)
     *     .setLargeIcon(aBitmap)
     *     .setStyle(new Notification.InboxStyle()
     *         .addLine(str1)
     *         .addLine(str2)
     *         .setContentTitle(&quot;&quot;)
     *         .setSummaryText(&quot;+3 more&quot;))
     *     .build();
     * </pre>
     *
     * @see Notification#bigContentView
     */
    public static class InboxStyle extends Style {
        private final ArrayList<CharSequence> mTexts = new ArrayList<CharSequence>();

        public InboxStyle() {
        }

        public InboxStyle(Builder builder) {
            setBuilder(builder);
        }

        /**
         * Overrides ContentTitle in the big form of the template.
         * This defaults to the value passed to setContentTitle().
         */
        public InboxStyle setBigContentTitle(CharSequence title) {
            mBigContentTitle = Builder.limitCharSequenceLength(title);
            return this;
        }

        /**
         * Set the first line of text after the detail section in the big form of the template.
         */
        public InboxStyle setSummaryText(CharSequence cs) {
            mSummaryText = Builder.limitCharSequenceLength(cs);
            mSummaryTextSet = true;
            return this;
        }

        /**
         * Append a line to the digest section of the Inbox notification.
         */
        public InboxStyle addLine(CharSequence cs) {
            mTexts.add(Builder.limitCharSequenceLength(cs));
            return this;
        }

        /**
         *
         */
        // @RestrictTo(LIBRARY_GROUP)
        @Override
        public void apply(NotificationBuilderWithBuilderAccessor builder) {
            if (Build.VERSION.SDK_INT >= 16) {
                Notification.InboxStyle style =
                        new Notification.InboxStyle(builder.getBuilder())
                                .setBigContentTitle(mBigContentTitle);
                if (mSummaryTextSet) {
                    style.setSummaryText(mSummaryText);
                }
                for (CharSequence text : mTexts) {
                    style.addLine(text);
                }
            }
        }
    }

    /**
     * Structure to encapsulate a named action that can be shown as part of this notification.
     * It must include an icon, a label, and a {@link PendingIntent} to be fired when the action is
     * selected by the user. Action buttons won't appear on platforms prior to Android 4.1.
     * <p>
     * Apps should use {@link NotificationCompat.Builder#addAction(int, CharSequence, PendingIntent)}
     * or {@link NotificationCompat.Builder#addAction(Action)}
     * to attach actions.
     */
    public static class Action {
        /**
         * {@link SemanticAction}: No semantic action defined.
         */
        public static final int SEMANTIC_ACTION_NONE = 0;

        /**
         * {@link SemanticAction}: Reply to a conversation, chat, group, or wherever replies
         * may be appropriate.
         */
        public static final int SEMANTIC_ACTION_REPLY = 1;

        /**
         * {@link SemanticAction}: Mark content as read.
         */
        public static final int SEMANTIC_ACTION_MARK_AS_READ = 2;

        /**
         * {@link SemanticAction}: Mark content as unread.
         */
        public static final int SEMANTIC_ACTION_MARK_AS_UNREAD = 3;

        /**
         * {@link SemanticAction}: Delete the content associated with the notification. This
         * could mean deleting an email, message, etc.
         */
        public static final int SEMANTIC_ACTION_DELETE = 4;

        /**
         * {@link SemanticAction}: Archive the content associated with the notification. This
         * could mean archiving an email, message, etc.
         */
        public static final int SEMANTIC_ACTION_ARCHIVE = 5;

        /**
         * {@link SemanticAction}: Mute the content associated with the notification. This could
         * mean silencing a conversation or currently playing media.
         */
        public static final int SEMANTIC_ACTION_MUTE = 6;

        /**
         * {@link SemanticAction}: Unmute the content associated with the notification. This could
         * mean un-silencing a conversation or currently playing media.
         */
        public static final int SEMANTIC_ACTION_UNMUTE = 7;

        /**
         * {@link SemanticAction}: Mark content with a thumbs up.
         */
        public static final int SEMANTIC_ACTION_THUMBS_UP = 8;

        /**
         * {@link SemanticAction}: Mark content with a thumbs down.
         */
        public static final int SEMANTIC_ACTION_THUMBS_DOWN = 9;

        /**
         * {@link SemanticAction}: Call a contact, group, etc.
         */
        public static final int SEMANTIC_ACTION_CALL = 10;

        static final String EXTRA_SHOWS_USER_INTERFACE =
                "android.support.action.showsUserInterface";

        static final String EXTRA_SEMANTIC_ACTION = "android.support.action.semanticAction";

        final Bundle mExtras;
        private final RemoteInput[] mRemoteInputs;

        /**
         * Holds {@link tornaco.apps.shortx.core.compat.RemoteInput}s that only accept data, meaning
         * {@link tornaco.apps.shortx.core.compat.RemoteInput#getAllowFreeFormInput} is false, {@link tornaco.apps.shortx.core.compat.RemoteInput#getChoices}
         * is null or empty, and {@link tornaco.apps.shortx.core.compat.RemoteInput#getAllowedDataTypes is non-null and not
         * empty. These {@link tornaco.apps.shortx.core.compat.RemoteInput }s will be ignored by devices that do not
         * support non-text-based {@link tornaco.apps.shortx.core.compat.RemoteInput }s. See {@link Builder#build}.
         * <p>
         * You can test if a RemoteInput matches these constraints using
         * {@link tornaco.apps.shortx.core.compat.RemoteInput#isDataOnly}.
         */
        private final RemoteInput[] mDataOnlyRemoteInputs;

        private final boolean mAllowGeneratedReplies;
        boolean mShowsUserInterface = true;

        private final @SemanticAction
        int mSemanticAction;

        /**
         * Small icon representing the action.
         */
        public int icon;
        /**
         * Title of the action.
         */
        public CharSequence title;
        /**
         * Intent to send when the user invokes this action. May be null, in which case the action
         * may be rendered in a disabled presentation.
         */
        public PendingIntent actionIntent;

        public Action(int icon, CharSequence title, PendingIntent intent) {
            this(icon, title, intent, new Bundle(), null, null, true, SEMANTIC_ACTION_NONE, true);
        }

        Action(int icon, CharSequence title, PendingIntent intent, Bundle extras,
               RemoteInput[] remoteInputs, RemoteInput[] dataOnlyRemoteInputs,
               boolean allowGeneratedReplies, @SemanticAction int semanticAction,
               boolean showsUserInterface) {
            this.icon = icon;
            this.title = NotificationCompat.Builder.limitCharSequenceLength(title);
            this.actionIntent = intent;
            this.mExtras = extras != null ? extras : new Bundle();
            this.mRemoteInputs = remoteInputs;
            this.mDataOnlyRemoteInputs = dataOnlyRemoteInputs;
            this.mAllowGeneratedReplies = allowGeneratedReplies;
            this.mSemanticAction = semanticAction;
            this.mShowsUserInterface = showsUserInterface;
        }

        public int getIcon() {
            return icon;
        }

        public CharSequence getTitle() {
            return title;
        }

        public PendingIntent getActionIntent() {
            return actionIntent;
        }

        /**
         * Get additional metadata carried around with this Action.
         */
        public Bundle getExtras() {
            return mExtras;
        }

        /**
         * Return whether the platform should automatically generate possible replies for this
         * {@link Action}
         */
        public boolean getAllowGeneratedReplies() {
            return mAllowGeneratedReplies;
        }

        /**
         * Get the list of inputs to be collected from the user when this action is sent.
         * May return null if no remote inputs were added. Only returns inputs which accept
         * a text input. For inputs which only accept data use {@link #getDataOnlyRemoteInputs}.
         */
        public RemoteInput[] getRemoteInputs() {
            return mRemoteInputs;
        }

        /**
         * Returns the {@link SemanticAction} associated with this {@link Action}. A
         * {@link SemanticAction} denotes what an {@link Action}'s {@link PendingIntent} will do
         * (eg. reply, mark as read, delete, etc).
         *
         * @see SemanticAction
         */
        public @SemanticAction
        int getSemanticAction() {
            return mSemanticAction;
        }

        /**
         * Get the list of inputs to be collected from the user that ONLY accept data when this
         * action is sent. These remote inputs are guaranteed to return true on a call to
         * {@link tornaco.apps.shortx.core.compat.RemoteInput#isDataOnly}.
         *
         * <p>May return null if no data-only remote inputs were added.
         *
         * <p>This method exists so that legacy RemoteInput collectors that pre-date the addition
         * of non-textual RemoteInputs do not access these remote inputs.
         */
        public RemoteInput[] getDataOnlyRemoteInputs() {
            return mDataOnlyRemoteInputs;
        }

        /**
         * Return whether or not triggering this {@link Action}'s {@link PendingIntent} will open a
         * user interface.
         */
        public boolean getShowsUserInterface() {
            return mShowsUserInterface;
        }

        /**
         * Builder class for {@link Action} objects.
         */
        public static final class Builder {
            private final int mIcon;
            private final CharSequence mTitle;
            private final PendingIntent mIntent;
            private boolean mAllowGeneratedReplies = true;
            private final Bundle mExtras;
            private ArrayList<RemoteInput> mRemoteInputs;
            private @SemanticAction
            int mSemanticAction;
            private boolean mShowsUserInterface = true;

            /**
             * Construct a new builder for {@link Action} object.
             *
             * @param icon   icon to show for this action
             * @param title  the title of the action
             * @param intent the {@link PendingIntent} to fire when users trigger this action
             */
            public Builder(int icon, CharSequence title, PendingIntent intent) {
                this(icon, title, intent, new Bundle(), null, true, SEMANTIC_ACTION_NONE, true);
            }

            /**
             * Construct a new builder for {@link Action} object using the fields from an
             * {@link Action}.
             *
             * @param action the action to read fields from.
             */
            public Builder(Action action) {
                this(action.icon, action.title, action.actionIntent, new Bundle(action.mExtras),
                        action.getRemoteInputs(), action.getAllowGeneratedReplies(),
                        action.getSemanticAction(), action.mShowsUserInterface);
            }

            private Builder(int icon, CharSequence title, PendingIntent intent, Bundle extras,
                            RemoteInput[] remoteInputs, boolean allowGeneratedReplies,
                            @SemanticAction int semanticAction, boolean showsUserInterface) {
                mIcon = icon;
                mTitle = NotificationCompat.Builder.limitCharSequenceLength(title);
                mIntent = intent;
                mExtras = extras;
                mRemoteInputs = remoteInputs == null ? null : new ArrayList<>(
                        Arrays.asList(remoteInputs));
                mAllowGeneratedReplies = allowGeneratedReplies;
                mSemanticAction = semanticAction;
                mShowsUserInterface = showsUserInterface;
            }

            /**
             * Merge additional metadata into this builder.
             *
             * <p>Values within the Bundle will replace existing extras values in this Builder.
             *
             * @see Action#getExtras
             */
            public Builder addExtras(Bundle extras) {
                if (extras != null) {
                    mExtras.putAll(extras);
                }
                return this;
            }

            /**
             * Get the metadata Bundle used by this Builder.
             *
             * <p>The returned Bundle is shared with this Builder.
             */
            public Bundle getExtras() {
                return mExtras;
            }

            /**
             * Add an input to be collected from the user when this action is sent.
             * Response values can be retrieved from the fired intent by using the
             * {@link tornaco.apps.shortx.core.compat.RemoteInput#getResultsFromIntent} function.
             *
             * @param remoteInput a {@link tornaco.apps.shortx.core.compat.RemoteInput} to add to the action
             * @return this object for method chaining
             */
            public Builder addRemoteInput(RemoteInput remoteInput) {
                if (mRemoteInputs == null) {
                    mRemoteInputs = new ArrayList<RemoteInput>();
                }
                mRemoteInputs.add(remoteInput);
                return this;
            }

            /**
             * Set whether the platform should automatically generate possible replies to add to
             * {@link tornaco.apps.shortx.core.compat.RemoteInput#getChoices()}. If the {@link Action} doesn't have a
             * {@link tornaco.apps.shortx.core.compat.RemoteInput}, this has no effect.
             *
             * @param allowGeneratedReplies {@code true} to allow generated replies, {@code false}
             *                              otherwise
             * @return this object for method chaining
             * The default value is {@code true}
             */
            public Builder setAllowGeneratedReplies(boolean allowGeneratedReplies) {
                mAllowGeneratedReplies = allowGeneratedReplies;
                return this;
            }

            /**
             * Sets the {@link SemanticAction} for this {@link Action}. A {@link SemanticAction}
             * denotes what an {@link Action}'s {@link PendingIntent} will do (eg. reply, mark
             * as read, delete, etc).
             *
             * @param semanticAction a {@link SemanticAction} defined within {@link Action} with
             *                       {@code SEMANTIC_ACTION_} prefixes
             * @return this object for method chaining
             */
            public Builder setSemanticAction(@SemanticAction int semanticAction) {
                mSemanticAction = semanticAction;
                return this;
            }

            /**
             * Set whether or not this {@link Action}'s {@link PendingIntent} will open a user
             * interface.
             *
             * @param showsUserInterface {@code true} if this {@link Action}'s {@link PendingIntent}
             *                           will open a user interface, otherwise {@code false}
             * @return this object for method chaining
             * The default value is {@code true}
             */
            public Builder setShowsUserInterface(boolean showsUserInterface) {
                mShowsUserInterface = showsUserInterface;
                return this;
            }

            /**
             * Apply an extender to this action builder. Extenders may be used to add
             * metadata or change options on this builder.
             */
            public Builder extend(Extender extender) {
                extender.extend(this);
                return this;
            }

            /**
             * Combine all of the options that have been set and return a new {@link Action}
             * object.
             *
             * @return the built action
             */
            public Action build() {
                List<RemoteInput> dataOnlyInputs = new ArrayList<>();
                List<RemoteInput> textInputs = new ArrayList<>();
                if (mRemoteInputs != null) {
                    for (RemoteInput input : mRemoteInputs) {
                        if (input.isDataOnly()) {
                            dataOnlyInputs.add(input);
                        } else {
                            textInputs.add(input);
                        }
                    }
                }
                RemoteInput[] dataOnlyInputsArr = dataOnlyInputs.isEmpty()
                        ? null : dataOnlyInputs.toArray(new RemoteInput[dataOnlyInputs.size()]);
                RemoteInput[] textInputsArr = textInputs.isEmpty()
                        ? null : textInputs.toArray(new RemoteInput[textInputs.size()]);
                return new Action(mIcon, mTitle, mIntent, mExtras, textInputsArr,
                        dataOnlyInputsArr, mAllowGeneratedReplies, mSemanticAction,
                        mShowsUserInterface);
            }
        }


        /**
         * Extender interface for use with {@link Builder#extend}. Extenders may be used to add
         * metadata or change options on an action builder.
         */
        public interface Extender {
            /**
             * Apply this extender to a notification action builder.
             *
             * @param builder the builder to be modified.
             * @return the build object for chaining.
             */
            Builder extend(Builder builder);
        }

        /**
         * Wearable extender for notification actions. To add extensions to an action,
         * create a new {@link WearableExtender} object using
         * the {@code WearableExtender()} constructor and apply it to a
         * {@link Builder} using
         * {@link Builder#extend}.
         *
         * <pre class="prettyprint">
         * NotificationCompat.Action action = new NotificationCompat.Action.Builder(
         *         R.drawable.archive_all, "Archive all", actionIntent)
         *         .extend(new NotificationCompat.Action.WearableExtender()
         *                 .setAvailableOffline(false))
         *         .build();</pre>
         */
        public static final class WearableExtender implements Extender {
            /**
             * Notification action extra which contains wearable extensions
             */
            private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";

            // Keys within EXTRA_WEARABLE_EXTENSIONS for wearable options.
            private static final String KEY_FLAGS = "flags";
            private static final String KEY_IN_PROGRESS_LABEL = "inProgressLabel";
            private static final String KEY_CONFIRM_LABEL = "confirmLabel";
            private static final String KEY_CANCEL_LABEL = "cancelLabel";

            // Flags bitwise-ored to mFlags
            private static final int FLAG_AVAILABLE_OFFLINE = 0x1;
            private static final int FLAG_HINT_LAUNCHES_ACTIVITY = 1 << 1;
            private static final int FLAG_HINT_DISPLAY_INLINE = 1 << 2;

            // Default value for flags integer
            private static final int DEFAULT_FLAGS = FLAG_AVAILABLE_OFFLINE;

            private int mFlags = DEFAULT_FLAGS;

            private CharSequence mInProgressLabel;
            private CharSequence mConfirmLabel;
            private CharSequence mCancelLabel;

            /**
             * Create a {@link WearableExtender} with default
             * options.
             */
            public WearableExtender() {
            }

            /**
             * Create a {@link WearableExtender} by reading
             * wearable options present in an existing notification action.
             *
             * @param action the notification action to inspect.
             */
            public WearableExtender(Action action) {
                Bundle wearableBundle = action.getExtras().getBundle(EXTRA_WEARABLE_EXTENSIONS);
                if (wearableBundle != null) {
                    mFlags = wearableBundle.getInt(KEY_FLAGS, DEFAULT_FLAGS);
                    mInProgressLabel = wearableBundle.getCharSequence(KEY_IN_PROGRESS_LABEL);
                    mConfirmLabel = wearableBundle.getCharSequence(KEY_CONFIRM_LABEL);
                    mCancelLabel = wearableBundle.getCharSequence(KEY_CANCEL_LABEL);
                }
            }

            /**
             * Apply wearable extensions to a notification action that is being built. This is
             * typically called by the {@link Builder#extend}
             * method of {@link Builder}.
             */
            @Override
            public Builder extend(Builder builder) {
                Bundle wearableBundle = new Bundle();

                if (mFlags != DEFAULT_FLAGS) {
                    wearableBundle.putInt(KEY_FLAGS, mFlags);
                }
                if (mInProgressLabel != null) {
                    wearableBundle.putCharSequence(KEY_IN_PROGRESS_LABEL, mInProgressLabel);
                }
                if (mConfirmLabel != null) {
                    wearableBundle.putCharSequence(KEY_CONFIRM_LABEL, mConfirmLabel);
                }
                if (mCancelLabel != null) {
                    wearableBundle.putCharSequence(KEY_CANCEL_LABEL, mCancelLabel);
                }

                builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, wearableBundle);
                return builder;
            }

            @Override
            public WearableExtender clone() {
                WearableExtender that = new WearableExtender();
                that.mFlags = this.mFlags;
                that.mInProgressLabel = this.mInProgressLabel;
                that.mConfirmLabel = this.mConfirmLabel;
                that.mCancelLabel = this.mCancelLabel;
                return that;
            }

            /**
             * Set whether this action is available when the wearable device is not connected to
             * a companion device. The user can still trigger this action when the wearable device
             * is offline, but a visual hint will indicate that the action may not be available.
             * Defaults to true.
             */
            public WearableExtender setAvailableOffline(boolean availableOffline) {
                setFlag(FLAG_AVAILABLE_OFFLINE, availableOffline);
                return this;
            }

            /**
             * Get whether this action is available when the wearable device is not connected to
             * a companion device. The user can still trigger this action when the wearable device
             * is offline, but a visual hint will indicate that the action may not be available.
             * Defaults to true.
             */
            public boolean isAvailableOffline() {
                return (mFlags & FLAG_AVAILABLE_OFFLINE) != 0;
            }

            private void setFlag(int mask, boolean value) {
                if (value) {
                    mFlags |= mask;
                } else {
                    mFlags &= ~mask;
                }
            }

            /**
             * Set a label to display while the wearable is preparing to automatically execute the
             * action. This is usually a 'ing' verb ending in ellipsis like "Sending..."
             *
             * @param label the label to display while the action is being prepared to execute
             * @return this object for method chaining
             */
            @Deprecated
            public WearableExtender setInProgressLabel(CharSequence label) {
                mInProgressLabel = label;
                return this;
            }

            /**
             * Get the label to display while the wearable is preparing to automatically execute
             * the action. This is usually a 'ing' verb ending in ellipsis like "Sending..."
             *
             * @return the label to display while the action is being prepared to execute
             */
            @Deprecated
            public CharSequence getInProgressLabel() {
                return mInProgressLabel;
            }

            /**
             * Set a label to display to confirm that the action should be executed.
             * This is usually an imperative verb like "Send".
             *
             * @param label the label to confirm the action should be executed
             * @return this object for method chaining
             */
            @Deprecated
            public WearableExtender setConfirmLabel(CharSequence label) {
                mConfirmLabel = label;
                return this;
            }

            /**
             * Get the label to display to confirm that the action should be executed.
             * This is usually an imperative verb like "Send".
             *
             * @return the label to confirm the action should be executed
             */
            @Deprecated
            public CharSequence getConfirmLabel() {
                return mConfirmLabel;
            }

            /**
             * Set a label to display to cancel the action.
             * This is usually an imperative verb, like "Cancel".
             *
             * @param label the label to display to cancel the action
             * @return this object for method chaining
             */
            @Deprecated
            public WearableExtender setCancelLabel(CharSequence label) {
                mCancelLabel = label;
                return this;
            }

            /**
             * Get the label to display to cancel the action.
             * This is usually an imperative verb like "Cancel".
             *
             * @return the label to display to cancel the action
             */
            @Deprecated
            public CharSequence getCancelLabel() {
                return mCancelLabel;
            }

            /**
             * Set a hint that this Action will launch an {@link Activity} directly, telling the
             * platform that it can generate the appropriate transitions.
             *
             * @param hintLaunchesActivity {@code true} if the content intent will launch
             *                             an activity and transitions should be generated, false otherwise.
             * @return this object for method chaining
             */
            public WearableExtender setHintLaunchesActivity(
                    boolean hintLaunchesActivity) {
                setFlag(FLAG_HINT_LAUNCHES_ACTIVITY, hintLaunchesActivity);
                return this;
            }

            /**
             * Get a hint that this Action will launch an {@link Activity} directly, telling the
             * platform that it can generate the appropriate transitions
             *
             * @return {@code true} if the content intent will launch an activity and transitions
             * should be generated, false otherwise. The default value is {@code false} if this was
             * never set.
             */
            public boolean getHintLaunchesActivity() {
                return (mFlags & FLAG_HINT_LAUNCHES_ACTIVITY) != 0;
            }

            /**
             * Set a hint that this Action should be displayed inline - i.e. it will have a visual
             * representation directly on the notification surface in addition to the expanded
             * Notification
             *
             * @param hintDisplayInline {@code true} if action should be displayed inline, false
             *                          otherwise
             * @return this object for method chaining
             */
            public WearableExtender setHintDisplayActionInline(
                    boolean hintDisplayInline) {
                setFlag(FLAG_HINT_DISPLAY_INLINE, hintDisplayInline);
                return this;
            }

            /**
             * Get a hint that this Action should be displayed inline - i.e. it should have a
             * visual representation directly on the notification surface in addition to the
             * expanded Notification
             *
             * @return {@code true} if the Action should be displayed inline, {@code false}
             * otherwise. The default value is {@code false} if this was never set.
             */
            public boolean getHintDisplayActionInline() {
                return (mFlags & FLAG_HINT_DISPLAY_INLINE) != 0;
            }
        }

        /**
         * Provides meaning to an {@link Action} that hints at what the associated
         * {@link PendingIntent} will do. For example, an {@link Action} with a
         * {@link PendingIntent} that replies to a text message notification may have the
         * {@link #SEMANTIC_ACTION_REPLY} {@link SemanticAction} set within it.
         */
        @IntDef({
                SEMANTIC_ACTION_NONE,
                SEMANTIC_ACTION_REPLY,
                SEMANTIC_ACTION_MARK_AS_READ,
                SEMANTIC_ACTION_MARK_AS_UNREAD,
                SEMANTIC_ACTION_DELETE,
                SEMANTIC_ACTION_ARCHIVE,
                SEMANTIC_ACTION_MUTE,
                SEMANTIC_ACTION_UNMUTE,
                SEMANTIC_ACTION_THUMBS_UP,
                SEMANTIC_ACTION_THUMBS_DOWN,
                SEMANTIC_ACTION_CALL
        })
        @Retention(RetentionPolicy.SOURCE)
        public @interface SemanticAction {
        }
    }


    /**
     * Extender interface for use with {@link Builder#extend}. Extenders may be used to add
     * metadata or change options on a notification builder.
     */
    public interface Extender {
        /**
         * Apply this extender to a notification builder.
         *
         * @param builder the builder to be modified.
         * @return the build object for chaining.
         */
        Builder extend(Builder builder);
    }

    /**
     * Helper class to add wearable extensions to notifications.
     * <p class="note"> See
     * <a href="{@docRoot}wear/notifications/creating.html">Creating Notifications
     * for Android Wear</a> for more information on how to use this class.
     * <p>
     * To create a notification with wearable extensions:
     * <ol>
     * <li>Create a {@link Builder}, setting any desired
     * properties.
     * <li>Create a {@link WearableExtender}.
     * <li>Set wearable-specific properties using the
     * {@code add} and {@code set} methods of {@link WearableExtender}.
     * <li>Call {@link Builder#extend} to apply the extensions to a
     * notification.
     * <li>Post the notification to the notification
     * system with the {@code NotificationManagerCompat.notify(...)} methods
     * and not the {@code NotificationManager.notify(...)} methods.
     * </ol>
     *
     * <pre class="prettyprint">
     * Notification notification = new NotificationCompat.Builder(mContext)
     *         .setContentTitle(&quot;New mail from &quot; + sender.toString())
     *         .setContentText(subject)
     *         .setSmallIcon(R.drawable.new_mail)
     *         .extend(new NotificationCompat.WearableExtender()
     *                 .setContentIcon(R.drawable.new_mail))
     *         .build();
     * NotificationManagerCompat.from(mContext).notify(0, notification);</pre>
     *
     * <p>Wearable extensions can be accessed on an existing notification by using the
     * {@code WearableExtender(Notification)} constructor,
     * and then using the {@code get} methods to access values.
     *
     * <pre class="prettyprint">
     * NotificationCompat.WearableExtender wearableExtender =
     *         new NotificationCompat.WearableExtender(notification);
     * List&lt;Notification&gt; pages = wearableExtender.getPages();</pre>
     */
    public static final class WearableExtender implements Extender {
        /**
         * Sentinel value for an action index that is unset.
         */
        public static final int UNSET_ACTION_INDEX = -1;

        /**
         * Size value for use with {@link #setCustomSizePreset} to show this notification with
         * default sizing.
         * <p>For custom display notifications created using {@link #setDisplayIntent},
         * the default is {@link #SIZE_MEDIUM}. All other notifications size automatically based
         * on their content.
         */
        public static final int SIZE_DEFAULT = 0;

        /**
         * Size value for use with {@link #setCustomSizePreset} to show this notification
         * with an extra small size.
         * <p>This value is only applicable for custom display notifications created using
         * {@link #setDisplayIntent}.
         */
        public static final int SIZE_XSMALL = 1;

        /**
         * Size value for use with {@link #setCustomSizePreset} to show this notification
         * with a small size.
         * <p>This value is only applicable for custom display notifications created using
         * {@link #setDisplayIntent}.
         */
        public static final int SIZE_SMALL = 2;

        /**
         * Size value for use with {@link #setCustomSizePreset} to show this notification
         * with a medium size.
         * <p>This value is only applicable for custom display notifications created using
         * {@link #setDisplayIntent}.
         */
        public static final int SIZE_MEDIUM = 3;

        /**
         * Size value for use with {@link #setCustomSizePreset} to show this notification
         * with a large size.
         * <p>This value is only applicable for custom display notifications created using
         * {@link #setDisplayIntent}.
         */
        public static final int SIZE_LARGE = 4;

        /**
         * Size value for use with {@link #setCustomSizePreset} to show this notification
         * full screen.
         * <p>This value is only applicable for custom display notifications created using
         * {@link #setDisplayIntent}.
         */
        public static final int SIZE_FULL_SCREEN = 5;

        /**
         * Sentinel value for use with {@link #setHintScreenTimeout} to keep the screen on for a
         * short amount of time when this notification is displayed on the screen. This
         * is the default value.
         */
        public static final int SCREEN_TIMEOUT_SHORT = 0;

        /**
         * Sentinel value for use with {@link #setHintScreenTimeout} to keep the screen on
         * for a longer amount of time when this notification is displayed on the screen.
         */
        public static final int SCREEN_TIMEOUT_LONG = -1;

        /**
         * Notification extra which contains wearable extensions
         */
        private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";

        // Keys within EXTRA_WEARABLE_EXTENSIONS for wearable options.
        private static final String KEY_ACTIONS = "actions";
        private static final String KEY_FLAGS = "flags";
        private static final String KEY_DISPLAY_INTENT = "displayIntent";
        private static final String KEY_PAGES = "pages";
        private static final String KEY_BACKGROUND = "background";
        private static final String KEY_CONTENT_ICON = "contentIcon";
        private static final String KEY_CONTENT_ICON_GRAVITY = "contentIconGravity";
        private static final String KEY_CONTENT_ACTION_INDEX = "contentActionIndex";
        private static final String KEY_CUSTOM_SIZE_PRESET = "customSizePreset";
        private static final String KEY_CUSTOM_CONTENT_HEIGHT = "customContentHeight";
        private static final String KEY_GRAVITY = "gravity";
        private static final String KEY_HINT_SCREEN_TIMEOUT = "hintScreenTimeout";
        private static final String KEY_DISMISSAL_ID = "dismissalId";
        private static final String KEY_BRIDGE_TAG = "bridgeTag";

        // Flags bitwise-ored to mFlags
        private static final int FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE = 0x1;
        private static final int FLAG_HINT_HIDE_ICON = 1 << 1;
        private static final int FLAG_HINT_SHOW_BACKGROUND_ONLY = 1 << 2;
        private static final int FLAG_START_SCROLL_BOTTOM = 1 << 3;
        private static final int FLAG_HINT_AVOID_BACKGROUND_CLIPPING = 1 << 4;
        private static final int FLAG_BIG_PICTURE_AMBIENT = 1 << 5;
        private static final int FLAG_HINT_CONTENT_INTENT_LAUNCHES_ACTIVITY = 1 << 6;

        // Default value for flags integer
        private static final int DEFAULT_FLAGS = FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE;

        private static final int DEFAULT_CONTENT_ICON_GRAVITY = GravityCompat.END;
        private static final int DEFAULT_GRAVITY = Gravity.BOTTOM;

        private ArrayList<Action> mActions = new ArrayList<Action>();
        private int mFlags = DEFAULT_FLAGS;
        private PendingIntent mDisplayIntent;
        private ArrayList<Notification> mPages = new ArrayList<Notification>();
        private Bitmap mBackground;
        private int mContentIcon;
        private int mContentIconGravity = DEFAULT_CONTENT_ICON_GRAVITY;
        private int mContentActionIndex = UNSET_ACTION_INDEX;
        private int mCustomSizePreset = SIZE_DEFAULT;
        private int mCustomContentHeight;
        private int mGravity = DEFAULT_GRAVITY;
        private int mHintScreenTimeout;
        private String mDismissalId;
        private String mBridgeTag;

        /**
         * Create a {@link WearableExtender} with default
         * options.
         */
        public WearableExtender() {
        }

        public WearableExtender(Notification notification) {
            Bundle extras = getExtras(notification);
            Bundle wearableBundle = extras != null ? extras.getBundle(EXTRA_WEARABLE_EXTENSIONS)
                    : null;
            if (wearableBundle != null) {
                final ArrayList<Parcelable> parcelables =
                        wearableBundle.getParcelableArrayList(KEY_ACTIONS);
                if (Build.VERSION.SDK_INT >= 16 && parcelables != null) {
                    Action[] actions = new Action[parcelables.size()];
                    for (int i = 0; i < actions.length; i++) {
                        if (Build.VERSION.SDK_INT >= 20) {
                            actions[i] = NotificationCompat.getActionCompatFromAction(
                                    (Notification.Action) parcelables.get(i));
                        } else if (Build.VERSION.SDK_INT >= 16) {
                            actions[i] = NotificationCompatJellybean.getActionFromBundle(
                                    (Bundle) parcelables.get(i));
                        }
                    }
                    Collections.addAll(mActions, actions);
                }

                mFlags = wearableBundle.getInt(KEY_FLAGS, DEFAULT_FLAGS);
                mDisplayIntent = wearableBundle.getParcelable(KEY_DISPLAY_INTENT);

                Notification[] pages = getNotificationArrayFromBundle(
                        wearableBundle, KEY_PAGES);
                if (pages != null) {
                    Collections.addAll(mPages, pages);
                }

                mBackground = wearableBundle.getParcelable(KEY_BACKGROUND);
                mContentIcon = wearableBundle.getInt(KEY_CONTENT_ICON);
                mContentIconGravity = wearableBundle.getInt(KEY_CONTENT_ICON_GRAVITY,
                        DEFAULT_CONTENT_ICON_GRAVITY);
                mContentActionIndex = wearableBundle.getInt(KEY_CONTENT_ACTION_INDEX,
                        UNSET_ACTION_INDEX);
                mCustomSizePreset = wearableBundle.getInt(KEY_CUSTOM_SIZE_PRESET,
                        SIZE_DEFAULT);
                mCustomContentHeight = wearableBundle.getInt(KEY_CUSTOM_CONTENT_HEIGHT);
                mGravity = wearableBundle.getInt(KEY_GRAVITY, DEFAULT_GRAVITY);
                mHintScreenTimeout = wearableBundle.getInt(KEY_HINT_SCREEN_TIMEOUT);
                mDismissalId = wearableBundle.getString(KEY_DISMISSAL_ID);
                mBridgeTag = wearableBundle.getString(KEY_BRIDGE_TAG);
            }
        }

        /**
         * Apply wearable extensions to a notification that is being built. This is typically
         * called by the {@link Builder#extend} method of
         * {@link Builder}.
         */
        @Override
        public Builder extend(Builder builder) {
            Bundle wearableBundle = new Bundle();

            if (!mActions.isEmpty()) {
                ArrayList<Parcelable> parcelables = new ArrayList<>(mActions.size());
                for (Action action : mActions) {
                    if (Build.VERSION.SDK_INT >= 20) {
                        parcelables.add(
                                WearableExtender.getActionFromActionCompat(action));
                    } else if (Build.VERSION.SDK_INT >= 16) {
                        parcelables.add(NotificationCompatJellybean.getBundleForAction(action));
                    }
                }
                wearableBundle.putParcelableArrayList(KEY_ACTIONS, parcelables);
            }
            if (mFlags != DEFAULT_FLAGS) {
                wearableBundle.putInt(KEY_FLAGS, mFlags);
            }
            if (mDisplayIntent != null) {
                wearableBundle.putParcelable(KEY_DISPLAY_INTENT, mDisplayIntent);
            }
            if (!mPages.isEmpty()) {
                wearableBundle.putParcelableArray(KEY_PAGES, mPages.toArray(
                        new Notification[mPages.size()]));
            }
            if (mBackground != null) {
                wearableBundle.putParcelable(KEY_BACKGROUND, mBackground);
            }
            if (mContentIcon != 0) {
                wearableBundle.putInt(KEY_CONTENT_ICON, mContentIcon);
            }
            if (mContentIconGravity != DEFAULT_CONTENT_ICON_GRAVITY) {
                wearableBundle.putInt(KEY_CONTENT_ICON_GRAVITY, mContentIconGravity);
            }
            if (mContentActionIndex != UNSET_ACTION_INDEX) {
                wearableBundle.putInt(KEY_CONTENT_ACTION_INDEX,
                        mContentActionIndex);
            }
            if (mCustomSizePreset != SIZE_DEFAULT) {
                wearableBundle.putInt(KEY_CUSTOM_SIZE_PRESET, mCustomSizePreset);
            }
            if (mCustomContentHeight != 0) {
                wearableBundle.putInt(KEY_CUSTOM_CONTENT_HEIGHT, mCustomContentHeight);
            }
            if (mGravity != DEFAULT_GRAVITY) {
                wearableBundle.putInt(KEY_GRAVITY, mGravity);
            }
            if (mHintScreenTimeout != 0) {
                wearableBundle.putInt(KEY_HINT_SCREEN_TIMEOUT, mHintScreenTimeout);
            }
            if (mDismissalId != null) {
                wearableBundle.putString(KEY_DISMISSAL_ID, mDismissalId);
            }
            if (mBridgeTag != null) {
                wearableBundle.putString(KEY_BRIDGE_TAG, mBridgeTag);
            }

            builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, wearableBundle);
            return builder;
        }

        // @RequiresApi(20)
        private static Notification.Action getActionFromActionCompat(Action actionCompat) {
            Notification.Action.Builder actionBuilder = new Notification.Action.Builder(
                    actionCompat.getIcon(), actionCompat.getTitle(),
                    actionCompat.getActionIntent());
            Bundle actionExtras;
            if (actionCompat.getExtras() != null) {
                actionExtras = new Bundle(actionCompat.getExtras());
            } else {
                actionExtras = new Bundle();
            }
            actionExtras.putBoolean(NotificationCompatJellybean.EXTRA_ALLOW_GENERATED_REPLIES,
                    actionCompat.getAllowGeneratedReplies());
            if (Build.VERSION.SDK_INT >= 24) {
                actionBuilder.setAllowGeneratedReplies(actionCompat.getAllowGeneratedReplies());
            }
            actionBuilder.addExtras(actionExtras);
            RemoteInput[] remoteInputCompats = actionCompat.getRemoteInputs();
            if (remoteInputCompats != null) {
                android.app.RemoteInput[] remoteInputs = RemoteInput.fromCompat(remoteInputCompats);
                for (android.app.RemoteInput remoteInput : remoteInputs) {
                    actionBuilder.addRemoteInput(remoteInput);
                }
            }
            return actionBuilder.build();
        }

        @Override
        public WearableExtender clone() {
            WearableExtender that = new WearableExtender();
            that.mActions = new ArrayList<>(this.mActions);
            that.mFlags = this.mFlags;
            that.mDisplayIntent = this.mDisplayIntent;
            that.mPages = new ArrayList<>(this.mPages);
            that.mBackground = this.mBackground;
            that.mContentIcon = this.mContentIcon;
            that.mContentIconGravity = this.mContentIconGravity;
            that.mContentActionIndex = this.mContentActionIndex;
            that.mCustomSizePreset = this.mCustomSizePreset;
            that.mCustomContentHeight = this.mCustomContentHeight;
            that.mGravity = this.mGravity;
            that.mHintScreenTimeout = this.mHintScreenTimeout;
            that.mDismissalId = this.mDismissalId;
            that.mBridgeTag = this.mBridgeTag;
            return that;
        }

        /**
         * Add a wearable action to this notification.
         *
         * <p>When wearable actions are added using this method, the set of actions that
         * show on a wearable device splits from devices that only show actions added
         * using {@link Builder#addAction}. This allows for customization
         * of which actions display on different devices.
         *
         * @param action the action to add to this notification
         * @return this object for method chaining
         * @see Action
         */
        public WearableExtender addAction(Action action) {
            mActions.add(action);
            return this;
        }

        /**
         * Adds wearable actions to this notification.
         *
         * <p>When wearable actions are added using this method, the set of actions that
         * show on a wearable device splits from devices that only show actions added
         * using {@link Builder#addAction}. This allows for customization
         * of which actions display on different devices.
         *
         * @param actions the actions to add to this notification
         * @return this object for method chaining
         * @see Action
         */
        public WearableExtender addActions(List<Action> actions) {
            mActions.addAll(actions);
            return this;
        }

        /**
         * Clear all wearable actions present on this builder.
         *
         * @return this object for method chaining.
         * @see #addAction
         */
        public WearableExtender clearActions() {
            mActions.clear();
            return this;
        }

        /**
         * Get the wearable actions present on this notification.
         */
        public List<Action> getActions() {
            return mActions;
        }

        /**
         * Set an intent to launch inside of an activity view when displaying
         * this notification. The {@link PendingIntent} provided should be for an activity.
         *
         * <pre class="prettyprint">
         * Intent displayIntent = new Intent(context, MyDisplayActivity.class);
         * PendingIntent displayPendingIntent = PendingIntent.getActivity(context,
         *         0, displayIntent, PendingIntent.FLAG_UPDATE_CURRENT);
         * Notification notification = new NotificationCompat.Builder(context)
         *         .extend(new NotificationCompat.WearableExtender()
         *                 .setDisplayIntent(displayPendingIntent)
         *                 .setCustomSizePreset(NotificationCompat.WearableExtender.SIZE_MEDIUM))
         *         .build();</pre>
         *
         * <p>The activity to launch needs to allow embedding, must be exported, and
         * should have an empty task affinity. It is also recommended to use the device
         * default light theme.
         *
         * <p>Example AndroidManifest.xml entry:
         * <pre class="prettyprint">
         * &lt;activity android:name=&quot;com.example.MyDisplayActivity&quot;
         *     android:exported=&quot;true&quot;
         *     android:allowEmbedded=&quot;true&quot;
         *     android:taskAffinity=&quot;&quot;
         *     android:theme=&quot;@android:style/Theme.DeviceDefault.Light&quot; /&gt;</pre>
         *
         * @param intent the {@link PendingIntent} for an activity
         * @return this object for method chaining
         * @see WearableExtender#getDisplayIntent
         */
        public WearableExtender setDisplayIntent(PendingIntent intent) {
            mDisplayIntent = intent;
            return this;
        }

        /**
         * Get the intent to launch inside of an activity view when displaying this
         * notification. This {@code PendingIntent} should be for an activity.
         */
        public PendingIntent getDisplayIntent() {
            return mDisplayIntent;
        }

        /**
         * Add an additional page of content to display with this notification. The current
         * notification forms the first page, and pages added using this function form
         * subsequent pages. This field can be used to separate a notification into multiple
         * sections.
         *
         * @param page the notification to add as another page
         * @return this object for method chaining
         * @see WearableExtender#getPages
         */
        public WearableExtender addPage(Notification page) {
            mPages.add(page);
            return this;
        }

        /**
         * Add additional pages of content to display with this notification. The current
         * notification forms the first page, and pages added using this function form
         * subsequent pages. This field can be used to separate a notification into multiple
         * sections.
         *
         * @param pages a list of notifications
         * @return this object for method chaining
         * @see WearableExtender#getPages
         */
        public WearableExtender addPages(List<Notification> pages) {
            mPages.addAll(pages);
            return this;
        }

        /**
         * Clear all additional pages present on this builder.
         *
         * @return this object for method chaining.
         * @see #addPage
         */
        public WearableExtender clearPages() {
            mPages.clear();
            return this;
        }

        /**
         * Get the array of additional pages of content for displaying this notification. The
         * current notification forms the first page, and elements within this array form
         * subsequent pages. This field can be used to separate a notification into multiple
         * sections.
         *
         * @return the pages for this notification
         */
        public List<Notification> getPages() {
            return mPages;
        }

        /**
         * Set a background image to be displayed behind the notification content.
         * Contrary to the {@link BigPictureStyle}, this background
         * will work with any notification style.
         *
         * @param background the background bitmap
         * @return this object for method chaining
         * @see WearableExtender#getBackground
         */
        public WearableExtender setBackground(Bitmap background) {
            mBackground = background;
            return this;
        }

        /**
         * Get a background image to be displayed behind the notification content.
         * Contrary to the {@link BigPictureStyle}, this background
         * will work with any notification style.
         *
         * @return the background image
         * @see WearableExtender#setBackground
         */
        public Bitmap getBackground() {
            return mBackground;
        }

        /**
         * Set an icon that goes with the content of this notification.
         */
        @Deprecated
        public WearableExtender setContentIcon(int icon) {
            mContentIcon = icon;
            return this;
        }

        /**
         * Get an icon that goes with the content of this notification.
         */
        @Deprecated
        public int getContentIcon() {
            return mContentIcon;
        }

        /**
         * Set the gravity that the content icon should have within the notification display.
         * Supported values include {@link Gravity#START} and
         * {@link Gravity#END}. The default value is {@link Gravity#END}.
         *
         * @see #setContentIcon
         */
        @Deprecated
        public WearableExtender setContentIconGravity(int contentIconGravity) {
            mContentIconGravity = contentIconGravity;
            return this;
        }

        /**
         * Get the gravity that the content icon should have within the notification display.
         * Supported values include {@link Gravity#START} and
         * {@link Gravity#END}. The default value is {@link Gravity#END}.
         *
         * @see #getContentIcon
         */
        @Deprecated
        public int getContentIconGravity() {
            return mContentIconGravity;
        }

        /**
         * Set an action from this notification's actions to be clickable with the content of
         * this notification. This action will no longer display separately from the
         * notification's content.
         *
         * <p>For notifications with multiple pages, child pages can also have content actions
         * set, although the list of available actions comes from the main notification and not
         * from the child page's notification.
         *
         * @param actionIndex The index of the action to hoist onto the current notification page.
         *                    If wearable actions were added to the main notification, this index
         *                    will apply to that list, otherwise it will apply to the regular
         *                    actions list.
         */
        public WearableExtender setContentAction(int actionIndex) {
            mContentActionIndex = actionIndex;
            return this;
        }

        /**
         * Get the index of the notification action, if any, that was specified as being clickable
         * with the content of this notification. This action will no longer display separately
         * from the notification's content.
         *
         * <p>For notifications with multiple pages, child pages can also have content actions
         * set, although the list of available actions comes from the main notification and not
         * from the child page's notification.
         *
         * <p>If wearable specific actions were added to the main notification, this index will
         * apply to that list, otherwise it will apply to the regular actions list.
         *
         * @return the action index or {@link #UNSET_ACTION_INDEX} if no action was selected.
         */
        public int getContentAction() {
            return mContentActionIndex;
        }

        /**
         * Set the gravity that this notification should have within the available viewport space.
         * Supported values include {@link Gravity#TOP},
         * {@link Gravity#CENTER_VERTICAL} and {@link Gravity#BOTTOM}.
         * The default value is {@link Gravity#BOTTOM}.
         */
        @Deprecated
        public WearableExtender setGravity(int gravity) {
            mGravity = gravity;
            return this;
        }

        /**
         * Get the gravity that this notification should have within the available viewport space.
         * Supported values include {@link Gravity#TOP},
         * {@link Gravity#CENTER_VERTICAL} and {@link Gravity#BOTTOM}.
         * The default value is {@link Gravity#BOTTOM}.
         */
        @Deprecated
        public int getGravity() {
            return mGravity;
        }

        /**
         * Set the custom size preset for the display of this notification out of the available
         * presets found in {@link WearableExtender}, e.g.
         * {@link #SIZE_LARGE}.
         * <p>Some custom size presets are only applicable for custom display notifications created
         * using {@link WearableExtender#setDisplayIntent}. Check the
         * documentation for the preset in question. See also
         * {@link #setCustomContentHeight} and {@link #getCustomSizePreset}.
         */
        @Deprecated
        public WearableExtender setCustomSizePreset(int sizePreset) {
            mCustomSizePreset = sizePreset;
            return this;
        }

        /**
         * Get the custom size preset for the display of this notification out of the available
         * presets found in {@link WearableExtender}, e.g.
         * {@link #SIZE_LARGE}.
         * <p>Some custom size presets are only applicable for custom display notifications created
         * using {@link #setDisplayIntent}. Check the documentation for the preset in question.
         * See also {@link #setCustomContentHeight} and {@link #setCustomSizePreset}.
         */
        @Deprecated
        public int getCustomSizePreset() {
            return mCustomSizePreset;
        }

        /**
         * Set the custom height in pixels for the display of this notification's content.
         * <p>This option is only available for custom display notifications created
         * using {@link WearableExtender#setDisplayIntent}. See also
         * {@link WearableExtender#setCustomSizePreset} and
         * {@link #getCustomContentHeight}.
         */
        @Deprecated
        public WearableExtender setCustomContentHeight(int height) {
            mCustomContentHeight = height;
            return this;
        }

        /**
         * Get the custom height in pixels for the display of this notification's content.
         * <p>This option is only available for custom display notifications created
         * using {@link #setDisplayIntent}. See also {@link #setCustomSizePreset} and
         * {@link #setCustomContentHeight}.
         */
        @Deprecated
        public int getCustomContentHeight() {
            return mCustomContentHeight;
        }

        /**
         * Set whether the scrolling position for the contents of this notification should start
         * at the bottom of the contents instead of the top when the contents are too long to
         * display within the screen.  Default is false (start scroll at the top).
         */
        public WearableExtender setStartScrollBottom(boolean startScrollBottom) {
            setFlag(FLAG_START_SCROLL_BOTTOM, startScrollBottom);
            return this;
        }

        /**
         * Get whether the scrolling position for the contents of this notification should start
         * at the bottom of the contents instead of the top when the contents are too long to
         * display within the screen. Default is false (start scroll at the top).
         */
        public boolean getStartScrollBottom() {
            return (mFlags & FLAG_START_SCROLL_BOTTOM) != 0;
        }

        /**
         * Set whether the content intent is available when the wearable device is not connected
         * to a companion device.  The user can still trigger this intent when the wearable device
         * is offline, but a visual hint will indicate that the content intent may not be available.
         * Defaults to true.
         */
        public WearableExtender setContentIntentAvailableOffline(
                boolean contentIntentAvailableOffline) {
            setFlag(FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE, contentIntentAvailableOffline);
            return this;
        }

        /**
         * Get whether the content intent is available when the wearable device is not connected
         * to a companion device.  The user can still trigger this intent when the wearable device
         * is offline, but a visual hint will indicate that the content intent may not be available.
         * Defaults to true.
         */
        public boolean getContentIntentAvailableOffline() {
            return (mFlags & FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE) != 0;
        }

        /**
         * Set a hint that this notification's icon should not be displayed.
         *
         * @param hintHideIcon {@code true} to hide the icon, {@code false} otherwise.
         * @return this object for method chaining
         */
        @Deprecated
        public WearableExtender setHintHideIcon(boolean hintHideIcon) {
            setFlag(FLAG_HINT_HIDE_ICON, hintHideIcon);
            return this;
        }

        /**
         * Get a hint that this notification's icon should not be displayed.
         *
         * @return {@code true} if this icon should not be displayed, false otherwise.
         * The default value is {@code false} if this was never set.
         */
        @Deprecated
        public boolean getHintHideIcon() {
            return (mFlags & FLAG_HINT_HIDE_ICON) != 0;
        }

        /**
         * Set a visual hint that only the background image of this notification should be
         * displayed, and other semantic content should be hidden. This hint is only applicable
         * to sub-pages added using {@link #addPage}.
         */
        @Deprecated
        public WearableExtender setHintShowBackgroundOnly(boolean hintShowBackgroundOnly) {
            setFlag(FLAG_HINT_SHOW_BACKGROUND_ONLY, hintShowBackgroundOnly);
            return this;
        }

        /**
         * Get a visual hint that only the background image of this notification should be
         * displayed, and other semantic content should be hidden. This hint is only applicable
         * to sub-pages added using {@link WearableExtender#addPage}.
         */
        @Deprecated
        public boolean getHintShowBackgroundOnly() {
            return (mFlags & FLAG_HINT_SHOW_BACKGROUND_ONLY) != 0;
        }

        /**
         * Set a hint that this notification's background should not be clipped if possible,
         * and should instead be resized to fully display on the screen, retaining the aspect
         * ratio of the image. This can be useful for images like barcodes or qr codes.
         *
         * @param hintAvoidBackgroundClipping {@code true} to avoid clipping if possible.
         * @return this object for method chaining
         */
        @Deprecated
        public WearableExtender setHintAvoidBackgroundClipping(
                boolean hintAvoidBackgroundClipping) {
            setFlag(FLAG_HINT_AVOID_BACKGROUND_CLIPPING, hintAvoidBackgroundClipping);
            return this;
        }

        /**
         * Get a hint that this notification's background should not be clipped if possible,
         * and should instead be resized to fully display on the screen, retaining the aspect
         * ratio of the image. This can be useful for images like barcodes or qr codes.
         *
         * @return {@code true} if it's ok if the background is clipped on the screen, false
         * otherwise. The default value is {@code false} if this was never set.
         */
        @Deprecated
        public boolean getHintAvoidBackgroundClipping() {
            return (mFlags & FLAG_HINT_AVOID_BACKGROUND_CLIPPING) != 0;
        }

        /**
         * Set a hint that the screen should remain on for at least this duration when
         * this notification is displayed on the screen.
         *
         * @param timeout The requested screen timeout in milliseconds. Can also be either
         *                {@link #SCREEN_TIMEOUT_SHORT} or {@link #SCREEN_TIMEOUT_LONG}.
         * @return this object for method chaining
         */
        @Deprecated
        public WearableExtender setHintScreenTimeout(int timeout) {
            mHintScreenTimeout = timeout;
            return this;
        }

        /**
         * Get the duration, in milliseconds, that the screen should remain on for
         * when this notification is displayed.
         *
         * @return the duration in milliseconds if > 0, or either one of the sentinel values
         * {@link #SCREEN_TIMEOUT_SHORT} or {@link #SCREEN_TIMEOUT_LONG}.
         */
        @Deprecated
        public int getHintScreenTimeout() {
            return mHintScreenTimeout;
        }

        /**
         * Set a hint that this notification's {@link BigPictureStyle} (if present) should be
         * converted to low-bit and displayed in ambient mode, especially useful for barcodes and
         * qr codes, as well as other simple black-and-white tickets.
         *
         * @param hintAmbientBigPicture {@code true} to enable converstion and ambient.
         * @return this object for method chaining
         */
        public WearableExtender setHintAmbientBigPicture(boolean hintAmbientBigPicture) {
            setFlag(FLAG_BIG_PICTURE_AMBIENT, hintAmbientBigPicture);
            return this;
        }

        /**
         * Get a hint that this notification's {@link BigPictureStyle} (if present) should be
         * converted to low-bit and displayed in ambient mode, especially useful for barcodes and
         * qr codes, as well as other simple black-and-white tickets.
         *
         * @return {@code true} if it should be displayed in ambient, false otherwise
         * otherwise. The default value is {@code false} if this was never set.
         */
        public boolean getHintAmbientBigPicture() {
            return (mFlags & FLAG_BIG_PICTURE_AMBIENT) != 0;
        }

        /**
         * Set a hint that this notification's content intent will launch an {@link Activity}
         * directly, telling the platform that it can generate the appropriate transitions.
         *
         * @param hintContentIntentLaunchesActivity {@code true} if the content intent will launch
         *                                          an activity and transitions should be generated, false otherwise.
         * @return this object for method chaining
         */
        public WearableExtender setHintContentIntentLaunchesActivity(
                boolean hintContentIntentLaunchesActivity) {
            setFlag(FLAG_HINT_CONTENT_INTENT_LAUNCHES_ACTIVITY, hintContentIntentLaunchesActivity);
            return this;
        }

        /**
         * Get a hint that this notification's content intent will launch an {@link Activity}
         * directly, telling the platform that it can generate the appropriate transitions
         *
         * @return {@code true} if the content intent will launch an activity and transitions should
         * be generated, false otherwise. The default value is {@code false} if this was never set.
         */
        public boolean getHintContentIntentLaunchesActivity() {
            return (mFlags & FLAG_HINT_CONTENT_INTENT_LAUNCHES_ACTIVITY) != 0;
        }

        /**
         * Sets the dismissal id for this notification. If a notification is posted with a
         * dismissal id, then when that notification is canceled, notifications on other wearables
         * and the paired Android phone having that same dismissal id will also be canceled. See
         * <a href="{@docRoot}wear/notifications/index.html">Adding Wearable Features to
         * Notifications</a> for more information.
         *
         * @param dismissalId the dismissal id of the notification.
         * @return this object for method chaining
         */
        public WearableExtender setDismissalId(String dismissalId) {
            mDismissalId = dismissalId;
            return this;
        }

        /**
         * Returns the dismissal id of the notification.
         *
         * @return the dismissal id of the notification or null if it has not been set.
         */
        public String getDismissalId() {
            return mDismissalId;
        }

        /**
         * Sets a bridge tag for this notification. A bridge tag can be set for notifications
         * posted from a phone to provide finer-grained control on what notifications are bridged
         * to wearables. See <a href="{@docRoot}wear/notifications/index.html">Adding Wearable
         * Features to Notifications</a> for more information.
         *
         * @param bridgeTag the bridge tag of the notification.
         * @return this object for method chaining
         */
        public WearableExtender setBridgeTag(String bridgeTag) {
            mBridgeTag = bridgeTag;
            return this;
        }

        /**
         * Returns the bridge tag of the notification.
         *
         * @return the bridge tag or null if not present.
         */
        public String getBridgeTag() {
            return mBridgeTag;
        }

        private void setFlag(int mask, boolean value) {
            if (value) {
                mFlags |= mask;
            } else {
                mFlags &= ~mask;
            }
        }
    }

    /**
     * <p>Helper class to add Android Auto extensions to notifications. To create a notification
     * with car extensions:
     *
     * <ol>
     * <li>Create an {@link Builder}, setting any desired
     * properties.
     * <li>Create a {@link CarExtender}.
     * <li>Set car-specific properties using the {@code add} and {@code set} methods of
     * {@link CarExtender}.
     * to apply the extensions to a notification.
     * <li>Post the notification to the notification system with the
     * {@code NotificationManagerCompat.notify(...)} methods and not the
     * {@code NotificationManager.notify(...)} methods.
     * </ol>
     *
     * <pre class="prettyprint">
     * Notification notification = new NotificationCompat.Builder(context)
     *         ...
     *         .extend(new CarExtender()
     *                 .set*(...))
     *         .build();
     * </pre>
     *
     * <p>Car extensions can be accessed on an existing notification by using the
     * {@code CarExtender(Notification)} constructor, and then using the {@code get} methods
     * to access values.
     */
    public static final class CarExtender implements Extender {
        /**
         *
         **/
        // @RestrictTo(LIBRARY_GROUP)
        static final String EXTRA_CAR_EXTENDER = "android.car.EXTENSIONS";
        private static final String EXTRA_LARGE_ICON = "large_icon";
        private static final String EXTRA_CONVERSATION = "car_conversation";
        private static final String EXTRA_COLOR = "app_color";
        /**
         *
         **/
        // @RestrictTo(LIBRARY_GROUP)
        static final String EXTRA_INVISIBLE_ACTIONS = "invisible_actions";

        private static final String KEY_AUTHOR = "author";
        private static final String KEY_TEXT = "text";
        private static final String KEY_MESSAGES = "messages";
        private static final String KEY_REMOTE_INPUT = "remote_input";
        private static final String KEY_ON_REPLY = "on_reply";
        private static final String KEY_ON_READ = "on_read";
        private static final String KEY_PARTICIPANTS = "participants";
        private static final String KEY_TIMESTAMP = "timestamp";

        private Bitmap mLargeIcon;
        private UnreadConversation mUnreadConversation;
        private int mColor = NotificationCompat.COLOR_DEFAULT;

        /**
         * Create a {@link CarExtender} with default options.
         */
        public CarExtender() {
        }

        /**
         * Create a {@link CarExtender} from the CarExtender options of an existing Notification.
         *
         * @param notification The notification from which to copy options.
         */
        public CarExtender(Notification notification) {
            if (Build.VERSION.SDK_INT < 21) {
                return;
            }

            Bundle carBundle = getExtras(notification) == null
                    ? null : getExtras(notification).getBundle(EXTRA_CAR_EXTENDER);
            if (carBundle != null) {
                mLargeIcon = carBundle.getParcelable(EXTRA_LARGE_ICON);
                mColor = carBundle.getInt(EXTRA_COLOR, NotificationCompat.COLOR_DEFAULT);

                Bundle b = carBundle.getBundle(EXTRA_CONVERSATION);
                mUnreadConversation = getUnreadConversationFromBundle(b);
            }
        }

        // @RequiresApi(21)
        private static UnreadConversation getUnreadConversationFromBundle(@Nullable Bundle b) {
            if (b == null) {
                return null;
            }
            Parcelable[] parcelableMessages = b.getParcelableArray(KEY_MESSAGES);
            String[] messages = null;
            if (parcelableMessages != null) {
                String[] tmp = new String[parcelableMessages.length];
                boolean success = true;
                for (int i = 0; i < tmp.length; i++) {
                    if (!(parcelableMessages[i] instanceof Bundle)) {
                        success = false;
                        break;
                    }
                    tmp[i] = ((Bundle) parcelableMessages[i]).getString(KEY_TEXT);
                    if (tmp[i] == null) {
                        success = false;
                        break;
                    }
                }
                if (success) {
                    messages = tmp;
                } else {
                    return null;
                }
            }

            PendingIntent onRead = b.getParcelable(KEY_ON_READ);
            PendingIntent onReply = b.getParcelable(KEY_ON_REPLY);

            android.app.RemoteInput remoteInput = b.getParcelable(KEY_REMOTE_INPUT);

            String[] participants = b.getStringArray(KEY_PARTICIPANTS);
            if (participants == null || participants.length != 1) {
                return null;
            }

            RemoteInput remoteInputCompat = remoteInput != null
                    ? new RemoteInput(remoteInput.getResultKey(),
                    remoteInput.getLabel(),
                    remoteInput.getChoices(),
                    remoteInput.getAllowFreeFormInput(),
                    remoteInput.getExtras(),
                    null /* allowedDataTypes */)
                    : null;

            return new UnreadConversation(messages, remoteInputCompat, onReply,
                    onRead, participants, b.getLong(KEY_TIMESTAMP));
        }

        // @RequiresApi(21)
        private static Bundle getBundleForUnreadConversation(@NonNull UnreadConversation uc) {
            Bundle b = new Bundle();
            String author = null;
            if (uc.getParticipants() != null && uc.getParticipants().length > 1) {
                author = uc.getParticipants()[0];
            }
            Parcelable[] messages = new Parcelable[uc.getMessages().length];
            for (int i = 0; i < messages.length; i++) {
                Bundle m = new Bundle();
                m.putString(KEY_TEXT, uc.getMessages()[i]);
                m.putString(KEY_AUTHOR, author);
                messages[i] = m;
            }
            b.putParcelableArray(KEY_MESSAGES, messages);
            RemoteInput remoteInputCompat = uc.getRemoteInput();
            if (remoteInputCompat != null) {
                android.app.RemoteInput remoteInput =
                        new android.app.RemoteInput.Builder(remoteInputCompat.getResultKey())
                                .setLabel(remoteInputCompat.getLabel())
                                .setChoices(remoteInputCompat.getChoices())
                                .setAllowFreeFormInput(remoteInputCompat.getAllowFreeFormInput())
                                .addExtras(remoteInputCompat.getExtras())
                                .build();
                b.putParcelable(KEY_REMOTE_INPUT, remoteInput);
            }
            b.putParcelable(KEY_ON_REPLY, uc.getReplyPendingIntent());
            b.putParcelable(KEY_ON_READ, uc.getReadPendingIntent());
            b.putStringArray(KEY_PARTICIPANTS, uc.getParticipants());
            b.putLong(KEY_TIMESTAMP, uc.getLatestTimestamp());
            return b;
        }

        /**
         * Apply car extensions to a notification that is being built. This is typically called by
         * the {@link Builder#extend(Extender)}
         * method of {@link Builder}.
         */
        @Override
        public Builder extend(Builder builder) {

            Bundle carExtensions = new Bundle();

            if (mLargeIcon != null) {
                carExtensions.putParcelable(EXTRA_LARGE_ICON, mLargeIcon);
            }
            if (mColor != NotificationCompat.COLOR_DEFAULT) {
                carExtensions.putInt(EXTRA_COLOR, mColor);
            }

            if (mUnreadConversation != null) {
                Bundle b = getBundleForUnreadConversation(mUnreadConversation);
                carExtensions.putBundle(EXTRA_CONVERSATION, b);
            }

            builder.getExtras().putBundle(EXTRA_CAR_EXTENDER, carExtensions);
            return builder;
        }

        /**
         * Sets the accent color to use when Android Auto presents the notification.
         * <p>
         * Android Auto uses the color set with {@link Builder#setColor(int)}}
         * to accent the displayed notification. However, not all colors are acceptable in an
         * automotive setting. This method can be used to override the color provided in the
         * notification in such a situation.
         */
        public CarExtender setColor(@ColorInt int color) {
            mColor = color;
            return this;
        }

        /**
         * Gets the accent color.
         *
         * @see #setColor
         */
        @ColorInt
        public int getColor() {
            return mColor;
        }

        /**
         * Sets the large icon of the car notification.
         * <p>
         * If no large icon is set in the extender, Android Auto will display the icon
         * specified by {@link Builder#setLargeIcon(Bitmap)}
         *
         * @param largeIcon The large icon to use in the car notification.
         * @return This object for method chaining.
         */
        public CarExtender setLargeIcon(Bitmap largeIcon) {
            mLargeIcon = largeIcon;
            return this;
        }

        /**
         * Gets the large icon used in this car notification, or null if no icon has been set.
         *
         * @return The large icon for the car notification.
         * @see CarExtender#setLargeIcon
         */
        public Bitmap getLargeIcon() {
            return mLargeIcon;
        }

        /**
         * Sets the unread conversation in a message notification.
         *
         * @param unreadConversation The unread part of the conversation this notification conveys.
         * @return This object for method chaining.
         */
        public CarExtender setUnreadConversation(UnreadConversation unreadConversation) {
            mUnreadConversation = unreadConversation;
            return this;
        }

        /**
         * Returns the unread conversation conveyed by this notification.
         *
         * @see #setUnreadConversation(UnreadConversation)
         */
        public UnreadConversation getUnreadConversation() {
            return mUnreadConversation;
        }

        /**
         * A class which holds the unread messages from a conversation.
         */
        public static class UnreadConversation {
            private final String[] mMessages;
            private final RemoteInput mRemoteInput;
            private final PendingIntent mReplyPendingIntent;
            private final PendingIntent mReadPendingIntent;
            private final String[] mParticipants;
            private final long mLatestTimestamp;

            UnreadConversation(String[] messages, RemoteInput remoteInput,
                               PendingIntent replyPendingIntent, PendingIntent readPendingIntent,
                               String[] participants, long latestTimestamp) {
                mMessages = messages;
                mRemoteInput = remoteInput;
                mReadPendingIntent = readPendingIntent;
                mReplyPendingIntent = replyPendingIntent;
                mParticipants = participants;
                mLatestTimestamp = latestTimestamp;
            }

            /**
             * Gets the list of messages conveyed by this notification.
             */
            public String[] getMessages() {
                return mMessages;
            }

            /**
             * Gets the remote input that will be used to convey the response to a message list, or
             * null if no such remote input exists.
             */
            public RemoteInput getRemoteInput() {
                return mRemoteInput;
            }

            /**
             * Gets the pending intent that will be triggered when the user replies to this
             * notification.
             */
            public PendingIntent getReplyPendingIntent() {
                return mReplyPendingIntent;
            }

            /**
             * Gets the pending intent that Android Auto will send after it reads aloud all messages
             * in this object's message list.
             */
            public PendingIntent getReadPendingIntent() {
                return mReadPendingIntent;
            }

            /**
             * Gets the participants in the conversation.
             */
            public String[] getParticipants() {
                return mParticipants;
            }

            /**
             * Gets the firs participant in the conversation.
             */
            public String getParticipant() {
                return mParticipants.length > 0 ? mParticipants[0] : null;
            }

            /**
             * Gets the timestamp of the conversation.
             */
            public long getLatestTimestamp() {
                return mLatestTimestamp;
            }

            /**
             * Builder class for {@link UnreadConversation} objects.
             */
            public static class Builder {
                private final List<String> mMessages = new ArrayList<String>();
                private final String mParticipant;
                private RemoteInput mRemoteInput;
                private PendingIntent mReadPendingIntent;
                private PendingIntent mReplyPendingIntent;
                private long mLatestTimestamp;

                /**
                 * Constructs a new builder for {@link UnreadConversation}.
                 *
                 * @param name The name of the other participant in the conversation.
                 */
                public Builder(String name) {
                    mParticipant = name;
                }

                /**
                 * Appends a new unread message to the list of messages for this conversation.
                 * <p>
                 * The messages should be added from oldest to newest.
                 *
                 * @param message The text of the new unread message.
                 * @return This object for method chaining.
                 */
                public Builder addMessage(String message) {
                    mMessages.add(message);
                    return this;
                }

                /**
                 * Sets the pending intent and remote input which will convey the reply to this
                 * notification.
                 *
                 * @param pendingIntent The pending intent which will be triggered on a reply.
                 * @param remoteInput   The remote input parcelable which will carry the reply.
                 * @return This object for method chaining.
                 * @see UnreadConversation#getRemoteInput
                 * @see UnreadConversation#getReplyPendingIntent
                 */
                public Builder setReplyAction(
                        PendingIntent pendingIntent, RemoteInput remoteInput) {
                    mRemoteInput = remoteInput;
                    mReplyPendingIntent = pendingIntent;

                    return this;
                }

                /**
                 * Sets the pending intent that will be sent once the messages in this notification
                 * are read.
                 *
                 * @param pendingIntent The pending intent to use.
                 * @return This object for method chaining.
                 */
                public Builder setReadPendingIntent(PendingIntent pendingIntent) {
                    mReadPendingIntent = pendingIntent;
                    return this;
                }

                /**
                 * Sets the timestamp of the most recent message in an unread conversation.
                 * <p>
                 * If a messaging notification has been posted by your application and has not
                 * yet been cancelled, posting a later notification with the same id and tag
                 * but without a newer timestamp may result in Android Auto not displaying a
                 * heads up notification for the later notification.
                 *
                 * @param timestamp The timestamp of the most recent message in the conversation.
                 * @return This object for method chaining.
                 */
                public Builder setLatestTimestamp(long timestamp) {
                    mLatestTimestamp = timestamp;
                    return this;
                }

                /**
                 * Builds a new unread conversation object.
                 *
                 * @return The new unread conversation object.
                 */
                public UnreadConversation build() {
                    String[] messages = mMessages.toArray(new String[mMessages.size()]);
                    String[] participants = {mParticipant};
                    return new UnreadConversation(messages, mRemoteInput, mReplyPendingIntent,
                            mReadPendingIntent, participants, mLatestTimestamp);
                }
            }
        }
    }


    /**
     * Get an array of Notification objects from a parcelable array bundle field.
     * Update the bundle to have a typed array so fetches in the future don't need
     * to do an array copy.
     */
    static Notification[] getNotificationArrayFromBundle(Bundle bundle, String key) {
        Parcelable[] array = bundle.getParcelableArray(key);
        if (array instanceof Notification[] || array == null) {
            return (Notification[]) array;
        }
        Notification[] typedArray = new Notification[array.length];
        for (int i = 0; i < array.length; i++) {
            typedArray[i] = (Notification) array[i];
        }
        bundle.putParcelableArray(key, typedArray);
        return typedArray;
    }

    /**
     * Gets the {@link Notification#extras} field from a notification in a backwards
     * compatible manner. Extras field was supported from JellyBean (Api level 16)
     * forwards. This function will return {@code null} on older api levels.
     */
    @Nullable
    public static Bundle getExtras(Notification notification) {
        if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras;
        } else if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification);
        } else {
            return null;
        }
    }

    /**
     * Get the number of actions in this notification in a backwards compatible
     * manner. Actions were supported from JellyBean (Api level 16) forwards.
     */
    public static int getActionCount(Notification notification) {
        if (Build.VERSION.SDK_INT >= 19) {
            return notification.actions != null ? notification.actions.length : 0;
        } else if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getActionCount(notification);
        } else {
            return 0;
        }
    }

    /**
     * Get an action on this notification in a backwards compatible
     * manner. Actions were supported from JellyBean (Api level 16) forwards.
     *
     * @param notification The notification to inspect.
     * @param actionIndex  The index of the action to retrieve.
     */
    public static Action getAction(Notification notification, int actionIndex) {
        if (Build.VERSION.SDK_INT >= 20) {
            return getActionCompatFromAction(notification.actions[actionIndex]);
        } else if (Build.VERSION.SDK_INT >= 19) {
            Notification.Action action = notification.actions[actionIndex];
            Bundle actionExtras = null;
            SparseArray<Bundle> actionExtrasMap = notification.extras.getSparseParcelableArray(
                    NotificationCompatExtras.EXTRA_ACTION_EXTRAS);
            if (actionExtrasMap != null) {
                actionExtras = actionExtrasMap.get(actionIndex);
            }
            return NotificationCompatJellybean.readAction(action.icon, action.title,
                    action.actionIntent, actionExtras);
        } else if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getAction(notification, actionIndex);
        } else {
            return null;
        }
    }

    // @RequiresApi(20)
    static Action getActionCompatFromAction(Notification.Action action) {
        final RemoteInput[] remoteInputs;
        final android.app.RemoteInput[] srcArray = action.getRemoteInputs();
        if (srcArray == null) {
            remoteInputs = null;
        } else {
            remoteInputs = new RemoteInput[srcArray.length];
            for (int i = 0; i < srcArray.length; i++) {
                android.app.RemoteInput src = srcArray[i];
                remoteInputs[i] = new RemoteInput(src.getResultKey(), src.getLabel(),
                        src.getChoices(), src.getAllowFreeFormInput(), src.getExtras(), null);
            }
        }

        final boolean allowGeneratedReplies;
        if (Build.VERSION.SDK_INT >= 24) {
            allowGeneratedReplies = action.getExtras().getBoolean(
                    NotificationCompatJellybean.EXTRA_ALLOW_GENERATED_REPLIES)
                    || action.getAllowGeneratedReplies();
        } else {
            allowGeneratedReplies = action.getExtras().getBoolean(
                    NotificationCompatJellybean.EXTRA_ALLOW_GENERATED_REPLIES);
        }

        final boolean showsUserInterface =
                action.getExtras().getBoolean(Action.EXTRA_SHOWS_USER_INTERFACE, true);

        final @Action.SemanticAction int semanticAction;
        semanticAction = action.getExtras().getInt(
                Action.EXTRA_SEMANTIC_ACTION, Action.SEMANTIC_ACTION_NONE);

        return new Action(action.icon, action.title, action.actionIntent,
                action.getExtras(), remoteInputs, null, allowGeneratedReplies,
                semanticAction, showsUserInterface);
    }

    /**
     * Returns the invisible actions contained within the given notification.
     */
    // @RequiresApi(21)
    public static List<Action> getInvisibleActions(Notification notification) {
        ArrayList<Action> result = new ArrayList<>();

        Bundle carExtenderBundle = notification.extras.getBundle(CarExtender.EXTRA_CAR_EXTENDER);
        if (carExtenderBundle == null) {
            return result;
        }

        Bundle listBundle = carExtenderBundle.getBundle(CarExtender.EXTRA_INVISIBLE_ACTIONS);
        if (listBundle != null) {
            for (int i = 0; i < listBundle.size(); i++) {
                result.add(NotificationCompatJellybean.getActionFromBundle(
                        listBundle.getBundle(Integer.toString(i))));
            }
        }
        return result;
    }

    /**
     * Returns the content title of a {@link Notification}.
     **/
    // @RequiresApi(19)
    public static CharSequence getContentTitle(Notification notification) {
        return notification.extras.getCharSequence(Notification.EXTRA_TITLE);
    }

    /**
     * Get the category of this notification in a backwards compatible
     * manner.
     *
     * @param notification The notification to inspect.
     */
    public static String getCategory(Notification notification) {
        if (Build.VERSION.SDK_INT >= 21) {
            return notification.category;
        } else {
            return null;
        }
    }

    /**
     * Get whether or not this notification is only relevant to the current device.
     *
     * <p>Some notifications can be bridged to other devices for remote display.
     * If this hint is set, it is recommend that this notification not be bridged.
     */
    public static boolean getLocalOnly(Notification notification) {
        if (Build.VERSION.SDK_INT >= 20) {
            return (notification.flags & Notification.FLAG_LOCAL_ONLY) != 0;
        } else if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras.getBoolean(NotificationCompatExtras.EXTRA_LOCAL_ONLY);
        } else if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification).getBoolean(
                    NotificationCompatExtras.EXTRA_LOCAL_ONLY);
        } else {
            return false;
        }
    }

    /**
     * Get the key used to group this notification into a cluster or stack
     * with other notifications on devices which support such rendering.
     */
    public static String getGroup(Notification notification) {
        if (Build.VERSION.SDK_INT >= 20) {
            return notification.getGroup();
        } else if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras.getString(NotificationCompatExtras.EXTRA_GROUP_KEY);
        } else if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification).getString(
                    NotificationCompatExtras.EXTRA_GROUP_KEY);
        } else {
            return null;
        }
    }

    /**
     * Get whether this notification to be the group summary for a group of notifications.
     * Grouped notifications may display in a cluster or stack on devices which
     * support such rendering. Requires a group key also be set using {@link Builder#setGroup}.
     *
     * @return Whether this notification is a group summary.
     */
    public static boolean isGroupSummary(Notification notification) {
        if (Build.VERSION.SDK_INT >= 20) {
            return (notification.flags & Notification.FLAG_GROUP_SUMMARY) != 0;
        } else if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras.getBoolean(NotificationCompatExtras.EXTRA_GROUP_SUMMARY);
        } else if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification).getBoolean(
                    NotificationCompatExtras.EXTRA_GROUP_SUMMARY);
        } else {
            return false;
        }
    }

    /**
     * Get a sort key that orders this notification among other notifications from the
     * same package. This can be useful if an external sort was already applied and an app
     * would like to preserve this. Notifications will be sorted lexicographically using this
     * value, although providing different priorities in addition to providing sort key may
     * cause this value to be ignored.
     *
     * <p>This sort key can also be used to order members of a notification group. See
     * {@link Builder#setGroup}.
     *
     * @see String#compareTo(String)
     */
    public static String getSortKey(Notification notification) {
        if (Build.VERSION.SDK_INT >= 20) {
            return notification.getSortKey();
        } else if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras.getString(NotificationCompatExtras.EXTRA_SORT_KEY);
        } else if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification).getString(
                    NotificationCompatExtras.EXTRA_SORT_KEY);
        } else {
            return null;
        }
    }

    /**
     * @return the ID of the channel this notification posts to.
     */
    public static String getChannelId(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getChannelId();
        } else {
            return null;
        }
    }

    /**
     * Returns the time at which this notification should be canceled by the system, if it's not
     * canceled already.
     */
    public static long getTimeoutAfter(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getTimeoutAfter();
        } else {
            return 0;
        }
    }

    /**
     * Returns what icon should be shown for this notification if it is being displayed in a
     * Launcher that supports badging. Will be one of {@link #BADGE_ICON_NONE},
     * {@link #BADGE_ICON_SMALL}, or {@link #BADGE_ICON_LARGE}.
     */
    public static int getBadgeIconType(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getBadgeIconType();
        } else {
            return BADGE_ICON_NONE;
        }
    }

    /**
     * Returns the {@link ShortcutInfoCompat#getId()} () id} that this
     * notification supersedes, if any.
     */
    public static String getShortcutId(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getShortcutId();
        } else {
            return null;
        }
    }

    /**
     * Returns which type of notifications in a group are responsible for audibly alerting the
     * user. See {@link #GROUP_ALERT_ALL}, {@link #GROUP_ALERT_CHILDREN},
     * {@link #GROUP_ALERT_SUMMARY}.
     */
    @GroupAlertBehavior
    public static int getGroupAlertBehavior(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getGroupAlertBehavior();
        } else {
            return GROUP_ALERT_ALL;
        }
    }

    /**
     * @deprecated This type should not be instantiated as it contains only static methods.
     */
    @Deprecated
    @SuppressWarnings("PrivateConstructorForUtilityClass")
    public NotificationCompat() {
    }
}
