import {GoogleLogin} from "@react-oauth/google";
import {GoogleOAuthProvider} from "@react-oauth/google";
import {httpClient} from "../../http/HttpClient";
import {setAuthToken} from "./AuthUtils";

const GoogleLoginButton = () => {
    const clientId = process.env.REACT_APP_GOOGLE_CLIENT_ID
    return (
            <>
                <GoogleOAuthProvider clientId={clientId}>
                    <GoogleLogin
                            onSuccess={async res => {
                                const idToken = res.credential
                                console.log(idToken)
                                const response = await httpClient.post("/api/v1/auth/login", {
                                    type: "GOOGLE_ID_TOKEN",
                                    token: idToken
                                })

                                setAuthToken(response.data)

                            }}
                            onFailure={(err) => {
                                console.log(err);
                            }}
                    />
                </GoogleOAuthProvider>
            </>
    );
};

export default GoogleLoginButton
