import './Header.css';


export default function Header() {
    const basePath = "bgbg"

    return (
            <div className="header">
                <a href={`${basePath}/user`}>User</a>
                <a href={`${basePath}/group-list`}>Group</a>
            </div>
    )
}
