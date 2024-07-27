import {Grid,  Typography} from "@mui/material";
import {CategoryItem} from "../CategoryItem";
import {useEffect, useState} from "react";
import {apiClient} from "../../api/apiClient.ts";
import {NewCategoryFrom} from "../NewCategoryFrom";
import {Category} from "../../types/Category.ts";

export const CategorySection = () => {

    const [categories, setCategories] = useState<Category[]>([]);

    const fetchCategories = () =>  apiClient.getCategories({
            onSuccess: (res) => setCategories(res),
            onFailure: (e) => {},
        }
    );

    useEffect(() =>{
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
            <Grid item xs={12}>
                <Typography
                    variant="h5"
                    component="h2"
                >
                    Categories
                </Typography>
            </Grid>
            <NewCategoryFrom refetchCategories={fetchCategories}/>
            {
                categories.map((category) => (
                    <Grid item xs={12} key={category?.id}>
                        <CategoryItem name={category?.category}/>
                    </Grid>
                ))
            }
        </Grid>
    )
}