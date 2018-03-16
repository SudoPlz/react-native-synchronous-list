# react-native-synchronous-list



## Consept
#### What:
This is an infinite list implementation that uses a template defined in javascript to render it's (native) children through an interesting mechanism called [synchronous rendering](https://www.youtube.com/watch?v=HXKFQu2cP4c) first envisioned by [Tal Kol](https://medium.com/@talkol) from wix.

#### Why:
Because trying to render children on the fly while scrolling sends to much information through the RNBridge and blocks it.
As a result we see a white flicker when scrolling fast, plus it doesn't give the native feeling that native lists give.

#### How:

- When the app starts we register a component "template" that we'll be using as our row view of our native list.
- We then create a set of instructions so that the native side knows how to convert that template into a native view
- Finally we use the list like a typical native UI component


[Here's his](https://github.com/wix/rn-synchronous-render) initial proof of concept project:




## Installation

1 . `$ npm install --save react-native-synchronous-list`

or

`$ yarn add react-native-synchronous-list`


2 . `$ react-native link react-native-synchronous-list`

3 .  Run the project

You should able to see this:

iOS             |  Android
:-------------------------:|:-------------------------:
<a href="https://github.com/SudoPlz/react-native-synchronous-list/tree/master/RNExample/ios" target="_blank"><img src="https://dha4w82d62smt.cloudfront.net/items/291j1U0E2a2x3j0O1C3K/Screen%20Recording%202018-02-14%20at%2006.53%20PM.gif"  height="640" width="320"/></a>  |  <a href="https://github.com/SudoPlz/react-native-synchronous-list/tree/master/RNExample/android" target="_blank"><img src="https://cl.ly/1c2U050O1B0F/Screen%20Recording%202018-03-16%20at%2002.16%20PM.gif" height="640" width="340"/></a>



## Caution

That's still a WIP project and you should definitely not use it as it is on production apps.


## Typical usage

### Step 1:
Basically you first register a template synchronously by invoking the SyncRegistry.

[Here's](https://github.com/SudoPlz/react-native-synchronous-list/blob/master/RNExample/src/RowTemplate.js) how a template looks like and [here's](https://github.com/SudoPlz/react-native-synchronous-list/blob/master/RNExample/src/RowTemplate.js#L38) where we register it.

So we'll declare that jsx template and the native code will create a recipe out of it.
Next time we want to create that view, the native code will create a new view based on that recipe (without the need to go to javascript) - it all happens on the native side now.

### Step 2:
Prepare the native component for rendering

    this.synchronousList.prepareRows();

In the example we just do it whenever we first get a [ref of the list](https://github.com/SudoPlz/react-native-synchronous-list/blob/c0af3b808059f9520c2f633210b2136ca60b6456/RNExample/src/ListExample.js#L70).


### Step 3:
Then all you have to do is render the list like a [normal js component](https://github.com/SudoPlz/react-native-synchronous-list/blob/c0af3b808059f9520c2f633210b2136ca60b6456/RNExample/src/ListExample.js#L66).

## Available props

| Name | Type| Description | Default |
| --- | --- | --- | --- |
| data | array | **REQUIRED** The data that we'll be mapping to our views | - |
| numRenderRows | number | **REQUIRED** The total views we'll be re/using (this list is recycling views remember?). Choose a value that's big enough so that the rows cover at least 1 screen. | - |
| loopMode | string |  Either **no-loop** (typical list), **repeat-empty** (a list that repeats empty views after we're out of data or **repeat-edge** (a least that repeats views that were in the beggining of our data once we're out of data (infinite loop mode) | `no-loop` |
| horizontal | bool | Wether we'll be running the list in horizontal mode or vertical | `false` |
| dynamicViewSizes | bool | True if we'll be calculating the row sizes based on the props data or false if we'll be using the `rowHeight` and `rowWidth` static values | `false` |
| rowHeight | number | The height of each row when using the list in static row size mode. | - |
| rowWidth | number | The width of each row when using the list in static row size mode. | - |
| templateName | string | The name of the template object we'll be using. (No need to change that) | 'RNSynchronousListRowTemplate' |

## Exposed methods

- (Promise) prependDataToDataSource(newData : Array) Prepends the newData to the list datasource
- (Promise) appendDataToDataSource(newData : Array) Appends the newData to the list datasource
- (Promise) updateDataAtIndex(index: int, newData : Object) Updates the data of the specified item
- (void) scrollToItem(position: int) Scrolls to the position specified


## Example

See the [RNExample](https://github.com/SudoPlz/react-native-synchronous-list/tree/master/RNExample) folder

## Todo

| TODO | Status |
| --- | --- |
| Recipe registration implementation | ✅ |
| SyncRootView implementation | ✅ |
| RecyclerListView implementation | ✅ |
| RecyclerListView row item item (extends `SyncRootView`) | ✅|
| Reload working ( [RN Issue here](https://github.com/facebook/react-native/issues/18413#issuecomment-373694707) )| :x: |

## License
MIT © Ioannis Kokkinidis 2017-2018
