<template>
  <el-card>
    <h1 class="me-author-name">酸菜鱼</h1>
    <div class="me-author-description">
      <span><i class="el-icon-location-outline"></i> &nbsp;江西&南昌</span>
      <span><i class="me-icon-job"></i> &nbsp;java开发</span>
    </div>
    <div class="me-author-tool">
      <i @click="showTool(qq)" :title="qq.title" class="iconfont icon-111QQ"></i>
      <i @click="showTool(gitee)" :title="gitee.title" class="iconfont icon-111gitee"></i>
      <i @click="showTool(github)" :title="github.title" class="iconfont icon-111github-fill"></i>
    </div>
  </el-card>

</template>

<script>
  export default {
    name: 'CardMe',
    data() {
      return {
        qq: {title: 'QQ', message: '2734668575'},
        gitee: {
          title: 'gitee',
          message:'<a target="_blank" href="https://gitee.com/dashuizhi">https://gitee.com/dashuizhi</a>'
        },
        github: {
          title: 'github',
          message: '<a target="_blank" href="https://github.com/keeip">https://github.com/keeip</a>'
        }
      }
    },
    methods: {
      showTool(tool) {
        this.$message({
          duration: 0,
          showClose: true,
          dangerouslyUseHTMLString: true,
          message: '<strong>' + tool.message + '</strong>'
        });
      }
    }
  }
</script>

<style scoped>
  .me-author-name {
    text-align: center;
    font-size: 30px;
    border-bottom: 1px solid #5FB878;
  }

  .me-author-description {
    padding: 8px 0;
  }

  .me-icon-job {
    padding-left: 16px;
  }

  .me-author-tool {
    text-align: center;
    padding-top: 10px;
  }

  .me-author-tool i {
    cursor: pointer;
    padding: 4px 10px;
    font-size: 30px;
  }
</style>
