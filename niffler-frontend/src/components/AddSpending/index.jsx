import dayjs from "dayjs";
import {useState} from "react";
import {postData} from "../../api/api";
import {MAX_TEXT_INPUT_FIELD_LENGTH} from "../../constants/const";
import {showError, showSuccess} from "../../toaster/toaster";
import {Button} from "../Button/index";
import {FormCalendar} from "../FormCalendar";
import {FormInput} from "../FormInput";
import {FormSelect} from "../FormSelect";

const initialSpendingState = {
    amount: "",
    description: "",
    category: null,
    spendDate: new Date(),
};

const initialErrorState = {
    amount: null,
    spendDate: null,
    category: null,
    description: null,
};

export const AddSpending = ({categories, addSpendingCallback}) => {
    const [data, setData] = useState(initialSpendingState);
    const [formErrors, setFormErrors] = useState(initialErrorState);

    const validateCategory = () => {
        if (!data.category) {
            setFormErrors({...formErrors, category: "Category is required"});
            return false;
        }
        return true;
    };
    const validateAmount = () => {
        if (!data.amount) {
            setFormErrors({...formErrors, amount: "Amount is required"});
            return false;
        } else if (data.amount <= 0) {
            setFormErrors({...formErrors, amount: "Amount should be greater than 0"});
            return false;
        }
        return true;
    };

    const validateSpendDate = () => {
        if(!data.spendDate) {
            setFormErrors({...formErrors, spendDate: "Spend date is required"});
            return false;
        }
        else if(dayjs(data.spendDate).isAfter(new Date())) {
            setFormErrors({...formErrors, spendDate: "You can not pick future date"});
            return false;
        }
        return true;
    };

    const validateDescription = () => {
        if(data?.description?.length > MAX_TEXT_INPUT_FIELD_LENGTH) {
            setFormErrors({
                ...formErrors,
                description: `Description length can not be greater than ${MAX_TEXT_INPUT_FIELD_LENGTH} characters`
            });
            return false;
        }
        return true;
    };

    const isFormValid = () => {
        return validateCategory()
            && validateAmount()
            && validateSpendDate()
            && validateDescription();
    };

    const handleAddSpendingSubmit = (e) => {
        e.preventDefault();
        console.log(isFormValid());
        if(isFormValid()) {
            const dataToSend = {...data, category: data.category?.value};
            postData({
                path: "/addSpend",
                data: dataToSend,
                onSuccess: (data) => {
                    addSpendingCallback(data);
                    setData(initialSpendingState);
                    setFormErrors(initialErrorState);
                    showSuccess("Spending successfully added!");
                },
                onFail: (err) => {
                    showError("Can not add spending!");
                    console.error(err);
                }
            });
        }
    }

    return (
        <section className={"main-content__section main-content__section-add-spending"}>
            <h2>Add new spending</h2>
            <form onSubmit={handleAddSpendingSubmit} className={"add-spending__form"}>
                <FormSelect label="Category"
                            placeholder={"Choose spending category"}
                            options={categories}
                            required
                            value={data.category}
                            error={formErrors.category}
                            onChange={(category) => {
                                setFormErrors({...formErrors, category: null})
                                setData({...data, category})
                            }
                }/>
                <FormInput placeholder={"Set Amount"}
                           label={"Amount"}
                           type="number"
                           required
                           value={data.amount}
                           error={formErrors.amount}
                           handleChangeValue={(evt) => {
                               setFormErrors({...formErrors, amount: null})
                               setData({...data, amount: evt.target.value})
                           }}/>
                <FormCalendar label={"Spend date"}
                              value={data.spendDate}
                              error={formErrors.spendDate}
                              required
                              onChange={(spendDate) => {
                                  setFormErrors({...formErrors, spendDate: null})
                                  setData({...data, spendDate})
                              }}/>
                <FormInput placeholder={"Spending description"}
                           label={"Description"}
                           type="text"
                           max={255}
                           value={data.description}
                           error={formErrors.description}
                           handleChangeValue={(evt) => {
                               setFormErrors({...formErrors, description: null})
                               setData({...data, description: evt.target.value})
                           }}/>
                <Button buttonText={"Add new spending"}/>
            </form>
        </section>
    );
}


