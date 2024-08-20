import * as React from 'react';
import {FC, useEffect, useState} from 'react';
import FormControl from '@mui/material/FormControl';
import {Box, Chip, List, MenuItem} from '@mui/material';
import {apiClient} from "../../api/apiClient.ts";
import {useSnackBar} from "../../context/SnackBarContext.tsx";
import {MAX_CATEGORIES_COUNT} from "../../const/constants.ts";
import {Input} from "../Input";

interface CategorySelectInterface {
    selectedCategory: string;
    onSelectCategory: (category: string) => void;
    error: boolean;
    helperText: string;
}

export const CategorySelect: FC<CategorySelectInterface> = ({
                                                                selectedCategory = "",
                                                                onSelectCategory,
                                                                error,
                                                                helperText
                                                            }) => {
    const snackbar = useSnackBar();
    const [allCategories, setAllCategories] = useState<string[]>([]);

    useEffect(() => {
        apiClient.getCategories({
            onSuccess: (data) => {
                const categories = data.map(v => v.name);
                setAllCategories(categories);
            },
            onFailure: (e) => {
                snackbar.showSnackBar("Failed to fetch categories", "error");
                console.error(`Failed to fetch categories: ${e.message}`);
            },
        }, true);
    }, []);

    const handlePressEnter = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.key === "Enter") {
            event.stopPropagation();
            event.preventDefault();
            const value = (event.target as HTMLInputElement).value
            onSelectCategory(value);
            if (!allCategories.includes(value)) {
                pushToCategories(value);
            }
        }
    };

    const pushToCategories = (category: string) => {
        if (allCategories.length < MAX_CATEGORIES_COUNT) {
            setAllCategories([...allCategories, category]);
        }
    }

    const isCategorySelected = (category: string) => {
        return selectedCategory === category;
    }

    return (
        <Box sx={{
            display: "flex",
            alignItens: "center",
        }}>
            <FormControl sx={{width: "100%"}}>
                <Input
                    id="category"
                    name="category"
                    value={selectedCategory}
                    onChange={(e) => onSelectCategory(e.target.value)}
                    onKeyDown={handlePressEnter}
                    error={error}
                    helperText={helperText}
                    disabled={allCategories.length >= MAX_CATEGORIES_COUNT}
                    placeholder={"Add new category"}
                    type="text"
                />
                <List sx={{
                    display: "flex",
                    flexWrap: "wrap",
                    marginTop: 1,
                }}>
                    {allCategories.map((name) => (
                        <MenuItem key={name} sx={{margin: "4px", padding: 0, borderRadius: "20px"}}>
                            <Box sx={{display: "flex", flexWrap: "wrap", gap: 0.5}}>
                                <Chip key={name} label={name} tabIndex={0} onClick={() => onSelectCategory(name)}
                                      color={isCategorySelected(name) ? "primary" : "default"}/>
                            </Box>
                        </MenuItem>
                    ))}
                </List>
            </FormControl>
        </Box>
    );
}
