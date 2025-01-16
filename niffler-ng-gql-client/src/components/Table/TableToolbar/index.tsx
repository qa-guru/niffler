import {Toolbar} from "@mui/material";
import {FC} from "react";
import {SearchInput} from "../../SearchInput";

interface TableToolbarProps {
    onSearchSubmit: (value: string) => void;
}

export const TableToolbar: FC<TableToolbarProps> = ({onSearchSubmit}) => {
    return (
        <Toolbar>
            <SearchInput onSearchSubmit={onSearchSubmit}/>
        </Toolbar>
    );
}
