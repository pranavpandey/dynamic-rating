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

package com.pranavpandey.android.dynamic.rating.listener;

import android.content.Context;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.rating.DynamicRating;
import com.pranavpandey.android.dynamic.rating.R;
import com.pranavpandey.android.dynamic.rating.Rating;
import com.pranavpandey.android.dynamic.rating.listener.base.RatingListener;

/**
 * A {@link RatingListener} to provide default functionality for the various rating callbacks.
 */
public abstract class DynamicRatingListener implements RatingListener {

    /**
     * Context used by this listener.
     */
    private final Context mContext;

    /**
     * Dynamic rating instance used by this listener.
     */
    private final DynamicRating mDynamicRating;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The context to be used.
     * @param dynamicRating The dynamic rating object to be used.
     */
    public DynamicRatingListener(@NonNull Context context, @NonNull DynamicRating dynamicRating) {
        this.mContext = context;
        this.mDynamicRating = dynamicRating;
    }

    /**
     * This method will be called on getting a positive feedback from the user.
     *
     * @param rating The rating selected by the user.
     */
    public abstract void onRate(float rating);

    /**
     * This method will be called on getting a negative feedback from the user.
     *
     * @param rating The rating selected by the user.
     */
    public abstract void onFeedback(float rating);

    /**
     * Get the context used by this listener.
     *
     * @return The context used by this listener.
     */
    public @Nullable Context getContext() {
        return mContext;
    }

    /**
     * Get the dynamic rating instance used by this listener.
     *
     * @return The dynamic rating instance used by this listener.
     */
    public @Nullable DynamicRating getDynamicRating() {
        return mDynamicRating;
    }

    @Override
    public @Nullable CharSequence getRatingTitle() {
        if (getContext() == null) {
            return null;
        }

        return getContext().getString(R.string.adr_contribute);
    }

    @Override
    public @Nullable CharSequence getRatingMessage() {
        if (getContext() == null) {
            return null;
        }

        return getContext().getString(R.string.adr_rate_feedback_desc);
    }

    @Override
    public @Nullable CharSequence getActionLater() {
        if (getContext() == null) {
            return null;
        }

        return getContext().getString(R.string.adr_later);
    }

    @Override
    public @Nullable CharSequence getActionRate(float rating) {
        if (getContext() == null) {
            return null;
        }

        return getContext().getString((isRatingUnknown(rating) || !isRatingNegative(rating))
                ? R.string.adr_rate : R.string.adr_feedback);
    }

    @Override
    public @Nullable CharSequence getActionSkip() {
        if (getContext() == null) {
            return null;
        }

        return getContext().getString(R.string.adr_skip);
    }

    @Override
    public boolean isRatingUnknown(float rating) {
        return rating <= 0;
    }

    @Override
    public boolean isRatingNegative(float rating) {
        return rating < Rating.Value.POSITIVE;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) { }

    @Override
    public void onRatingSelected(@NonNull RatingBar ratingBar, float rating) {
        if (isRatingNegative(rating)) {
            onFeedback(rating);
        } else {
            onRate(rating);
        }

        if (getDynamicRating() != null) {
            getDynamicRating().setReminder(false);
        }
    }

    @Override
    public void onRatingSkipped(boolean remind) {
        if (getDynamicRating() != null) {
            getDynamicRating().setReminder(remind);
        }
    }
}
