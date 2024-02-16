import GoogleLoginButton from "./GoogleLoginButton";
import {isLogin, usePermissionState} from "./AuthUtils";
import RegisterAdmin from "./RegisterAdmin";

export default function Auth() {
    const permission = usePermissionState()

    if (!isLogin())
        return <GoogleLoginButton/>

    if (permission.groupList.length === 0)
        return <RegisterAdmin/>
}
