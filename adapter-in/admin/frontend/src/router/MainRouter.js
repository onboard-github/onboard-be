import React from 'react';
import {Route, Routes} from 'react-router-dom';
import User from "../user/User";
import App from "../App";
import GroupList from "../group/GroupList";

export default function MainRouter() {
    return (
            <Routes>
                <Route path='/' element={<App/>}/>
                <Route path='/user' element={<User/>}/>
                <Route path='/group-list' element={<GroupList/>}/>
            </Routes>
    );
}
