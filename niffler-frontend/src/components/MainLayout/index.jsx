import {useQuery} from "@apollo/client";
import {deleteData, getData} from "../../api/api";
import {QUERY_ALL_CATEGORIES, QUERY_ALL_CURRENCIES} from "../../api/graphql/queries";
import {CurrencyContext} from "../../contexts/CurrencyContext";
import {FilterContext} from "../../contexts/FilterContext";
import {UserContext} from "../../contexts/UserContext";
import {showSuccess} from "../../toaster/toaster";
import {AddSpending} from "../AddSpending";
import {PageContainer} from "../PageContainer";
import {SpendingHistory} from "../SpendingHistory";
import {useContext, useEffect, useMemo, useState} from "react";
import {SpendingStatistics} from "../SpendingStatistics";


export const MainLayout = () => {
    const [spendings, setSpendings] = useState([]);
    const {data: categoriesData, loading: categoriesLoading, error: categoriesError} = useQuery(QUERY_ALL_CATEGORIES);
    const {data: currenciesData, loading: currenciesLoading, error: currenciesError} = useQuery(QUERY_ALL_CURRENCIES);

    const categories = useMemo(() => {
        if (categoriesLoading || categoriesError) return [];
        return categoriesData.categories.map((v) => {
            return {value: v?.category, label: v?.category}
        });
    }, [categoriesLoading, categoriesError, categoriesData]);

    const currencies = useMemo(() => {
        if (currenciesLoading || currenciesError) return [];
        const result = currenciesData.currencies.map((v) => {
            return {value: v?.currency, label: v?.currency}
        });
        result.push({value: "ALL", label: "ALL"});
        return result;

    }, [currenciesLoading, currenciesError, currenciesData]);

    const [statistic, setStatistic] = useState([]);
    const [isGraphOutdated, setIsGraphOutdated] = useState(false);

    const { user } = useContext(UserContext);

    const [filter, setFilter] = useState(null);
    const value = { filter, setFilter };


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
