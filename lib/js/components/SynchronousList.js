import PropTypes from 'prop-types';
import React from 'react';
import {
  requireNativeComponent,
  findNodeHandle,
  NativeModules,
} from 'react-native';


const SynchronousListNativeComponent = requireNativeComponent('RCTSynchronousList', null);
const SynchronousListManager = NativeModules.SynchronousListManager;

const LOOP_MODES = {
  NO_LOOP: 'no-loop',
  REPEAT_EMPTY: 'repeat-empty',
  REPEAT_EDGE: 'repeat-edge',
}

class SynchronousList extends React.Component {

  prepareRows() {
    SynchronousListManager.prepareRows();
  }

  updateDataAtIndex(index, newData) {
    SynchronousListManager.updateDataAtIndex(index, newData);
  }

  appendDataToDataSource(newData) {
    SynchronousListManager.appendDataToDataSource(newData);
  }

  prependDataToDataSource(newData) {
    SynchronousListManager.prependDataToDataSource(newData);
  }

  render() {
    return (
      <SynchronousListNativeComponent {...this.props} />
    );
  }
}
SynchronousList.LOOP_MODES = LOOP_MODES;
export default SynchronousList;
