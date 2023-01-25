import {getData} from "../../api/api";
import {useLoadedData} from "../../api/hooks";
import {CurrencyContext} from "../../contexts/CurrencyContext";
import {FilterContext} from "../../contexts/FilterContext";
import {UserContext} from "../../contexts/UserContext";
import {AddSpending} from "../AddSpending";
import {Header} from "../Header";
import {SpendingHistory} from "../SpendingHistory";
import {useContext, useEffect, useState} from "react";
import {SpendingStatistics} from "../SpendingStatistics";

export const MainLayout = ({showSuccess}) => {
    const [spendings, setSpendings] = useState([]);
    const [categories, setCategories] = useState({});
    const [statistic, setStatistic] = useState([]);
    const { user, setUser } = useContext(UserContext);

    const [filter, setFilter] = useState(null);
    const value = { filter, setFilter };

    const [currencies, setCurrencies] = useState([]);

    const [selectedCurrency, setSelectedCurrency] = useState({value: "ALL", label: "ALL"});
    const curContext = { selectedCurrency, setSelectedCurrency };


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
        path: "/allCurrencies",
        onSuccess: (data) => {
            const currencies = Array.from(data.map((v) => {
                return {value: v?.currency, label: v?.currency}
            }));
            currencies.push({value: "ALL", label: "ALL"});
            setCurrencies(currencies);
        },
        onFail: (err) => {
            console.log(err);
        }
    });

    useEffect(() => {
        getData({
            path:`/spends`,
            params: {
                filterPeriod: filter === "ALL" ? null : filter,
                filterCurrency: selectedCurrency?.value === "ALL" ? null : selectedCurrency?.value,
            },
            onSuccess: (data) => {
                setSpendings(data);
            },
            onFail: (error) => {
                console.log(error);
            },
        });
    }, [filter, selectedCurrency]);

    useEffect(() => {
        getData({
            path: "/statistic",
            params: {
                filterPeriod: filter === "ALL" ? null : filter,
                filterCurrency: selectedCurrency?.value === "ALL" ? null : selectedCurrency?.value,
            },
            onSuccess: (data) => {
                setStatistic(data);
            },
            onFail: (error) => {
                console.log(error);
            },
        });
    }, [filter, selectedCurrency]);


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
                    <FilterContext.Provider value={value}>
                        <CurrencyContext.Provider value={curContext}>
                            <AddSpending addSpendingCallback={addNewSpendingInTableCallback} categories={categories} />
                            <SpendingStatistics statistic={statistic} defaultCurrency={user?.currency} />
                            <SpendingHistory spendings={spendings} currencies={currencies}/>
                        </CurrencyContext.Provider>
                    </FilterContext.Provider>
                </div>
            </main>
            <footer className={"footer"}>
                Study project for QA Automation Advanced. 2023
            </footer>
        </div>
    )
}
