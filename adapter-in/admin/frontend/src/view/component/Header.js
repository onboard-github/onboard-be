import './Header.css';
import {usePermissionState} from "../auth/AuthUtils";


export default function Header() {
    const basePath = "/bgbg"
    const permission  = usePermissionState()

    return (
            <div className="header">
                {
                    permission.groupList.map(
                            it => <a href={`${basePath}${it.url}`}>{it.label}</a>
                    )
                }
            </div>
    )
}
