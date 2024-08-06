import {Grid, TextField} from "@mui/material";
import {FC, FormEvent, useState} from "react";
import {apiClient} from "../../api/apiClient.ts";
import {useSnackBar} from "../../context/SnackBarContext.tsx";

interface NewCategoryFromProps {
    refetchCategories: () => Promise<void>,
}

export const NewCategoryFrom: FC<NewCategoryFromProps> = ({refetchCategories}) => {

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
            <TextField
                id="category"
                name="category"
                type="text"
                value={newCategory}
                onChange={e => setNewCategory(e.target.value)}
                error={false}
                helperText={""}
                fullWidth
                placeholder={"Add new category"}
            />
        </Grid>
    );
}