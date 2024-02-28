import GoogleLoginButton from "./GoogleLoginButton";
import {isLogin, usePermissionState} from "./AuthUtils";
import RegisterAdmin from "./RegisterAdmin";
import KakaoLoginButton from "./KakaoLoginButton";

export default function Auth() {
    const permission = usePermissionState()

    if (!isLogin())
        return <KakaoLoginButton/>

    if (permission.groupList.length === 0)
        return <RegisterAdmin/>
}
