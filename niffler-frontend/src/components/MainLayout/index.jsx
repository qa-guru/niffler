import {deleteData, getData} from "../../api/api";
import {useLoadedData} from "../../api/hooks";
import {CurrencyContext} from "../../contexts/CurrencyContext";
import {FilterContext} from "../../contexts/FilterContext";
import {UserContext} from "../../contexts/UserContext";
import {showSuccess} from "../../toaster/toaster";
import {AddSpending} from "../AddSpending";
import {PageContainer} from "../PageContainer";
import {SpendingHistory} from "../SpendingHistory";
import {useContext, useEffect, useState} from "react";
import {SpendingStatistics} from "../SpendingStatistics";

export const MainLayout = () => {
    const [spendings, setSpendings] = useState([]);
    const [categories, setCategories] = useState({});
    const [statistic, setStatistic] = useState([]);
    const [isGraphOutdated, setIsGraphOutdated] = useState(false);

    const { user } = useContext(UserContext);

    const [filter, setFilter] = useState(null);
    const value = { filter, setFilter };

    const [currencies, setCurrencies] = useState([]);

    const [selectedCurrency, setSelectedCurrency] = useState({value: "ALL", label: "ALL"});
    const curContext = { selectedCurrency, setSelectedCurrency };

    const getStatistics = () => getData({
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

    const getSpends = () => {
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
    }


    useLoadedData({
            path: "/categories",
            onSuccess: (data) => {
                setCategories(Array.from(data.map((v) => {
                    return {value: v?.category, label: v?.category}
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
        getSpends();
    }, [filter, selectedCurrency]);

    useEffect(() => {
        getStatistics();
    }, [filter, selectedCurrency, isGraphOutdated]);


    const addNewSpendingInTableCallback = (data) => {
        const newSpendings = [...spendings];
        newSpendings.push(data);
        setSpendings(newSpendings);
        getData({
            path: "/statistic",
            onSuccess: (data) => {
                setStatistic(data);
            },
            onFail: (error) => {
                console.log(error);
            },
        });
    }

    const handleDeleteItems = (ids) => {
        deleteData({
            path: "/deleteSpends",
            params: {
                ids: ids.join(",")
            },
            onSuccess: () => {
                showSuccess("Spendings deleted");
                getSpends();
                getStatistics();
            }
        })
    }

    return (
        <PageContainer>
            <div className={"main-content"}>
                <FilterContext.Provider value={value}>
                    <CurrencyContext.Provider value={curContext}>
                        <AddSpending addSpendingCallback={addNewSpendingInTableCallback} categories={categories} />
                        <SpendingStatistics statistic={statistic} defaultCurrency={user?.currency}/>
                        <SpendingHistory spendings={spendings}
                                         currencies={currencies}
                                         categories={categories}
                                         handleDeleteItems={handleDeleteItems}
                                         isGraphOutdated={isGraphOutdated}
                                         setIsGraphOutdated={setIsGraphOutdated}
                        />
                    </CurrencyContext.Provider>
                </FilterContext.Provider>
            </div>
        </PageContainer>
    );
}
