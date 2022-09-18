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

package com.pranavpandey.android.dynamic.rating.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.pranavpandey.android.dynamic.rating.R;
import com.pranavpandey.android.dynamic.rating.Rating;
import com.pranavpandey.android.dynamic.rating.listener.base.RatingListener;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog;
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment;

/**
 * A {@link DynamicDialogFragment} to show rating dialog.
 */
public class DynamicRatingDialog extends DynamicDialogFragment implements RatingListener {

    /**
     * Tag for this dialog fragment.
     */
    public static final String TAG = "DynamicRatingDialog";

    /**
     * Rating listener used by this dialog.
     */
    private RatingListener mRatingListener;

    /**
     * Initialize the new instance of this dialog fragment.
     *
     * @return An instance of {@link DynamicRatingDialog}.
     */
    public static @NonNull DynamicRatingDialog newInstance() {
        return new DynamicRatingDialog();
    }

    @Override
    protected @NonNull DynamicDialog.Builder onCustomiseBuilder(
            @NonNull DynamicDialog.Builder dialogBuilder, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.adr_dialog_rating,
                new LinearLayout(requireContext()), false);
        RatingBar ratingBar = view.findViewById(R.id.adr_dialog_rating_bar);

        dialogBuilder.setTitle(getRatingTitle());
        Dynamic.set(view.findViewById(R.id.adr_dialog_rating_message), getRatingMessage());

        if (ratingBar != null) {
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    DynamicRatingDialog.this.onRatingChanged(ratingBar, rating, fromUser);
                }
            });
        }

        dialogBuilder.setNegativeButton(getActionLater(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onRatingSkipped(true);
                    }
                }).setPositiveButton(getActionRate(Rating.Value.UNKNOWN),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ratingBar != null) {
                            onRatingSelected(ratingBar, ratingBar.getRating());
                        }
                    }
                }).setNeutralButton(getActionSkip(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onRatingSkipped(false);
                    }
                });

        setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (ratingBar != null) {
                    onRatingChanged(ratingBar, ratingBar.getRating(), false);
                }
            }
        });

        return dialogBuilder.setView(view).setViewRoot(
                view.findViewById(R.id.adr_dialog_rating_root));
    }

    @Override
    public void showDialog(@NonNull FragmentActivity fragmentActivity) {
        showDialog(fragmentActivity, TAG);
    }

    @Override
    public @Nullable CharSequence getRatingTitle() {
        if (getRatingListener() != null) {
            return getRatingListener().getRatingTitle();
        }

        return null;
    }

    @Override
    public @Nullable CharSequence getRatingMessage() {
        if (getRatingListener() != null) {
            return getRatingListener().getRatingMessage();
        }

        return null;
    }

    @Override
    public @Nullable CharSequence getActionLater() {
        if (getRatingListener() != null) {
            return getRatingListener().getActionLater();
        }

        return null;
    }

    @Override
    public @Nullable CharSequence getActionRate(float rating) {
        if (getRatingListener() != null) {
            return getRatingListener().getActionRate(rating);
        }

        return null;
    }

    @Override
    public @Nullable CharSequence getActionSkip() {
        if (getRatingListener() != null) {
            return getRatingListener().getActionSkip();
        }

        return null;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if (getRatingListener() != null) {
            getRatingListener().onRatingChanged(ratingBar, rating, fromUser);
        }

        if (getDynamicDialog() != null) {
            getDynamicDialog().getButton(DynamicDialog.BUTTON_POSITIVE)
                    .setText(getActionRate(rating));
            getDynamicDialog().getButton(DynamicDialog.BUTTON_POSITIVE)
                    .setEnabled(!isRatingUnknown(rating));
        }
    }

    @Override
    public boolean isRatingUnknown(float rating) {
        if (getRatingListener() != null) {
            return getRatingListener().isRatingUnknown(rating);
        }

        return false;
    }

    @Override
    public boolean isRatingNegative(float rating) {
        if (getRatingListener() != null) {
            return getRatingListener().isRatingNegative(rating);
        }

        return false;
    }

    @Override
    public void onRatingSelected(@NonNull RatingBar ratingBar, float rating) {
        if (getRatingListener() != null) {
            getRatingListener().onRatingSelected(ratingBar, rating);
        }
    }

    @Override
    public void onRatingSkipped(boolean remind) {
        if (getRatingListener() != null) {
            getRatingListener().onRatingSkipped(remind);
        }
    }

    /**
     * Get the rating listener used by this dialog.
     *
     * @return The rating listener used by this dialog.
     */
    public @Nullable RatingListener getRatingListener() {
        return mRatingListener;
    }

    /**
     * Set the rating listener for this dialog.
     *
     * @param ratingListener The rating listener to be set.
     *
     * @return The {@link DynamicRatingDialog} object to allow for chaining of calls to
     *         set methods.
     */
    public @NonNull DynamicRatingDialog setDynamicRatingListener(
            @Nullable RatingListener ratingListener) {
        this.mRatingListener = ratingListener;

        return this;
    }
}
