import {useEffect} from "react";
import { getData} from "./api";

const useLoadedData = ({path, onSuccess, onFail }) => {
    useEffect(() => {
        getData({path, onSuccess, onFail});
    }, []);
};


export {useLoadedData};
