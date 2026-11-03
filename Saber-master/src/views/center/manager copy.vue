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
          prop="merchantName"
          label="商户名称"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="merchantNo"
          label="商户号"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="serverNo"
          label="服务商号"
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
              :type="scope.row.status === 1 ? 'primary' : 'success'"
              disable-transitions
              >{{ scope.row.status == 1 ? "启用" : "禁用" }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column
          prop="type"
          label="标签"
          width="100"
          :filters="[
            { text: '普通商户', value: 1 },
            { text: '特约商户', value: 2 },
          ]"
          :filter-method="filterTag"
          filter-placement="bottom-end"
        >
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.type === 1 ? 'primary' : 'success'"
              disable-transitions
              >{{ scope.row.type ==1?"普通商户":"特约商户" }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建日期" width="180"></el-table-column>
          <el-table-column prop="updateTime" label="更新日期" width="180">
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="250">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type=""
              plain
              @click="handleView(scope.$index, scope.row)"
              >查看
            </el-button>
            <el-button
              size="mini"
              type="primary"
              plain
              @click="handleEdit(scope.$index, scope.row)"
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
      <el-form ref="form" :model="merchant" label-width="80px">
        <el-form-item label="商户名称">
          <el-input v-model="merchant.merchantName"></el-input>
        </el-form-item>
        <el-form-item label="登录名">
          <el-input v-model="merchant.account"></el-input>
        </el-form-item>
        <el-form-item label="商户简称">
          <el-input v-model="merchant.merchantShortName"></el-input>
        </el-form-item>
        <el-form-item label="联系人姓名">
          <el-input v-model="merchant.contactName"></el-input>
        </el-form-item>
        <el-form-item label="联系人邮箱">
          <el-input v-model="merchant.contactEmail"></el-input>
        </el-form-item>
        <el-form-item label="联系人手机号">
          <el-input v-model="merchant.contactPhone"></el-input>
        </el-form-item>
        <el-form-item label="商户类型">
          <el-radio-group v-model="merchant.type">
            <el-radio label="1">普通商户</el-radio>
            <el-radio label="2">特约商户</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="merchant.status" ></el-switch>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="merchant.remark"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">立即创建</el-button>
          <el-button @click="handleClose">取消</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>
  </basic-container>
</template>
<script>
import { getList, getDetail, submit, remove } from "@/api/merchant/merchant";
export default {
  data() {
    return {
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
        merchantName: "",
        account:"",
        merchantShortName:"",
        contactName:'',
        contactEmail:'',
        contactPhone:'',
        // merchantNo: "",
        // serverNo: "",
        status: "",
        type: "",
        remark:'',
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
    handleAdd() {
      this.dialogTitle = "新增商户";
      this.drawer = true;
      //   this.viewMode = false;
      console.log("add");
    },
    //编辑提交
    onSubmit() {
      console.log(this.merchant)
      submit(this.merchant).then((res) => {
        if (res.data.success) {
          this.drawer = false;
          this.$message({
              type: "success",
              message: "操作成功！",
            });
        }else{
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
    },
    clearFilter() {
      this.$refs.filterTable.clearFilter();
    },
    filterState(value, row) {
      return row.status === value;
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
   
    handleView(index, row) {
      this.dialogTitle = "查看博客";
      this.formVisible = true;
      this.viewMode = true;
      getDetail(row.id).then((res) => {
        if (res.data.success) {
          this.merchant = res.data.data;
        }
      });
      console.log("view");
      console.log(index);
      console.log(row);
    },
    handleEdit(index, row) {
      this.dialogTitle = "修改商户";
      this.drawer = true;
      //   this.viewMode = false;
      getDetail(row.id).then((res) => {
        if (res.data.success) {
          this.merchant = res.data.data;
        }
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
    submitmerchant(formName) {
      // 表单验证
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log(this.merchant);
          submit(this.merchant).then((res) => {
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