import {FormControlLabel, Grid, Switch, Typography} from "@mui/material";
import {CategoryItem} from "../CategoryItem";
import {FC, useState} from "react";
import {NewCategoryFrom} from "../NewCategoryFrom";
import {MAX_CATEGORIES_COUNT} from "../../const/constants.ts";
import {Categories} from "../../types/Category.ts";

interface CategorySectionInterface {
    categories: Categories;
}

export const CategorySection: FC<CategorySectionInterface> = ({categories}) => {

    const [showArchived, setShowArchived] = useState<boolean>(false);
    const isUnarchiveCategoriesEnabled = categories.filter(category => !category.archived).length < MAX_CATEGORIES_COUNT;

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
            <NewCategoryFrom
                isDisabled={
                    !isUnarchiveCategoriesEnabled}
            />
            {showArchived
                ?
                categories.map((category) => (
                    (<Grid
                        item
                        xs={12}
                        key={category?.id}
                    >
                        <CategoryItem
                            category={category}
                            isUnarchiveEnabled={isUnarchiveCategoriesEnabled}/>
                    </Grid>)
                ))
                :
                categories.map((category) => (!category.archived &&
                    (<Grid item xs={12} key={category?.id}>
                        <CategoryItem
                            category={category}
                            isUnarchiveEnabled={isUnarchiveCategoriesEnabled}/>
                    </Grid>)
                ))
            }
        </Grid>
    )
}
