(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["about"],{"14b7":function(e,t,n){},2679:function(e,t,n){"use strict";n.r(t);var i=n("7a23"),c={class:"upload"};function a(e,t,n,a,o,s){var l=Object(i["x"])("UploadComponent");return Object(i["q"])(),Object(i["e"])("div",c,[Object(i["h"])(l)])}var o=Object(i["D"])("data-v-2390ebac");Object(i["t"])("data-v-2390ebac");var s={class:"container"},l=Object(i["g"])("Files "),u={key:0};Object(i["r"])();var r=o((function(e,t,n,c,a,r){return Object(i["q"])(),Object(i["e"])("div",s,[Object(i["h"])("div",null,[Object(i["h"])("label",null,[l,Object(i["h"])("input",{type:"file",id:"file",ref:"file",multiple:"",onChange:t[1]||(t[1]=function(e){return r.handleFilesUpload()})},null,544)]),Object(i["h"])("button",{onClick:t[2]||(t[2]=function(e){return r.submitFiles()})},"Submit")]),Object(i["h"])(i["b"],{name:"fade"},{default:o((function(){return[a.show?(Object(i["q"])(),Object(i["e"])("p",u,"Ajout de l'image réussi !")):Object(i["f"])("",!0)]})),_:1})])})),b=(n("b0c0"),n("09de")),f={name:"UploadComponent",data:function(){return{file:"",show:!1}},methods:{eraseMsg:function(){var e=this;setTimeout((function(){return e.show=!1}),2e3)},submitFiles:function(){var e=this,t=new FormData;t.append("file",this.file,this.file.name),Object(b["c"])(t,{headers:{"Content-Type":"image/jpeg"}},(function(){e.show=!0,e.eraseMsg()}),(function(t){e.show=!1,console.log(t),e.eraseMsg()}))},handleFilesUpload:function(){this.file=this.$refs.file.files[0]}}};n("9cc4");f.render=r,f.__scopeId="data-v-2390ebac";var d=f,p={name:"Upload",components:{UploadComponent:d}};p.render=a;t["default"]=p},"9cc4":function(e,t,n){"use strict";n("14b7")}}]);
//# sourceMappingURL=about.1d06eee3.js.map