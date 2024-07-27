import {FC, useState} from "react";
import {Box, Chip, IconButton, useTheme} from "@mui/material";
import EditIcon from "../../assets/icons/ic_edit.svg?react";
import ArchiveIcon from "../../assets/icons/ic_archive.svg?react";


interface CategoryItemInterface {
    name: string;
}
export const CategoryItem: FC<CategoryItemInterface> = ({name}) => {
    const [isEdit, setEdit] = useState<boolean>(false);
    const theme = useTheme();
    return(
        <Box sx={{
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
        }}>
            <Chip label={name}/>
            <Box>
                <IconButton color="primary" aria-label="Edit category" onClick={() => {setEdit(true)}}>
                    <EditIcon color={theme.palette.gray_600.main}/>
                </IconButton>
                <IconButton color="primary" aria-label="Archive category" onClick={() => {}}>
                    <ArchiveIcon color={theme.palette.gray_600.main}/>
                </IconButton>
            </Box>
        </Box>
    )
}