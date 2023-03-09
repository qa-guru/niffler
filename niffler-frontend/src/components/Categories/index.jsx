import {useMutation, useQuery} from "@apollo/client";
import {useEffect, useState} from "react";
import {getData, postData} from "../../api/api";
import {qraphQlClient} from "../../api/graphql/graphql";
import {CREATE_CATEGORY_MUTATION} from "../../api/graphql/mutations";
import {QUERY_ALL_CATEGORIES} from "../../api/graphql/queries";
import {showError, showSuccess} from "../../toaster/toaster";
import {Button} from "../Button";
import {FormInput} from "../FormInput";

export const Categories = () => {

    const [newCategory, setNewCategory] = useState("");
    const {data: categoriesData, loading: categoriesLoading, error: categoriesError} = useQuery(QUERY_ALL_CATEGORIES);
    const [addCategory, { data, loading, error }] = useMutation(CREATE_CATEGORY_MUTATION);

    const addNewSpendCategory = (e) => {
        e.preventDefault();
        addCategory(
            { variables:
                    { input: {
                        category: newCategory
                    }}}).then(res => {
            console.log(res);

        });
        // postData({
        //     path:"/category",
        //     data: {
        //         category: newCategory,
        //     },
        //     onSuccess: (data) => {
        //         setNewCategory("");
        //         showSuccess("New category added!");
        //     },
        //     onFail: (err) => {
        //         console.log(err);
        //         showError("Can not add new category");
        //     }
        // });
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
