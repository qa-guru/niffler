import { useEffect, useState} from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {toast, ToastContainer} from "react-toastify";
import {getData} from "./api/api";
import {LoginPage} from "./components/LoginPage";
import {MainLayout} from "./components/MainLayout";
import {Profile} from "./components/Profile";
import {ProtectedRoutes} from "./components/ProtectedRoutes";
import {Redirect} from "./components/Redirect";
import {UserContext} from "./contexts/UserContext";
import 'react-toastify/dist/ReactToastify.css';


function App() {

    const [user, setUser] = useState(null);
    const [userLoading, setUserLoading] = useState(true);
    const userContext = { user, setUser };

    useEffect(() => {
        getData({
                path: "/currentUser",
                onSuccess: (user) => {
                    setUser(user);
                    setUserLoading(false);
                },
                onFail: (err) => {
                    console.log(err);
                    setUserLoading(false);
                }
            }
        );
    }, []);

    const showSuccessMessage = (message) => {
        toast.success(message, {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
        });
    };

    const showFailMessage = (message) => {
        toast.error(message, {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
        });
    };

    return (
        <div className="App">
            <ToastContainer/>
            <BrowserRouter>
                <UserContext.Provider value={userContext}>
                    {userLoading ? <div className="loader"></div> : (
                        <Routes>
                            <Route path="/redirect" element={<Redirect />} exact={false}/>
                            <Route path="/authorized" element={<Redirect />} exact={false}/>
                            <Route element={<ProtectedRoutes/>}>
                                <Route path="/profile" element={<Profile showSuccess={showSuccessMessage} showFail={showFailMessage}/>}/>
                                <Route path="/main" element={<MainLayout showSuccess={showSuccessMessage} />}/>
                            </Route>
                            <Route path="*" element={<LoginPage/>}/>
                        </Routes>
                    )}
                </UserContext.Provider>
            </BrowserRouter>
        </div>
    );
}

export default App;
