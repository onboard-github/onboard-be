import React, {useState} from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import Navigation from "./view/component/Navigation";
import {BrowserRouter} from 'react-router-dom';
import MainRouter from "./router/MainRouter";
import {Layout} from "antd";

const {Header, Content, Footer, Sider} = Layout;


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(<App/>);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

function App() {
    const [navCollapsed, setNavCollapsed] = useState(false);

    return (
            <BrowserRouter basename={process.env.REACT_APP_BASE_PATH}>
                <Layout>
                    <Sider collapsed={navCollapsed} onCollapse={setNavCollapsed}>
                        <Navigation/>
                    </Sider>
                    <Content>
                        <MainRouter/>
                    </Content>
                </Layout>
            </BrowserRouter>
    );
}
