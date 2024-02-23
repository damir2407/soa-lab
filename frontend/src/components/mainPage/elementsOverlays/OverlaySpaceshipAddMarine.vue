<template>
  <v-form v-model="valid" lazy-validation ref="form">
    <!-- Оверлей загрузки солдата на корабль -->
    <v-card
        color="#F7FAFC"
    >
      <v-card-text class="font-weight-medium" style="font-size: 15pt; ">
        <div style="color: black; text-align: center; margin-bottom: 5%; font-size: 25px; line-height: 1">
          <br>Загрузить космодесантника на корабль
        </div>
      </v-card-text>

      <v-card-text class="font-weight-medium" style="font-size: 15pt; ">
        <v-icon left style="display: inline-block">
          {{ icons.mdiHumanScooter }}
        </v-icon>
        <v-select
            v-model="ChooseSpacemarine"
            :items="Spacemarines"
            :item-text="item => `${item.name} [${item.id}]`"
            :item-value="'id'"
            :rules="rules.clearFieldValid"
            label="Выберите космодесантника"
            solo
            required
            style="border-radius: 5px; display: inline-block; width: 90%"
        ></v-select>

        <v-icon left style="display: inline-block">
          {{ icons.mdiShipWheel }}
        </v-icon>
        <v-select
            v-model="ChooseSpaceship"
            :items="Spaceships"
            :item-text="item => item.spaceMarineId ? `${item.name} [${item.id}] (Владелец десантник с id = ${item.spaceMarineId})` : `${item.name} [${item.id}]`"
            :item-value="'id'"
            :rules="rules.clearFieldValid"
            label="Выберите корабль"
            solo
            required
            style="border-radius: 5px; display: inline-block; width: 90%"
        ></v-select>

        <v-btn style="margin-left: 23%; margin-bottom: 2% ;margin-top: 20px"
               color=#0E1117
               outlined
               :loading="loadingRemove"
               @click="addMarineToSpaceship"
        >
          <v-icon left>
            {{ icons.mdiFormatVerticalAlignBottom }}
          </v-icon>
          Загрузить на корабль
        </v-btn>
      </v-card-text>
      <v-divider style="margin-bottom: 5%"></v-divider>

      <v-btn style="margin-left: 39%; margin-bottom: 5%"
             color=#0E1117
             outlined
             @click="closeDialog"
      >
        Закрыть
      </v-btn>
    </v-card>

    <v-alert v-if="errorFlag" type="error" dismissible style="position: absolute; right: 2%; bottom: 2%">
      {{ errorMessage }}
    </v-alert>
  </v-form>
</template>

<script>
import axios from "axios";
import {mdiFormatVerticalAlignBottom, mdiShipWheel, mdiHumanScooter} from "@mdi/js";

export default {
  name: "OverlaySpaceshipAddMarine",

  data: () => ({
    icons: {
      mdiFormatVerticalAlignBottom,
      mdiShipWheel,
      mdiHumanScooter
    },
    errorFlag: false,
    errorMessage: '',

    absolute: true,
    valid: true,

    ChooseSpaceship: '',
    ChooseSpacemarine: '',
    Spaceships: [],
    Spacemarines: [],
    loadingRemove: false,

    rules: {
      clearFieldValid: [
        v => !!v || 'Поле не может быть пустым'
      ],
      numberValid: [
        v => !!v || 'Поле не может быть пустым',
        v => !!/^\d*$/.test(v) || 'Допустимы только числа',
      ],
    },
  }),
  methods: {
    closeDialog() {
      this.$emit('updateParent', {
        dialog: false,
      })
      this.getListOfSpacemarines()
      this.getListOfStarShips()
    },

    getListOfSpacemarines() {
      let data = {
        page: 1,
        limit: 100,
      }
      axios.get(`https://localhost:10000/api/v1/space-marines`, data, {headers: this.getHeader()})
      .then(resp => {
        console.log(resp.data)
        this.Spacemarines = resp.data.content
      })
    },

    getListOfStarShips() {
      let data = {
        page: 1,
        limit: 100,
      }
      axios.get(`https://localhost:8100/api/v1/star-ships`, data, {headers: this.getHeader()})
      .then(resp => {
        console.log(resp.data)
        this.Spaceships = resp.data
      })
    },

    async addMarineToSpaceship() {
      if (this.$refs.form.validate()) {
        this.loadingRemove = true
        console.log(this.ChooseSpaceship)
        console.log(this.ChooseSpacemarine)
        let str = "https://localhost:8100/api/v1/star-ships/" + this.ChooseSpaceship + "/load/" + this.ChooseSpacemarine
        axios.post(str)
        .then(resp => {
          console.log(resp.data)
        })
        .catch(err => {
          this.showError(err.response.data)
        })
        await new Promise(resolve => setTimeout(resolve, this.awaitTimer))
        this.loadingRemove = false
        this.$root.$refs.SearchComp.getListOfSpacemarines();
        this.closeDialog()
      }
    },

    async showError(errorMessage) {
      if (this.errorFlag === true) {
        this.errorMessage = ''
        this.errorFlag = false
        await new Promise(resolve => setTimeout(resolve, 2000))
      }
      this.errorMessage = errorMessage
      this.errorFlag = true
      await new Promise(resolve => setTimeout(resolve, this.errorTimer))
      this.errorMessage = ""
      this.errorFlag = false
    }

  },
  beforeMount() {
    this.getListOfSpacemarines()
    this.getListOfStarShips()
  },
}
</script>

<style scoped>

</style>