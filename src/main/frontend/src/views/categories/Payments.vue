<template>
  <div class="app-container">
    <el-form label-position="left" label-width="150px" style="width: 90%; margin-left:50px;">
      <el-row>
        <el-col :span="12">
          <el-form-item label="Username " prop="username">
            <el-autocomplete
              v-model="username"
              clearable
              :fetch-suggestions="querySearchAsync"
              placeholder="Please input username"
              @select="handleSelect"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <hr style="margin-top: 10px; margin-bottom: 30px;">

      <!-- <span @click="downloadTemplate" style="display: block;padding-bottom: 20px; color: green;cursor:pointer;">(*) Download Full Invoices</span> -->
      <el-row :gutter="20">
        <el-table
          v-loading="listLoading"
          :data="list"
          border
          fit
          highlight-current-row
          height="250"
          style="width: 100%;"
        >
          <el-table-column label="No." align="center" width="50" type="index" />
          <el-table-column label="Invoice Name" align="center" width="150">
            <template slot-scope="{row}">
              <span>{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="Customer" align="center" width="140">
            <template slot-scope="{row}">
              <span>{{ row.user.username }}</span>
            </template>
          </el-table-column>
          <el-table-column label="Crop" align="center" width="130">
            <template slot-scope="{row}">
              <span>{{ row.crop.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="Device Name" align="center" width="120">
            <template slot-scope="{row}">
              <span>{{ row.device.deviceName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="Total Money" align="center" width="110">
            <template slot-scope="{row}">
              <span>{{ formatMoney(row.money) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="Due Date" align="center" width="120">
            <template slot-scope="{row}">
              <span>{{ formatDate(row.dueDate) }}</span>
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
        </el-table>
        <br>
        <el-col :span="12">
          <el-form-item label="Debit">
            <el-input v-model="payment.money" style="{color:black;font-weight:bold;}" :readonly="true" />
          </el-form-item>
          <el-form-item label="Old Debit">
            <el-input v-model="payment.debit" style="{color:black;font-weight:bold;}" :readonly="true" />
          </el-form-item>
          <el-form-item label="Total">
            <el-input v-model="payment.total" style="{color:black;font-weight:bold;}" :readonly="true" />
          </el-form-item>
          <el-form-item label="Paid">
            <el-input v-model="payment.paid" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="4" :push="10">
          <div class="dialog-footer">
            <el-popconfirm
              confirm-button-text="OK"
              cancel-button-text="No, Thanks"
              icon="fas fa-info-circle"
              icon-color="red"
              title="You cannot modify after submit. Are you sure to pay this?"
              @onConfirm="submitPayment()"
            >
              <el-button slot="reference" size="mini" type="primary">
                Submit
              </el-button>
            </el-popconfirm>
          </div>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import { getUsers } from '@/api/user'
import { getInvoices } from '@/api/invoice'
import { pay } from '@/api/payment'

export default {
  name: 'Payments',
  data() {
    return {
      uploading: false,
      username: '',
      payment: {
        money: 0,
        debit: 0,
        total: 0,
        totalNumber: 0,
        paid: 0
      },
      list: undefined,
      listLoading: false
    }
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
    downloadTemplate() {
      getTemplate().then(response => {
        const blob = new Blob([response.data], { type: 'application/xlsx' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        const name = this.partname
        link.download = 'template.xlsx'
        link.click()
      })
    },
    querySearchAsync(queryString, cb) {
      const query = {
        status: 1,
        page: 1,
        size: 10,
        key: queryString
      }
      let results = []
      let listAreas = []
      getUsers(query).then(response => {
        listAreas = response.data.items
        results = listAreas.map(s => {
          return {
            value: s.username,
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
      this.username = item.value
      this.loadData()
    },
    loadData() {
      const data = {
        username: this.username,
        status: 1
      }
      getInvoices(data).then(response => {
        this.list = response.data.invoices
        this.payment.money = this.formatMoney(response.data.totalMoney)
        this.payment.debit = this.formatMoney(response.data.totalDebit)
        this.payment.total = this.formatMoney(response.data.total)
        this.totalNumber = response.data.total
        this.payment.paid = response.data.total
      })
    },
    submitPayment() {
      const data = {
        username: this.username,
        invoices: this.list,
        amount: this.payment.paid
      }
      pay(data).then(response => {
        this.$notify({
          title: 'Success',
          message: 'Pay Successfully',
          type: 'success',
          duration: 2000
        })
        this.loadData()
      })
    }
  }
}
</script>
