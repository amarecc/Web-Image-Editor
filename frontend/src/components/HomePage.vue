<template>
  <div class="hello">
    

  <Gallerie v-if="getAllImagesMetaData.length > 0"/>

  </div>
</template>

<script>
import Gallerie from '@/components/Gallerie.vue'
import { mapGetters } from "vuex"

export default {
  name: "HomePage",
  props: {
    msg: String,
  },
  components: {
    Gallerie
  },
  data() {
    return {
      selected: '',
      image: '',
      errors: [],
    };
  },
  methods: {
    err(e) {
      console.log(e);
      this.errors.push(e);
    }
  },
  created() {
    this.$store.dispatch('retrieveImagesMD');
  },
  computed: {
    ...mapGetters([
      'getAllImagesMetaData'
    ])
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
