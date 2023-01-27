import {useState} from "react";
import {postData} from "../../api/api";
import {Button} from "../Button/index";
import {FormCalendar} from "../FormCalendar";
import {FormInput} from "../FormInput";
import {FormSelect} from "../FormSelect";


export const AddSpending = ({categories, addSpendingCallback}) => {

    const [data, setData] = useState({
        amount: "",
        description: "",
        category: null,
        spendDate: new Date(),
    });


    const handleAddSpendingSubmit = (e) => {
        e.preventDefault();
        const dataToSend = {...data, category: data.category?.value};
        postData({
            path: "/addSpend",
            data: dataToSend,
            onSuccess: (data) => {
                addSpendingCallback(data);
            },
            onFail: (err) => {
                console.log(err);
            }
        });
    }

    return (
        <section className={"main-content__section main-content__section-add-spending"}>
            <h2>Add new spending</h2>
            <form onSubmit={handleAddSpendingSubmit} className={"add-spending__form"}>
                <FormSelect label="Category:" placeholder={"Choose spending category"} options={categories}
                            value={data.category} onChange={(category) => {
                    setData({...data, category})
                }
                }/>
                <FormInput placeholder={"Set Amount"}
                           label={"Amount"}
                           type="number"
                           value={data.amount}
                           handleChangeValue={(evt) => setData({...data, amount: evt.target.value})}/>
                <FormCalendar label={"Spend date"}
                              value={data.spendDate}
                              onChange={(spendDate) => setData({...data, spendDate})}/>
                <FormInput placeholder={"Spending description"}
                           label={"Description"}
                           type="text"
                           max={255}
                           value={data.description}
                           handleChangeValue={(evt) => setData({...data, description: evt.target.value})}/>
                <Button buttonText={"Add new spending"}/>
            </form>
        </section>
    );
}


