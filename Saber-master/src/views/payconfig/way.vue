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
          prop="wayCode"
          label="支付方式代码"
          width="300"
        ></el-table-column>
        <el-table-column
          prop="wayName"
          label="支付方式名称"
          width="300"
        ></el-table-column>
        <el-table-column prop="createTime" label="创建日期" width="180"></el-table-column>
          <!-- <el-table-column prop="updateTime" label="更新日期" width="180">
        </el-table-column> -->
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
    
    <el-dialog
      :title="dialogTitle"
      width="600px"
      :visible.sync="formVisible"
      @close="resetForm('payWay')"
    >
      <el-form :model="payWay" :rules="rules" ref="payWay">
        <el-form-item label="支付方式名称" prop="wayName" label-width="100px">
          <el-input v-model="payWay.wayName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="支付方式代码" prop="wayCode" label-width="100px">
          <el-input v-model="payWay.wayCode" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="formVisible = false">取 消</el-button>
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
import { getList, getDetail, submit, remove } from "@/api/payconfig/way";
export default {
  data() {
    return {
      // 是否显示编辑框
      formVisible: false,
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
      payWay: {
        wayName: "",
        waycode: "",
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
      this.dialogTitle = "新增支付方式";
      this.formVisible = true;
      //   this.viewMode = false;
      console.log("add");
    },
    //编辑提交
    onSubmit() {
      console.log(this.payWay)
      submit(this.payWay).then((res) => {
        if (res.data.success) {
          this.formVisible = false;
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
    resetForm(formName) {
      console.log(formName)
      this.$refs[formName].clearValidate();
      this.$refs[formName].resetFields();
    },
    //编辑取消
    handleClose() {
      this.formVisible = false;
      this.$refs[formName].clearValidate();
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
    // resetForm(formName) {
    //   this.$refs[formName].clearValidate();
    // },
   
    handleView(index, row) {
      this.dialogTitle = "查看博客";
      this.formVisible = true;
      this.viewMode = true;
      getDetail(row.id).then((res) => {
        if (res.data.success) {
          this.payWay = res.data.data;
        }
      });
      console.log("view");
      console.log(index);
      console.log(row);
    },
    handleEdit(index, row) {
      this.dialogTitle = "修改商户";
      this.formVisible = true;
      //   this.viewMode = false;
      getDetail(row.id).then((res) => {
        if (res.data.success) {
          this.payWay = res.data.data;
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
    submitPayWay(formName) {
      // 表单验证
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log(this.payWay);
          submit(this.payWay).then((res) => {
            if (res.data.success) {
              this.formVisible = false;
              this.onLoad();
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
/deep/.el-formVisible__header {
  color: #060404;
  font-size: 32px;
}
</style>