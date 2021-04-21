<template>
    <div class="container">
        <div v-if="getCurrentImg.data" class= "current-img-div">
            <ImageEdit 
                    :image="getCurrentImg" 
                    :metadata="getCurrentImgMD"
                    @confirm="unselectCurrentImg" 
                    @cancel="unselectCurrentImg" 
                    @deleteImg="removeImg" 
                    @updateImg="updateImg"
                    @saveImg="saveImg">
            <template v-slot:title>Edition de l'image</template>
            <p></p>
            </ImageEdit>
        </div>
        <vueper-slides 
            class="no-shadow"
            slide-multiple
            :gap="3"
            :slide-ratio="1 / 4"
            arrows-outside
            :visible-slides="3"
            :breakpoints="{ 800: { visibleSlides: 2, slideMultiple: 2 } }">
            <vueper-slide @click="selectCurrentImg(image.id)" v-for="image in getAllImages" :key="image.id" :image="image.data"/>
        </vueper-slides>
    </div>
</template>

<script>
import ImageEdit from '@/components/ImageEdit.vue';
import { VueperSlides, VueperSlide } from 'vueperslides'
import { mapGetters, mapActions } from "vuex"

export default {
    name: "Gallerie",
    components: {
        ImageEdit,
        VueperSlide,
        VueperSlides
    },
    methods: {
        ...mapActions([
            'selectCurrentImg',
            'unselectCurrentImg',
            'removeImg',
            'updateImg'
        ]),
        saveImg() {
            const url = this.getCurrentImg.data;
            const link = document.createElement('a')
            link.href = url
            link.setAttribute('download', this.getCurrentImgMD.name)
            document.body.appendChild(link)
            link.click()
        }
    },
    computed: {
        ...mapGetters([
            "getAllImages",
            "getCurrentImg",
            "getCurrentImgMD"
        ])
    }
}
</script>

<style scoped>

.container {
    text-align: center;
}

.current-img-div {
    margin-top: 2%;
    width: 80vw;
    display: inline-block;
}

.current-img {
    max-width: 100%;
    max-height: 600px;
    height: auto;
}
</style>
