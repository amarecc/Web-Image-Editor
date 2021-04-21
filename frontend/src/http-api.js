import axios from 'axios';

const baseUrl = "images/"

function get(id, config, thenCallback, catchCallback) {
    axios.get(baseUrl + id, config)
        .then((response) => {
            thenCallback(response);
        })
        .catch((err) => {
            catchCallback(err);
        });
}

function getPromise(id, config) {
    return axios.get(baseUrl + id, config);
}

function del(id, config, thenCallback, catchCallback) {
    axios.delete(baseUrl + id, config)
        .then((response) => {
            thenCallback(response);
        })
        .catch((err) => {
            catchCallback(err);
        });
}

//images/ only url to POST, no need to specify an id
function post(ext, data, config, thenCallback, catchCallback) {
    axios.post(baseUrl + ext, data, config)
        .then((response) => {
            thenCallback(response);
        })
        .catch((err) => {
            catchCallback(err);
        });
}

function concurrencyRequest(promises, thenCallback, catchCallback) {
    Promise.all(promises)
        .then((response) => {
            thenCallback(response);
        })
        .catch((err) => {
            catchCallback(err);
        });
}

export { get, del, post, concurrencyRequest, getPromise };