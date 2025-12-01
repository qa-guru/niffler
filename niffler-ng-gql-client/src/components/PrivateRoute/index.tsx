import {Outlet} from "react-router-dom"
import {MenuAppBar} from "../MenuAppBar"
import {Box} from "@mui/material"
import {Loader} from "../Loader";
import {initLocalStorageAndRedirectToAuth} from "../../api/authUtils.ts";
import {useSnackBar} from "../../context/SnackBarContext.tsx";
import {useSessionQuery} from "../../generated/graphql.tsx";


export const PrivateRoute = () => {
    const snackbar = useSnackBar();
    const {loading} = useSessionQuery({
        onError: (err) => {
            console.error(err);
            snackbar.showSnackBar("Can not load session", "error");
            initLocalStorageAndRedirectToAuth();
        },
        onCompleted: (data) => {
            if (!data?.session.username) {
                initLocalStorageAndRedirectToAuth();
            }
        },
        errorPolicy: "none",
    });

    return (
        loading ?
            <Loader/>
            :
            <>
                <MenuAppBar/>
                <Box component="main" sx={{
                    height: 100,
                    marginTop: 6,
                    flexGrow: 1,
                    p: 3,
                }}>
                    <Outlet/>
                </Box>
            </>
    );
}
