import _ from 'lodash';
import React from 'react';
const ReactNative = require('react-native/Libraries/Renderer/shims/ReactNative');
const NativeModules = require('react-native').NativeModules;
const UIManager = NativeModules.UIManager;
const RCCSyncRegistry = NativeModules.RCCSyncRegistry;

const origCreateView = UIManager.createView;
const origSetChildren = UIManager.setChildren;
const origManageChildren = UIManager.manageChildren;

export default class SyncRegistry {
  static registerComponent(registeredName, componentGenerator, propNames) {
    const Template = componentGenerator();
    const props = {};
    for (const propName of propNames) {
      props[propName] = `__${propName}__`;
    }
    const recipe = [];
    prepareRecipeBySwizzlingUiManager(recipe);
    const rendered = ReactNative.render(<Template {...props} />, 1);
    restoreUiManager();
    RCCSyncRegistry.registerRecipe(registeredName, props, recipe);
  }
}

function prepareRecipeBySwizzlingUiManager(recipe) {
  UIManager.createView = (...args) => {
    // let childArgs = args;
    // const props = childArgs[3];
    // for (propKey in props) {
    //   const curProp = props[propKey];
    //   if (curProp && curProp.indexOf != null && curProp.indexOf('_') != -1) {
    //     console.log(`tampered prop: ${JSON.stringify(curProp)}`)
    //     childArgs[3][propKey] = 'test';
    //   }
    // }
    // console.log(`new args: ${JSON.stringify(childArgs)}`)
    recipe.push({ cmd: 'createView', args });
  }
  UIManager.setChildren = (...args) => {
    recipe.push({ cmd: 'setChildren', args });
  }
  UIManager.manageChildren = (...args) => {
  }
}

function restoreUiManager() {
  UIManager.createView = origCreateView;
  UIManager.setChildren = origSetChildren;
  UIManager.manageChildren = origManageChildren;
}
