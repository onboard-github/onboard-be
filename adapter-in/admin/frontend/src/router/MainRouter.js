import React from 'react';
import {Route, Routes} from 'react-router-dom';
import User from "../view/user/User";
import GroupList from "../view/group/GroupList";
import Auth from "../view/auth/Auth";
import Main from "./Main"
import {usePermissionState} from "../view/auth/AuthUtils";
import {KakaoRedirect} from "../view/auth/KakaoRedirect";
import {AdminRole} from "./AdminRole";

export default function MainRouter() {

    const [permission] = usePermissionState()

    if (permission.groupList == null || permission.groupList.length === 0)
        return  (
                <Routes>
                    <Route path='/auth/kakao' element={<KakaoRedirect/>}/>
                    <Route path='/*' element={<Auth/>}/>
                </Routes>
        );

    return (
            <Routes>
                <Route path='/' element={<Main/>}/>
                <Route path='/user' element={<User/>}/>
                <Route path='/group-list' element={<GroupList/>}/>
                <Route path='/admin' element={<AdminRole/>}/>
            </Routes>
    );
}
