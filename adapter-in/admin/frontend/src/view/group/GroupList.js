import "./GroupList.css"
import {useEffect, useState} from "react";
import {httpClient} from "../../http/HttpClient";
import {Input, Pagination, Space, Table} from "antd";
import * as PropTypes from "prop-types";

const {Search} = Input

const columns = [
    {key: "id", title: "ID", dataIndex: "id"},
    {key: "name", title: "그룹명", dataIndex: "name"},
    {key: "organization", title: "그룹 소속", dataIndex: "organization"},
    {key: "description", title: "그룹 설명", dataIndex: "description"},
    {key: "accessCode", title: "참여 코드", dataIndex: "accessCode"},
]

Search.propTypes = {enterButton: PropTypes.string};
export default function GroupList() {
    const [groupList, setGroupList] = useState([])
    const [page, setPage] = useState(0)
    const [keyword, setKeyword] = useState(null)
    const [totalCount, setTotalCount] = useState(0)
    const pageSize = 15

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


    return <div className={"container"}>
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
                className={"table"}
                columns={columns}
                dataSource={groupList}
                pagination={false}
        />
        <Pagination
                className={"pagination"}
                defaultCurrent={1}
                total={totalCount}
                pageSize={pageSize}
                onChange={onPageChange}
                showSizeChanger={false}
        />
    </div>
}
