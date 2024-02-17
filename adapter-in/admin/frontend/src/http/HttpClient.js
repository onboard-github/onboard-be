import axios from "axios";
import {getAccessToken} from "../view/auth/AuthUtils";

export const httpClient = axios.create({
    baseURL: process.env.REACT_APP_HOST_URL,
    headers: {
        Authorization: `Bearer ${getAccessToken()}`
    }
});
