<template>
  <div>
    <span style="color:red;display:inline-block;margin-top:10px;margin-bottom:10px">(*) Note: Default password is: <b>{{ this.defaultPass }}</b></span>
    <hr>
    <el-table
      v-loading="listLoading"
      :data="tableData"
      style="width: 100%"
    >
      <el-table-column
        label="No."
        width="80"
      >
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.$index+1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="Username"
        width="180"
      >
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.row.username }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="Operations"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="danger"
            @click="handleReset(scope.row)"
          >Reset Pass</el-button>
          <el-button
            v-if="scope.row.status == 1"
            size="mini"
            type="danger"
            @click="handleDelete(scope.row, 0)"
          >Deactive</el-button>
          <el-button
            v-if="scope.row.status == 0"
            size="mini"
            type="primary"
            @click="handleDelete(scope.row, 1)"
          >Active</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

import { getUsers, resetPass, deleteUser } from '@/api/user'

export default {
  data() {
    return {
      tableData: null,
      listLoading: true,
      defaultPass: ''
    }
  },
  created() {
    this.getList(),
    this.generatePassword()
  },
  methods: {
    getList() {
      this.listLoading = true
      getUsers({ status: 1 }).then(response => {
        this.tableData = response.data.users
        setTimeout(() => {
          this.listLoading = false
        }, 0 * 1000)
      })
    },
    handleReset(row) {
      resetPass(row.username, this.defaultPass).then(response => {
        this.$notify({
          title: 'Success',
          message: 'Reset password successfully !',
          type: 'success',
          duration: 2000
        })
        this.generatePassword()
      })
    },
    handleDelete(row, deleted) {
      deleteUser(row.username, deleted).then(response => {
        this.$notify({
          title: 'Success',
          message: 'Ssuccessfully !',
          type: 'success',
          duration: 2000
        })
      })
    },
    generatePassword() {
      var length = 8
      var charset = '0123456789'
      var retVal = ''
      for (var i = 0, n = charset.length; i < length; ++i) {
        retVal += charset.charAt(Math.floor(Math.random() * n))
      }
      this.defaultPass = retVal
    }
  }

}
</script>
