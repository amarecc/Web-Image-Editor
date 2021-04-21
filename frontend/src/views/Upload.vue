<template>
  <div class="upload">
    <UploadComponent></UploadComponent>
  </div>
</template>

<script>
// @ is an alias to /src
import UploadComponent from '@/components/UploadComponent.vue'

export default {
  name: 'Upload',
  components: {
    UploadComponent
  }
}
</script>
