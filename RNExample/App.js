import React from 'react';
import {
  StyleSheet,
  Text,
  View,
  Dimensions,
  AppRegistry,
} from 'react-native';
import { SynchronousList, SyncRegistry } from 'react-native-synchronous-list';
import RowTemplate from './RowTemplate';


const RowTemplateName = 'MyRowTemplate';

SyncRegistry.registerComponent(RowTemplateName, () => RowTemplate, ['item.name','item.width','item.height', 'index']);

const dataObj = [{
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


const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#D5D7FF',
    alignItems: 'center',
    justifyContent: 'center',
  },
});

export default class App extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <SynchronousList
          ref={l => {
            this.synchronousList = l;
            setTimeout(() => {
              this.synchronousList.prepareRows();
            }, 1000)
          }}
          style={{ top: 0, width: Dimensions.get('window').width, height: Dimensions.get('window').height, backgroundColor: 'pink' }}
          horizontal
          templateName={RowTemplateName}
          rowHeight={150}
          rowWidth={Dimensions.get('window').width}
          dynamicViewSizes
          numRenderRows={10}
          data={dataObj}
          loopMode="no-loop"
        />
      </View>
    );
  }
}


AppRegistry.registerComponent('RNExample', () => App);