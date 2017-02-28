/*
 *  Copyright (C) 2017 Savoir-faire Linux Inc.
 *
 *  Author: Aline Bonnet <aline.bonnet@savoirfairelinux.com>
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
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package cx.ring.contactrequests;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cx.ring.R;
import cx.ring.model.TrustRequest;

public class ContactRequestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TrustRequest> mTrustRequests;
    private Context mContext;
    private PendingContactRequestsPresenter mPresenter;

    public ContactRequestsAdapter(Context context, List<TrustRequest> trustRequests, PendingContactRequestsPresenter presenter) {
        mContext = context;
        mTrustRequests = trustRequests;
        mPresenter = presenter;
    }

    public void replaceAll(List<TrustRequest> trustRequests) {
        mTrustRequests = trustRequests;
        notifyDataSetChanged();
    }

    class ContactRequestView extends RecyclerView.ViewHolder {
        @BindView(R.id.button_accept)
        AppCompatButton mButtonAccept;

        @BindView(R.id.button_refuse)
        AppCompatButton mButtonRefuse;

        @BindView(R.id.button_block)
        AppCompatButton mButtonBlock;

        @BindView(R.id.photo)
        AppCompatImageView mPhoto;

        @BindView(R.id.display_name)
        TextView mDisplayname;

        private String mContactId;

        ContactRequestView(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setContactId(String contactId) {
            mContactId = contactId;
        }

        @OnClick(R.id.button_accept)
        public void acceptClicked() {
            mPresenter.acceptTrustRequest(mContactId);
        }

        @OnClick(R.id.button_refuse)
        public void refuseClicked() {
            mPresenter.refuseTrustRequest(mContactId);
        }

        @OnClick(R.id.button_block)
        public void blockClicked() {
            mPresenter.blockTrustRequest(mContactId);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View holderView;

        holderView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact_request, parent, false);
        viewHolder = new ContactRequestView(holderView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TrustRequest trustRequest = mTrustRequests.get(position);

        //default photo
        Drawable photo = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_contact_picture, null);
        ((ContactRequestView) holder).mPhoto.setImageDrawable(photo);

        ((ContactRequestView) holder).mDisplayname.setText(trustRequest.getDisplayname());

        ((ContactRequestView) holder).setContactId(trustRequest.getContactId());
    }

    @Override
    public int getItemCount() {
        return mTrustRequests.size();
    }
}
