import PropTypes from 'prop-types';
import { requireNativeComponent, ViewPropTypes } from 'react-native';

var iface = {
  name: 'CVrVideoView',
  propTypes: {
    ...ViewPropTypes,
    video: PropTypes.shape({
      uri: PropTypes.string.isRequired,
      type: PropTypes.string.isRequired,
      kill: PropTypes.bool.isRequired,
    }).isRequired,
    volume: PropTypes.number,
    displayMode: PropTypes.string,
    enableFullscreenButton: PropTypes.bool,
    enableCardboardButton: PropTypes.bool,
    enableInfoButton: PropTypes.bool,
    enableTouchTracking: PropTypes.bool,
    hidesTransitionView: PropTypes.bool,

    onLoadSuccess: PropTypes.func,
    onNewFrame: PropTypes.func,
    onCompletion: PropTypes.func,
    onPress: PropTypes.func,
    onNewFrame: PropTypes.func
  }
};

export default requireNativeComponent('CVrVideoView', iface);
