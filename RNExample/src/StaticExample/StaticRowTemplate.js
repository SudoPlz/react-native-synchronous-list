import React from 'react';
import {
  TextInput,
  View,
} from 'react-native';

export default class StaticRowTemplate extends React.Component {
  render() {
    return (
      <View style={{ justifyContent: 'center', padding: 10, width: 300, height: 250, backgroundColor: '#AAA13377'}}>
        <TextInput
          style={{ textAlign: 'center', backgroundColor: '#FFFFFF99', flexGrow: 1 }}
          editable={false}
          value={this.props.item}
        />
      </View>
    );
  }
}