import {useContext} from "react";
import {Navigate} from 'react-router-dom';
import {UserContext} from "../../contexts/UserContext";

export const ProtectedRoute = ({ children}) => {
    const { user, setUser } = useContext(UserContext);

    if (!user) {
        return <Navigate to="/" replace/>;
    }
    return children;
};
