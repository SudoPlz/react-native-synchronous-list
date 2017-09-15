# react-native-synchronous-list

What: A list that uses a template to render it's children through the native side of react-native.
Why: Because trying to render children on the fly while scrolling sends to much information through the RNBridge and blocks it
As a result we see a white flicker when scrolling fast, plus it doesn't give the native feeling that native lists give.

How: If you want to learn more check out Tal Kol. He's the person that thought of that brilliant idea.

His talk in react-native-eu should be published soon, but until then here are the talk slides: https://speakerdeck.com/talkol/going-over-the-speed-limit-synchronous-rendering-in-react-native

Here's his initial proof of concept project:
https://github.com/wix/rn-synchronous-render



## Installation

1 . `$ npm install --save react-native-synchronous-list`

or

`$ yarn add react-native-synchronous-list`


2 . `$ react-native link react-native-synchronous-list` and check `MainApplication.java` to verify the package was added.

3 .  rebuild your project



## Usage
See the Snack example


## Todo
- Android version

## License
MIT © Ioannis Kokkinidis 2017-2018
