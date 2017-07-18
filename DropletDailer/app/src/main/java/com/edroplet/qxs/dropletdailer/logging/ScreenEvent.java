/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.edroplet.qxs.dropletdailer.logging;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores constants identifying individual screens/dialogs/fragments in the application, and also
 * provides a mapping of integer id -> screen name mappings for analytics purposes.
 */
public final class ScreenEvent {

    private static final Map<Integer, String> sScreenNameMap = new HashMap<>();

    public static final String FRAGMENT_TAG_SEPARATOR = "#";

    public static final int UNKNOWN = 0;

    // The dialpad in the main Dialer activity
    public static final int DIALPAD = 1;

    // The speed dial tab in the main Dialer activity
    public static final int SPEED_DIAL = 2;

    // The recents tab in the main Dialer activity
    public static final int CALL_LOG = 3;

    // The voicemail tab in the main Dialer activity
    public static final int VOICEMAIL_LOG = 4;

    // The all contacts tab in the main Dialer activity
    public static final int ALL_CONTACTS = 5;

    // List of search results returned by typing into the search box.
    public static final int REGULAR_SEARCH = 6;

    // List of search results returned by typing into the dialpad.
    public static final int SMART_DIAL_SEARCH = 7;

    // The All and Missed call log tabs in CallLogActivity
    public static final int CALL_LOG_FILTER = 8;

    // Dialer settings screen.
    public static final int SETTINGS = 9;

    // The "Import/export contacts" dialog launched via the overflow menu.
    public static final int IMPORT_EXPORT_CONTACTS = 10;

    // The "Clear frequents" dialog launched via the overflow menu.
    public static final int CLEAR_FREQUENTS = 11;

    // The "Send feedback" dialog launched via the overflow menu.
    public static final int SEND_FEEDBACK = 12;

    // The main in call screen that displays caller details and contact photos
    public static final int INCALL = 13;

    // The screen that displays the glowpad widget (slide right to answer,
    // slide left to dismiss).
    public static final int INCOMING_CALL = 14;

    // Conference management fragment displayed for conferences that support
    // management of individual calls within the conference.
    public static final int CONFERENCE_MANAGEMENT = 15;

    // The dialpad displayed in-call that is used to send dtmf tones.
    public static final int INCALL_DIALPAD = 16;

    // Menu options displayed when long pressing on a call log entry.
    public static final int CALL_LOG_CONTEXT_MENU = 17;

    // Screen displayed to allow the user to see an overview of all blocked
    // numbers.
    public static final int BLOCKED_NUMBER_MANAGEMENT = 18;

    // Screen displayed to allow the user to add a new blocked number.
    public static final int BLOCKED_NUMBER_ADD_NUMBER = 19;

    // Should match ContactsExtension.ScreenEvent.ScreenType values in
    // http://cs/google3/logs/proto/wireless/android/contacts/contacts_extensions.proto
    public static final class ScreenType {
        public static final int UNKNOWN = 0;
        public static final int SEARCH = 1;
        public static final int SEARCH_EXIT = 2;
        public static final int FAVORITES = 3;
        public static final int ALL_CONTACTS = 4;
        public static final int QUICK_CONTACT = 5;
        public static final int EDITOR = 6;

        private ScreenType() {
        }

        public static String getFriendlyName(int screenType) {
            switch (screenType) {
                case SEARCH: // fall-through
                case SEARCH_EXIT: return "Search";
                case FAVORITES: return "Favorites";
                case ALL_CONTACTS: return "AllContacts";
                case QUICK_CONTACT: return "QuickContact";
                case EDITOR: return "Editor";
                case UNKNOWN: // fall-through
                default: return null;
            }
        }
    }

    private ScreenEvent() {
    }
}
