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

package com.pranavpandey.android.dynamic.rating.listener.base;

import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * An interface to get the various rating callbacks.
 */
public interface RatingListener extends RatingBar.OnRatingBarChangeListener {

    /**
     * This method will be called to get the rating title.
     *
     * @return The rating title to be used.
     */
    @Nullable CharSequence getRatingTitle();

    /**
     * This method will be called to get the rating message.
     *
     * @return The rating message to be used.
     */
    @Nullable CharSequence getRatingMessage();

    /**
     * This method will be called to get the text for the remind later action.
     *
     * @return The text for the remind later action to be used.
     */
    @Nullable CharSequence getActionLater();

    /**
     * This method will be called to get the text for the rate action.
     *
     * @param rating The rating value.
     *
     * @return The text for the rate action to be used.
     */
    @Nullable CharSequence getActionRate(float rating);

    /**
     * This method will be called to get the text for the skip action.
     *
     * @return The text for the skip action to be used.
     */
    @Nullable CharSequence getActionSkip();

    /**
     * This method will be called to check if the rating is unknown.
     *
     * @param rating The rating value to be checked.
     *
     * @return {@code true} if the rating is unknown.
     */
    boolean isRatingUnknown(float rating);

    /**
     * This method will be called to check if the rating is negative.
     *
     * @param rating The rating value to be checked.
     *
     * @return {@code true} if the rating is negative.
     */
    boolean isRatingNegative(float rating);

    /**
     * This method will be called when user interacts with the rating bar.
     *
     * @param ratingBar The rating bar.
     * @param rating The rating selected by the user.
     */
    void onRatingSelected(@NonNull RatingBar ratingBar, float rating);

    /**
     * This method will be called when the rating is skipped.
     *
     * @param remind {@code true} to remind again.
     */
    void onRatingSkipped(boolean remind);
}
