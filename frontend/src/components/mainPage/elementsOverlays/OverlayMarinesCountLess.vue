<template>
  <v-form v-model="valid" lazy-validation ref="form">
    <!-- Оверлей поиска глав с id меньшим заданного -->
    <v-card
        color="#F7FAFC"
    >
      <v-card-text class="font-weight-medium" style="font-size: 15pt; ">
        <div style="color: black; text-align: center; margin-bottom: 5%; font-size: 25px; line-height: 1">
          <br>Вычислить кол-во глав, где marinesCount меньше заданного
        </div>
      </v-card-text>

      <v-card-text class="font-weight-medium" style="font-size: 15pt; ">
        <v-icon left style="display: inline-block">
          {{ icons.mdiFormatListNumbered }}
        </v-icon>
        <v-text-field
            v-model="MarinesCount"
            light
            type="number"
            :rules="rules.numberValid"
            label="Введите marinesCount!"
            solo
            required
            style="border-radius: 5px; display: inline-block; width: 90%"
        ></v-text-field>

        <v-btn style="margin-left: 33%; margin-bottom: 2%; margin-top: 20px"
               outlined
               :loading="loadingRemove"
               @click="marinesCountLower"
        >
          <v-icon left>
            {{ icons.mdiFormatTextWrappingOverflow }}
          </v-icon>
          Отправить
        </v-btn>
      </v-card-text>
      <v-divider style="margin-bottom: 5%"></v-divider>

      <v-btn style="margin-left: 38%; margin-bottom: 5%"
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
import {mdiFormatListNumbered, mdiFormatTextWrappingOverflow} from "@mdi/js";
import VueSimpleAlert from "vue-simple-alert";

export default {
  name: "OverlayMarinesCountLess",

  data: () => ({
    icons: {
      mdiFormatTextWrappingOverflow,
      mdiFormatListNumbered
    },
    absolute: true,
    valid: true,

    errorFlag: false,
    errorMessage: '',

    MarinesCount: '',
    Spaceships: [],
    loadingRemove: false,

    rules: {
      clearFieldValid: [
        v => v.indexOf(' ') === -1 || 'Поле не может быть пустым',
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
    },

    async marinesCountLower() {
      if (this.$refs.form.validate()) {
        this.loadingRemove = true
        let str = "https://localhost:10000/api/v1/space-marines/count-chapter-lower"
        axios.post(str, null, {
          params: {
            number: this.MarinesCount
          }
        })
        .then(resp => {
          VueSimpleAlert.fire({
            title: "",
            text: `Количество десантников, marines count которых меньше заданного: ${resp.data}`,
            type: "success",
            timer: 10000
          })
          console.log(resp.data)
        })
        .catch(err => {
          this.showError(err.response.data)
        })
        await new Promise(resolve => setTimeout(resolve, this.awaitTimer))
        this.loadingRemove = false
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
}
</script>

<style scoped>

</style>