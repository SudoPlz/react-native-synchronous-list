import React from 'react';
import {
  Text,
  View,
  Dimensions,
} from 'react-native';
import { SynchronousList, SyncRegistry } from 'react-native-synchronous-list';
import StaticRowTemplate from './StaticRowTemplate';


const RowTemplateName = 'MyStaticRowTemplate';


let simpleDataObj = [];

for (const i = 0; i < 15000; i++) {
  simpleDataObj.push(`Row ${i}`);
}

class StaticListExample extends React.Component {
  render() {

    return (
      <SynchronousList
        ref={l => {
          this.synchronousList = l;
          setTimeout(() => {
            this.synchronousList.prepareRows();
          }, 100)

          // setInterval(async () => {
          //   const curViewIndex = await this.synchronousList.getCurrentViewIndex();
          //   console.log(`Currently at :${curViewIndex}`);
          // }, 500);
        }}
        style={{ top: 0, width: Dimensions.get('window').width, height: Dimensions.get('window').height, backgroundColor: '#222222' }}
        templateName={RowTemplateName}
        rowHeight={60}
        numRenderRows={30}
        data={simpleDataObj}
        loopMode={SynchronousList.LOOP_MODES.REPEAT_EMPTY}
      />
    );
  }
}

StaticListExample.registerRecipe = () => {
  SyncRegistry.registerComponent(RowTemplateName, () => StaticRowTemplate, ['item', 'index']);
}
export default StaticListExample;