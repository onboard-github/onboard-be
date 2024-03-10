import {httpClient} from "../../http/HttpClient";
import {useEffect, useState} from "react";

export function getAccessToken() {
    return localStorage.getItem("accessToken")
}

export function setAccessToken(token) {
    localStorage.setItem("accessToken", token)
}

const keyPermission = "permission"
const emptyPermission = {groupList: []}

// keyName 이름의 String 을 가져와 JSON 형태로 다시 Parse 진행
export async function getPermissionList() {
    const permission = JSON.parse(sessionStorage.getItem(keyPermission))
    if (permission != null && permission.groupList.length > 0) return permission

    try {
        const response = await httpClient.get('/admin/v1/admin/permission');
        const newPermission = response.data

        sessionStorage.setItem(keyPermission, JSON.stringify(newPermission));
        return newPermission
    } catch (e) {
        sessionStorage.setItem(keyPermission, JSON.stringify(emptyPermission));
        return emptyPermission
    }
}

export function isLogin() {
    return getAccessToken() != null
}

export function usePermissionState() {
    const [permission, setPermission] = useState(emptyPermission)

    useEffect(() => {
                async function run() {
                    setPermission(await getPermissionList())
                }

                run()
            }
            , []
    )

    return [permission]
}
