package com.sudoplz.rnsynchronouslistmanager.Views.List;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.facebook.react.bridge.ReadableMap;
import com.sudoplz.rnsynchronouslistmanager.Views.SyncRootView;
import com.sudoplz.rnsynchronouslistmanager.Utils.SPGlobals;

import java.util.ArrayList;

/**
 * Created by SudoPlz on 15/02/2018.
 */

public class SPAdapter extends RecyclerView.Adapter <SPViewHolder> {

    ArrayList<Object> data = new ArrayList<Object>();

    public SPAdapter() {
        super();
    }

    public SPAdapter(ArrayList initialData) {
        super();
        if (initialData != null && initialData.size() > 0) {
            data = initialData;
        }
    }

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

        SyncRootView child = new SyncRootView(SPGlobals.getInstance().getMainViewTemplateName());
        SPViewHolder holder = new SPViewHolder(child);
//        child.runApplication();
        return holder;

    }

    public void setAdapterData(ArrayList initialData) {
        if (initialData != null && initialData.size() > 0) {
            this.data = initialData;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(SPViewHolder holder, int position) {
        ReadableMap dataForChild = (ReadableMap) data.get(position);

        if (holder.viewHasInitialised()) {
            System.out.println("@@@@@@@@@@@@@@@@@ updating child: "+position);
            holder.updateItemProps(dataForChild);
        } else {
            System.out.println("@@@@@@@@@@@@@@@@@ setting child: "+position);
            holder.setItemProps(dataForChild);
        }
    }

    public void clearData() {
        if (this.data != null) {
            int dataCnt = this.data.size();
            if (dataCnt > 0) {
                this.data.clear();
                notifyItemRangeRemoved(0, dataCnt);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (this.data != null) {
            return this.data.size();
        }
        return 0;
    }
}
