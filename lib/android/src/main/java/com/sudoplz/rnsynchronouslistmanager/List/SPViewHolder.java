package com.sudoplz.rnsynchronouslistmanager.List;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.react.bridge.ReadableMap;
import com.sudoplz.rnsynchronouslistmanager.Sync.SyncRootView;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.ReactContext;

/**
 * Created by SudoPlz on 06/02/2018.
 */

public class SPViewHolder extends RecyclerView.ViewHolder {
    private SPView curView;
    public SPViewHolder(SPView itemView) {
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

}
