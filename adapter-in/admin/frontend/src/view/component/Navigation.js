import {usePermissionState} from "../auth/AuthUtils";
import {Menu} from 'antd';
import {Link, useLocation} from 'react-router-dom';


export default function () {
    const [permission] = usePermissionState()
    const location = useLocation();

    return (
            <div>
                <Menu
                        theme="dark"
                        mode="vertical"
                        defaultSelectedKeys={location.pathname}
                        inlineCollapsed={false}
                >
                    {
                        permission.groupList.map(
                                it => <Menu.Item key={it.url}><Link to={it.url}>{it.label}</Link></Menu.Item>
                        )
                    }
                </Menu>
            </div>
    )
}
