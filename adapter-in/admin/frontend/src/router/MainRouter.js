import React from 'react';
import {Route, Routes} from 'react-router-dom';
import User from "../view/user/User";
import App from "../App";
import GroupList from "../view/group/GroupList";
import Auth from "../view/auth/Auth";
import {usePermissionState} from "../view/auth/AuthUtils";

export default function MainRouter() {

    const permission = usePermissionState()

    if (permission.groupList == null || permission.groupList.length === 0)
        return <Auth/>

    return (
            <Routes>
                <Route path='/' element={<App/>}/>
                <Route path='/user' element={<User/>}/>
                <Route path='/group-list' element={<GroupList/>}/>
            </Routes>
    );
}
