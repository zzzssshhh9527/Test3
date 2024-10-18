<template>
  <!--  主界面-->
  <p>个人通讯录系统</p>
  <div class="mb-4">
    <el-button type="primary" @click="openAddUserDialog">添加</el-button>
  </div>
  <el-table :data="tableData" style="width: 100%">
    <el-table-column prop="name" label="姓名" width="200"/>
    <el-table-column prop="address" label="地址" width="200"/>
    <el-table-column prop="phone" label="电话"/>
    <el-table-column fixed="right" label="操作" min-width="120">
      <template #default="scope">
        <el-button link type="primary" size="large" @click="removeUser(scope.row.id)">删除</el-button>
        <el-button link type="primary" size="large" @click="openUpdateUserDialog(scope.row)">修改</el-button>
      </template>
    </el-table-column>
  </el-table>

  <!--  新增模态框-->
  <el-dialog v-model="isOpenAddDialog" title="添加联系人" width="500">
    <el-form :model="addUserInfo">
      <el-form-item label="姓名" :label-width="formLabelWidth">
        <el-input v-model="addUserInfo.name" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="地址" :label-width="formLabelWidth">
        <el-input v-model="addUserInfo.address" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="电话" :label-width="formLabelWidth">
        <el-input v-model="addUserInfo.phone" autocomplete="off"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="isOpenAddDialog = false">取消</el-button>
        <el-button type="primary" @click="addUser">确认</el-button>
      </div>
    </template>
  </el-dialog>

  <!--  修改模态框-->
  <el-dialog v-model="isOpenUpdateDialog" title="修改联系人信息" width="500">
    <el-form :model="updateUserInfo">
      <el-form-item label="姓名" :label-width="formLabelWidth">
        <el-input v-model="updateUserInfo.name" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="地址" :label-width="formLabelWidth">
        <el-input v-model="updateUserInfo.address" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="电话" :label-width="formLabelWidth">
        <el-input v-model="updateUserInfo.phone" autocomplete="off"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="isOpenUpdateDialog = false">取消</el-button>
        <el-button type="primary" @click="updateUser">确认</el-button>
      </div>
    </template>
  </el-dialog>

</template>

<script>

import axios from "axios";

export default {
  name: 'App',
  data() {
    return {
      tableData: [],
      isOpenAddDialog: false,
      isOpenUpdateDialog: false,
      addUserInfo: {
        id: undefined,
        name: undefined,
        address: undefined,
        phone: undefined
      },
      updateUserInfo: {
        id: undefined,
        name: undefined,
        address: undefined,
        phone: undefined
      }
    }
  },
  methods: {
    openAddUserDialog() {
      const _self = this
      _self.addUserInfo.id = _self.tableData.length + 1
      _self.isOpenAddDialog = true
    },
    openUpdateUserDialog(user) {
      const _self = this
      _self.isOpenUpdateDialog = true
      _self.updateUserInfo.id = user.id
      _self.updateUserInfo.name = user.name
      _self.updateUserInfo.address = user.address
      _self.updateUserInfo.phone = user.phone
    },
    addUser() {
      const _self = this
      _self.axios.post("/api/user/", _self.addUserInfo).then(function () {
        _self.isOpenAddDialog = false
        _self.query()
      })
    },
    updateUser() {
      const _self = this
      _self.axios.put("/api/user/", _self.updateUserInfo).then(function () {
        _self.isOpenUpdateDialog = false
        _self.query()
      })
    },

    removeUser(userid) {
      console.log(userid);
      const _self = this;
      _self.$confirm('此操作将永久删除该联系人, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function () {
        // 用户点击确定，执行删除操作
        return _self.axios.delete("/api/user/" + userid);
      }).then(function (resp) {
        // 删除成功，重新加载数据
        console.log(resp);
        _self.query();
      }).catch(function () {
        // 用户点击取消或关闭对话框，不执行任何操作
        console.log('用户取消了删除操作');
      });
    },
    query() {
      const _self = this
      _self.axios = axios;
      _self.axios.get("/api/user/", {}).then(function (resp) {
        console.log(resp)
        _self.tableData = resp.data
      })
    }
  },
  mounted() {
    this.query()
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
