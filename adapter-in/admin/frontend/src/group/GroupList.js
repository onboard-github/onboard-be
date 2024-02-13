import {useEffect, useState} from "react";
import {httpClient} from "../http/HttpClient";

export default function GroupList() {
    const [groupList, setGroupList] = useState([])

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const response = await httpClient.get('/admin/v1/group-list');
            setGroupList(response.data.list);
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    return (
            <div>
                <table>
                    <thead>
                    <tr>
                        <td>ID</td>
                        <td>그룹명</td>
                        <td>그룹 소속</td>
                        <td>그룹 설명</td>
                        <td>참여 코드</td>
                    </tr>
                    </thead>
                    <tbody>
                    {groupList.map(group => (
                            <tr key={group.id}>
                                <td>{group.id}</td>
                                <td>{group.name}</td>
                                <td>{group.organization}</td>
                                <td>{group.description}</td>
                                <td>{group.accessCode}</td>
                            </tr>
                    ))}
                    </tbody>
                </table>
            </div>
    )
}
