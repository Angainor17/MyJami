/*
 *  Copyright (C) 2004-2013 Savoir-Faire Linux Inc.
 *
 *  Author: Alexandre Lision <alexandre.lision@savoirfairelinux>
 *          Alexandre Savard <alexandre.savard@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  If you modify this program, or any covered work, by linking or
 *  combining it with the OpenSSL project's OpenSSL library (or a
 *  modified version of that library), containing parts covered by the
 *  terms of the OpenSSL or SSLeay licenses, Savoir-Faire Linux Inc.
 *  grants you additional permission to convey the resulting work.
 *  Corresponding Source for a non-source form of such a combination
 *  shall include the source code for the parts of OpenSSL used as well
 *  as that of the covered work.
 */
package org.sflphone.model;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Random;

import org.sflphone.service.StringMap;

import android.content.ContentResolver;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class SipCall implements Parcelable {

    private static final String TAG = SipCall.class.getSimpleName();

    private String mCallID = "";
    private Account mAccount = null;
    private CallContact contact = null;
    private boolean isRecording = false;
    private long timestamp_start = 0;

    public static final String USER_ID = "user_id";

    private int mCallType = state.CALL_TYPE_UNDETERMINED;
    private int mCallState = state.CALL_STATE_NONE;
    private int mMediaState = state.MEDIA_STATE_NONE;

    /************************
     * Construtors
     ***********************/

    private SipCall(Parcel in) {
        ArrayList<String> list = in.createStringArrayList();

        mCallID = list.get(0);
        mAccount = in.readParcelable(Account.class.getClassLoader());
        contact = in.readParcelable(CallContact.class.getClassLoader());
        isRecording = in.readByte() == 1;
        mCallType = in.readInt();
        mCallState = in.readInt();
        mMediaState = in.readInt();
        timestamp_start = in.readLong();
    }

    public SipCall(String id, Account account, int call_type, int call_state, int media_state, CallContact c) {
        mCallID = id;
        mAccount = account;
        mCallType = call_type;
        mCallState = call_state;
        mMediaState = media_state;
        contact = c;
    }

    public interface state {
        public static final int CALL_TYPE_UNDETERMINED = 0;
        public static final int CALL_TYPE_INCOMING = 1;
        public static final int CALL_TYPE_OUTGOING = 2;

        public static final int CALL_STATE_NONE = 0;
        public static final int CALL_STATE_INCOMING = 1;
        public static final int CALL_STATE_RINGING = 2;
        public static final int CALL_STATE_CURRENT = 3;
        public static final int CALL_STATE_HUNGUP = 4;
        public static final int CALL_STATE_BUSY = 5;
        public static final int CALL_STATE_FAILURE = 6;
        public static final int CALL_STATE_HOLD = 7;
        public static final int CALL_STATE_UNHOLD = 8;

        public static final int MEDIA_STATE_NONE = 0; // No media currently
        public static final int MEDIA_STATE_ACTIVE = 1; // Media is active
        public static final int MEDIA_STATE_LOCAL_HOLD = 2; // Media is put on hold bu user
        public static final int MEDIA_STATE_REMOTE_HOLD = 3; // Media is put on hold by peer
        public static final int MEDIA_STATE_ERROR = 5; // Media is in error state
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        ArrayList<String> list = new ArrayList<String>();

        list.add(mCallID);

        out.writeStringList(list);
        out.writeParcelable(mAccount, 0);

        out.writeParcelable(contact, 0);
        out.writeByte((byte) (isRecording ? 1 : 0));
        out.writeInt(mCallType);
        out.writeInt(mCallState);
        out.writeInt(mMediaState);
        out.writeLong(timestamp_start);
    }

    public static final Parcelable.Creator<SipCall> CREATOR = new Parcelable.Creator<SipCall>() {
        public SipCall createFromParcel(Parcel in) {
            return new SipCall(in);
        }

        public SipCall[] newArray(int size) {
            return new SipCall[size];
        }
    };

    public void setCallID(String callID) {
        mCallID = callID;
    }

    public String getCallId() {
        return mCallID;
    }

    public long getTimestamp_start() {
        return timestamp_start;
    }

    public void setTimestamp_start(long timestamp_start) {
        this.timestamp_start = timestamp_start;
    }

    public void setAccount(Account account) {
        mAccount = account;
    }

    public Account getAccount() {
        return mAccount;
    }

    public void setCallType(int callType) {
        mCallType = callType;
    }

    public int getCallType() {
        return mCallType;
    }

    public String getCallTypeString() {
        switch (mCallType) {
        case state.CALL_TYPE_INCOMING:
            return "CALL_TYPE_INCOMING";
        case state.CALL_TYPE_OUTGOING:
            return "CALL_TYPE_OUTGOING";
        default:
            return "CALL_TYPE_UNDETERMINED";
        }
    }

    public void setCallState(int callState) {
        mCallState = callState;
    }

    public int getCallStateInt() {
        return mCallState;
    }

    public String getmCallID() {
        return mCallID;
    }

    public void setmCallID(String mCallID) {
        this.mCallID = mCallID;
    }

    public CallContact getContact() {
        return contact;
    }

    public void setContact(CallContact contacts) {
        contact = contacts;
    }

    public int getmCallType() {
        return mCallType;
    }

    public void setmCallType(int mCallType) {
        this.mCallType = mCallType;
    }

    public int getmMediaState() {
        return mMediaState;
    }

    public void setmMediaState(int mMediaState) {
        this.mMediaState = mMediaState;
    }

    public String getCallStateString() {

        String text_state;

        switch (mCallState) {
        case state.CALL_STATE_INCOMING:
            text_state = "INCOMING";
            break;
        case state.CALL_STATE_RINGING:
            text_state = "RINGING";
            break;
        case state.CALL_STATE_CURRENT:
            text_state = "CURRENT";
            break;
        case state.CALL_STATE_HUNGUP:
            text_state = "HUNGUP";
            break;
        case state.CALL_STATE_BUSY:
            text_state = "BUSY";
            break;
        case state.CALL_STATE_FAILURE:
            text_state = "FAILURE";
            break;
        case state.CALL_STATE_HOLD:
            text_state = "HOLD";
            break;
        case state.CALL_STATE_UNHOLD:
            text_state = "UNHOLD";
            break;
        default:
            text_state = "NULL";
        }

        return text_state;
    }

    public void setMediaState(int mediaState) {
        mMediaState = mediaState;
    }

    public int getMediaState() {
        return mMediaState;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean isRecording) {
        this.isRecording = isRecording;
    }

    public static class SipCallBuilder {

        private String bCallID = "";
        private Account bAccount = null;
        private CallContact bContact = null;

        private int bCallType = state.CALL_TYPE_UNDETERMINED;
        private int bCallState = state.CALL_STATE_NONE;
        private int bMediaState = state.MEDIA_STATE_NONE;

        public SipCallBuilder setCallType(int bCallType) {
            this.bCallType = bCallType;
            return this;
        }

        public SipCallBuilder setMediaState(int state) {
            this.bMediaState = state;
            return this;
        }

        public SipCallBuilder setCallState(int state) {
            this.bCallState = state;
            return this;
        }

        private static final String TAG = SipCallBuilder.class.getSimpleName();

        public SipCallBuilder startCallCreation(String id) {
            bCallID = id;
            bCallType = SipCall.state.CALL_TYPE_INCOMING;
            return this;
        }

        public SipCallBuilder startCallCreation() {
            Random random = new Random();
            bCallID = Integer.toString(Math.abs(random.nextInt()));
            return this;
        }

        public SipCallBuilder setAccount(Account a) {
            bAccount = a;
            return this;
        }

        public SipCallBuilder setContact(CallContact c) {
            bContact = c;
            return this;
        }

        public SipCall build() throws InvalidObjectException {
            if (bCallID.contentEquals("") || bAccount == null || bContact == null) {
                throw new InvalidObjectException("SipCallBuilder's parameters missing");
            }
            return new SipCall(bCallID, bAccount, bCallType, bCallState, bMediaState, bContact);
        }

        public static SipCallBuilder getInstance() {
            return new SipCallBuilder();
        }

        public static SipCall buildMyselfCall(ContentResolver cr, String displayName) {
            return new SipCall("default", null, SipCall.state.CALL_TYPE_UNDETERMINED, state.CALL_STATE_NONE, state.MEDIA_STATE_NONE,
                    CallContact.ContactBuilder.buildUserContact(cr, displayName));

        }

    }

    public void printCallInfo() {
        Log.i(TAG, "CallInfo: CallID: " + mCallID);
        Log.i(TAG, "          AccountID: " + mAccount.getAccountID());
        Log.i(TAG, "          CallState: " + mCallState);
        Log.i(TAG, "          CallType: " + mCallType);
    }

    /**
     * Compare sip calls based on call ID
     */
    @Override
    public boolean equals(Object c) {
        if (c instanceof SipCall && ((SipCall) c).mCallID.contentEquals((mCallID))) {
            return true;
        }
        return false;

    }

    public boolean isOutGoing() {
        if (mCallType == state.CALL_TYPE_OUTGOING)
            return true;

        return false;
    }

    public boolean isRinging() {
        if (mCallState == state.CALL_STATE_RINGING || mCallState == state.CALL_STATE_NONE)
            return true;

        return false;
    }

    public boolean isIncoming() {
        if (mCallType == state.CALL_TYPE_INCOMING)
            return true;

        return false;
    }

    public void setCallState(String newState) {
        if (newState.equals("INCOMING")) {
            setCallState(SipCall.state.CALL_STATE_INCOMING);
        } else if (newState.equals("RINGING")) {
            setCallState(SipCall.state.CALL_STATE_RINGING);
        } else if (newState.equals("CURRENT")) {
            setCallState(SipCall.state.CALL_STATE_CURRENT);
        } else if (newState.equals("HUNGUP")) {
            setCallState(SipCall.state.CALL_STATE_HUNGUP);
        } else if (newState.equals("BUSY")) {
            setCallState(SipCall.state.CALL_STATE_BUSY);
        } else if (newState.equals("FAILURE")) {
            setCallState(SipCall.state.CALL_STATE_FAILURE);
        } else if (newState.equals("HOLD")) {
            setCallState(SipCall.state.CALL_STATE_HOLD);
        } else if (newState.equals("UNHOLD")) {
            setCallState(SipCall.state.CALL_STATE_CURRENT);
        } else {
            setCallState(SipCall.state.CALL_STATE_NONE);
        }

    }

    public boolean isOngoing() {
        if (mCallState == state.CALL_STATE_RINGING || mCallState == state.CALL_STATE_NONE || mCallState == state.CALL_STATE_FAILURE
                || mCallState == state.CALL_STATE_BUSY || mCallState == state.CALL_STATE_HUNGUP)
            return false;

        return true;
    }

    public boolean isOnHold() {
        return mCallState == state.CALL_STATE_HOLD;
    }

    public boolean isCurrent() {
        return mCallState == state.CALL_STATE_CURRENT;
    }

}