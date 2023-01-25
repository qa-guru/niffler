
import {useContext} from "react";
import {CurrencyContext} from "../../contexts/CurrencyContext";
import {FilterContext} from "../../contexts/FilterContext";
import {Button} from "../Button";
import {ButtonIcon} from "../ButtonIcon";
import {FormSelect} from "../FormSelect";
import {SpendingTable} from "../SpendingTable";

export const SpendingHistory = ({spendings, currencies}) => {

    const {filter, setFilter} = useContext(FilterContext);
    const {selectedCurrency, setSelectedCurrency} = useContext(CurrencyContext);

    return (
        <section className={"main-content__section main-content__section-history"}>
            <h2>History of spendings</h2>
            <div className={"spendings__content"}>
                <div className={"spendings__controls"}>
                    <div>
                        <cite className="cite">"All the limits are in your head"</cite>
                        <div className="spendings__filters">
                            <div className="spendings__buttons">
                                <Button small type="button" buttonText={"Today"} onClick={() => setFilter("TODAY")}/>
                                <Button small type="button" buttonText={"Last week"} onClick={() =>{setFilter("WEEK")}}/>
                                <Button small type="button" buttonText={"Last month"} onClick={() =>{setFilter("MONTH")}}/>
                                <Button small type="button" buttonText={"All time"} onClick={() => setFilter("ALL")}/>
                            </div>
                            <div style={{width: 120, marginRight: 20}}>
                                <FormSelect options={currencies}
                                            placeholder="Select currency"
                                            value={selectedCurrency}
                                            onChange={(currency) => {
                                                setSelectedCurrency(currency)}}
                                />
                            </div>
                            {(filter !== null || selectedCurrency.value !== "ALL") &&
                                (<ButtonIcon iconType={"close"} onClick={() => {
                                    setFilter(null);
                                    setSelectedCurrency({value: "ALL", label: "ALL"});
                                }}/>)
                            }
                        </div>
                    </div>
                    <img className={"spendings__img"} src="/images/gringotts2.jpeg" width={250} alt={"Image of Gringotts"}></img>
                </div>
                <SpendingTable spendings={spendings}/>
            </div>
        </section>
    );
}
