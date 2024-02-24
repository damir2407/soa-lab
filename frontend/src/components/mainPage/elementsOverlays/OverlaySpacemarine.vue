<template>
  <v-form v-model="valid" lazy-validation ref="form">
    <!-- Оверлей Spacemarine -->
    <v-card color="#F7FAFC"
    >
      <v-card-text class="font-weight-medium" style="font-size: 15pt">
        <div style="color: black; text-align: center; margin-bottom: 5%; font-size: 25px; line-height: 1">
          <br>Информация о космодесантнике
        </div>
      </v-card-text>

      <v-card-text class="font-weight-medium" style="font-size: 15pt">
        <v-icon left style="display: inline-block">
          {{ icons.mdiFormTextbox }}
        </v-icon>
        <v-text-field
            light
            label="Имя"
            v-model="name"
            name="SpacemarineName"
            type="string"
            :rules="rules.clearFieldValid"
            color=#0E1117
            background-color=#EDF2F7
            outlined
            style="border-radius: 5px; display: inline-block; width: 90%"
        />

        <v-icon left style="display: inline-block">
          {{ icons.mdiHeart }}
        </v-icon>
        <v-text-field
            light
            label="Количество здоровья"
            v-model="health"
            name="health"
            type="number"
            :rules="rules.healthValid"
            color=#0E1117
            background-color=#EDF2F7
            outlined
            style="border-radius: 5px; display: inline-block; width: 90%"
        />
        <v-icon left style="display: inline-block">
          {{ icons.mdiHumanMaleHeight }}
        </v-icon>
        <v-text-field
            light
            label="Рост"
            v-model="height"
            name="Height"
            type="number"
            :rules="rules.heightValid"
            color=#0E1117
            background-color=#EDF2F7
            outlined
            style="border-radius: 5px; display: inline-block; width: 90%"
        />

        <v-icon left style="display: inline-block">
          {{ icons.mdiHumanMale }}
        </v-icon>
        <v-select
            label="Категория"
            solo
            v-model="category"
            :items="categoryList"
            name="Category"
            required
            editable
            style="display: inline-block; width: 90%"
        ></v-select>

        <v-card-text class="font-weight-medium" style="font-size: 20px; text-align: center">
          Координаты
        </v-card-text>
        <v-row>
          <v-col>
            <v-text-field
                dense
                label="Координата X"
                v-model="coordinateX"
                type="number"
                color=#0E1117
                background-color=#EDF2F7
                outlined
                style="border-radius: 5px;"
            />
          </v-col>
          <v-col>
            <v-text-field
                dense
                label="Координата Y"
                v-model="coordinateY"
                :rules="rules.coordinateYRule"
                type="number"
                color=#0E1117
                background-color=#EDF2F7
                outlined
                style="border-radius: 5px; margin-bottom: 20px"
            />
          </v-col>
        </v-row>


        <v-icon left style="display: inline-block">
          {{ icons.mdiPistol }}
        </v-icon>
        <v-overflow-btn
            label="Тип оружия"
            light
            v-model="weaponType"
            :items="weaponTypeList"
            :rules="rules.clearFieldValid"
            name="WeaponType"
            required
            editable
            dense
            style="display: inline-block; width: 90%"
        ></v-overflow-btn>
        <v-card-text class="font-weight-medium" style="font-size: 20px; text-align: center">
          Глава
        </v-card-text>
        <v-icon left style="display: inline-block">
          {{ icons.mdiFormTextbox }}
        </v-icon>
        <v-text-field
            light
            label="Название"
            v-model="chapterName"
            name="ChapterName"
            type="string"
            :rules="rules.clearFieldValid"
            color=#0E1117
            background-color=#EDF2F7
            outlined
            style="border-radius: 5px; display: inline-block; width: 90%"
        />
        <v-icon left style="display: inline-block">
          {{ icons.mdiFormTextbox }}
        </v-icon>
        <v-text-field
            light
            label="Родительский легион"
            v-model="parentLegion"
            name="ParentLegion"
            type="string"
            :rules="rules.clearFieldValid"
            color=#0E1117
            background-color=#EDF2F7
            outlined
            style="border-radius: 5px; display: inline-block; width: 90%"
        />
        <v-icon left style="display: inline-block">
          {{ icons.mdiCounter }}
        </v-icon>
        <v-text-field
            light
            label="Количество космодесантников"
            v-model="marinesCount"
            name="MarinesCount"
            type="number"
            color=#0E1117
            background-color=#EDF2F7
            outlined
            style="border-radius: 5px; display: inline-block; width: 90%"
        />
        <v-icon left style="display: inline-block">
          {{ icons.mdiNumeric1Circle }}
        </v-icon>
        <v-text-field
            light
            label="Мир"
            v-model="world"
            name="World"
            type="string"
            :rules="rules.clearFieldValid"
            color=#0E1117
            background-color=#EDF2F7
            outlined
            style="border-radius: 5px; display: inline-block; width: 90%"
        />

      </v-card-text>


      <v-btn style="margin-left: 25%; margin-bottom: 5%"
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
import {
  mdiDelete,
  mdiHeart,
  mdiHumanMale,
  mdiSwordCross,
  mdiPistol,
  mdiFormTextbox,
  mdiCounter,
  mdiNumeric1Circle, mdiHumanMaleHeight
} from "@mdi/js";

export default {
  name: "OverlaySpacemarine",
  props: {
    spacemarineIndex: Number
  },

  data: () => ({
    icons: {
      mdiDelete,
      mdiHeart,
      mdiHumanMale,
      mdiSwordCross,
      mdiPistol,
      mdiFormTextbox,
      mdiCounter,
      mdiNumeric1Circle,
      mdiHumanMaleHeight
    },

    errorFlag: false,
    errorMessage: '',

    loadingRemove: false,
    loadingSave: false,

    absolute: true,
    valid: true,

    name: '',
    health: '',
    height: '',
    category: '',
    weaponType: '',
    creationDate: '',
    coordinateX: '',
    coordinateY: '',
    chapterName: '',
    parentLegion: '',
    marinesCount: '',
    world: '',
    categoryList: ["SCOUT", "ASSAULT", "SUPPRESSOR", "HELIX"],
    weaponTypeList: ["MELTAGUN", "BOLT_PISTOL", "BOLT_RIFLE", "FLAMER", "HEAVY_FLAMER"],

    rules: {
      clearFieldValid: [
        v => !!v.trim() || 'Поле не может быть пустым',
      ],
      numberValid: [
        v => !!v || 'Поле не может быть пустым',
        v => !!/^\d*$/.test(v) || 'Допустимы только числа',
      ],
      healthValid: [
        v => !!v || 'Поле не может быть пустым',
        v => !!/^\d*$/.test(v) || 'Допустимы только числа',
        v => +v > 0 || 'Значение должно быть больше 0',
      ],
      heightValid: [
        v => !!v || 'Поле не может быть пустым',
        v => /^(\d*\.)?\d+$/.test(v) || 'Допустимы только числа',
        v => +v > 0 || 'Значение должно быть больше 0',
      ],
      coordinateYRule: [
        v => !!v || 'Поле не может быть пустым',
        v => Number.isInteger(Number(v)) || 'Допустимы только целые числа',
      ],

    },
  }),
  methods: {
    async submit() {
      if (this.$refs.form.validate()) {
        this.loadingSave = true
        let str = "https://localhost:10000/api/v1/space-marines/" + this.spacemarineIndex
        let data = {
          name: this.name,
          coordinates: {x: this.coordinateX, y: this.coordinateY},
          health: this.health,
          height: this.height,
          category: this.category,
          weaponType: this.weaponType,
          chapter: {
            name: this.chapterName,
            parentLegion: this.parentLegion,
            marinesCount: this.marinesCount,
            world: this.world
          }
        }

        axios.put(str, data, {headers: this.getHeader()})
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

    getSpacemarineByID: function (id) {
      axios.get(`https://localhost:10000/api/v1/space-marines/` + id, null, {headers: this.getHeader()})
      .then(resp => {
        this.name = resp.data.name
        this.health = resp.data.health
        this.height = resp.data.height
        this.category = resp.data.category
        this.weaponType = resp.data.weaponType
        this.creationDate = resp.data.creationDate
        this.coordinateX = resp.data.coordinates.x
        this.coordinateY = resp.data.coordinates.y
        this.chapterName = resp.data.chapter.name
        this.marinesCount = resp.data.chapter.marinesCount
        this.parentLegion = resp.data.chapter.parentLegion
        this.world = resp.data.chapter.world

      })
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
      this.getSpacemarineByID(this.spacemarineIndex)
    },
  }
  ,
  beforeMount() {
    this.updateOverlay()
  }
}
</script>

<style scoped>

</style>