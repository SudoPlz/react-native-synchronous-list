import React from 'react';
import {
  AppRegistry,
  StyleSheet,
  View,
} from 'react-native';

import StaticListExample from './StaticExample';
import DynamicListExample from './DynamicExample';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});

export default class App extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <StaticListExample />
      </View>
    );
  }
}

AppRegistry.registerComponent('RNExample', () => App);
