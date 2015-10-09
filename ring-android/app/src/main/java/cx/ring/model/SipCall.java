/*
 *  Copyright (C) 2004-2014 Savoir-Faire Linux Inc.
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
package cx.ring.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class SipCall implements Parcelable {

    public static String ID = "id";
    public static String ACCOUNT = "account";
    public static String CONTACT = "contact";
    public static String TYPE = "type";
    public static String STATE = "State";
    public static String NUMBER = "number";

    private static final String TAG = SipCall.class.getSimpleName();

    private String mCallID = "";
    private String mAccount = "";
    private CallContact mContact = null;
    private String mNumber = "";
    private boolean isRecording = false;
    private long timestampStart_ = 0;
    private long timestampEnd_ = 0;

    private int mCallType;
    private int mCallState = State.NONE;

    public SipCall(String id, String account, String number, int direction) {
        mCallID = id;
        mAccount = account;
        mNumber = number;
        mCallType = direction;
    }

    public SipCall(SipCall call) {
        mCallID = call.mCallID;
        mAccount = call.mAccount;
        mContact = call.mContact;
        mNumber = call.mNumber;
        isRecording = call.isRecording;
        timestampStart_ = call.timestampStart_;
        timestampEnd_ = call.timestampEnd_;
        mCallType = call.mCallType;
        mCallState = call.mCallState;
    }

    /**
     * *********************
     * Construtors
     * *********************
     */

    protected SipCall(Parcel in) {
        mCallID = in.readString();
        mAccount = in.readString();
        mContact = in.readParcelable(CallContact.class.getClassLoader());
        mNumber = in.readString();
        isRecording = in.readByte() == 1;
        mCallType = in.readInt();
        mCallState = in.readInt();
        timestampStart_ = in.readLong();
        timestampEnd_ = in.readLong();
    }

    public SipCall(Bundle args) {
        mCallID = args.getString(ID);
        mAccount = args.getString(ACCOUNT);
        mCallType = args.getInt(TYPE);
        mCallState = args.getInt(STATE);
        mContact = args.getParcelable(CONTACT);
        mNumber = args.getString(NUMBER);
    }

    public String getRecordPath() {
        return "";
    }

    public int getCallType() {
        return mCallType;
    }

    public Bundle getBundle() {
        Bundle args = new Bundle();
        args.putString(SipCall.ID, mCallID);
        args.putString(SipCall.ACCOUNT, mAccount);
        args.putInt(SipCall.STATE, mCallState);
        args.putInt(SipCall.TYPE, mCallType);
        args.putParcelable(SipCall.CONTACT, mContact);
        args.putString(SipCall.NUMBER, mNumber);
        return args;
    }

    public interface Direction {
        int INCOMING = 1;
        int OUTGOING = 2;
    }

    public interface State {
        int NONE = 0;
        int CONNECTING = 1;
        int RINGING = 2;
        int CURRENT = 3;
        int HUNGUP = 4;
        int BUSY = 5;
        int FAILURE = 6;
        int HOLD = 7;
        int UNHOLD = 8;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mCallID);
        out.writeString(mAccount);
        out.writeParcelable(mContact, 0);
        out.writeString(mNumber);
        out.writeByte((byte) (isRecording ? 1 : 0));
        out.writeInt(mCallType);
        out.writeInt(mCallState);
        out.writeLong(timestampStart_);
        out.writeLong(timestampEnd_);
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

    public long getTimestampStart() {
        return timestampStart_;
    }

    public void setTimestampStart(long timestampStart) {
        this.timestampStart_ = timestampStart;
    }

    public long getTimestampEnd() {
        return timestampEnd_;
    }

    public void setTimestampEnd(long timestampEnd) {
        this.timestampEnd_ = timestampEnd;
    }

    public void setAccount(String account) {
        mAccount = account;
    }

    public String getAccount() {
        return mAccount;
    }

    public String getCallTypeString() {
        switch (mCallType) {
            case Direction.INCOMING:
                return "INCOMING";
            case Direction.OUTGOING:
                return "OUTGOING";
            default:
                return "CALL_TYPE_UNDETERMINED";
        }
    }

    public void setCallState(int callState) {
        mCallState = callState;
    }

    public void setContact(CallContact c) {
        mContact = c;
    }

    public CallContact getContact() {
        return mContact;
    }

    public void setNumber(String n) {
        mNumber = n;
    }

    public String getNumber() {
        return mNumber;
    }

    public String getCallStateString() {

        String text_state;

        switch (mCallState) {
            case State.NONE:
                text_state = "NONE";
                break;
            case State.RINGING:
                text_state = "RINGING";
                break;
            case State.CURRENT:
                text_state = "CURRENT";
                break;
            case State.HUNGUP:
                text_state = "HUNGUP";
                break;
            case State.BUSY:
                text_state = "BUSY";
                break;
            case State.FAILURE:
                text_state = "FAILURE";
                break;
            case State.HOLD:
                text_state = "HOLD";
                break;
            case State.UNHOLD:
                text_state = "UNHOLD";
                break;
            default:
                text_state = "NULL";
        }

        return text_state;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean isRecording) {
        this.isRecording = isRecording;
    }

    public void printCallInfo() {
        Log.i(TAG, "CallInfo: CallID: " + mCallID);
        Log.i(TAG, "          AccountID: " + mAccount);
        Log.i(TAG, "          CallState: " + mCallState);
        Log.i(TAG, "          CallType: " + mCallType);
    }

    /**
     * Compare sip calls based on call ID
     */
    @Override
    public boolean equals(Object c) {
        return c instanceof SipCall && ((SipCall) c).mCallID.contentEquals((mCallID));
    }

    public boolean isOutGoing() {
        return mCallType == Direction.OUTGOING;
    }

    public boolean isRinging() {
        return mCallState == State.RINGING || mCallState == State.NONE;
    }

    public boolean isIncoming() {
        return mCallType == Direction.INCOMING;
    }

    public boolean isOngoing() {
        return !(mCallState == State.RINGING || mCallState == State.NONE || mCallState == State.FAILURE
                || mCallState == State.BUSY || mCallState == State.HUNGUP);

    }

    public boolean isOnHold() {
        return mCallState == State.HOLD;
    }

    public boolean isCurrent() {
        return mCallState == State.CURRENT;
    }


}