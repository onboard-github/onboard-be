import axios from "axios";
import {getAccessToken, getRefreshToken, setAuthToken} from "../view/auth/AuthUtils";

export const httpClient = axios.create({
    baseURL: process.env.REACT_APP_SERVER_URL,
    headers: {
        Authorization: `Bearer ${getAccessToken()}`
    }
});

httpClient.interceptors.response.use(
        response => {
            return response;
        },
        async error => {
            if (error.response.status === 401) {
                try {
                    const refreshToken = getRefreshToken()
                    if (refreshToken == null) return Promise.reject(error)

                    const tokenResponse = await httpClient.post('/api/v1/auth/login', {
                        "token": refreshToken,
                        "type": "REFRESH"
                    })

                    setAuthToken(tokenResponse.data)

                    const accessToken = tokenResponse.data.accessToken

                    error.config.headers['Authorization'] = `Bearer ${accessToken}`;

                    return await httpClient.request(error.config)
                } catch (e) {
                    return Promise.reject(e);
                }
            }

            return Promise.reject(error);
        }
)
