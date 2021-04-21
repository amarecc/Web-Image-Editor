<template>
  <div id="nav">
    <router-link to="/">Editeur d'images</router-link> |
    <router-link to="/Upload">Upload</router-link>
  </div>
  <router-view/>
</template>

<style>

* {
  margin: 0;
}

html, body {
  min-height: 100%;
  background-color: #f6f6f6;;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
  background-color: white;
  box-shadow: rgba(3,27,78,0.1) 0px 2px 4px;
  margin-bottom: 1%;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
