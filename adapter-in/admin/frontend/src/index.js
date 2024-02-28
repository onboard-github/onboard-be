import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import Header from "./view/component/Header";
import {BrowserRouter} from 'react-router-dom';
import MainRouter from "./router/MainRouter";
import {Layout} from "antd";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
        <BrowserRouter basename={process.env.REACT_APP_BASE_PATH}>
            <Header/>
            <Layout.Content>
                <MainRouter/>
            </Layout.Content>
        </BrowserRouter>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
