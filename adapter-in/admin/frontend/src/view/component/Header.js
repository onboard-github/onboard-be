import {usePermissionState} from "../auth/AuthUtils";
import {Layout, Menu} from 'antd';
import {Link, useLocation} from 'react-router-dom';


export default function () {
    const permission = usePermissionState()
    const location = useLocation();

    return (
            <Layout.Header>
                <Menu
                        theme="dark"
                        mode="horizontal"
                        defaultSelectedKeys={location.pathname}
                >
                    {
                        permission.groupList.map(
                                it => <Menu.Item key={it.url}><Link to={it.url}>{it.label}</Link></Menu.Item>
                        )
                    }
                </Menu>
            </Layout.Header>
    )
}
