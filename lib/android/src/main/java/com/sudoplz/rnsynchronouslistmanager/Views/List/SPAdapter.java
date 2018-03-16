package com.sudoplz.rnsynchronouslistmanager.Views.List;

//import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
//import android.widget.TextView;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.sudoplz.rnsynchronouslistmanager.Views.SyncRootView;
import com.sudoplz.rnsynchronouslistmanager.Utils.SPGlobals;

import java.util.ArrayList;

/**
 * Created by SudoPlz on 15/02/2018.
 */

public class SPAdapter extends RecyclerView.Adapter <SPAdapter.SPViewHolder> {

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
//        TextView text = new TextView(parent.getContext());
//        text.setText("YOO");
//        SPViewHolder holder = new SPViewHolder(text);

        SyncRootView child = new SyncRootView(SPGlobals.getInstance().getMainViewTemplateName(), null, (ReactContext) parent.getContext());
        SPViewHolder holder = new SPViewHolder(child);
        return holder;

    }


    @Override
    public void onBindViewHolder(SPViewHolder holder, int position) {
        ReadableMap dataForChild = (ReadableMap) data.get(position);
        holder.setItemData(dataForChild, position);
    }

    public void setAdapterData(ArrayList initialData) {
        if (initialData != null && initialData.size() > 0) {
            this.data = initialData;
            notifyDataSetChanged();
        }
    }

    public void prependData(ArrayList dataToPrepend) {
        if (dataToPrepend != null && dataToPrepend.size() > 0) {
            if (this.data != null && this.data.size() > 0) {
                for (int i = 0; i < dataToPrepend.size(); i++) {
                    ReadableMap dataForChild = (ReadableMap) dataToPrepend.get(i);
                    this.data.add(0, dataForChild);
                    notifyItemRangeInserted(0, dataToPrepend.size());
                }
            } else {
                this.data = dataToPrepend;
                notifyDataSetChanged();
            }
        }
    }

    public void appendData(ArrayList dataToPrepend) {
        if (dataToPrepend != null && dataToPrepend.size() > 0) {
            if (this.data != null && this.data.size() > 0) {
                int initialDataEndPosition = this.data.size();
                for (int i = 0; i < dataToPrepend.size(); i++) {
                    ReadableMap dataForChild = (ReadableMap) dataToPrepend.get(i);
                    this.data.add(dataForChild);
                }
                notifyItemRangeInserted(initialDataEndPosition - 1, dataToPrepend.size());
            } else {
                this.data = dataToPrepend;
                notifyDataSetChanged();
            }
        }
    }

    public void updateDataAtIndex(int indexToUpdate, ReadableMap updatedChild) {
        if (updatedChild != null) {
            if (this.data != null && this.data.size() > indexToUpdate) {
                this.data.set(indexToUpdate, updatedChild);
                notifyItemChanged(indexToUpdate);
            }
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



    /////////////////////////////////////////////////////////////
    //////////////////////// VIEW HOLDER ////////////////////////
    /////////////////////////////////////////////////////////////

    class SPViewHolder extends RecyclerView.ViewHolder {

        private SyncRootView curView;
        public SPViewHolder(SyncRootView itemView) {
            super(itemView);
            curView = itemView;
        }


        public void setItemData(ReadableMap newProps, int newPosition) {
            if (curView != null) { // if we have a view
                if (curView.hasInitialised()) {
                    // if that view has been initialised in the past
                    if (curView.hasLastPosition()) {
                        // if it has a last position set
//                    if (curView.getLastPosition() != newPosition) {
//                        // and if that last position is different from the new position
//                        System.out.println("\n@@@@@@@@@@@@@@@@@ we want the view in: "+curView.getLastPosition()+" to become: "+newPosition);
//                        curView.terminate();
//                        curView.setLastPosition(newPosition);
//                        curView.drawOnScreen(newProps);
//                    } else { // if it's the same position we assume we have new props, and update'em
//                        System.out.println("\n@@@@@@@@@@@@@@@@@ we want the view in: "+curView.getLastPosition()+" to receive new props");
//                        curView.updateProps(newProps);
//                    }
//                    System.out.println("\n@@@@@@@@@@@@@@@@@ we want the view in: "+curView.getLastPosition()+" to become: "+newPosition);
                        curView.setLastPosition(newPosition);
                        curView.updateProps(newProps);
                    } else {
//                    System.out.println("\n@@@@@@@@@@@@@@@@@ we want a view to become: "+newPosition);
                        // but if we don't have a last position
                        curView.setLastPosition(newPosition);
                        curView.drawOnScreen(newProps);
                    }
                } else {
//                System.out.println("\n@@@@@@@@@@@@@@@@@ we want an uninitialised view to become: "+newPosition);
                    // if the view has not been initialised
                    curView.setLastPosition(newPosition);
                    curView.drawOnScreen(newProps);
                }
            }
        }

        public Boolean viewHasInitialised() {
            return curView.hasInitialised();
        }
    }

}
