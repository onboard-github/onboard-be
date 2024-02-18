import {useState} from "react";
import axios from "axios";
import {httpClient} from "../../http/HttpClient";

export default function RegisterAdmin() {
    const [formData, setFormData] = useState({})

    const handleChange = (e) => {
        e.preventDefault();

        setFormData(
                {
                    [e.target.name]: e.target.value
                }
        )
    };

    function submit(e) {
        e.preventDefault()

        httpClient.post("/admin/v1/admin/permission", {
            role: ["ADMIN"],
            memo: formData.name
        })

        alert("Done")
    }

    return <div>
        <label>User Id</label> {1}
        <br/>
        <label>이름</label>
        <input type={"text"} name={"name"} value={formData.name} onChange={handleChange}/>
        <br/>
        <button onClick={submit}>제출</button>
    </div>
}
