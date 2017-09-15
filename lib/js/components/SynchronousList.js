import PropTypes from 'prop-types';
import React from 'react';
import {
  requireNativeComponent,
  findNodeHandle,
  NativeModules,
} from 'react-native';


const SynchronousListNativeComponent = requireNativeComponent('RCTSynchronousList', null);
const SynchronousListManager = NativeModules.RCTSynchronousListManager;

const LOOP_MODES = {
  NO_LOOP: 'no-loop',
  REPEAT_EMPTY: 'repeat-empty',
  REPEAT_EDGE: 'repeat-edge',
}

class SynchronousList extends React.Component {
  render() {
    return (
      <SynchronousListNativeComponent {...this.props} />
    );
  }
}
SynchronousList.LOOP_MODES = LOOP_MODES;
export default SynchronousList;
