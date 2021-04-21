import { get, del, getPromise } from '@/http-api.js'

export default {
    retrieveImagesMD({ commit }) {
        get("", "", 
            (res) => { commit('SET_METADATA', res.data); this.dispatch("retrieveImages"); }, 
            () => {});
    },
    retrieveImages({ commit, state }) {
        const promises = [];
        let payload = [];

        // Execute promise in id order
        for (var i = 0; i < state.allImagesMetaData.length; i++) {
            promises.push(
                getPromise(state.allImagesMetaData[i].id, { responseType:"blob" })
            );
        }
        Promise.all(promises)
            .then((responses) => {  
            for (let i = 0; i < responses.length; i++) {
                let reader = new window.FileReader();
                reader.readAsDataURL(responses[i].data); 

                reader.onloadend = () => {
                    console.log(reader.result);
                    var imageDataUrl = reader.result;
                    payload.push({'id': state.allImagesMetaData[i].id, 'data': imageDataUrl})

                    if (payload.length == state.allImagesMetaData.length)
                        commit('SET_IMAGES', payload);
                    }
                }
        });
    },
    selectCurrentImg({ commit, state }, id) {
        let index = state.allImages.findIndex(image => image.id == id);
        commit('SET_CURRENT_IMG', index);

        index = state.allImagesMetaData.findIndex(elem => { return elem.id == id });
        commit('SET_CURRENT_IMGMD', index)
    },
    unselectCurrentImg({ commit }) {
        commit('REMOVE_CURRENT_IMG');
    },
    removeImg({ commit, state }, image) {
        del(image['id'], "", () => {
            let index = state.allImages.findIndex(elem => {
                return image['id'] == elem.id;
            });
            
            commit('REMOVE_CURRENT_IMG');
            commit('DELETE_IMG', index);
        }, 
        () => {}
        );
    },
    updateImg({ commit, state }, {res, image}) {
        if (res != undefined) {
            let index = state.allImages.findIndex(elem => {
                return image['id'] == elem.id;
            });
            
            let reader = new window.FileReader();
            reader.readAsDataURL(res); 

            reader.onloadend = () => {
                var imageDataUrl = reader.result;
                commit('UPDATE_IMAGES', {index: index, data: imageDataUrl});
                
                commit('UPDATE_CURRENT_IMG', imageDataUrl);
            }
        }
    }
}