import React from 'react';
import {
  TextInput,
  View,
} from 'react-native';
import { SyncRegistry } from 'react-native-synchronous-list';

const TemplateName = 'MyTemplate';

const RowTemplate = (props) => (
  <View style={{padding: 10, width: props['item.width'], height: props['item.height'], backgroundColor: '#AAA13377'}}>
    <TextInput
      style={{ backgroundColor: '#FFFFFF99', flexGrow: 1 }}
      editable={false}
      value={props['item.name']}
    />
  </View>
);
  
/*
  CAUTION:
    We swizzle react.render so if you try to register a recipe while other components are rendering,
    you will get errors (usually manage children errors)
    ALWAYS register the recipe when react is NOT rendering other components
    (Preferably right when the app launches - and before you render other components!!)
*/
SyncRegistry.registerComponent(TemplateName, () => RowTemplate, ['item.name','item.width','item.height', 'index']);

RowTemplate.TemplateName = TemplateName;
export default RowTemplate;