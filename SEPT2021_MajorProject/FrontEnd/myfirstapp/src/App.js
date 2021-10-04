import React, { Component } from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import jwt_decode from "jwt-decode";

/* Redux / Memory Stores */
import { Provider } from "react-redux";
import store from "./store";

import { SET_CURRENT_USER } from "./actions/types";
import { logout } from "./actions/securityActions";

import setJWTToken from "./securityUtils/setJWTToken";
import SecureRoute from "./securityUtils/SecureRoute";

import Dashboard from "./components/Dashboard";
import Header from "./components/Layout/Header";
import Footer from "./components/Layout/Footer";
import AddPerson from "./components/Persons/AddPerson";
import Landing from "./components/Layout/Landing";
import Register from "./components/UserManagement/Register";
import Login from "./components/UserManagement/Login";
import UserManager from "./components/UserManager";
//import UserToPublisher from "./components/UserToPublisher";
import Update from "./components/UserManagement/Update";
import profile from "./components/UserManagement/profile";
import Book from "./components/Books/Book";
import BookList from "./components/Books/BookList";
import ContactUs from "./components/Information/ContactUs";
import AboutUs from "./components/Information/AboutUs";


import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import AdminBoard from "./components/AdminBoard";
import ProducerBoard from "./components/ProducerBoard";
import UserManager2 from "./components/UserManager2";

const jwtToken = localStorage.jwtToken; // Defaults to undefined.

if (jwtToken) {
  setJWTToken(jwtToken);
  
  store.dispatch({
    type: SET_CURRENT_USER,
    payload: jwt_decode(jwtToken)
  });

  const currentTime = Date.now() / 1000;
  
  if (jwt_decode(jwtToken).exp < currentTime) {
    store.dispatch(logout());
    window.location.href = "/";
  }
}

const App = () => {
  return (
    <Provider store={store}>
      <Router>
        <div className="App">
          <Switch>
            <Route exact path="/" component={Landing} />
            <Route exact path="/register" component={Register} />
            <Route exact path="/login" component={Login} />
            <Route exact path="/userManager" component={UserManager} />
            <Route exact path="/userManager2" component={UserManager2} />
            {/* <Route exact path="/UserToPublisher" component={UserToPublisher} /> */}
            <Route exact path="/Update" component={Update} />
            <Route exact path="/profile" component={profile} />
            <Route path="/add" exact component={Book} />
            <Route path="/edit/:id" exact component={Book} />
            <Route path="/list" exact component={BookList} />
            <Route exact path="/ContactUs" component={ContactUs} />
            <Route exact path="/AboutUs" component={AboutUs} />

            <SecureRoute exact path="/dashboard" component={Dashboard} />
            <SecureRoute exact path="/addPerson" component={AddPerson} />
            <SecureRoute exact path="/adminboard" component={AdminBoard} />
            <SecureRoute exact path="/producerboard" component={ProducerBoard} />
          </Switch>
        </div>
      </Router>
    </Provider>
  );
};

export default App;