import {Outlet} from "react-router-dom"
import {MenuAppBar} from "../MenuAppBar"
import {Box} from "@mui/material"
import {useEffect, useState} from "react";
import {SessionContext, USER_INITIAL_STATE} from "../../context/SessionContext";
import {Loader} from "../Loader";
import {apiClient} from "../../api/apiClient.ts";
import {initLocalStorageAndRedirectToAuth} from "../../api/authUtils.ts";
import {User} from "../../types/User.ts";
import {useSnackBar} from "../../context/SnackBarContext.tsx";


export const PrivateRoute = () => {
    const [user, setUser] = useState<User>(USER_INITIAL_STATE);
    const [loading, setLoading] = useState<boolean>(true);
    const snackbar = useSnackBar();

    useEffect(() => {
        apiClient.getSession({
            onSuccess: (res) => {
                if (!res.username) {
                    initLocalStorageAndRedirectToAuth();
                } else {
                    apiClient.getProfile({
                        onSuccess: (data) => {
                            setUser(data);
                            setLoading(false);
                        },
                        onFailure: (e) => {
                            setLoading(false);
                            snackbar.showSnackBar(e.message, "error");
                            console.error(e.message)
                        },
                    })
                }
            },
            onFailure: (err) => {
                console.error(err.message);
                snackbar.showSnackBar(err.message, "error");
            }
        });
    }, []);

    return (
        loading ?
            <Loader/>
            :
            <SessionContext.Provider value={{
                user,
                updateUser: setUser,
            }}>
                <MenuAppBar/>
                <Box component="main" sx={{
                    height: 100,
                    marginTop: 6,
                    flexGrow: 1,
                    p: 3,
                }}>
                    <Outlet/>
                </Box>
            </SessionContext.Provider>
    )
}
