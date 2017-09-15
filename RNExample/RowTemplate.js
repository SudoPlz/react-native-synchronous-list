import React from 'react';
import {
  TextInput,
  View,
} from 'react-native';

export default class RowTemplate extends React.Component {
  render() {
    return (
      <View style={{padding: 10, width: this.props['item.width'], height: this.props['item.height'], backgroundColor: '#AAAA3377'}}>
        <TextInput
          style={{ backgroundColor: '#FFFFFF88', flexGrow: 1 }}
          editable={false}
          value={this.props['item.name']}
        />
      </View>
    );
  }
}