import App from './App.vue';
import plugins from './plugins';
import store from './store';
import uviewPlus from 'uview-plus';
import manifest from '@/manifest.json';

import { createSSRApp } from 'vue';
import directive from './directive'; // directive

import { useDict } from '@/utils/dict';
import { parseTime, resetForm, addDateRange, handleTree, selectDictLabel, selectDictLabels,previewPictures,debounce } from '@/utils/ruoyi';
import TabBar from '@/components/tabbar/index.vue'
import NavBar from '@/components/navbar/index.vue'

export function createApp() {
  const app = createSSRApp(App);

  // 使用插件
  app.use(store);
  app.use(uviewPlus);
  app.use(plugins);
  directive(app);

  // 全局方法挂载
  app.config.globalProperties.$getVersion = function () {
    return manifest.versionName;
  };
  app.config.globalProperties.useDict = useDict;
  app.config.globalProperties.parseTime = parseTime;
  app.config.globalProperties.previewPictures = previewPictures;
  app.config.globalProperties.debounce = debounce;
  app.config.globalProperties.resetForm = resetForm;
  app.config.globalProperties.handleTree = handleTree;
  app.config.globalProperties.addDateRange = addDateRange;
  app.config.globalProperties.selectDictLabel = selectDictLabel;
  app.config.globalProperties.selectDictLabels = selectDictLabels;
  app.component('TabBar', TabBar)
  app.component('NavBar', NavBar)
  return {
    app,
  };
}
