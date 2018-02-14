package com.sudoplz.rnsynchronouslistmanager.Sync;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

/**
 * Created by SudoPlz on 02/11/2017.
 */

public class Recipe {
    private ReadableMap recipeProps;
    private ReadableArray recipeInstructions;
    public Recipe(ReadableMap props, ReadableArray recipe) {
        recipeProps = props;
        recipeInstructions = recipe;
    }

    public ReadableMap getRecipeProps() {
        return recipeProps;
    }

    public ReadableArray getRecipeInstructions() {
        return recipeInstructions;
    }
}
