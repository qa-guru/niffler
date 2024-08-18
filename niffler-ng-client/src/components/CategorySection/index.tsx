import {FormControlLabel, Grid, Switch, Typography} from "@mui/material";
import {CategoryItem} from "../CategoryItem";
import {useEffect, useState} from "react";
import {apiClient} from "../../api/apiClient.ts";
import {NewCategoryFrom} from "../NewCategoryFrom";
import {Category} from "../../types/Category.ts";
import {MAX_CATEGORIES_COUNT} from "../../const/constants.ts";
import {useSnackBar} from "../../context/SnackBarContext.tsx";

export const CategorySection = () => {

    const [categories, setCategories] = useState<Category[]>([]);
    const [showArchived, setShowArchived] = useState<boolean>(false);
    const snackbar = useSnackBar();

    const isUnarchiveCategoriesEnabled = categories.filter(category => !category.archived).length < MAX_CATEGORIES_COUNT;
    const fetchCategories = () => apiClient.getCategories({
            onSuccess: (res) => setCategories(res),
            onFailure: (e) => {
                console.error(e.message);
                snackbar.showSnackBar(e.message, "error");
            },
        }
    );

    useEffect(() => {
        fetchCategories();
    }, []);

    return (
        <Grid
            container
            spacing={2}
            sx={{
                maxWidth: "624px",
                margin: "5px auto"
            }}
        >
            <Grid item xs={12} sx={{
                display: "flex",
                width: "100%",
                alignItems: "center",
                justifyContent: "space-between",
            }}>
                <Typography
                    variant="h5"
                    component="h2"
                    sx={{
                        marginRight: 2,
                    }}
                >
                    Categories
                </Typography>
                <FormControlLabel
                    control={<Switch checked={showArchived} onChange={() => setShowArchived(!showArchived)}/>}
                    label="Show archived" sx={{textAlign: "end"}}/>
            </Grid>
            <NewCategoryFrom refetchCategories={fetchCategories} isDisabled={
                !isUnarchiveCategoriesEnabled}
            />
            {showArchived
                ?
                categories.map((category) => (
                    (<Grid item xs={12} key={category?.id}>
                        <CategoryItem category={category} onUpdateCategory={fetchCategories}
                                      isUnarchiveEnabled={isUnarchiveCategoriesEnabled}/>
                    </Grid>)
                ))
                :
                categories.map((category) => (!category.archived &&
                    (<Grid item xs={12} key={category?.id}>
                        <CategoryItem category={category} onUpdateCategory={fetchCategories}
                                      isUnarchiveEnabled={isUnarchiveCategoriesEnabled}/>
                    </Grid>)
                ))
            }
        </Grid>
    )
}
