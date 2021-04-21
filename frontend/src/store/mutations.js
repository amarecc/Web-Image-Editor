export default {
    SET_METADATA(state, payload) {
        state.allImagesMetaData = payload;
    },
    SET_IMAGES(state, payload) {
        state.allImages = payload;
    },
    UPDATE_IMAGES(state, {index, data}) {
        state.allImages[index].data = data;
    },
    SET_CURRENT_IMG(state, index) {
        state.currentImg['data'] = state.allImages[index].data;
        state.currentImg['id'] = state.allImages[index].id;
    },
    REMOVE_CURRENT_IMG(state) {
        state.currentImg['data'] = "";
        state.currentImg['id'] = -1;
    },
    UPDATE_CURRENT_IMG(state, data) {
        state.currentImg['data'] = data;
    },
    SET_CURRENT_IMGMD(state, index) {
        state.currentImgMD = state.allImagesMetaData[index];
    },
    DELETE_IMG(state, index) {
        state.allImages.splice(index, 1);
    }
}