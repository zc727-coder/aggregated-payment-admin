<template>
  <basic-container>
    <el-row>
      <el-col :span="24">
        <div class="tool-box">
          <el-button
            type="primary"
            icon="el-icon-circle-plus-outline"
            size="small"
            @click="handleAdd"
            >新增</el-button
          >
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="small"
            @click="handleMultiDelete"
            >批量删除</el-button
          >
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-table
        :data="data"
        @selection-change="selectChange"
        style="width: 100%"
      >
        <el-table-column prop="id" type="selection" width="55">
        </el-table-column>
        <el-table-column
          prop="appId"
          label="应用APPID"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="appName"
          label="应用名称"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="merchantNo"
          label="商户号"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          width="100"
          :filters="[
            { text: '启用', value: 1 },
            { text: '禁用', value: 0 },
          ]"
          :filter-method="filterState"
          filter-placement="bottom-end"
        >
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.status == 1 ? 'primary' : 'success'"
              disable-transitions
              >{{ scope.row.status == 1 ? "启用" : "禁用" }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建日期"
          width="180"
        ></el-table-column>
        <!-- <el-table-column prop="updateTime" label="更新日期" width="180">
        </el-table-column> -->
        <el-table-column label="操作" fixed="right" width="350">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="success"
              plain
              @click="payConfig(scope.$index, scope.row)"
              >支付渠道配置
            </el-button>
            <el-button
              size="mini"
              type="primary"
              plain
              @click="handleEndit(scope.$index, scope.row)"
              >编辑
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)"
              >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <el-row>
      <el-pagination
        background
        :page-sizes="[10, 20, 30, 50]"
        :page-size="10"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="sizeChange"
        @current-change="currentChange"
        :total="page.total"
      >
      </el-pagination>
    </el-row>
    <!-- 编辑操作 -->
    <el-drawer
      :title="dialogTitle"
      :visible.sync="drawer"
      :direction="direction"
      :before-close="handleClose"
      size="50%"
    >
      <el-divider></el-divider>
      <el-form ref="form" :model="merchantApp" label-width="80px">
        <el-form-item label="应用APPID">
          <el-input v-model="merchantApp.appId"></el-input>
        </el-form-item>
        <el-form-item label="商户号">
          <el-input v-model="merchantApp.merchantNo"></el-input>
        </el-form-item>
        <el-form-item label="应用名称">
          <el-input v-model="merchantApp.appName"></el-input>
        </el-form-item>
        <el-form-item label="应用私钥">
          <el-input v-model="merchantApp.appSecret"></el-input>
        </el-form-item>
        <!-- <el-form-item label="状态">
          <el-switch v-model="merchantApp.status"></el-switch>
        </el-form-item> -->
        <el-form-item label="状态">
          <el-radio-group v-model="merchantApp.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="merchantApp.remark"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">立即创建</el-button>
          <el-button @click="handleClose">取消</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>

    <el-dialog
      :title="payConfigTitle"
      width="800px"
      :visible.sync="payConfigDrawer"
      @close="resetForm('payWay')"
    >
      <el-row>
        <el-table align-center :data="paysData" style="width: 100%">
          <el-table-column prop="icon" label="产品图片" width="200">
            <template slot-scope="scope">
              <el-image
                style="width: 100px; height: 50px"
                :src="scope.row.icon"
                :preview-src-list="scope.row.imgPaths"
              >
              </el-image>
            </template>
          </el-table-column>
          <el-table-column label="支付名称" prop="ifName"> </el-table-column>
          <el-table-column label="接口名称" prop="ifCode"> </el-table-column>
          <el-table-column
            prop="ifConfigState"
            label="状态"
            width="100"
            :filters="[
              { text: '启用', value: 1 },
              { text: '禁用', value: 0 },
            ]"
            :filter-method="filterPayState"
            filter-placement="bottom-end"
          >
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.ifConfigState === 1 ? 'primary' : 'success'"
                disable-transitions
                >{{ scope.row.ifConfigState == 1 ? "启用" : "禁用" }}</el-tag
              >
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="150">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                plain
                @click="handleConfig(scope.$index, scope.row)"
                >支付配置参数
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
      <el-drawer
        size="60%"
        :append-to-body="true"
        :title="configTitle"
        :visible.sync="configDrawer"
      >
        <el-form ref="form" :model="infoForm" label-width="250px">
          <!-- <el-form-item label="状态">
            <el-switch v-model="infoForm.status"></el-switch>
          </el-form-item> -->
          <el-form-item label="状态">
            <el-radio-group v-model="infoForm.status">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="备注">
            <el-input type="textarea" v-model="infoForm.remark"></el-input>
          </el-form-item>
          <el-form-item label="环境配置">
            <el-radio-group v-model="infoForm.ifParams.sandbox">
              <el-radio :label="1">沙箱环境</el-radio>
              <el-radio :label="2">正式环境</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="应用App ID">
            <el-input v-model="infoForm.ifParams.appId"></el-input>
          </el-form-item>
          <el-form-item label="应用私钥">
            <el-input v-model="infoForm.ifParams.privateKey"></el-input>
          </el-form-item>
          <el-form-item label="支付宝公钥(不使用证书时必填)">
            <el-input v-model="infoForm.ifParams.alipayPublicKey"></el-input>
          </el-form-item>
          <el-form-item label="接口签名方式(推荐使用RSA2)">
            <el-input v-model="infoForm.ifParams.signType"></el-input>
          </el-form-item>
          <el-form-item label="接口签名方式(推荐使用RSA2)">
            <el-radio-group v-model="infoForm.ifParams.signType">
              <el-radio label="RSA">RSA</el-radio>
              <el-radio label="RSA2">RSA2</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="paySubmit">保存</el-button>
            <el-button @click="handleClose">取消</el-button>
          </el-form-item>
        </el-form>
      </el-drawer>
      <div slot="footer" class="dialog-footer">
        <el-button @click="payConfigDrawer = false">取 消</el-button>
        <el-button
          v-if="!viewMode"
          type="primary"
          @click="submitPayWay('payWay')"
          >确 定</el-button
        >
      </div>
    </el-dialog>
  </basic-container>
</template>
<script>
import {
  getPayConfigList,
  getList,
  getDetail,
  submit,
  getPpayConfigDetail,
  payOnSubmit,
  remove,
} from "@/api/merchantApp/merchantApp";
export default {
  data() {
    return {
      paysData: [],
      active: 0,
      // 表单映射模型
      payWay: {
        wayName: "",
        waycode: "",
      },
      // 是否显示编辑框
      drawer: false,
      payConfigDrawer: false,
      configDrawer: false,
      direction: "rtl",
      // 是否查看
      viewMode: false,
      // dialog标题
      dialogTitle: "",
      configTitle: "",
      payConfigTitle: "",
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
      merchantNo:"",
      infoForm: {
        merchantNo: "",
        infoId: "",
        ifCode: "",
        status: "",
        remark: "",
        ifParams: {
          sandbox: "",
          appId: "",
          privateKey: "",
          alipayPublicKey: "",
          signType: "",
          useCert: "", //费率，目前可以不用，实际开发可以配置使用
        },
      },
      // 表单映射模型
      merchantApp: {
        appId: "",
        merchantNo: "",
        appName: "",
        appSecret: "",
        status: "",
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
    // next() {
    //   if (this.active++ > 2) this.active = 0;
    // },
    handleAdd() {
      this.dialogTitle = "新增商户应用";
      this.drawer = true;
      //   this.viewMode = false;
      console.log("add");
    },

    handleEndit(index, row) {
      this.dialogTitle = "编辑";
      this.drawer = true;
      this.viewMode = true;
      getDetail(row.id).then((res) => {
        if (res.data.success) {
          this.merchantApp = res.data.data;
        }
      });
      console.log("view");
      console.log(index);
      console.log(row);
    },
    //编辑
    //编辑提交
    onSubmit() {
      console.log(this.merchantApp);
      submit(this.merchantApp).then((res) => {
        if (res.data.success) {
          this.drawer = false;
          this.$message({
            type: "success",
            message: "操作成功！",
          });
          this.onLoad();
        } else {
          this.$message({
            type: "error",
            message: res.data.msg,
          });
        }
      });
    },
    //编辑提交
    paySubmit() {
      this.infoForm.merchantNo = this.merchantNo;
      console.log(this.infoForm);

      payOnSubmit(this.infoForm).then((res) => {
        if (res.data.success) {
          this.configDrawer = false;
          this.$message({
            type: "success",
            message: "操作成功！",
          });
          this.onLoad();
        } else {
          this.$message({
            type: "error",
            message: res.data.msg,
          });
        }
      });
    },
    //编辑取消
    handleClose() {
      this.drawer = false;
      this.configDrawer = false;
    },
    clearFilter() {
      this.$refs.filterTable.clearFilter();
    },
    filterState(value, row) {
      return row.status === value;
    },
    filterPayState(value, row) {
      console.log(value);
      console.log(row);
      return row.ifConfigState === value;
    },
    filterTag(value, row) {
      return row.type === value;
    },
    onLoad() {
      getList().then((res) => {
        this.data = res.data.data.records;
        this.page.total = res.data.data.total;
      });
    },
    selectChange(val) {
      this.multiSelection = val;
      console.log(this.multiSelection);
    },
    currentChange(currentPage) {
      this.page.currentPage = currentPage;
    },
    sizeChange(pageSize) {
      this.page.pageSize = pageSize;
    },
    resetForm(formName) {
      this.$refs[formName].clearValidate();
    },

    handleConfig(index, row) {
      // this.next();
      // console.log(row);
      this.infoForm.ifCode = row.ifCode;
      console.log("==========");
      console.log(this.infoForm);
      console.log("==========");
      // console.log(this.infoForm.infoId)
      this.configTitle = "支付参数配置";
      this.configDrawer = true;
      //todo 差从后端查数据回来
      getPpayConfigDetail(this.infoForm.infoId, this.infoForm.ifCode).then(
        (res) => {
      
          if (res.data.success) {
           
            if (res.data.code != 201) {
              this.infoForm = res.data.data;
            }
          } else {
            console.log("fewfewfwef");
          }
        }
      );
      console.log(this.infoForm);
    },
    payConfig(index, row) {
      this.payConfigTitle = "支付配置";
      ``;
      this.payConfigDrawer = true;
      console.log("-=-=-=-=-=-=")
      console.log( row.merchantNo)
      console.log("-=-=-=-=-=-=")
      this.infoForm.infoId = row.appId;
      this.merchantNo = row.merchantNo;
      //   this.viewMode = false;
      getPayConfigList().then((res) => {
        this.paysData = res.data.data.records;
        // this.page.total = res.data.data.total;
      });
      console.log("edit");
      console.log(index);
      console.log(row);
    },

    handleDelete(index, row) {
      this.$confirm("确定将选择数据删除?", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        remove(row.id).then((res) => {
          if (res.data.success) {
            this.onLoad();
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
      });
      console.log("delete");
      console.log(index);
      console.log(row);
    },
    handleMultiDelete() {
      console.log("multi-delete");
      if (this.multiSelection.length === 0) {
        this.$message.warning("请选择至少一条数据");
        return;
      }
      this.$confirm("确定将选择数据删除?", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        remove(this.ids).then((res) => {
          if (res.data.success) {
            this.onLoad();
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
      });
    },
    submitmerchantApp(formName) {
      // 表单验证
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log(this.merchantApp);
          submit(this.merchantApp).then((res) => {
            if (res.data.success) {
              this.formVisible = false;
              this.$message({ type: "success", message: "操作成功！" });
            } else {
              this.$message({
                type: "error",
                message: res.data.msg,
              });
            }
          });
        }
      });
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