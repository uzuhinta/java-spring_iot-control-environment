<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.key" placeholder="Crop name" style="width: 500px;padding-right:20px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="fas fa-search" @click="handleFilter">
        Search
      </el-button>
      <!-- <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="fas fa-download" @click="handleDownload">
        Export
      </el-button> -->
      <el-checkbox v-model="showDelete" class="filter-item" style="margin-left:15px;" @change="viewDelete()">
        View end
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
      <el-table-column label="Crop Name" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.crop.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Customer" align="center" width="140">
        <template slot-scope="{row}">
          <span>{{ row.farm.user.username }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Device Name" align="center" width="120">
        <template slot-scope="{row}">
          <span>{{ row.deviceName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Plant" align="center" width="110">
        <template slot-scope="{row}">
          <span>{{ row.crop.plant.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Start Date" align="center" width="120">
        <template slot-scope="{row}">
          <span>{{ formatDate(row.crop.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="End Date" align="center" width="120">
        <template slot-scope="{row}">
          <span>{{ formatDate(row.crop.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Payment Type" align="center" width="120">
        <template slot-scope="{row}">
          <span v-if="row.crop.paymentType">Per Day</span>
          <span v-if="!row.crop.paymentType">Per Crop</span>
        </template>
      </el-table-column>
      <el-table-column label="Description" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.crop.description }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Last Update" align="center" width="120">
        <template slot-scope="{row}">
          <span>{{ formatDate(row.crop.updatedAt) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Status" align="center" width="90">
        <template slot-scope="{row}">
          <span v-if="row.crop.status">Running</span>
          <span v-if="!row.crop.status">End</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
  </div>
</template>

<script>
import { getCrops } from '@/api/crop'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

const deleteOptions = [
  { key: 1, display_name: 'No Paid' },
  { key: 0, display_name: 'Paid' }
]

export default {
  name: 'Invoices',
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
        updatedAt: undefined
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
    formatMoney(m) {
      return m.toLocaleString('it-IT', { style: 'currency', currency: 'VND' })
    },
    formatDate(d) {
      d = new Date(d)
      const ye = new Intl.DateTimeFormat('en', { year: 'numeric' }).format(d)
      const mo = new Intl.DateTimeFormat('en', { month: '2-digit' }).format(d)
      const da = new Intl.DateTimeFormat('en', { day: '2-digit' }).format(d)
      return ye + '-' + mo + '-' + da
    },
    formatDate(d) {
      d = new Date(d)
      const ye = new Intl.DateTimeFormat('en', { year: 'numeric' }).format(d)
      const mo = new Intl.DateTimeFormat('en', { month: '2-digit' }).format(d)
      const da = new Intl.DateTimeFormat('en', { day: '2-digit' }).format(d)
      return ye + '-' + mo + '-' + da
    },
    getList() {
      this.listLoading = true
      getCrops(this.listQuery).then(response => {
        console.log(response)
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
