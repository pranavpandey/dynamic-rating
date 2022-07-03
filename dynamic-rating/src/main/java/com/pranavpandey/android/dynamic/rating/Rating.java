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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

/**
 * An interface to hold various rating constants.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Rating {

    /**
     * File name for the dynamic rating preferences.
     */
    String PREFS = "dynamic_rating";

    /**
     * An interface to hold key constants.
     */
    @Retention(RetentionPolicy.SOURCE)
    @interface Key {

        /**
         * Constant for the key prefix.
         */
        String BASE = "adr_key_";

        /**
         * Key constant for the first launch date.
         */
        String FIRST_LAUNCH = "first_launch";

        /**
         * Key constant for the last launch date.
         */
        String LAST_LAUNCH = "last_launch";

        /**
         * Key constant for the last reminder date.
         */
        String LAST_REMINDER = "last_reminder";

        /**
         * Key constant for the launch count.
         */
        String LAUNCH_COUNT = "launch_count";

        /**
         * Key constant for the rating request.
         */
        String IS_REQUEST = "is_request";
    }

    /**
     * An interface to hold value constants.
     */
    @Retention(RetentionPolicy.SOURCE)
    @interface Value {

        /**
         * Constant value for the unknown rating.
         */
        float UNKNOWN = -1f;

        /**
         * Minimum value for the positive rating.
         */
        float POSITIVE = 4f;

        /**
         * Default value for the first launch date.
         */
        long FIRST_LAUNCH = 0L;

        /**
         * Default value for the last launch date.
         */
        long LAST_LAUNCH = 0L;

        /**
         * Default value for the last reminder date.
         */
        long LAST_REMINDER = new Date().getTime();

        /**
         * Default value for the launch count.
         */
        int LAUNCH_COUNT = 0;

        /**
         * Default value for the rating request.
         */
        boolean IS_REQUEST = true;
    }

    /**
     * An interface to hold default values.
     */
    @Retention(RetentionPolicy.SOURCE)
    @interface Default {

        /**
         * Default value for the rate interval.
         */
        int RATE_INTERVAL = 2;

        /**
         * Default value for the launch count.
         */
        int RATE_COUNT = 5;

        /**
         * Default value for the remind interval.
         */
        int REMIND_INTERVAL = 2;
    }
}
