import React from 'react';
import {
  AppRegistry,
  StyleSheet,
  View,
  Text,
} from 'react-native';

import ListExample from './ListExample';


/*
CAUTION:
  We swizzle react.render so if you try to register a recipe while other components are rendering,
  you will get errors (usually manage children errors)
  ALWAYS register the recipe when react is NOT rendering other components
  (Preferably at app launch)
*/
ListExample.registerRecipe();

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
        <ListExample />
      </View>
    );
  }
}
AppRegistry.registerComponent('RNExample', () => App);
