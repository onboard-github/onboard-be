import {useLocation} from "react-router-dom";
import {httpClient} from "../../http/HttpClient";
import axios from "axios";
import {setAccessToken} from "./AuthUtils";

export function KakaoRedirect() {
    const location = useLocation()
    const queryParams = new URLSearchParams(location.search);

    // 특정 쿼리 매개변수 값 가져오기
    const code = queryParams.get('code');

    async function run() {
        const kakaoResponse = await axios.post("https://kauth.kakao.com/oauth/token", {
                    grant_type: "authorization_code",
                    client_id: process.env.REACT_APP_KAKAO_APP_KEY,
                    redirect_uri: `${process.env.REACT_APP_HOST_URL}/bgbg/auth/kakao`,
                    code: code,
                    client_secret: "asdf"
                },
                {
                    headers: {
                        "Content-type": "application/x-www-form-urlencoded;charset=utf-8"
                    }
                }
        )

        const tokenResponse = await httpClient.post('/api/v1/auth/login', {
            "token": kakaoResponse.data.access_token,
            "type": "KAKAO_ACCESS_TOKEN"
        })

        setAccessToken(tokenResponse.data.accessToken)

        window.location.href = process.env.REACT_APP_BASE_PATH;
    }

    run()

    return <div></div>
}
