<template>
  <div>
    <el-button type="primary" @click="GetKey">getPublicKey</el-button><br />
     <el-button type="primary" @click="exchangeKey">exchangeKey</el-button><br />
    <el-button type="primary" @click="dialogVisible = true">Load from File</el-button>
    <el-dialog title="Load JSON document from file" :visible.sync="dialogVisible">
      <el-upload :file-list="uploadFiles" action="alert" :auto-upload="false" multiple :on-change="loadJsonFromFile">
        <el-button size="small" type="primary">Select a file</el-button>
        <div slot="tip">upload only jpg/png files, and less than 500kb</div>
      </el-upload>
      <span slot="footer">
        <el-button type="primary" @click="dialogVisible = false">cancel</el-button>
        <el-button type="primary" @click="loadJsonFromFileConfirmed">confirm</el-button>
      </span>
    </el-dialog>
  </div>
 
</template>
 
<script>
import CryptoJS from 'crypto-js';
import axios from 'axios';
import JsEncrypt from 'jsencrypt';
function a(a) {
        var d,
            e,
            b = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
            c = "";
        for (d = 0; a > d; d += 1)
            e = Math.random() * b.length, e = Math.floor(e), c += b.charAt(e);
        return c
}


export default {
  data () {
    return {
      // data for upload files
      uploadFilename: null,
      uploadFiles: [],
      dialogVisible: false,
      encryptKey:null,
      iv:"1234567890123456",
      publicKey : null, // 公钥
      privateKey :null, // 私钥
      user:null,

    }
  },
  create() {
      axios
        .request({
            url:"/getKey",
            method:"GET",
            responseType:"text"
        }).then(function(response){
            var that=this;
            console.log(response)
            //that.encryptKey=response.data["encryptKey"];
            //that.iv=response.data["iv"];
        });
  } ,
  methods: {
    RSAdencrypt(pas) {
      var jse = new JsEncrypt()
      //   jse.setPublicKey(publicKey)
      jse.setPrivateKey(this.privateKey)
      return jse.decrypt(pas)
    },
    RSAencrypt(pas) {
      var jse = new JsEncrypt({
        default_key_size: 2048 // 密钥位数
      })
      jse.setPublicKey(this.publicKey)
      return jse.encrypt(pas)
    },
    encrypt(word, keyStr, ivStr) {
        keyStr = keyStr ? keyStr : "absoietlj32fai12";
        ivStr = ivStr ? ivStr : "absoietlj32fai12";
        console.log(keyStr)
        console.log(iv)
        let key = CryptoJS.enc.Utf8.parse(keyStr);
        let iv = CryptoJS.enc.Utf8.parse(ivStr);
        let srcs = CryptoJS.enc.Utf8.parse(word);
        
        let encrypted = CryptoJS.AES.encrypt(srcs, key, {
        iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
        });
        return encrypted.toString();
    },
    decrypt(word, keyStr, ivStr) {
        keyStr = keyStr ? keyStr : "absoietlj32fai12";
        ivStr = ivStr ? ivStr : "absoietlj32fai12";
        var key = CryptoJS.enc.Utf8.parse(keyStr);
        let iv = CryptoJS.enc.Utf8.parse(ivStr);
        
        var decrypt = CryptoJS.AES.decrypt(word, key, {
        iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
        });
        return decrypt.toString(CryptoJS.enc.Utf8);
    },
    GetKey(){
      var that = this;
      var key=a(16);
      axios.request({
        url:"http://localhost:8080/api/v1/getPublicKey",
        method:"GET",
        responseType:"json"
      }).then(function(response){
        console.log(response);
        that.publicKey=response.data["publicKey"];
        console.log(that.publicKey)
      });
      this.encryptKey=key;
    },
    exchangeKey(){
      console.log(this.publicKey)
      var urls="http://localhost:8080/api/v1/exchangeKey";
      console.log(urls);
      var params = new URLSearchParams();
      this.user=a(12);
      this.uploadFilename=this.user;
      params.append("user",this.uploadFilename);
      params.append("cipher",this.RSAencrypt(this.encryptKey));
      params.append("publicKey",this.publicKey);
      axios.post(
        urls,params).then(function(response){
        console.log(response);
      });
    },
    loadJsonFromFile (file, fileList) {
      this.uploadFilename = file.name
      this.uploadFiles = fileList
    },
    loadJsonFromFileConfirmed () {
      console.log(this.uploadFiles)
      if (this.uploadFiles) {
        for (let i = 0; i < this.uploadFiles.length; i++) {
          let file = this.uploadFiles[i]
          console.log(file.raw)
          if (!file) continue
          let reader = new FileReader()
          reader.onload = async (e) => {
            try {
              let document = e.target.result
              var cipher=(this.encrypt(document,this.encryptKey,this.iv))
              var urls="http://localhost:8080/api/v1/exchangefile";
              var params = new URLSearchParams();
              params.append("user",this.uploadFilename);
              params.append("cipher",cipher);
              axios.post(urls,params).then(function(response){
                console.log(response);
              });
            } catch (err) {
              console.log(`load JSON document from file error: ${err.message}`)
              this.showSnackbar(`Load JSON document from file error: ${err.message}`, 4000)
            }
          }
          reader.readAsText(file.raw)
        }
      }
      this.dialogVisible = false
    }
  }
}
</script>