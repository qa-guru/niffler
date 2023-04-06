import {useEffect, useState} from "react";
import {getData, postData} from "../../api/api";
import {showError, showSuccess} from "../../toaster/toaster";
import {Button} from "../Button";
import {FormInput} from "../FormInput";

export const Categories = () => {

    const [newCategory, setNewCategory] = useState("");
    const [categories, setCategories] = useState([]);

    useEffect(() => {
        getData({
            path: "/categories",
            onSuccess: (data) => {
                setCategories(data);
            },
            onFail: (err) => {
                console.log(err);
            }
        });
    }, []);


    const addNewSpendCategory = (e) => {
        e.preventDefault();
        postData({
            path: "/category",
            data: {
                category: newCategory,
            },
            onSuccess: (data) => {
                setCategories([...categories, data]);
                setNewCategory("");
                showSuccess("New category added!");
            },
            onFail: (err) => {
                console.log(err);
                showError("Can not add new category");
            }
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
                {categories?.length > 0 ? (
                    <ul className="categories__list">
                        {categories.map(item => (
                            <li key={item.id} className="categories__item">{item.category}</li>
                        ))}
                    </ul>
                ) : (<span>No spending categories yet!</span>)}
            </div>
        </section>);
}
