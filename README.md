# react-native-synchronous-list



### Android branch notes:

| TODO | Status |
| --- | --- |
| Recipe registration implementation | ✅ |
| SyncRootView implementation | ✅ |
| RecyclerListView implementation | :x: |
| RecyclerListView row item item (extends `SyncRootView`) | :x: |

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

<a href="https://cl.ly/0s2E1w2W2a2f" target="_blank"><img src="https://dha4w82d62smt.cloudfront.net/items/291j1U0E2a2x3j0O1C3K/Screen%20Recording%202018-02-14%20at%2006.53%20PM.gif" style="display: block;height: 40%;width: 40%;"/></a>

## Caution

That's still a WIP project and you should definitely not use it as it is on production apps.


## Typical usage

### Step 1:
Basically you first register a template synchronously by invoking the SyncRegistry.

This project uses 2 different list implementations:

A) One with static dimension rows ([here](https://github.com/SudoPlz/react-native-synchronous-list/blob/master/RNExample/src/StaticExample/StaticRowTemplate.js) is the JS template that we'll be using for that, and we register this template with our list [here](https://github.com/SudoPlz/react-native-synchronous-list/blob/master/RNExample/src/StaticExample/index.js#L13))

B) One with dynamic dimension rows (and [here](https://github.com/SudoPlz/react-native-synchronous-list/blob/master/RNExample/src/DynamicExample/DynamicRowTemplate.js) is the JS template that we'll be using for that, and we register this template with our list [here](https://github.com/SudoPlz/react-native-synchronous-list/blob/master/RNExample/src/DynamicExample/index.js#L14))

Those are just examples to give you an idea of how you could use this list.

### Step 2:
Prepare the native component for rendering (usually on `componentWillMount`)

    this.synchronousList.prepareRows();

In the example we just do it whenever we first get a [ref of the list](https://github.com/SudoPlz/react-native-synchronous-list/blob/master/RNExample/src/StaticExample/index.js#L27) though.


### Step 3:
Then all you have to do is render the list like a [normal js component](https://github.com/SudoPlz/react-native-synchronous-list/blob/master/RNExample/src/App.js#L23).

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

## Example

See the RNExample folder

## Todo
- Find out why there's a short delay when rendering thousands of children in our native list implementation
- Android version on the #android branch

## License
MIT © Ioannis Kokkinidis 2017-2018
