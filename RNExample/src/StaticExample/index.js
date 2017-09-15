import React from 'react';
import {
  Text,
  View,
  Dimensions,
} from 'react-native';
import { SynchronousList, SyncRegistry } from 'react-native-synchronous-list';
import StaticRowTemplate from './StaticRowTemplate';


const RowTemplateName = 'MyStaticRowTemplate';

SyncRegistry.registerComponent(RowTemplateName, () => StaticRowTemplate, ['item', 'index']);

const simpleDataObj =
["Row 1", "Row 2", "Row 3", "Row 4", "Row 5", "Row 6", "Row 7"];


export default class App extends React.Component {
  render() {

    return (
      <SynchronousList
        ref={l => {
          this.synchronousList = l;
          setTimeout(() => {
            this.synchronousList.prepareRows();
          }, 100)
        }}
        style={{ top: 0, width: Dimensions.get('window').width, height: Dimensions.get('window').height, backgroundColor: '#222222' }}
        templateName={RowTemplateName}
        rowHeight={260}
        numRenderRows={10}
        data={simpleDataObj}
        loopMode={SynchronousList.LOOP_MODES.REPEAT_EMPTY}
      />
    );
  }
}