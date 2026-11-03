<template>
  <basic-container>
    <el-row>
      <el-col :span="24">
        <div class="tool-box">
          <el-select
            v-model="formLabelAlign.appId"
            @change="selectChange($event)"
            placeholder="请选择具体应用"
          >
            <el-option
              v-for="item in options"
              :key="item.appId"
              :label="item.appName"
              :value="item.appId"
            >
            </el-option>
          </el-select>
        </div>
      </el-col>
    </el-row>
    <el-divider></el-divider>
    <span>支付操作：</span>
    <template>
      <el-radio v-model="radio" label="AUTO_BAR">聚合主扫</el-radio>
      <el-radio v-model="radio" label="2">聚合被扫</el-radio>
    </template>
    <el-divider></el-divider>

    <el-form
      :label-position="labelPosition"
      label-width="80px"
      :model="formLabelAlign"
    >
      <span>支付信息：</span>
      <el-divider></el-divider>
      <el-form-item label="订单编号">
        {{ formLabelAlign.mchOrderNo }}
        <el-button @click="flashMchOrderNo" type="primary">刷新订单</el-button>
      </el-form-item>
      <el-form-item label="订单标题">
        <el-input v-model="formLabelAlign.orderTitle"></el-input>
      </el-form-item>
      <el-form-item label="支付标题">
        <el-input v-model="formLabelAlign.subject"></el-input>
      </el-form-item>
      <el-form-item label="支付信息">
        <el-input v-model="formLabelAlign.body"></el-input>
      </el-form-item>
      <el-form-item label="订单金额">
        <el-input v-model="formLabelAlign.amount"></el-input>
      </el-form-item>
      <el-form-item label="条形码">
        <el-input v-model="formLabelAlign.authCode"></el-input>
      </el-form-item>
      <el-button
        type="primary"
        icon="el-icon-circle-plus-outline"
        size="small"
        @click="handleAdd"
        >发起支付</el-button
      >
    </el-form>
  </basic-container>
</template>
<script>
import { getList,submit } from "@/api/pay/pay";
export default {
  data() {
    return {
      formLabelAlign: {
        mchOrderNo: "123",
        authCode: "",
        amount: "",
        appId: "",
        orderTitle: "",
        subject: "",
        body: "",
        mchNo: "M1686040353",
      },
      radio: "AUTO_BAR",
      option:{

      },
      options: [
        {
          appId: "1",
          appName: "黄金糕",
        },
        {
          appId: "2",
          appName: "双皮奶",
        },
      ],
      value: "",
      // 是否显示编辑框
      drawer: false,
      direction: "rtl",
      // 是否查看
      viewMode: false,
      // dialog标题
      dialogTitle: "",
      // 列表数据
      data: [], // 选中的数据
      multiSelection: [],
      // 分页数据
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0,
      },
      // 校验规则
      rules: {
        title: [{ required: true, message: "请输入标题", trigger: "blur" }],
      },
      // 表单映射模型
      merchant: {
        id: "",
        wayCode:"",
        merchantName: "",
        account: "",
        merchantShortName: "",
        contactName: "",
        contactEmail: "",
        contactPhone: "",
        // merchantNo: "",
        // serverNo: "",
        status: "",
        type: "",
        remark: "",
        // time: "",
      },
    };
  },
  created() {
    this.onLoad();
  },
  computed: {
    ids() {
      let ids = [];
      this.multiSelection.forEach((ele) => {
        ids.push(ele.id);
      });
      return ids.join(",");
    },
  },
  methods: {
    flashMchOrderNo() {
      let nowDate = parseInt(new Date().getTime() / 1000) + "";
      this.formLabelAlign.mchOrderNo = this.formLabelAlign.mchNo + nowDate;
    },
    handleAdd() {
      // this.formLabelAlign.appId=this.option.appId;
      this.formLabelAlign.mchNo=this.option.merchantNo;
      console.log(this.radio)
      this.formLabelAlign.wayCode=this.radio;
      submit(this.formLabelAlign).then((res) => {
        if (res.data.success) {
          this.drawer = false;
          this.$message({
            type: "success",
            message: "操作成功！",
          });
        } else {
          this.$message({
            type: "error",
            message: res.data.msg,
          });
        }
      });



    },
   
    onLoad() {
      getList().then((res) => {
        console.log(res);
        this.options = res.data.data;
        //this.data = res.data.data.records;
        //this.page.total = res.data.data.total;
      });
    },
    selectChange(val) {
      // console.log("================");
      // console.log("================");
      console.log(val);
      for (let index = 0; index < this.options.length; index++) {
        const element = this.options[index];
        if(val === element.appId){
          this.option = element;
        }
      }
      console.log("-=--=-=-=-")
      console.log(this.option)
    },
  },
};
</script>
<style lang="scss" scoped>
.el-pagination {
  margin-top: 20px;
}
// 编辑
/deep/.el-drawer__header {
  color: #060404;
  font-size: 32px;
}
</style>