<template>
  <v-form v-model="valid" lazy-validation ref="form">
    <!-- Оверлей создания Spaceship -->
    <v-card color="#F7FAFC"
    >
      <v-card-text class="font-weight-medium" style="font-size: 15pt">
        <div style="color: black; text-align: center; margin-bottom: 5%; font-size: 25px; line-height: 1">
          <br>Создание космического корабля
        </div>
      </v-card-text>

      <v-card-text class="font-weight-medium" style="font-size: 15pt">
        <v-icon left style="display: inline-block">
          {{ icons.mdiFormTextbox }}
        </v-icon>
        <v-text-field
            light
            label="Название"
            v-model="name"
            name="SpaceshipName"
            type="string"
            :rules="rules.clearFieldValid"
            color=#0E1117
            background-color=#EDF2F7
            outlined
            style="border-radius: 5px; display: inline-block; width: 90%"
        />

        <v-icon left style="display: inline-block">
          {{ icons.mdiShipWheel }}
        </v-icon>
        <v-text-field
            light
            label="Максимальная скорость"
            v-model="maxSpeed"
            name="MaxSpeed"
            type="number"
            :rules="rules.numberValid"
            color=#0E1117
            background-color=#EDF2F7
            outlined
            style="border-radius: 5px; display: inline-block; width: 90%"
        />

      </v-card-text>

      <v-btn style="margin-left: 25%; margin-bottom: 5%; margin-top: 10px"
             color=#0E1117
             outlined
             :loading="loadingSave"
             @click="submit"
      >
        <v-icon style="margin-right: 8px">mdi-cloud-upload</v-icon>
        Сохранить и закрыть
      </v-btn>
    </v-card>

    <v-alert v-if="errorFlag" type="error" dismissible style="position: absolute; right: 2%; bottom: 2%">
      {{ errorMessage }}
    </v-alert>
  </v-form>
</template>

<script>
import axios from "axios";
import {mdiDelete, mdiFormTextbox, mdiShipWheel} from "@mdi/js";

export default {
  name: "OverlaySSpaceshipCreate",

  data: () => ({
    icons: {
      mdiDelete,
      mdiFormTextbox,
      mdiShipWheel
    },

    errorFlag: false,
    errorMessage: '',

    loadingRemove: false,
    loadingSave: false,

    absolute: true,
    valid: true,

    name: '',
    maxSpeed: '',
    coordinateX: '',
    coordinateY: '',

    rules: {
      clearFieldValid: [
        v => !!v.trim() || 'Поле не может быть пустым',
      ],
      numberValid: [
        v => !!v || 'Поле не может быть пустым',
        v => (parseFloat(v) > 0) || 'Число должно быть больше 0',
      ],
    },
  }),
  methods: {
    async submit() {
      if (this.$refs.form.validate()) {
        this.loadingSave = true
        let data = {
          name: this.name,
          maxSpeed: this.maxSpeed,
        }
        console.log(data)
        axios.post(`https://localhost:8100/api/v1/star-ships`, data, {headers: this.getHeader()})
        .then(resp => {
          console.log(resp.data)
        })
        .catch(err => {
          this.showError(err.response.data)
        })
        await new Promise(resolve => setTimeout(resolve, this.awaitTimer))
        this.updateOverlay()

        this.$emit('updateParent', {
          dialog: false,
        })
        this.loadingSave = false
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
    },

    updateOverlay() {
    },
  },
  beforeMount() {
    this.updateOverlay()
  }
}
</script>

<style scoped>

</style>