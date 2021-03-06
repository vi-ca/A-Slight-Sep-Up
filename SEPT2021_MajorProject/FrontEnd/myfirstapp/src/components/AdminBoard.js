import React, { Component } from 'react'
import Person from './Persons/Person'
import CreatePersonButton from './Persons/CreatePersonButton';
import Header from "./Layout/Header";
import Footer from "./Layout/Footer";
import { Link } from 'react-router-dom';
import { getUser } from "../actions/userActions";
import store from "../store";


class AdminBoard extends Component {
    render() {
        const user = store.getState().security.user 
        return (
            <>
                <Header />
                <Link className="btn btn-lg btn-primary mr-2" to="/UserManager">
                <h1>User Manager</h1>
                </Link>
                <Link className="btn btn-lg btn-primary mr-2" to="/add">
                <h1>Add Books</h1>
                </Link>
                <Footer />
            </>

        )
    }
}
export default AdminBoard;
