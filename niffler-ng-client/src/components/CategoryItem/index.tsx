import {FC, FormEvent, useState} from "react";
import {Box, Chip, IconButton, Tooltip, useTheme} from "@mui/material";
import EditIcon from "../../assets/icons/ic_edit.svg?react";
import ArchiveIcon from "../../assets/icons/ic_archive.svg?react";
import {useDialog} from "../../context/DialogContext.tsx";
import CrossIcon from "../../assets/icons/ic_cross.svg?react";
import {apiClient} from "../../api/apiClient.ts";
import {Category} from "../../types/Category.ts";
import {useSnackBar} from "../../context/SnackBarContext.tsx";
import UnarchiveOutlinedIcon from '@mui/icons-material/UnarchiveOutlined';
import {Input} from "../Input";

interface CategoryItemInterface {
    category: Category;
    onUpdateCategory: () => void;
    isUnarchiveEnabled: boolean;
}

export const CategoryItem: FC<CategoryItemInterface> = ({category, onUpdateCategory, isUnarchiveEnabled}) => {
    const dialog = useDialog();
    const snackbar = useSnackBar();
    const [isEdit, setEdit] = useState<boolean>(false);
    const [editValue, setEditValue] = useState(category);
    const [error, setError] = useState(false);
    const theme = useTheme();

    const handleArchiveClick = (category: Category) => {
        dialog.showDialog({
            title: "Archive category",
            description: `Do you really want to archive ${category.name}? After this change it won't be available while creating spends`,
            onSubmit: () => {
                apiClient.editCategory({
                        ...category,
                        archived: true,
                    }, {
                        onSuccess: () => {
                            onUpdateCategory();
                            snackbar.showSnackBar(`Category ${category.name} is archived`, "success");
                        },
                        onFailure: (e) => {
                            snackbar.showSnackBar(`Can not archive category ${category.name}`, "error");
                            console.error(e.message)
                        }
                    }
                )
            },
            submitTitle: "Archive",
        });
    };

    const handleUnarchiveClick = (category: Category) => {
        dialog.showDialog({
            title: "Unarchive category",
            description: `Do you really want to unarchive category ${category.name}?`,
            onSubmit: () => {
                apiClient.editCategory({
                        ...category,
                        archived: false,
                    }, {
                        onSuccess: () => {
                            onUpdateCategory();
                            snackbar.showSnackBar(`Category ${category.name} is unarchived`, "success");
                        },
                        onFailure: (e) => {
                            snackbar.showSnackBar(`Can not unarchive category ${category.name}`, "error");
                            console.error(e.message)
                        }
                    }
                )
            },
            submitTitle: "Unarchive",
        });
    };

    const handleChangeCategoryName = (e: FormEvent) => {
        e.preventDefault();
        if (editValue.name?.length > 1) {
            apiClient.editCategory(editValue, {
                    onSuccess: () => {
                        onUpdateCategory();
                        setEdit(false);
                        snackbar.showSnackBar("Category name is changed", "success");

                    },
                    onFailure: (e) => {
                        snackbar.showSnackBar("Can not change category name", "error");
                        console.error(e.message)
                    }
                }
            );
        } else {
            setError(true);
        }
    };

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
                            width: "100%",
                            position: "relative",
                        }}
                    >
                        <Input
                            id="category"
                            name="category"
                            type="text"
                            value={editValue.name}
                            error={error}
                            helperText={error ? "Allowed category length is from 2 to 50 symbols" : ""}
                            onChange={e => {
                                setEditValue({...category, name: e.target.value});
                                setError(false);
                            }}
                            placeholder={"Edit category"}
                        />
                        <IconButton
                            aria-label="close"
                            onClick={() => {
                                setEdit(false);
                                setEditValue(category);
                            }}
                            sx={{
                                position: "absolute",
                                zIndex: 1000,
                                right: 5,
                                top: 4,
                                color: (theme) => theme.palette.primary.main,
                            }}
                        >
                            <CrossIcon/>
                        </IconButton>
                    </Box>
                )
                : (

                    <>
                        <Chip
                            label={category.name}
                            color={category.archived ? "default" : "primary"}
                            onClick={() => {
                                if (!category.archived) {
                                    setEdit(true);
                                }
                            }}
                            tabIndex={category.archived ? -1 : 0}
                            sx={{
                                cursor: `${category.archived ? "default" : "pointer"}`
                            }}
                        />
                        {category.archived ? (
                            <Tooltip
                                title={isUnarchiveEnabled ? "Unarchive category" : "You've riched limit of active categories amount. Archive other category to enable this one"}>
                                  <span>
                                    <IconButton
                                        color="primary"
                                        aria-label="Unarchive category"
                                        onClick={() => {
                                            handleUnarchiveClick(editValue)
                                        }}
                                        disabled={!isUnarchiveEnabled}
                                    >
                                        <UnarchiveOutlinedIcon color="info"/>
                                    </IconButton>
                                  </span>
                            </Tooltip>
                        ) : (
                            <Box>
                                <Tooltip title={"Edit category name"}>
                                    <IconButton color="primary" aria-label="Edit category" onClick={() => {
                                        setEdit(true)
                                    }}>
                                        <EditIcon color={theme.palette.gray_600.main}/>
                                    </IconButton>
                                </Tooltip>
                                <Tooltip title={"Archive category"}>
                                    <IconButton color="primary" aria-label="Archive category" onClick={() => {
                                        handleArchiveClick(editValue)
                                    }}>
                                        <ArchiveIcon color={theme.palette.gray_600.main}/>
                                    </IconButton>
                                </Tooltip>
                            </Box>
                        )}
                    </>
                )}
        </Box>
    )
}
