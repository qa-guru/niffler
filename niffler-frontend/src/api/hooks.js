import {useEffect} from "react";
import { getData} from "./api";

const useLoadedData = ({path, onSuccess, onFail, params }) => {
    useEffect(() => {
        getData({path, onSuccess, onFail, params});
    }, []);
};


export {useLoadedData};
