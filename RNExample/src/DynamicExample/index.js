import React from 'react';
import {
  StyleSheet,
  Text,
  View,
  Dimensions,
} from 'react-native';
import { SynchronousList, SyncRegistry } from 'react-native-synchronous-list';
import DynamicRowTemplate from './DynamicRowTemplate';


const RowTemplateName = 'MyDynamicRowTemplate';

const complexDataObj = [{
  name: "Row 1",
  width: 850,
  height: 150,
},{
  name: "Row 2",
  width: 150,
  height: 30,
},{
  name: "Row 3",
  width: 500,
  height: 150,
},{
  name: "Row 4",
  width: 750,
  height: 150,
},{
  name: "Row 5",
  width: 150,
  height: 150,
},{
  name: "Row 6",
  width: 150,
  height: 150,
},{
  name: "Row 7",
  width: 150,
  height: 150,
},{
  name: "Row 8",
  width: 150,
  height: 150,
},{
  name: "Row 9",
  width: 150,
  height: 150,
},{
  name: "Row 10",
  width: 150,
  height: 150,
}];

class DynamicListExample extends React.Component {
  render() {
    return (
      <SynchronousList
          ref={l => {
            this.synchronousList = l;
            setTimeout(() => {
              this.synchronousList.prepareRows();
            }, 100)
            setInterval(async () => {
            const curViewIndex = await this.synchronousList.getCurrentViewIndex();
            console.log(`Currently at :${curViewIndex}`);
          }, 500);
          }}
          style={{ top: 0, width: Dimensions.get('window').width, height: Dimensions.get('window').height, backgroundColor: '#222222' }}
          templateName={RowTemplateName}
          rowWidth={Dimensions.get('window').width}
          dynamicViewSizes
          numRenderRows={10}
          data={complexDataObj}
          loopMode={SynchronousList.LOOP_MODES.NO_LOOP}
        />
    );
  }
}
DynamicListExample.registerRecipe = () => {
  SyncRegistry.registerComponent(RowTemplateName, () => DynamicRowTemplate, ['item.name','item.width','item.height', 'index']);
}
export default DynamicListExample;