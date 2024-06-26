/*
 * Copyright (C) 2017 The Android Open Source Project
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

import static tornaco.apps.shortx.core.compat.NotificationCompat.DEFAULT_SOUND;
import static tornaco.apps.shortx.core.compat.NotificationCompat.DEFAULT_VIBRATE;
import static tornaco.apps.shortx.core.compat.NotificationCompat.FLAG_GROUP_SUMMARY;
import static tornaco.apps.shortx.core.compat.NotificationCompat.GROUP_ALERT_ALL;
import static tornaco.apps.shortx.core.compat.NotificationCompat.GROUP_ALERT_CHILDREN;
import static tornaco.apps.shortx.core.compat.NotificationCompat.GROUP_ALERT_SUMMARY;

import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper around {@link Notification.Builder} that works in a backwards compatible way.
 */
class NotificationCompatBuilder implements NotificationBuilderWithBuilderAccessor {
    private final Notification.Builder mBuilder;
    private final NotificationCompat.Builder mBuilderCompat;

    // @RequiresApi(16) - uncomment when lint bug is fixed.
    private final RemoteViews mContentView;
    // @RequiresApi(16) - uncomment when lint bug is fixed.
    private final RemoteViews mBigContentView;
    // @RequiresApi(16) - uncomment when lint bug is fixed.
    private final List<Bundle> mActionExtrasList = new ArrayList<>();
    // @RequiresApi(16) - uncomment when lint bug is fixed.
    private final Bundle mExtras = new Bundle();
    // @RequiresApi(20) - uncomment when lint bug is fixed.
    private final int mGroupAlertBehavior;
    // @RequiresApi(21) - uncomment when lint bug is fixed.
    private RemoteViews mHeadsUpContentView;

    NotificationCompatBuilder(NotificationCompat.Builder b) {
        mBuilderCompat = b;
        if (Build.VERSION.SDK_INT >= 26) {
            mBuilder = new Notification.Builder(b.mContext, b.mChannelId);
        } else {
            mBuilder = new Notification.Builder(b.mContext);
        }
        Notification n = b.mNotification;
        mBuilder.setWhen(n.when)
                .setSmallIcon(n.icon, n.iconLevel)
                .setContent(n.contentView)
                .setTicker(n.tickerText, b.mTickerView)
                .setVibrate(n.vibrate)
                .setLights(n.ledARGB, n.ledOnMS, n.ledOffMS)
                .setOngoing((n.flags & Notification.FLAG_ONGOING_EVENT) != 0)
                .setOnlyAlertOnce((n.flags & Notification.FLAG_ONLY_ALERT_ONCE) != 0)
                .setAutoCancel((n.flags & Notification.FLAG_AUTO_CANCEL) != 0)
                .setDefaults(n.defaults)
                .setContentTitle(b.mContentTitle)
                .setContentText(b.mContentText)
                .setContentInfo(b.mContentInfo)
                .setContentIntent(b.mContentIntent)
                .setDeleteIntent(n.deleteIntent)
                .setFullScreenIntent(b.mFullScreenIntent,
                        (n.flags & Notification.FLAG_HIGH_PRIORITY) != 0)
                .setLargeIcon(b.mLargeIcon)
                .setNumber(b.mNumber)
                .setProgress(b.mProgressMax, b.mProgress, b.mProgressIndeterminate);
        if (Build.VERSION.SDK_INT < 21) {
            mBuilder.setSound(n.sound, n.audioStreamType);
        }
        mBuilder.setSubText(b.mSubText)
                .setUsesChronometer(b.mUseChronometer)
                .setPriority(b.mPriority);

        for (NotificationCompat.Action action : b.mActions) {
            addAction(action);
        }

        if (b.mExtras != null) {
            mExtras.putAll(b.mExtras);
        }

        mContentView = b.mContentView;
        mBigContentView = b.mBigContentView;
        mBuilder.setShowWhen(b.mShowWhen);

        mBuilder.setLocalOnly(b.mLocalOnly)
                .setGroup(b.mGroupKey)
                .setGroupSummary(b.mGroupSummary)
                .setSortKey(b.mSortKey);

        mGroupAlertBehavior = b.mGroupAlertBehavior;
        if (Build.VERSION.SDK_INT >= 21) {
            mBuilder.setCategory(b.mCategory)
                    .setColor(b.mColor)
                    .setVisibility(b.mVisibility)
                    .setPublicVersion(b.mPublicVersion)
                    .setSound(n.sound, n.audioAttributes);

            for (String person : b.mPeople) {
                mBuilder.addPerson(person);
            }
            mHeadsUpContentView = b.mHeadsUpContentView;

            if (b.mInvisibleActions.size() > 0) {
                // Invisible actions should be stored in the extender so we need to check if one
                // exists already.
                Bundle carExtenderBundle =
                        b.getExtras().getBundle(NotificationCompat.CarExtender.EXTRA_CAR_EXTENDER);
                if (carExtenderBundle == null) {
                    carExtenderBundle = new Bundle();
                }
                Bundle listBundle = new Bundle();
                for (int i = 0; i < b.mInvisibleActions.size(); i++) {
                    listBundle.putBundle(
                            Integer.toString(i),
                            NotificationCompatJellybean.getBundleForAction(
                                    b.mInvisibleActions.get(i)));
                }
                carExtenderBundle.putBundle(
                        NotificationCompat.CarExtender.EXTRA_INVISIBLE_ACTIONS, listBundle);
                b.getExtras().putBundle(
                        NotificationCompat.CarExtender.EXTRA_CAR_EXTENDER, carExtenderBundle);
                mExtras.putBundle(
                        NotificationCompat.CarExtender.EXTRA_CAR_EXTENDER, carExtenderBundle);
            }
        }
        if (Build.VERSION.SDK_INT >= 24) {
            mBuilder.setExtras(b.mExtras)
                    .setRemoteInputHistory(b.mRemoteInputHistory);
            if (b.mContentView != null) {
                mBuilder.setCustomContentView(b.mContentView);
            }
            if (b.mBigContentView != null) {
                mBuilder.setCustomBigContentView(b.mBigContentView);
            }
            if (b.mHeadsUpContentView != null) {
                mBuilder.setCustomHeadsUpContentView(b.mHeadsUpContentView);
            }
        }
        if (Build.VERSION.SDK_INT >= 26) {
            mBuilder.setBadgeIconType(b.mBadgeIcon)
                    .setShortcutId(b.mShortcutId)
                    .setTimeoutAfter(b.mTimeout)
                    .setGroupAlertBehavior(b.mGroupAlertBehavior);
            if (b.mColorizedSet) {
                mBuilder.setColorized(b.mColorized);
            }

            if (!TextUtils.isEmpty(b.mChannelId)) {
                mBuilder.setSound(null)
                        .setDefaults(0)
                        .setLights(0, 0, 0)
                        .setVibrate(null);
            }
        }
    }

    @Override
    public Notification.Builder getBuilder() {
        return mBuilder;
    }

    public Notification build() {
        final NotificationCompat.Style style = mBuilderCompat.mStyle;
        if (style != null) {
            style.apply(this);
        }

        RemoteViews styleContentView = style != null
                ? style.makeContentView(this)
                : null;
        Notification n = buildInternal();
        if (styleContentView != null) {
            n.contentView = styleContentView;
        } else if (mBuilderCompat.mContentView != null) {
            n.contentView = mBuilderCompat.mContentView;
        }
        if (style != null) {
            RemoteViews styleBigContentView = style.makeBigContentView(this);
            if (styleBigContentView != null) {
                n.bigContentView = styleBigContentView;
            }
        }
        if (Build.VERSION.SDK_INT >= 21 && style != null) {
            RemoteViews styleHeadsUpContentView =
                    mBuilderCompat.mStyle.makeHeadsUpContentView(this);
            if (styleHeadsUpContentView != null) {
                n.headsUpContentView = styleHeadsUpContentView;
            }
        }

        if (style != null) {
            Bundle extras = NotificationCompat.getExtras(n);
            if (extras != null) {
                style.addCompatExtras(extras);
            }
        }

        return n;
    }

    private void addAction(NotificationCompat.Action action) {
        Notification.Action.Builder actionBuilder = new Notification.Action.Builder(
                action.getIcon(), action.getTitle(), action.getActionIntent());
        if (action.getRemoteInputs() != null) {
            for (android.app.RemoteInput remoteInput : RemoteInput.fromCompat(
                    action.getRemoteInputs())) {
                actionBuilder.addRemoteInput(remoteInput);
            }
        }
        Bundle actionExtras;
        if (action.getExtras() != null) {
            actionExtras = new Bundle(action.getExtras());
        } else {
            actionExtras = new Bundle();
        }
        actionExtras.putBoolean(NotificationCompatJellybean.EXTRA_ALLOW_GENERATED_REPLIES,
                action.getAllowGeneratedReplies());
        if (Build.VERSION.SDK_INT >= 24) {
            actionBuilder.setAllowGeneratedReplies(action.getAllowGeneratedReplies());
        }

        actionExtras.putInt(NotificationCompat.Action.EXTRA_SEMANTIC_ACTION,
                action.getSemanticAction());

        // TODO. Need upgrade apk.
        if (Build.VERSION.SDK_INT >= 28) {
            // actionBuilder.setSemanticAction(action.getSemanticAction());
        }

        actionExtras.putBoolean(NotificationCompat.Action.EXTRA_SHOWS_USER_INTERFACE,
                action.getShowsUserInterface());
        actionBuilder.addExtras(actionExtras);
        mBuilder.addAction(actionBuilder.build());
    }

    protected Notification buildInternal() {
        if (Build.VERSION.SDK_INT >= 26) {
            return mBuilder.build();
        } else if (Build.VERSION.SDK_INT >= 24) {
            Notification notification = mBuilder.build();

            if (mGroupAlertBehavior != GROUP_ALERT_ALL) {
                // if is summary and only children should alert
                if (notification.getGroup() != null
                        && (notification.flags & FLAG_GROUP_SUMMARY) != 0
                        && mGroupAlertBehavior == GROUP_ALERT_CHILDREN) {
                    removeSoundAndVibration(notification);
                }
                // if is group child and only summary should alert
                if (notification.getGroup() != null
                        && (notification.flags & FLAG_GROUP_SUMMARY) == 0
                        && mGroupAlertBehavior == GROUP_ALERT_SUMMARY) {
                    removeSoundAndVibration(notification);
                }
            }

            return notification;
        } else if (Build.VERSION.SDK_INT >= 21) {
            mBuilder.setExtras(mExtras);
            Notification notification = mBuilder.build();
            if (mContentView != null) {
                notification.contentView = mContentView;
            }
            if (mBigContentView != null) {
                notification.bigContentView = mBigContentView;
            }
            if (mHeadsUpContentView != null) {
                notification.headsUpContentView = mHeadsUpContentView;
            }

            if (mGroupAlertBehavior != GROUP_ALERT_ALL) {
                // if is summary and only children should alert
                if (notification.getGroup() != null
                        && (notification.flags & FLAG_GROUP_SUMMARY) != 0
                        && mGroupAlertBehavior == GROUP_ALERT_CHILDREN) {
                    removeSoundAndVibration(notification);
                }
                // if is group child and only summary should alert
                if (notification.getGroup() != null
                        && (notification.flags & FLAG_GROUP_SUMMARY) == 0
                        && mGroupAlertBehavior == GROUP_ALERT_SUMMARY) {
                    removeSoundAndVibration(notification);
                }
            }
            return notification;
        } else if (Build.VERSION.SDK_INT >= 20) {
            mBuilder.setExtras(mExtras);
            Notification notification = mBuilder.build();
            if (mContentView != null) {
                notification.contentView = mContentView;
            }
            if (mBigContentView != null) {
                notification.bigContentView = mBigContentView;
            }

            if (mGroupAlertBehavior != GROUP_ALERT_ALL) {
                // if is summary and only children should alert
                if (notification.getGroup() != null
                        && (notification.flags & FLAG_GROUP_SUMMARY) != 0
                        && mGroupAlertBehavior == GROUP_ALERT_CHILDREN) {
                    removeSoundAndVibration(notification);
                }
                // if is group child and only summary should alert
                if (notification.getGroup() != null
                        && (notification.flags & FLAG_GROUP_SUMMARY) == 0
                        && mGroupAlertBehavior == GROUP_ALERT_SUMMARY) {
                    removeSoundAndVibration(notification);
                }
            }

            return notification;
        } else {
            SparseArray<Bundle> actionExtrasMap =
                    NotificationCompatJellybean.buildActionExtrasMap(mActionExtrasList);
            if (actionExtrasMap != null) {
                // Add the action extras sparse array if any action was added with extras.
                mExtras.putSparseParcelableArray(
                        NotificationCompatExtras.EXTRA_ACTION_EXTRAS, actionExtrasMap);
            }
            mBuilder.setExtras(mExtras);
            Notification notification = mBuilder.build();
            if (mContentView != null) {
                notification.contentView = mContentView;
            }
            if (mBigContentView != null) {
                notification.bigContentView = mBigContentView;
            }
            return notification;
        }
    }

    private void removeSoundAndVibration(Notification notification) {
        notification.sound = null;
        notification.vibrate = null;
        notification.defaults &= ~DEFAULT_SOUND;
        notification.defaults &= ~DEFAULT_VIBRATE;
    }
}
