package com.sudoplz.rnsynchronouslistmanager.Views.List;


import android.support.v7.widget.RecyclerView;

import com.facebook.react.bridge.ReadableMap;
import com.sudoplz.rnsynchronouslistmanager.Views.SyncRootView;

/**
 * Created by SudoPlz on 06/02/2018.
 */

public class SPViewHolder extends RecyclerView.ViewHolder {

    private SyncRootView curView;
    public SPViewHolder(SyncRootView itemView) {
        super(itemView);
        curView = itemView;
    }


    public void updateItemProps(ReadableMap newProps) {
        if (curView != null) {
//            String name = newProps.getString("name");
//            ((TextView) curView).setText(name);
            curView.updateProps(newProps);
        }
    }

    public void setItemProps(ReadableMap newProps) {
        curView.setInitialProps(newProps);
    }

    public Boolean viewHasInitialised() {
        return curView.hasInitialised();
    }
}
