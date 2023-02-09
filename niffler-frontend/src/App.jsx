import { useState} from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {ToastContainer} from "react-toastify";
import {LoginPage} from "./components/LoginPage";
import {MainLayout} from "./components/MainLayout";
import {Profile} from "./components/Profile";
import {ProtectedRoute} from "./components/ProtectedRoute";
import {Redirect} from "./components/Redirect";
import {UserContext} from "./contexts/UserContext";
import 'react-toastify/dist/ReactToastify.css';


function App() {

    const [user, setUser] = useState(null);
    const value = { user, setUser };

    return (
        <div className="App">
            <ToastContainer/>
            <BrowserRouter>
                <UserContext.Provider value={value}>
                        <Routes>
                               <Route path="/redirect" element={<Redirect />} exact={false}/>
                               <Route path="/authorized" element={<Redirect />} exact={false}/>
                               <Route path="/profile" element={
                                   <ProtectedRoute>
                                       <Profile/>
                                   </ProtectedRoute>
                               }/>
                               <Route path="/main" element={
                                   <ProtectedRoute>
                                       <MainLayout />
                                   </ProtectedRoute>
                               }/>
                               <Route path="*" element={<LoginPage/>}/>
                        </Routes>
                </UserContext.Provider>
            </BrowserRouter>
        </div>
    );
}

export default App;
