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
      <el-table :data="data"  @selection-change="selectChange" style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="服务商接口配置定义描述">
                <span>{{ props.row.isvParams }}</span>
              </el-form-item>
              <el-form-item label="特约商户接口配置定义描述">
                <span>{{ props.row.isvsubMchParams }}</span>
              </el-form-item>
              <el-form-item label="普通商户接口配置定义描述">
                <span>{{ props.row.normalMchParams }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column prop="id" type="selection" width="55">
        </el-table-column>
        <el-table-column prop="icon" label="产品图片" width="100">
          <template slot-scope="scope">
            <el-image
              style="width: 100px; height: 50px"
              :src="scope.row.icon"
              :preview-src-list="scope.row.imgPaths"
            >
            </el-image>
          </template>
        </el-table-column>
        <el-table-column label="接口代码" prop="ifCode"> </el-table-column>
        <el-table-column label="接口名称" prop="ifName"> </el-table-column>
        <el-table-column label="操作" fixed="right" width="250">
          <template slot-scope="scope">
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
      width="800px"
      :visible.sync="formVisible"
      @close="resetForm('editForm')"
    >
      <el-form
        label-width="200px"
        :model="editForm"
        :rules="rules"
        ref="editForm"
      >
        <el-form-item label="接口代码" prop="ifCode">
          <el-input
            size="small"
            v-model="editForm.ifCode"
            auto-complete="off"
            placeholder="请输入"
          ></el-input>
        </el-form-item>
        <el-form-item label="接口名称" prop="ifName">
          <el-input
            size="small"
            v-model="editForm.ifName"
            auto-complete="off"
            placeholder="请输入"
          ></el-input>
        </el-form-item>
        <el-form-item label="是否支持商户模式" prop="isMchMode">
          <el-radio-group v-model="editForm.isMchMode">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否支持服务商子商户模式" prop="isIsvMode">
          <el-radio-group v-model="editForm.isIsvMode">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="editForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="服务商接口配置定义描述" prop="isvParams">
          <el-input
            size="small"
            type="textarea"
            v-model="editForm.isvParams"
            auto-complete="off"
            placeholder="请输入"
          ></el-input>
        </el-form-item>
        <el-form-item label="支持的支付方式" prop="wayCodes">
          <el-checkbox
            :indeterminate="isIndeterminate"
            v-model="checkAll"
            @change="handleCheckAllChange"
            >全选</el-checkbox
          >
          <div style="margin: 15px 0"></div>
          <el-checkbox-group
            v-model="checkedCode"
            @change="handlecheckedCodeChange"
          >
            <el-checkbox
              v-for="code in codes"
              :label="code.wayCode"
              :key="code.wayCode"
            >
              {{ code.wayName }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="特约商户接口配置定义描述" prop="isvsubMchParams">
          <el-input
            size="small"
            type="textarea"
            v-model="editForm.isvsubMchParams"
            auto-complete="off"
            placeholder="请输入"
          ></el-input>
        </el-form-item>
        <el-form-item label="普通商户接口配置定义描述" prop="normalMchParams">
          <el-input
            size="small"
            type="textarea"
            v-model="editForm.normalMchParams"
            auto-complete="off"
            placeholder="请输入产品单位"
          ></el-input>
        </el-form-item>
        <el-form-item label="图片上传">
          <el-upload
            ref="my-upload"
            class="upload-demo"
            drag
            action="#"
            :http-request="pictureUpload"
            multiple
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <div class="el-upload__tip" slot="tip">
              只能上传jpg/png文件，且不超过500kb
            </div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="formVisible = false">取 消</el-button>
        <el-button
          v-if="!viewMode"
          type="primary"
          @click="submiteditForm('editForm')"
          >确 定</el-button
        >
      </div>
    </el-dialog>
  </basic-container>
</template>
<script>
import {
  getList,
  getWayCodeDetail,
  picUpload,
  getDetail,
  submit,
  remove,
} from "@/api/payconfig/config";
export default {
  data() {
    return {
      checkAll: false,
      checkedCode: [],
      codes: [],
      isIndeterminate: true,
      // 是否显示编辑框
      formVisible: false,
      direction: "rtl",
      // 是否查看
      viewMode: false,
      // dialog标题
      dialogTitle: "",
      // 列表数据
      data: [], 
      // 选中的数据
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
      editForm: {
        ifCode: "",
        ifName: "",
        isvParams: "",
        isvsubMchParams: "",
        normalMchParams: "",
        icon: "",
        isMchMode: "",
        isIsvMode: "",
        status: "",
        checkedCodes: [],
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
    handleCheckAllChange(val) {
      console.log(this.codes);
      console.log(val);
      if (val) {
        this.codes.forEach((element) => {
          this.checkedCode.push(element.wayCode);
        });
      } else {
        this.checkedCode = [];
      }
      this.editForm.checkedCodes = this.checkedCode;
      // this.checkedCode = val ? this.codes : [];
      this.isIndeterminate = false;
    },
    handlecheckedCodeChange(value) {
      this.editForm.checkedCodes = value;
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.codes.length;
      this.isIndeterminate =
        checkedCount > 0 && checkedCount < this.codes.length;
    },
    pictureUpload(params) {
      const file = params.file;
      // 根据后台需求数据格式
      const form = new FormData();
      // 文件对象
      form.append("file", file);
      picUpload(form).then((res) => {
        console.log(res);
        if (res.data.success) {
          this.editForm.icon = res.data.data;
        }
      });
      // return "/api/upload/avatar?&token=" + localStorage.getItem("logintoken");
    },
    handleAdd() {
      this.dialogTitle = "新增支付方式";
      this.formVisible = true;
      //   this.viewMode = false;
      console.log("add");
      getWayCodeDetail().then((res) => {
        if (res.data.success) {
          this.codes = res.data.data;
        }
      });
    },
    //编辑提交
    onSubmit() {
      console.log(this.editForm);
      submit(this.editForm).then((res) => {
        if (res.data.success) {
          this.formVisible = false;
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
    resetForm(formName) {
      this.$refs[formName].clearValidate();
    },
    //编辑取消
    handleClose() {
      this.formVisible = false;
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
      console.log(val)
      this.multiSelection = val;
      console.log(this.multiSelection);
    },
    currentChange(currentPage) {
      this.page.currentPage = currentPage;
    },
    sizeChange(pageSize) {
      this.page.pageSize = pageSize;
    },

    handleView(index, row) {
      this.dialogTitle = "查看博客";
      this.formVisible = true;
      this.viewMode = true;
      getDetail(row.id).then((res) => {
        if (res.data.success) {
          this.editForm = res.data.data;
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
      console.log(row.id);
      getWayCodeDetail().then((res) => {
        if (res.data.success) {
          this.codes = res.data.data;
        }
      });
      getDetail(row.id).then((res) => {
        if (res.data.success) {
          this.editForm = res.data.data;
          this.checkedCode =this.editForm.checkedCodes;
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
    submiteditForm(formName) {
      // 表单验证
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log(this.editForm);
          submit(this.editForm).then((res) => {
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
<style>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 200px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 100%;
}
</style>