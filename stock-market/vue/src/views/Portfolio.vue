<template>
  <!-- <Bar id="my-chart-id" :options="chartOptions" :data="chartData" /> -->
  <line-chart></line-chart>
</template>

<script>
// import { Bar } from 'vue-chartjs'
// import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js'

// ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)
import LineChart from "../components/LineChart.vue";

export default {
  name: "portfolio",
  components: { LineChart },
  data() {
    return {
      // chartData: {
      //   labels: ['January', 'February', 'March'],
      //   datasets: [{ data: [40, 20, 12] }]
      // },
      // chartOptions: {
      //   responsive: true
      // }
    };
  },
};
</script>

<style>
</style>