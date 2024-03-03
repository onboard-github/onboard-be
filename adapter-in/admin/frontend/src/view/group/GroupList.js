import style from "./GroupList.module.css"
import {useEffect, useState} from "react";
import {httpClient} from "../../http/HttpClient";
import {Button, Input, Pagination, Space, Table, Tooltip} from "antd";
import {DeleteOutlined, EditOutlined} from '@ant-design/icons';
import {usePermissionState} from "../auth/AuthUtils";

const {Search} = Input

export default function GroupList() {
    const [groupList, setGroupList] = useState([])
    const [page, setPage] = useState(0)
    const [keyword, setKeyword] = useState(null)
    const [totalCount, setTotalCount] = useState(0)
    const [permission] = usePermissionState()
    const pageSize = 15

    function onClickDeleteButton(groupId) {
        httpClient.delete("/admin/v1/group", {
            params: {groupId}
        }).then(() => setGroupList(groupList.filter(group => group.id !== groupId)))
    }

    function onClickEditButton(groupId) {
    }

    const columns = [
        {key: "id", title: "ID", dataIndex: "id"},
        {key: "name", title: "그룹명", dataIndex: "name"},
        {key: "organization", title: "그룹 소속", dataIndex: "organization"},
        {key: "description", title: "그룹 설명", dataIndex: "description"},
        {key: "accessCode", title: "참여 코드", dataIndex: "accessCode"},
        {
            key: "actions", title: "", render: (_, {id}) => (
                    <>
                        <Button
                                className={style.actionButton}
                                type="primary"
                                shape="circle"
                                icon={<DeleteOutlined/>}
                                size="small"
                                disabled={!permission.permissionList.includes("DELETE_GROUP")}
                                onClick={() => onClickDeleteButton(id)}
                        />
                        <Tooltip title="준비중인 기능입니다.">
                            <Button
                                    className={style.actionButton}
                                    type="primary"
                                    shape="circle"
                                    icon={<EditOutlined/>}
                                    size="small"
                                    disabled={true}
                                    onClick={() => onClickEditButton(id)}
                            />
                        </Tooltip>
                    </>
            ),
        },
    ]

    useEffect(() => {
        fetchData(null);
    }, []);

    const fetchData = async (keyword, page) => {
        try {
            setKeyword(keyword);
            setPage(page);

            const response = await httpClient.get('/admin/v1/group-list', {
                params: {
                    keyword: keyword,
                    pageNumber: page,
                    pageSize: pageSize,
                }
            });
            setGroupList(response.data.list);
            setTotalCount(response.data.totalCount);
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };
    const onSearch = (value) => fetchData(value, 0);

    const onPageChange = (value) => fetchData(keyword, value - 1)


    return <div className={style.container}>
        <Space direction="horizontal">
            <label>검색</label>
            <Search
                    placeholder="input search text"
                    onSearch={onSearch}
                    enterButton
                    size={"large"}
                    style={{width: "50vw"}}
            />
        </Space>
        <Table
                className={style.table}
                columns={columns}
                dataSource={groupList}
                pagination={false}
        />
        <Pagination
                className={style.pagination}
                defaultCurrent={1}
                total={totalCount}
                pageSize={pageSize}
                onChange={onPageChange}
                showSizeChanger={false}
        />
    </div>
}
