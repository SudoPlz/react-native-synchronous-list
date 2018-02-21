package com.sudoplz.rnsynchronouslistmanager.List;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.sudoplz.rnsynchronouslistmanager.Sync.SyncRootView;
import com.sudoplz.rnsynchronouslistmanager.Utils.SPGlobals;
import com.sudoplz.rnsynchronouslistmanager.Utils.WritableAdvancedMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by SudoPlz on 15/02/2018.
 */

public class SPAdapter extends RecyclerView.Adapter <SPViewHolder> {

    ArrayList<Object> data = new ArrayList<Object>();

//    public SPAdapter() {
//        super();
//    }

    @Override
    public SPViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        SPGlobals g = SPGlobals.getInstance();
//        SyncRootView rowView = new SyncRootView(
//                g.getMainViewTemplateName(),
//                g.getRcContext(),
//                g.getRcHost()
//        );
//        ;
//        SPViewHolder holder = new SPViewHolder(new SPView());
//        TextView text = new TextView(parent.getContext());
//        return new SPViewHolder(text);
        SPView child = new SPView();
        SPViewHolder holder = new SPViewHolder(child);
//        int position = holder.getAdapterPosition();
//        if (position != RecyclerView.NO_POSITION) {
//            ReadableMap dataForChild = (ReadableMap) data.get(position);
//            child.setInitialProps(dataForChild);
//            child.runApplication();
//        }
        ReadableMap dataForChild = (ReadableMap) data.get(0);
        child.setInitialProps(dataForChild);
//        child.runApplication();
        return holder;

    }

    public void setAdapterData(ArrayList initialData) {
        this.data = initialData;
    }

    @Override
    public void onBindViewHolder(SPViewHolder holder, int position) {
        ReadableMap dataForChild = (ReadableMap) data.get(position);
        holder.updateItemProps(dataForChild);
    }

    @Override
    public int getItemCount() {
        if (this.data != null) {
            return this.data.size();
        }
        return 0;
    }
}
