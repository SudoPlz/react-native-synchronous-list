import React from 'react';
import {
  AppRegistry,
  StyleSheet,
  View,
  Text,
} from 'react-native';

import { TemplateName } from './RowTemplate'; // must run before rendering anything else
import ListExample from './ListExample';



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
        <ListExample templateName={TemplateName}/>
      </View>
    );
  }
}
AppRegistry.registerComponent('RNExample', () => App);
