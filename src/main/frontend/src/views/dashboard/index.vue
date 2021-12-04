<template>
  <div class="dashboard-container">
    <component :is="currentRole" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  data() {
  },
  computed: {
  },
  created() {
    this.$router.push('/components/users')
  }
}
</script>
