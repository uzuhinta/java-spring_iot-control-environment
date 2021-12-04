<template>
  <el-form ref="ruleForm" :model="ruleForm" status-icon :rules="rules" label-width="120px" class="demo-ruleForm">

    <el-form-item label="Old Password" prop="oldPass">
      <el-input v-model="ruleForm.oldPass" type="password" autocomplete="off" />
    </el-form-item>
    <el-form-item label="New Password" prop="newPass">
      <el-input v-model="ruleForm.newPass" type="password" autocomplete="off" />
    </el-form-item>
    <el-form-item label="Confirm" prop="confirmPass">
      <el-input v-model="ruleForm.confirmPass" type="password" autocomplete="off" />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="submitForm('ruleForm')">Submit</el-button>
      <el-button @click="resetForm('ruleForm')">Reset</el-button>
    </el-form-item>
  </el-form>
</template>

<script>

import { changePass } from '@/api/user'
import { removeRole, removeToken } from '@/utils/auth'

export default {
  data() {
    var checkOldPass = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('Please input the old password'))
      } else {
        callback()
      }
    }
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input the password'))
      } else {
        if (this.ruleForm.confirmPass !== '') {
          this.$refs.ruleForm.validateField('checkPass')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input the password again'))
      } else if (value !== this.ruleForm.newPass) {
        callback(new Error('Two inputs don\'t match!'))
      } else {
        callback()
      }
    }
    return {
      ruleForm: {
        newPass: '',
        confirmPass: '',
        oldPass: ''
      },
      rules: {
        newPass: [
          { validator: validatePass, trigger: 'blur' }
        ],
        confirmPass: [
          { validator: validatePass2, trigger: 'blur' }
        ],
        oldPass: [
          { validator: checkOldPass, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    async logout() {
      removeToken()
      removeRole()
      // await this.$store.dispatch('auth/logout')
      this.$router.go('/login')
    },
    notify(callback) {
      setTimeout(callback, 2000)
      this.$notify({
        title: 'Success',
        message: 'Change success. Please login again !',
        type: 'success',
        duration: 2000
      })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          changePass(this.ruleForm).then(() => {
            this.notify(this.logout)
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>
