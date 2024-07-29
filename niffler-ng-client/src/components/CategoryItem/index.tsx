import {FC, useState} from "react";
import {Box, Chip, IconButton, TextField, useTheme} from "@mui/material";
import EditIcon from "../../assets/icons/ic_edit.svg?react";
import ArchiveIcon from "../../assets/icons/ic_archive.svg?react";
import {useDialog} from "../../context/DialogContext.tsx";
import CrossIcon from "../../assets/icons/ic_cross.svg?react";

interface CategoryItemInterface {
    name: string;
}

export const CategoryItem: FC<CategoryItemInterface> = ({name}) => {
    const dialog = useDialog();
    const [isEdit, setEdit] = useState<boolean>(false);
    const [editValue, setEditValue] = useState(name);
    const theme = useTheme();

    const handleArchiveClick = (category: string) => {
        dialog.showDialog({
            title: "Archive category",
            description: `Do you really want to archive ${category}? After this change it won't be available while creating spends`,
            onSubmit: () => {
            },
            submitTitle: "Archive",
        });
    }

    const handleChangeCategoryName = () => {

    }

    return (
        <Box sx={{
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
        }}>
            {isEdit ? (
                <Box
                    component={"form"}
                    onSubmit={handleChangeCategoryName}
                    sx={{
                        display: "flex",
                        width: "100%",
                        position: "relative",
                    }}
                >
                    <TextField
                        id="category"
                        name="category"
                        type="text"
                        value={editValue}
                        onChange={e => setEditValue(e.target.value)}
                        error={false}
                        helperText={""}
                        fullWidth
                        placeholder={"Edit category"}
                    />
                    <IconButton
                        aria-label="close"
                        onClick={() => setEdit(false)}
                        sx={{
                            position: "absolute",
                            zIndex: 1000,
                            right: 5,
                            top: 4,
                            color: (theme) => theme.palette.primary.main,
                        }}
                    >
                        <CrossIcon />
                    </IconButton>
                </Box>
                )
                : (
                    <>
                        <Chip label={name}/>
                        <Box>
                            <IconButton color="primary" aria-label="Edit category" onClick={() => {
                                setEdit(true)
                            }}>
                                <EditIcon color={theme.palette.gray_600.main}/>
                            </IconButton>
                            <IconButton color="primary" aria-label="Archive category" onClick={() => {
                                handleArchiveClick(name)
                            }}>
                                <ArchiveIcon color={theme.palette.gray_600.main}/>
                            </IconButton>
                        </Box>
                    </>
                )}
        </Box>
    )
}