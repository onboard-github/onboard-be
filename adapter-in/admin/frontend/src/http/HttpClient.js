import axios from "axios";

const appPhase = process.env.REACT_APP_ENV || 'local'

function getBaseUrl(appPhase){
    switch (appPhase){
        case "production":
            return "http://api.onboardgame.co.kr"
        case "sandbox":
            return "http://sandbox-api.onboardgame.co.kr"
        default:
            return "http://localhost:8080"
    }
}

export const httpClient = axios.create({
    baseURL: getBaseUrl(appPhase)
});
