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


    public void setItemData(ReadableMap newProps, int newPosition) {
        if (curView != null) { // if we have a view
            if (curView.hasInitialised()) {
                // if that view has been initialised in the past
                if (curView.hasLastPosition()) {
                    // if it has a last position set
                    if (curView.getLastPosition() != newPosition) {
                        // and if that last position is different from the new position
                        System.out.println("\n@@@@@@@@@@@@@@@@@ we want the view in: "+curView.getLastPosition()+" to become: "+newPosition);
                        curView.terminate();
                        curView.setLastPosition(newPosition);
                        curView.drawOnScreen(newProps);
                    } else { // if it's the same position we assume we have new props, and update'em
                        System.out.println("\n@@@@@@@@@@@@@@@@@ we want the view in: "+curView.getLastPosition()+" to receive new props");
                        curView.updateProps(newProps);
                    }
                } else {
                    System.out.println("\n@@@@@@@@@@@@@@@@@ we want a view to become: "+newPosition);
                    // but if we don't have a last position
                    curView.setLastPosition(newPosition);
                    curView.drawOnScreen(newProps);
                }
            } else {
                System.out.println("\n@@@@@@@@@@@@@@@@@ we want an uninitialised view to become: "+newPosition);
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
