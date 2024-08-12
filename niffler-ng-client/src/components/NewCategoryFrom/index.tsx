import {Grid, TextField, Tooltip} from "@mui/material";
import {FC, FormEvent, useState} from "react";
import {apiClient} from "../../api/apiClient.ts";
import {useSnackBar} from "../../context/SnackBarContext.tsx";

interface NewCategoryFromProps {
    refetchCategories: () => Promise<void>,
    isDisabled: boolean
}

export const NewCategoryFrom: FC<NewCategoryFromProps> = ({refetchCategories, isDisabled}) => {

    const [newCategory, setNewCategory] = useState<string>("");
    const snackbar = useSnackBar();

    const onSubmit = (e: FormEvent) => {
        e.preventDefault();
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
    };

    return (
        <Grid item xs={12}
              component="form"
              onSubmit={onSubmit}
        >
            <Tooltip title={isDisabled ? "You've reached maximum available count of active categories" : ""}>
                <TextField
                    id="category"
                    name="category"
                    type="text"
                    value={newCategory}
                    onChange={e => setNewCategory(e.target.value)}
                    error={false}
                    helperText={""}
                    disabled={isDisabled}
                    fullWidth
                    placeholder={"Add new category"}
                />
            </Tooltip>
        </Grid>
    );
}
