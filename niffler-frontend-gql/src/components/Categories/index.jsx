import {gql, useMutation, useQuery} from "@apollo/client";
import {useState} from "react";
import {CREATE_CATEGORY_MUTATION} from "../../api/graphql/mutations";
import {QUERY_ALL_CATEGORIES} from "../../api/graphql/queries";
import {showError, showSuccess} from "../../toaster/toaster";
import {Button} from "../Button";
import {FormInput} from "../FormInput";

export const Categories = () => {

    const [newCategory, setNewCategory] = useState("");
    const {data: categoriesData, loading: categoriesLoading, error: categoriesError} = useQuery(QUERY_ALL_CATEGORIES);
    const [createCategory, {data, loading, error}] = useMutation(CREATE_CATEGORY_MUTATION, {
        update(cache, {data: {createCategory}}) {
            cache.modify({
                fields: {
                    categories(existingCategories = []) {
                        const newCategoryRef = cache.writeFragment({
                            data: createCategory,
                            fragment: gql(`
                                fragment NewCategory on Category {
                                    id
                                    category
                                }
                            `)
                        });
                        return [...existingCategories, newCategoryRef];
                    }
                }
            });
        }
    });

    const addNewSpendCategory = (e) => {
        e.preventDefault();
        createCategory(
            {
                variables:
                    {
                        input: {
                            category: newCategory
                        }
                    }
            }).then(res => {
            if (res.data?.createCategory != null) {
                showSuccess("Category successfully created!");
            } else {
                showError("Category was not created!");
            }
        }).catch((err) => {
            showError("Category was not created!");
            console.log(err);
        });
    };

    return (
        <section className="main-content__section">
            <div className="main-content__section-add-category">
                <form onSubmit={addNewSpendCategory}>
                    <h2>Add new category</h2>
                    <p className="add-category__info">Note, that number of categories is limited!</p>
                    <p className="add-category__info">You can add not more than 8 different categories</p>
                    <div className="add-category__input-container">
                        <FormInput placeholder={"Add new category"}
                                   value={newCategory}
                                   label="Category name"
                                   fieldName={"category"}
                                   max={30}
                                   handleChangeValue={(evt) => setNewCategory(evt.target.value)}
                        />
                        <Button buttonText={"Create"}/>
                    </div>
                </form>
            </div>
            <div className="main-content__section-categories">
                <h3>All your spending categories</h3>
                {categoriesData?.categories?.length > 0 ? (
                    <ul className="categories__list">
                        {categoriesData.categories.map(item => (
                            <li key={item.id} className="categories__item">{item.category}</li>
                        ))}
                    </ul>
                ) : (<span>No spending categories yet!</span>)}
            </div>
        </section>);
}
