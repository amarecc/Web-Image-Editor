<template>
  <div class="root__container">
    <div class="image__container">
      <img :src="image['data']">
    </div>
    <div>
      <select v-model="currentAlgo" @change="selectAlgoChange">
        <option ref="algoSelect" v-for="(algo, index) in algos" 
                :key="index" 
                :value="algo">
          {{ algo.name }}
        </option>
      </select>
      <div v-if="currentAlgo.params.length > 0">
        <form @change="formUpdate" v-for="(param, i) in currentAlgo.params" :key="i">
          <label class="input__container">
            <input @click="selectInput" v-model="picked" v-if="param.type == 'radio'" 
                    :type="param.type" 
                    :value="param.value" 
                    :name="currentAlgo.apiName"
                    class="input__radio"/>
            <span class="input__span" v-if="param.type == 'radio'">{{ param.name }}</span>
          </label>
          <input v-model="valueParam" v-if="param.type == 'input'" 
                  :type="param.type" 
                  :disabled="picked == 'gaussian'"
                  :placeholder="param.name"
                  class="input__input"/>
        </form>
      </div>
    </div>
    <Modal v-model="showModal">
      <template v-slot:content>
        <pre>{{ JSON.stringify(metadata, null, 2) }}</pre>
      </template>
    </Modal>
    <div>
        <button class="button__apply" @click="applyAlgo()" :disabled="validator">Valider</button>
        <button class="button__cancel" @click="$emit('cancel', close)">Annuler</button>
        <button class="button__reset" @click="reset()">Reset</button>
        <button class="button__delete" @click="$emit('deleteImg', image)">Supprimer</button>
        <button class="button__save" @click="$emit('saveImg')">Enregistrer</button>
        <button class="button__apply" @click="openModal()">Meta Data</button>
    </div>
  </div>
</template>

<script>
import { post } from "@/http-api.js";
import jsonAlgos from "@/assets/algos.json"
import Modal from "@/components/Modal.vue"
import { useToast } from "vue-toastification"


export default {
  name: 'ImageEdit',
  inheritAttrs: false,
  props: {
    image: Object,
    metadata: Object
  },
  components: {
    Modal
  },
  setup() {
    const toast = useToast();
    
    return { toast };
  },
  data: () => ({
    algos: [],
    currentAlgo: {},
    picked: "",
    valueParam: "",
    imageSelected: "",
    showModal: false
  }),
  created() {
    this.algos = jsonAlgos;
    this.currentAlgo = this.algos[0];
  },
  methods: {
    applyAlgo() {
      var config = { 
        params: { },
        responseType: "blob"
       };
      config['params']['algorithm'] = this.currentAlgo.apiName;

      this.currentAlgo.params.forEach(param => {
        if (param.type == "input")
          config['params'][param.apiName] = this.valueParam;
        else if (param.type == "radio") 
          config['params'][param.apiName] = this.picked;
      });

      post(this.image['id'], {}, config, 
        (res) => { 
        this.$emit('updateImg', {res: res.data, image: this.image});
        this.toast.success("Algorithme appliqué !", { timeout:1500 }); 
        }, 
        (err) => {
          console.log(err);
          this.toast.success("Erreur dans l'application de l'agorithme !", { timeout:1500 });
        });
    },
    reset() {
      post(this.image['id'] + "/reset", {}, {responseType: "blob"}, (res) => { this.$emit('updateImg', {res: res.data, image: this.image}) }, (err) => { console.log(err); });
    },
    close() {
      this.$emit('input', false)
    },
    selectAlgoChange() {
      this.valueParam = "";
      this.picked = "";
    },
    openModal() {
      this.showModal = true;
    }
  },
  computed: {
    validator() {
      this.picked;

      let notValid = false;
      if (this.valueParam == "" && this.picked == "" && this.currentAlgo.params.length > 0)
        notValid = true;
      else if (this.currentAlgo.apiName == "addLumen") {
        if (this.currentAlgo.name == "Diminuer la luminiosité" && this.valueParam > 0)
          notValid = true;
        if (this.currentAlgo.name == "Augmenter la luminiosité" && this.valueParam < 0)
          notValid = true;
      }
      else if (this.currentAlgo.apiName == "equaliseHist" && this.picked == ""){
        notValid = true;
      }
      else if (this.currentAlgo.apiName == "blur" && this.picked == "average" && this.valueParam == "") {
        notValid = true;
      }

      return notValid;
    }
  }
}
</script>

<style scoped>
.image__container {
  display: grid;
  justify-content: space-around;
  max-width: 100%;
  min-height: 448px;
  padding: 40px 50px;
}

img {
  box-shadow: 0 8px 20px 0 rgba(0, 0, 0, 0.15);
}

.button__apply,
.button__cancel,
.button__reset,
.button__save,
.button__delete {
  border: transparent;
  cursor: pointer;
  padding: 10px;
  border-radius: 9px;
  box-shadow: 0 3px 5px 0 rgba(0, 0, 0, 0.15);
  transition: all 0.2s ease-in-out;
  margin: 40px 5px;
  text-decoration: none;
}

.button__apply:hover:enabled,
.button__cancel:hover,
.button__save:hover {
  background: #00ad5f;
  color: white;
}

.button__delete:hover,
.button__reset:hover {
  background: #EA2027;
  color: white;
}

form {
  text-align: center;
}

.input__input {
  border: none;
  border-radius: 9px;
  padding: 10px;
  box-shadow: 0 3px 5px 0 rgba(0, 0, 0, 0.15);
  margin-bottom: .6em;
}

.input__radio {
  margin-bottom: .6em;
  margin-right: 5px;
  height: 25px;
}

select {
	font-size: 16px;
	font-family: sans-serif;
	font-weight: 500;
	color: #444;
	line-height: 1.3;
	padding: .6em 1.4em .5em .8em;
	box-sizing: border-box;
	margin-bottom: .6em;
	border: 1px solid #aaa;
	box-shadow: 0 1px 0 1px rgba(0,0,0,.04);
	border-radius: .5em;
	-moz-appearance: none;
	-webkit-appearance: none;
	appearance: none;
	background-color: #fff;
}
</style>
