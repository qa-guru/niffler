import {getData} from "../../api/api";
import {useLoadedData} from "../../api/hooks";
import {UserContext} from "../../contexts/UserContext";
import {AddSpending} from "../AddSpending";
import {Header} from "../Header";
import {SpendingHistory} from "../SpendingHistory";
import {useContext, useState} from "react";
import {SpendingStatistics} from "../SpendingStatistics";

export const MainLayout = ({showSuccess}) => {
    const [spendings, setSpendings] = useState([]);
    const [categories, setCategories] = useState({});
    const [statistic, setStatistic] = useState([]);

    const { user, setUser } = useContext(UserContext);

    useLoadedData({
            path: "/categories",
            onSuccess: (data) => {
                setCategories(Array.from(data.map((v) => {
                    return {value: v?.description, label: v?.description}
                })));
            },
            onFail: (error) => {
                console.log(error);
            },
        }
    );
    useLoadedData({
            path: "/spends",
            onSuccess: (data) => {
                setSpendings(data);
            },
            onFail: (error) => {
                console.log(error);
            },
        }
    );

    useLoadedData({
            path: "/statistic",
            onSuccess: (data) => {
                setStatistic(data);
            },
            onFail: (error) => {
                console.log(error);
            },
        }
    );


    const addNewSpendingInTableCallback = (data) => {
        const newSpendings = [...spendings];
        newSpendings.push(data);
        setSpendings(newSpendings);
        getData({
            path: "/statistic",
            onSuccess: (data) => {
                setStatistic(data);
                showSuccess("Spending successfully added!")
            },
            onFail: (error) => {
                console.log(error);
            },
        });
    }

    return (
        <div className={"main-container"}>
            <Header />
            <main className={"main"}>
                <div className={"main-content"}>
                    <AddSpending addSpendingCallback={addNewSpendingInTableCallback} categories={categories} />
                    <SpendingStatistics currency={user?.currency} statistic={statistic} spendings={spendings}/>
                    <SpendingHistory spendings={spendings}/>
                </div>
            </main>
            <footer className={"footer"}>
                Study project for QA Automation Advanced. 2023
            </footer>
        </div>
    )
}
