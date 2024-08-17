import {BrowserRouter, Route, Routes} from "react-router-dom";
import {PrivateRoute} from "../PrivateRoute";
import {AuthorizedPage} from "../../pages/Authorized";
import {MainPage} from "../../pages/Main";
import {ProfilePage} from "../../pages/ProfilePage";
import {PeoplePage} from "../../pages/PeoplePage";
import {FC} from "react";
import {SpendingPage} from "../../pages/SpendingPage";
import {NotFoundPage} from "../../pages/NotFoundPage";

export const AppContent: FC = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/authorized" element={<AuthorizedPage/>}/>
                <Route element={<PrivateRoute/>}>
                    <Route path="/" element={<MainPage/>}/>
                    <Route path="/main" element={<MainPage/>}/>
                    <Route path="/profile" element={<ProfilePage/>}/>
                    <Route path="/spending" element={<SpendingPage/>}/>
                    <Route path="/spending/:id" element={<SpendingPage/>}/>
                    <Route path="/people">
                        <Route path="/people/all" element={<PeoplePage activeTab={"all"}/>}/>
                        <Route path="/people/friends" element={<PeoplePage activeTab={"friends"}/>}/>
                    </Route>
                    <Route path="*" element={<NotFoundPage/>}/>
                </Route>
            </Routes>
        </BrowserRouter>
    );
}
