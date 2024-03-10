import axios from "axios";
import {getAccessToken} from "../view/auth/AuthUtils";

export const httpClient = axios.create({
    baseURL: process.env.REACT_APP_SERVER_URL,
    headers: {
        Authorization: `Bearer ${getAccessToken()}`
    }
});
