import {Grid, Tooltip} from "@mui/material";
import {FC, FormEvent, useState} from "react";
import {useSnackBar} from "../../context/SnackBarContext.tsx";
import ErrorOutlineOutlinedIcon from '@mui/icons-material/ErrorOutlineOutlined';
import {Input} from "../Input";
import {CategoriesDocument, Category, useUpdateCategoryMutation} from "../../generated/graphql.tsx";
import graphqlClient from "../../api/graphqlClient.ts";

interface NewCategoryFromProps {
    isDisabled: boolean
}

export const NewCategoryFrom: FC<NewCategoryFromProps> = ({isDisabled}) => {

    const [newCategory, setNewCategory] = useState<string>("");
    const [error, setError] = useState(false);
    const snackbar = useSnackBar();
    const [updateCategory] = useUpdateCategoryMutation();

    const onSubmit = (e: FormEvent) => {
        e.preventDefault();
        if (newCategory?.length > 1) {
            updateCategory({
                variables: {
                    input: {
                        name: newCategory,
                        archived: false,
                    }
                },
                errorPolicy: "none",
                onCompleted: (data) => {
                    snackbar.showSnackBar(`You've added new category: ${newCategory}`, "success");
                    graphqlClient.cache.updateQuery({
                        query: CategoriesDocument
                    }, (initialData) => {
                        const categories = initialData?.user.categories.map((cat: Category) => cat.name);
                        const category = data.category;
                        if (category && !categories?.find((cat: Category) => cat.name === data.category.name)) {
                            return {
                                ...initialData,
                                user: {
                                    ...initialData?.user,
                                    categories: [
                                        ...initialData.user.categories, category
                                    ]
                                }
                            }
                        }
                        return initialData
                    });
                    setNewCategory("");
                },
                onError: (err) => {
                    snackbar.showSnackBar(`Error while adding category ${newCategory}`, "error");
                    console.error(err);
                }
            },);
        } else {
            setError(true);
        }
    };

    return (
        <Grid item xs={12}
              component="form"
              onSubmit={onSubmit}
              sx={{position: "relative"}}
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
