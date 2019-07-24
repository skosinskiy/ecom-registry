import React from 'react';
import {Redirect} from 'react-router-dom';
import {FuseUtils} from '@fuse/index';
import {ExampleConfig} from '../main/pages/example/ExampleConfig';
import {LoginPageConfig} from "../main/pages/auth/login/LoginPageConfig";

const routeConfigs = [
    ExampleConfig,
    LoginPageConfig
];

const routes = [
    ...FuseUtils.generateRoutesFromConfigs(routeConfigs),
    {
        path: '/',
        component: () => <Redirect to="/login"/>
    }
];

export default routes;
