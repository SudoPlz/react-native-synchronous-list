package com.sudoplz.rnsynchronouslistmanager.Sync;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.sudoplz.rnsynchronouslistmanager.Sync.Instructions.CreateViewInstruction;
import com.sudoplz.rnsynchronouslistmanager.Sync.Instructions.Instruction;
import com.sudoplz.rnsynchronouslistmanager.Sync.Instructions.SetViewInstruction;
import com.sudoplz.rnsynchronouslistmanager.Utils.WritableAdvancedMap;

import java.util.ArrayList;

/**
 * Created by SudoPlz on 19/02/2018.
 */

public class Recipe {

    private WritableAdvancedMap recipeBindings;
    private WritableAdvancedMap recipeInverseBindings;
    private ArrayList <Instruction> recipeInstructions;

    public Recipe(ReadableMap bindings, ReadableArray recipe) {
        recipeBindings = new WritableAdvancedMap(bindings);
        recipeInverseBindings = recipeBindings.getInverted();

        recipeInstructions = new ArrayList<Instruction>();
        for (int i = 0; i < recipe.size(); i++ ) {
            ReadableMap curInstruction = recipe.getMap(i);
            if (curInstruction.getString("cmd").equals("createView")) {
                recipeInstructions.add(new CreateViewInstruction(curInstruction.getArray("args")));
            } else if (curInstruction.getString("cmd").equals("setChildren")) {
                recipeInstructions.add(new SetViewInstruction(curInstruction.getArray("args")));
            }
        }
    }


    public ArrayList<Instruction> getRecipeInstructions() {
        return recipeInstructions;
    }

    public WritableAdvancedMap getRecipeBindings() {
        return recipeBindings;
    }

    public WritableAdvancedMap getRecipeInverseBindings() {
        return recipeInverseBindings;
    }
}
