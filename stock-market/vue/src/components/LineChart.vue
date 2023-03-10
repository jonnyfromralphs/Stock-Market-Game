<template>
  <div class="test">
    <LineChartGenerator
      :chart-options="chartOptions"
      :chart-data="chartData"
      :chart-id="chartId"
      :dataset-id-key="datasetIdKey"
      :plugins="plugins"
      :css-classes="cssClasses"
      :styles="styles"
      :width="width"
      :height="height"
    />
  </div>
</template>

<script>
import zoom from "chartjs-plugin-zoom";
import { Line as LineChartGenerator } from "vue-chartjs/legacy";

import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  LinearScale,
  CategoryScale,
  PointElement,
} from "chart.js";

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  LineElement,
  LinearScale,
  CategoryScale,
  PointElement,
  zoom
);

export default {
  name: "LineChart",
  components: {
    LineChartGenerator,
  },
  props: {
    chartId: {
      type: String,
      default: "line-chart",
    },
    datasetIdKey: {
      type: String,
      default: "label",
    },
    width: {
      type: Number,
      default: 400,
    },
    height: {
      type: Number,
      default: 400,
    },
    cssClasses: {
      default: "",
      type: String,
    },
    styles: {
      type: Object,
      default: () => {},
    },
    plugins: {
      type: Array,
      default: () => [],
    },
    labels: {
      type: Array,
      default: () => [],
    },
    dataPoints: {
      type: Array,
      default: () => [],
    },
    graphLabel: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      chartData: {
        labels: this.labels,
        datasets: [
          {
            label: this.graphLabel,
            backgroundColor: "#3c4d74",
            data: this.dataPoints,
            borderColor: "#3c4d74",
          },
        ],
      },
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          x: {
            display: false,
          },
        },
        animation: false,
        plugins: {
          zoom: {
            zoom: {
              wheel: {
                enabled: true,
              },
              drag: {
                enabled: true,
              },
              mode: "xy",
            },
          },
        },
        elements: {
          point: {
            radius: 0, // default to disabled in all datasets
          },
        },
      },
    };
  },
};
</script>

<style scoped>
.test {
  background-color: #fff;
  flex-grow: 2;
  border-radius: 0.5rem;

  box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
}
</style>
