import { createStore } from 'vuex';

import mutations from './mutations';
import actions from './actions';

export default createStore({
  state() {
    return {
      allImagesMetaData: [],
      allImages: [],
      currentImg: { id: 0, data: ""  },
      currentImgMD: ""
    }
  },
  getters: {
    getAllImagesMetaData: state => state.allImagesMetaData,
    getAllImages: state => state.allImages,
    getCurrentImg: state => state.currentImg,
    getCurrentImgMD: state => state.currentImgMD
  },
  actions,
  mutations
})
