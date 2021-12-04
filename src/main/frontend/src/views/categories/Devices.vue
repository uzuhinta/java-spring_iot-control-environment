<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.key" placeholder="Owner" style="width: 500px;padding-right:20px;" class="filter-item" @keyup.enter.native="handleFilter" />
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
      <el-table-column label="Farm" align="center" width="130">
        <template slot-scope="{row}">
          <span>{{ row.farm.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Device Name" align="center" width="120">
        <template slot-scope="{row}">
          <span>{{ row.deviceName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Topic" align="center" width="180">
        <template slot-scope="{row}">
          <span>{{ row.topicName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Description" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.description }}</span>
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

      <el-table-column fixed="right" label="Actions" align="center" width="220" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button v-if="row.status" type="warning" size="mini" @click="download(row)">
            Properties
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
        <el-form-item v-show="dialogStatus === 'update'" label="Farm" prop="farm">
          <el-input v-model="temp.farm.name" :disabled="true" />
        </el-form-item>
        <el-form-item v-show="dialogStatus === 'create'" label="Farm" prop="farm">
          <el-autocomplete
            v-model="temp.farm.name"
            clearable
            :fetch-suggestions="querySearchAsync"
            placeholder="Please input farm's name"
            @select="handleSelect"
          />
        </el-form-item>
        <el-form-item label="Device Name" prop="deviceName">
          <el-input v-model="temp.deviceName" />
        </el-form-item>
        <el-form-item label="Description" prop="description">
          <el-input v-model="temp.description" />
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
import { getDevices, updateDevices, createDevices, deleteDevices, getProperties } from '@/api/device'
import { getFarmsByName } from '@/api/farm'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

const deleteOptions = [
  { key: 1, display_name: 'Active' },
  { key: 0, display_name: 'Deactive' }
]

export default {
  name: 'Device',
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
        farm: { name: '' },
        deviceName: '',
        topicName: '',
        description: '',
        status: 1
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      downloadLoading: false,
      rules: {
        farm: [
          { required: true, message: 'Farm is required', trigger: 'blur' }
        ],
        deviceName: [
          { required: true, message: 'DeviceName is required', trigger: 'blur' }
        ],
        description: [
          { required: true, message: 'Status is required', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    if (this.$route.params.owner) {
      this.listQuery.key = '"' + this.$route.params.owner + '"'
    }
    this.getList()
  },
  methods: {
    querySearchAsync(queryString, cb) {
      const query = {
        status: 1,
        page: 1,
        size: 10,
        key: queryString
      }
      let results = []
      let listAreas = []
      getFarmsByName(query).then(response => {
        listAreas = response.data.items
        results = listAreas.map(s => {
          return {
            value: s.name,
            key: s
          }
        })
        clearTimeout(this.timeout)
        this.timeout = setTimeout(() => {
          cb(results)
        }, 100)
      })
    },
    handleSelect(item) {
      this.temp.farm = item.key
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
      getDevices(this.listQuery).then(response => {
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
        farm: { name: '' },
        deviceName: '',
        topicName: '',
        description: '',
        status: 1
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
          createDevices(this.temp).then(() => {
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
      console.log(this.temp)
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
          updateDevices(tempData).then(() => {
            const index = this.list.findIndex(v => v.id === this.temp.id)
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
      deleteDevices(row.id, status).then(() => {
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
        const tHeader = ['ID', 'Name', 'Address', 'Description', 'Status', 'Created At', 'Last Update']
        const filterVal = ['id', 'name', 'address', 'description', 'status', 'createdAt', 'updatedAt']
        const data = this.formatJson(filterVal)
        console.log(data)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'farms'
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
    },
    download(row) {
      getProperties(row.id).then(response => {
        const blob = new Blob([response.data], { type: 'application/txt' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = 'device.properties'
        link.click()
        this.listLoading = false
      }).catch(e => {
        this.listLoading = false
      })
    }
  }
}
</script>
