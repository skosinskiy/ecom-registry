import {FuseLoadable} from '@fuse';

export const LoginPageConfig = {
    settings: {
        layout: {
            config: {
                navbar: {
                    display: false
                },
                footer: {
                    display: false
                },
                toolbar: {
                    display: false
                },
                settings: {
                    display: false
                }
            }
        }
    },
    routes  : [
        {
            path     : '/login',
            component: FuseLoadable({
                loader: () => import('./LoginPage')
            })
        }
    ]
};
