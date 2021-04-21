<template>
    <div class="container">
        <div>
            <label>Files
                <input type="file" id="file" ref="file" multiple v-on:change="handleFilesUpload()"/>
            </label>
            <button v-on:click="submitFiles()">Submit</button>
        </div>

        <transition name="fade">
          <p v-if="show">Ajout de l'image réussi !</p>
        </transition>
  </div>
</template>

<script>
import { post } from "@/http-api.js"
import { useToast } from "vue-toastification"

export default {
    name: "UploadComponent",
    data() {
        return {
            file: ''
        }
    },
    setup() {
        const toast = useToast();
        
        
        return { toast };
    },
    methods: {
      /*
        Submits img to the server
      */
      submitFiles(){
        /*
          Initialize the form data
        */
        let formData = new FormData();

        formData.append('file', this.file, this.file.name);

        /*
          Make the request to the POST /images URL
        */
        post("", formData,
          {
            headers: {
                'Content-Type': this.file['type']
            }
          },
          () => {
            this.toast.success("Image upload !", { timeout:1500 });
          },
          () => {
            this.toast.error("Erreur pendant l'upload !", { timeout:1500 });
          });
      },

      /*
        Handles a change on the file upload
      */
      handleFilesUpload(){
        this.file = this.$refs.file.files[0];
      }
    }
}
</script>

<style scoped>
.fade-leave-active {
  transition: opacity .5s ease;
}
.fade-leave-to {
  opacity: 0;
}
</style>