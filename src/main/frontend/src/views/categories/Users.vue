<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.key" placeholder="Username" style="width: 500px;padding-right:20px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="fas fa-search" @click="handleFilter">
        Search
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="fas fa-edit" @click="handleCreate">
        Add
      </el-button>
      <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="fas fa-download" @click="handleDownload">
        Export
      </el-button>
      <el-checkbox v-model="showDelete" class="filter-item" style="margin-left:15px;" @change="viewDelete()">
        View deleted
      </el-checkbox>
    </div>

    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="No." align="center" width="50" type="index" />
      <el-table-column label="Username" align="center" width="90">
        <template slot-scope="{row}">
          <span>{{ row.username }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Fullname" align="center" width="100">
        <template slot-scope="{row}">
          <span>{{ row.fullName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Email" align="center" width="130">
        <template slot-scope="{row}">
          <span>{{ row.email }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Address" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.address }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Phone" align="center" width="100">
        <template slot-scope="{row}">
          <span>{{ row.phoneNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Topic" align="center" width="200">
        <template slot-scope="{row}">
          <span>{{ row.topicName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Last Update" align="center" width="120">
        <template slot-scope="{row}">
          <span>{{ formatDate(row.updatedAt) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Status" align="center" width="70">
        <template slot-scope="{row}">
          <span v-if="row.status">Active</span>
          <span v-if="!row.status">Deactive</span>
        </template>
      </el-table-column>

      <el-table-column fixed="right" label="Actions" align="center" width="205" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button v-if="row.status" type="warning" size="mini">
            <router-link :to="{ name: 'farms', params: { owner: row.username }}" style="color:black">
              Farms
            </router-link>
          </el-button>
          <el-button v-if="row.status" type="primary" size="mini" @click="handleUpdate(row)">
            Edit
          </el-button>
          <el-popconfirm
            v-if="row.status"
            confirm-button-text="OK"
            cancel-button-text="No, Thanks"
            icon="fas fa-info-circle"
            icon-color="red"
            title="Are you sure to delete this?"
            @onConfirm="handleDelete(row,$index,0)"
          >
            <el-button slot="reference" size="mini" type="danger">
              Delete
            </el-button>
          </el-popconfirm>

          <el-popconfirm
            v-if="!row.status"
            confirm-button-text="OK"
            cancel-button-text="No, Thanks"
            icon="fas fa-info-circle"
            icon-color="red"
            title="Are you sure to restore this?"
            @onConfirm="handleDelete(row,$index,1)"
          >
            <el-button slot="reference" size="mini" type="danger">
              Restore
            </el-button>
          </el-popconfirm>

        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" center>
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="130px" style="width: 400px; margin-left:50px;">
        <el-form-item v-show="dialogStatus === 'update'" label="Id" prop="id">
          <el-input v-model="temp.id" :disabled="true" />
        </el-form-item>
        <el-form-item v-show="dialogStatus === 'update'" label="Username" prop="username">
          <el-input v-model="temp.username" :disabled="true" />
        </el-form-item>
        <el-form-item v-show="dialogStatus === 'create'" label="Username" prop="username">
          <el-input v-model="temp.username" />
        </el-form-item>
        <el-form-item v-show="dialogStatus === 'create'" label="Password" prop="password">
          <el-input v-model="temp.password" />
        </el-form-item>
        <el-form-item label="Fullname" prop="fullName">
          <el-input v-model="temp.fullName" />
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="temp.email" />
        </el-form-item>
        <el-form-item label="Address" prop="address">
          <el-input v-model="temp.address" />
        </el-form-item>
        <el-form-item label="Phone" prop="phoneNumber">
          <el-input v-model="temp.phoneNumber" />
        </el-form-item>
        <el-form-item v-show="dialogStatus === 'create'" label="Role" prop="roleType">
          <el-select v-model="temp.roleType" class="filter-item" placeholder="Please select">
            <el-option v-for="item in roleOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item v-show="dialogStatus === 'update'" label="Status" prop="status">
          <el-select v-model="temp.status" class="filter-item" placeholder="Please select">
            <el-option v-for="item in deleteOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          Cancel
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          Confirm
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getUsers, updateUser, createUser, deleteUser } from '@/api/user'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

const deleteOptions = [
  { key: 1, display_name: 'Active' },
  { key: 0, display_name: 'Deactive' }
]

const roleOptions = [
  { key: 1, display_name: 'Customer' },
  { key: 2, display_name: 'Admin' }
]

export default {
  name: 'Users',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      dialogConfirmDeleteVisible: false,
      dialogConfirmRestoreVisible: false,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        status: 1,
        page: 1,
        size: 10,
        key: ''
      },
      deleteOptions,
      roleOptions,
      showDelete: false,
      temp: {
        id: undefined,
        username: '',
        fullName: '',
        email: '',
        address: '',
        phoneNumber: '',
        password: '',
        status: 1,
        updatedAt: undefined,
        roleType: 1
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      downloadLoading: false,
      rules: {
        password: [
          { required: true, message: 'Password is required', trigger: 'blur' }
        ],
        username: [
          { required: true, message: 'Username is required', trigger: 'blur' }
        ],
        fullName: [
          { required: true, message: 'Fullname is required', trigger: 'blur' }
        ],
        email: [
          { required: true, message: 'Email is required', trigger: 'blur' }
        ],
        phoneNumber: [
          { required: true, message: 'Phone is required', trigger: 'blur' }
        ],
        address: [
          { required: true, message: 'Address is required', trigger: 'blur' }
        ],
        status: [
          { required: true, message: 'Status is required', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    formatDate(d) {
      d = new Date(d)
      const ye = new Intl.DateTimeFormat('en', { year: 'numeric' }).format(d)
      const mo = new Intl.DateTimeFormat('en', { month: '2-digit' }).format(d)
      const da = new Intl.DateTimeFormat('en', { day: '2-digit' }).format(d)
      return ye + '-' + mo + '-' + da
    },
    getList() {
      this.listLoading = true
      getUsers(this.listQuery).then(response => {
        this.list = response.data.items
        this.total = response.data.total
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 0 * 1000)
      })
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        username: '',
        fullName: '',
        email: '',
        address: '',
        phoneNumber: '',
        password: '',
        status: 1,
        updatedAt: undefined
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.temp.status = 1
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createUser(this.temp).then(() => {
            this.list.unshift(this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Created Successfully',
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        } else {
          console.log('Not valid')
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateUser(tempData).then(() => {
            const index = this.list.findIndex(v => v.id === this.temp.id)
            this.temp.updatedAt = new Date()
            this.list.splice(index, 1, this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Update Successfully',
              type: 'success',
              duration: 2000
            })
          })
        } else {
          console.log('Not valid')
        }
      })
    },
    handleDelete(row, index, status) {
      deleteUser(row.id, status).then(() => {
        if (!status) {
          this.$notify({
            title: 'Success',
            message: 'Delete Successfully',
            type: 'success',
            duration: 2000
          })
        } else {
          this.$notify({
            title: 'Success',
            message: 'Restore Successfully',
            type: 'success',
            duration: 2000
          })
        }

        this.list.splice(index, 1)
      })
      // }
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['ID', 'Username', 'Fullname', 'Email', 'Address', 'Phone Number', 'Topic Name', 'Status', 'Created At', 'Last Update']
        const filterVal = ['id', 'username', 'fullName', 'email', 'address', 'phoneNumber', 'topicName', 'status', 'createdAt', 'updatedAt']
        const data = this.formatJson(filterVal)
        console.log(data)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'users'
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        if (j === 'createdAt' || j === 'updatedAt') {
          return this.formatDate(v[j])
        } else {
          return v[j]
        }
      }))
    },
    viewDelete: function() {
      this.listQuery.status = this.showDelete ? 0 : 1
      this.getList()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    }
  }
}
</script>
