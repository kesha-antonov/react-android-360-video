/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import CVrVideoView from './VrVideo360';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View
} from 'react-native';

export default class VrVideoComponent extends Component {

  constructor() {
    console.log("[VrVideoComponent][JS] constructor  -------------------");
    super();
    this._onChange = this._onChange.bind(this);
  }

  _onChange(event: Event) {
    console.log("[VrVideoComponent][JS] _onChange  -------------------");
    if (!this.props.onTouchPlayer) {
      return;
    }
    if(event.type === 'touch'){
      this.props.onTouchPlayer(event.nativeEvent.message);
      return ;
    }
  }

  // onDisplayModeChanged = () => {
  //   console.log("[VrVideoComponent][JS] onDisplayModeChanged  -------------------");
  // }

  // _onDisplayModeChanged(event: Event) {
  //   console.log("[VrVideoComponent][JS] _onDisplayModeChanged  --------------");
  //   if (!this.props.onDisplayModeChanged) {
  //     console.log("[VrVideoComponent][JS] _onDisplayModeChanged return ------");
  //     return;
  //   }
  //   console.log("[VrVideoComponent][JS] _onDisplayModeChanged call ----------");
  //   // this.props.onDisplayModeChanged(event.nativeEvent.message);
  // }

  render() {
    return (
      <View style={styles.container}>
        <CVrVideoView
          style={{height:300,width:300,backgroundColor:'#000000'}}
          video={{
            uri:'https://d2v9y0dukr6mq2.cloudfront.net/video/preview/eG7t61g/underwater-coral-reef-360-vr_S94kBUa0__WM.mp4',
            type: 'stereo',
            kill: false,
          }}
          displayMode={'fullscreen'}
          volume={1.0}
          enableInfoButton={false}
          enableFullscreenButton={true}
          enableCardboardButton={true}
          enableTouchTracking={true}
          hidesTransitionView={false}
          onChange={this._onChange}
          />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('VrVideoComponent', () => VrVideoComponent);
