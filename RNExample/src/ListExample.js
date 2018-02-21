import React from 'react';
import {
  StyleSheet,
  Text,
  View,
  Dimensions,
} from 'react-native';
import { SynchronousList } from 'react-native-synchronous-list';




const complexDataObj = [{
  name: "Row 1",
  width: 150,
  height: 150,
},{
  name: "Row 2",
  width: 250,
  height: 70,
},{
  name: "Row 3",
  width: 300,
  height: 150,
},{
  name: "Row 4",
  width: 450,
  height: 150,
},{
  name: "Row 5",
  width: 550,
  height: 150,
},{
  name: "Row 6",
  width: 450,
  height: 150,
},{
  name: "Row 7",
  width: 350,
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

// for (const i = 11; i < 1500; i++) { 
//   complexDataObj.push({
//     name: `Row ${i}`,
//     width: 150,
//     height: 150,
//   })
// }

class ListExample extends React.Component {
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
          style={{ top: 0, width: Dimensions.get('window').width, height: Dimensions.get('window').height, backgroundColor: '#F2F2F2' }}
          templateName={this.props.templateName}
          rowHeight={100}
          dynamicViewSizes
          numRenderRows={10}
          data={complexDataObj}
          loopMode={SynchronousList.LOOP_MODES.NO_LOOP}
        />
    );
  }
}
export default ListExample;