/*
 * Copyright 2022 Pranav Pandey
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

package com.pranavpandey.android.dynamic.rating;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.fragment.app.FragmentActivity;

import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.rating.dialog.DynamicRatingDialog;
import com.pranavpandey.android.dynamic.rating.listener.base.RatingListener;

import java.util.Date;

/**
 * Helper class to handle rating events requested by the app.
 * <p>It must be initialized once before accessing its methods.
 */
public class DynamicRating {

    /**
     * Singleton instance of {@link DynamicRating}.
     */
    private static DynamicRating sInstance;

    /**
     * Base key to store and retrieve the data.
     */
    private String mBaseKey;

    /**
     * Minimum no. of days to be passed.
     */
    private int mRateInterval;

    /**
     * Minimum no. of launch count to be reached.
     */
    private int mRateCount;

    /**
     * Minimum no. of days to be passed after the last reminder.
     */
    private int mRemindInterval;

    /**
     * Making default constructor private so that it cannot be initialized without a context.
     * <p>Use {@link #getInstance(Context)} instead.
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    private DynamicRating() { }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The context to be used.
     */
    private DynamicRating(@NonNull Context context) {
        DynamicPreferences.initializeInstance(context);

        this.mRateInterval = Rating.Default.RATE_INTERVAL;
        this.mRateCount = Rating.Default.RATE_COUNT;
        this.mRemindInterval = Rating.Default.REMIND_INTERVAL;
    }

    /**
     * Retrieves the singleton instance of {@link DynamicRating}.
     * <p>Must be called before accessing the public methods.
     *
     * @param context The context to retrieve resources.
     *
     * @return The singleton instance of {@link DynamicRating}.
     */
    public static synchronized @NonNull DynamicRating getInstance(@Nullable Context context) {
        if (context == null) {
            throw new NullPointerException("Context should not be null.");
        }

        if (sInstance == null) {
            sInstance = new DynamicRating(context);
        }

        return sInstance;
    }

    /**
     * Initialize the dynamic rating.
     *
     * @return The {@link DynamicRating} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicRating initialize() {
        if (isFirstLaunch()) {
            setFirstLaunch(System.currentTimeMillis());
        }

        if (isRequest()) {
            setLaunchCount(getLaunchCount() + 1);
        }

        return this;
    }

    /**
     * Checks whether the supplied date is due against the current date.
     *
     * @param millis The date to be checked.
     * @param threshold The threshold to be met.
     *
     * @return {@code true} if the supplied date is due against the current date.
     */
    private boolean isDueDate(long millis, int threshold) {
        return new Date().getTime() - millis >= threshold * 24 * 60 * 60 * 1000L;
    }

    /**
     * Returns the base to store and retrieve the data.
     *
     * @return The base to store and retrieve the data.
     */
    public @NonNull String getBaseKey() {
        return mBaseKey != null ? mBaseKey : Rating.Key.BASE;
    }

    /**
     * Sets the base key to store and retrieve the data.
     *
     * @param baseKey The base to be set.
     *
     * @return The {@link DynamicRating} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicRating setBaseKey(@Nullable String baseKey) {
        this.mBaseKey = baseKey;

        return this;
    }

    /**
     * Sets the minimum no. of days to be passed.
     *
     * @param interval The interval to be set.
     *
     * @return The {@link DynamicRating} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicRating setRateInterval(int interval) {
        this.mRateInterval = interval;

        return this;
    }

    /**
     * Sets the minimum no. of launch count to be reached.
     *
     * @param count The launch count to be set.
     *
     * @return The {@link DynamicRating} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicRating setRateCount(int count) {
        this.mRateCount = count;

        return this;
    }

    /**
     * Sets the minimum no. of days to be passed after the last reminder.
     *
     * @param interval The interval to be set.
     *
     * @return The {@link DynamicRating} object to allow for chaining of calls to set methods.
     */
    public @NonNull DynamicRating setRemindInterval(int interval) {
        this.mRemindInterval = interval;

        return this;
    }

    /**
     * Checks whether the dynamic rating is initialized for the first time.
     *
     * @return {@code true} if the dynamic rating is initialized for the first time.
     */
    public boolean isFirstLaunch() {
        return getFirstLaunch() == Rating.Value.FIRST_LAUNCH;
    }

    /**
     * Checks whether the rating interval conditions are met.
     *
     * @return {@code true} if the rating interval conditions are met.
     */
    public boolean isDueRating() {
        return isDueDate(getFirstLaunch(), mRateInterval);
    }

    /**
     * Checks whether the launch count conditions are met.
     *
     * @return {@code true} if the launch count conditions are met.
     */
    public boolean isDueCount() {
        return getLaunchCount() >= mRateCount;
    }

    /**
     * Checks whether the rating reminder conditions are met.
     *
     * @return {@code true} if the reminder conditions are met.
     */
    public boolean isDueReminder() {
        return isDueDate(getLastReminder(), mRemindInterval);
    }

    /**
     * Checks whether all the rating conditions are met.
     *
     * @return {@code true} if all the rating conditions are met.
     */
    public boolean shouldRate() {
        return isRequest() && isDueRating() && isDueCount() && isDueReminder();
    }

    /**
     * Show the {@link DynamicRatingDialog} if all the rating conditions are met.
     *
     * @param ratingListener The rating listener for the dialog.
     * @param fragmentActivity The fragment activity to show the dialog.
     *
     * @return {@code true} if all the rating conditions are met and the dialog is shown.
     */
    public boolean shouldRateDialog(@Nullable RatingListener ratingListener,
            @Nullable FragmentActivity fragmentActivity) {
        if (ratingListener == null || fragmentActivity == null || !shouldRate()) {
            return false;
        }

        showRateDialog(ratingListener, fragmentActivity);
        return true;
    }

    /**
     * Show the {@link DynamicRatingDialog} for the supplied parameters.
     *
     * @param ratingListener The rating listener for the dialog.
     * @param fragmentActivity The fragment activity to show the dialog.
     */
    public void showRateDialog(@Nullable RatingListener ratingListener,
            @Nullable FragmentActivity fragmentActivity) {
        if (ratingListener == null || fragmentActivity == null) {
            return;
        }

        DynamicRatingDialog.newInstance().setDynamicRatingListener(
                ratingListener).showDialog(fragmentActivity);
    }

    /**
     * Returns the key concatenated with the base key.
     *
     * @param key The key to be concatenated.
     *
     * @return The key concatenated with the base key.
     */
    public @NonNull String getKey(@NonNull String key) {
        return getBaseKey() + key;
    }

    /**
     * Set the reminder conditions for the next launch.
     *
     * @param remind {@code true} if to set the reminder.
     */
    public void setReminder(boolean remind) {
        setRequest(remind);
        setLastReminder(System.currentTimeMillis());
        DynamicPreferences.getInstance().delete(
                Rating.PREFS, getKey(Rating.Key.LAUNCH_COUNT));

        if (!remind) {
            DynamicPreferences.getInstance().delete(
                    Rating.PREFS, getKey(Rating.Key.LAST_REMINDER));
        }
    }

    /**
     * Returns the first launch date in milliseconds.
     *
     * @return The first launch date in milliseconds.
     */
    private long getFirstLaunch() {
        return DynamicPreferences.getInstance().load(Rating.PREFS,
                getKey(Rating.Key.FIRST_LAUNCH), Rating.Value.FIRST_LAUNCH);
    }

    /**
     * Set the first launch date in milliseconds.
     *
     * @param millis The date to be set.
     */
    private void setFirstLaunch(long millis) {
        DynamicPreferences.getInstance().save(Rating.PREFS,
                getKey(Rating.Key.FIRST_LAUNCH), millis);
    }

    /**
     * Returns the last launch date in milliseconds.
     *
     * @return The last launch date in milliseconds.
     */
    private long getLastLaunch() {
        return DynamicPreferences.getInstance().load(Rating.PREFS,
                getKey(Rating.Key.LAST_LAUNCH), Rating.Value.LAST_LAUNCH);
    }

    /**
     * Set the last launch date in milliseconds.
     *
     * @param millis The date to be set.
     */
    private void setLastLaunch(long millis) {
        DynamicPreferences.getInstance().save(Rating.PREFS,
                getKey(Rating.Key.LAST_LAUNCH), millis);
    }

    /**
     * Returns the last reminder date in milliseconds.
     *
     * @return The last reminder date in milliseconds.
     */
    private long getLastReminder() {
        return DynamicPreferences.getInstance().load(Rating.PREFS,
                getKey(Rating.Key.LAST_REMINDER), Rating.Value.LAST_REMINDER);
    }

    /**
     * Set the last reminder date in milliseconds.
     *
     * @param millis The date to be set.
     */
    private void setLastReminder(long millis) {
        DynamicPreferences.getInstance().save(Rating.PREFS,
                getKey(Rating.Key.LAST_REMINDER), millis);
    }

    /**
     * Returns the total launch count.
     *
     * @return The total launch count.
     */
    private int getLaunchCount() {
        return DynamicPreferences.getInstance().load(Rating.PREFS,
                getKey(Rating.Key.LAUNCH_COUNT), Rating.Value.LAUNCH_COUNT);
    }

    /**
     * Set the total launch count.
     *
     * @param count The count to be set.
     */
    private void setLaunchCount(int count) {
        DynamicPreferences.getInstance().save(Rating.PREFS,
                getKey(Rating.Key.LAUNCH_COUNT), count);
    }

    /**
     * Checks whether to request for the rating.
     *
     * @return {@code true} if request for the rating.
     */
    private boolean isRequest() {
        return DynamicPreferences.getInstance().load(Rating.PREFS,
                getKey(Rating.Key.IS_REQUEST), Rating.Value.IS_REQUEST);
    }

    /**
     * Set whether to request for the rating.
     *
     * @param request {@code true} to request for the rating.
     */
    private void setRequest(boolean request) {
        DynamicPreferences.getInstance().save(Rating.PREFS,
                getKey(Rating.Key.IS_REQUEST), request);
    }
}
