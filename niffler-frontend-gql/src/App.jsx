import {useQuery} from "@apollo/client";
import {useState} from "react";
import {Route, Routes} from "react-router-dom";
import {ToastContainer} from "react-toastify";
import {QUERY_CURRENT_USER} from "./api/graphql/queries";
import {FriendsLayout} from "./components/FriendsLayout";
import {LoginPage} from "./components/LoginPage";
import {MainLayout} from "./components/MainLayout";
import {PeopleLayout} from "./components/PeopleLayout";
import {Profile} from "./components/Profile";
import {ProtectedRoutes} from "./components/ProtectedRoutes";
import {Redirect} from "./components/Redirect";
import {PopupContext} from "./contexts/PopupContext";
import {UserContext} from "./contexts/UserContext";
import 'react-toastify/dist/ReactToastify.css';


function App() {
    const {data: currentUserData, loading, refetch} = useQuery(QUERY_CURRENT_USER);
    const userContext = {user: currentUserData?.user, updateUser: refetch};

    const [popupOpen, setPopupOpen] = useState(false);
    const popupContext = {
        isOpen: popupOpen,
        setIsOpen: setPopupOpen,
    };

    return (
        <div className="App">
            <ToastContainer/>
            <UserContext.Provider value={userContext}>
                <PopupContext.Provider value={popupContext}>
                    {loading ? <div className="loader"></div> : (
                        <Routes>
                            <Route path="/redirect" element={<Redirect/>} exact={false}/>
                            <Route path="/authorized" element={<Redirect/>} exact={false}/>
                            <Route element={<ProtectedRoutes/>}>
                                <Route path="/profile" element={<Profile/>}/>
                                <Route path="/main" element={<MainLayout/>}/>
                                <Route path="/people" element={<PeopleLayout/>}/>
                                <Route path="/friends" element={<FriendsLayout/>}/>
                            </Route>
                            <Route path="*" element={<LoginPage/>}/>
                        </Routes>
                    )}
                </PopupContext.Provider>
            </UserContext.Provider>
        </div>
    );
}

export default App;
