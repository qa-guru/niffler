import {BarElement, CategoryScale, Chart as ChartJS, Legend, LinearScale, Title, Tooltip} from "chart.js";
import {Bar} from "react-chartjs-2";
import {Button} from "../Button";
import {SpendingTable} from "../SpendingTable";

export const SpendingHistory = ({spendings}) => {

    return (
        <section className={"main-content__section main-content__section-history"}>
            <h2>History of spendings</h2>
            <div className={"spendings__content"}>
                <div className={"spendings__controls"}>
                    <div>
                        <cite className="cite">"All the limits are in your head"</cite>
                        <div className="spendings__buttons">
                            <Button small type="button" buttonText={"Today"}/>
                            <Button small type="button" buttonText={"Yesterday"}/>
                            <Button small type="button" buttonText={"Last week"}/>
                            <Button small type="button" buttonText={"Last month"}/>
                        </div>
                    </div>
                    <img className={"spendings__img"} src="/images/gringotts2.jpeg" width={250} alt={"Image of Gringotts"}></img>
                </div>
                <SpendingTable spendings={spendings}/>
            </div>
        </section>
    );
}
