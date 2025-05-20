import {Grid, Tooltip} from "@mui/material";
import {FC, FormEvent, useState} from "react";
import {apiClient} from "../../api/apiClient.ts";
import {useSnackBar} from "../../context/SnackBarContext.tsx";
import ErrorOutlineOutlinedIcon from '@mui/icons-material/ErrorOutlineOutlined';
import {Input} from "../Input";

interface NewCategoryFromProps {
    refetchCategories: () => void,
    isDisabled: boolean
}

export const NewCategoryFrom: FC<NewCategoryFromProps> = ({refetchCategories, isDisabled}) => {

    const [newCategory, setNewCategory] = useState<string>("");
    const [error, setError] = useState(false);
    const snackbar = useSnackBar();

    const onSubmit = (e: FormEvent) => {
        e.preventDefault();
        if (newCategory?.length > 1) {
            apiClient.addCategory(newCategory, {
                onSuccess: () => {
                    snackbar.showSnackBar(`You've added new category: ${newCategory}`, "success");
                    refetchCategories();
                    setNewCategory("");
                },
                onFailure: (error) => {
                    snackbar.showSnackBar(`Error while adding category ${newCategory}: ${error.message}`, "error");
                }
            });
        } else {
            setError(true);
        }
    };

    return (
        <Grid item xs={12}
              component="form"
              onSubmit={onSubmit}
              sx={{
                  position: "relative",
                  marginTop: 2
                 }}
        >
            <Input
                id="category"
                name="category"
                type="text"
                value={newCategory}
                error={error}
                helperText={error ? "Allowed category length is from 2 to 50 symbols" : ""}
                onChange={
                    e => {
                        setNewCategory(e.target.value);
                        setError(false);
                    }
                }
                disabled={isDisabled}
                placeholder={"Add new category"}
            />
            {
                isDisabled &&
                <Tooltip title={"You've reached maximum available count of active categories"}>
                    <ErrorOutlineOutlinedIcon color={"info"} sx={{
                        position: "absolute",
                        top: "30px",
                        right: "15px",
                    }}/>
                </Tooltip>
            }
        </Grid>
    );
}
