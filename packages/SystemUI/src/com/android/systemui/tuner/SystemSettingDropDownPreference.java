/*
 * Copyright (C) 2013 The CyanogenMod project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.tuner;

import android.content.Context;
import android.provider.Settings;
import android.support.v7.preference.DropDownPreference;
import android.support.v7.preference.PreferenceDataStore;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;

public class SystemSettingDropDownPreference extends DropDownPreference {
    public SystemSettingDropDownPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SystemSettingDropDownPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean persistString(String value) {
        if (shouldPersist()) {
            if (value == getPersistedString(null)) {
                // It's already there, so the same as persisting
                return true;
            }
            Settings.System.putString(getContext().getContentResolver(), getKey(), value);
            return true;
        }
        return false;
    }

    @Override
    protected String getPersistedString(String defaultReturnValue) {
        if (!shouldPersist()) {
            return defaultReturnValue;
        }
        String value = Settings.System.getString(getContext().getContentResolver(), getKey());
        return value == null ? defaultReturnValue : value;
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        final String value;
        if (!isPersisted()) {
            if (defaultValue == null) {
                return;
            }
            value = (String) defaultValue;
            if (shouldPersist()) {
                persistString(value);
            }
        } else {
            value = Settings.System.getString(getContext().getContentResolver(), getKey());
        }
        setValue(value);
    }

    protected boolean isPersisted() {
        return Settings.System.getString(getContext().getContentResolver(), getKey()) != null;
    }
}

