import axios from "axios";

export default function KakaoLoginButton() {

    function toParams (params) {
        return Object.keys(params || {}).map(function (key) {
            return key + '=' + params[key];
        }).join('&');
    }

    function requestAuthCode() {
        const params = {
            client_id: process.env.REACT_APP_KAKAO_APP_KEY,
            redirect_uri: `${process.env.REACT_APP_HOST_URL}/bgbg/auth/kakao`,
            response_type: "code",
            client_secret: "asdf"
        }

        console.log(JSON.stringify(params))

        axios.get("https://kauth.kakao.com/oauth/authorize", {
            params: params
        })
    }

    return <a href={`https://kauth.kakao.com/oauth/authorize?${toParams({
        client_id: process.env.REACT_APP_KAKAO_APP_KEY,
        redirect_uri: `${process.env.REACT_APP_HOST_URL}/bgbg/auth/kakao`,
        response_type: "code",
        client_secret: "asdf"
    })}`}>카카오 로그인</a>

    // return <button onClick={requestAuthCode}>카카오 로그인</button>
}
