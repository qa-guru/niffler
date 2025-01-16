import dayjs, {Dayjs} from "dayjs";
import {CurrencyValue} from "../../types/Currency.ts";
import {Category} from "../../types/Category.ts";
import {Spending, SpendingData} from "../../types/Spending.ts";
import {IStringIndex} from "../../types/IStringIndex.ts";
import {CurrencyValues} from "../../generated/graphql.tsx";

const MIN_AMOUNT_ERROR = `Amount has to be not less then 0.01`;
const MAX_DESCRIPTION_LENGTH = 100;
const MAX_DESCRIPTION_ERROR = `Description length has to be not longer that ${MAX_DESCRIPTION_LENGTH} symbols`;
const FUTURE_DATE_ERROR = "Date can not be in the future";
const EMPTY_CATEGORY_ERROR = "Please choose category";

export const SPENDING_INITIAL_STATE: SpendingData = {
    amount: 0,
    description: "",
    currency: CurrencyValues.Rub,
    spendDate: new Date(),
    category: {
        name: "",
    },
};

export type SpendingFormData = IStringIndex & {
    amount: {
        value: number,
        error: boolean,
        errorMessage: string,
    },
    description: {
        value: string,
        error: boolean,
        errorMessage: string,
    },
    currency: {
        value: CurrencyValue,
        error: boolean,
        errorMessage: string,
    },
    spendDate: {
        value: Dayjs,
        error: boolean,
        errorMessage: string,
    },
    category: {
        value: Category,
        error: boolean,
        errorMessage: string,
    },
}

export const convertSpendingToFormData = (spending: SpendingData): SpendingFormData => {
    return Object.keys(spending)
        .filter(key => key !== "__typename")
        .reduce((acc, key) => {
            acc[key] = {
                value: spending[key as keyof Spending],
                error: false,
                errorMessage: "",
            };
            return acc;
        }, {} as SpendingFormData);
}

export const spendingFormValidate = (formValues: SpendingFormData): SpendingFormData => {
    let newFormValues = {...formValues};

    newFormValues = {
        ...newFormValues,
        amount: {
            ...newFormValues.amount,
            error: formValues.amount.value <= 0,
            errorMessage: formValues.amount.value <= 0 ? MIN_AMOUNT_ERROR : "",
        },
        description: {
            ...newFormValues.description,
            error: formValues.description.value?.length > MAX_DESCRIPTION_LENGTH,
            errorMessage: formValues.description.value?.length > MAX_DESCRIPTION_LENGTH ? MAX_DESCRIPTION_ERROR : "",
        },
        spendDate: {
            ...newFormValues.spendDate,
            error: dayjs(formValues.spendDate.value).isAfter(dayjs()),
            errorMessage: dayjs(formValues.spendDate.value).isAfter(dayjs()) ? FUTURE_DATE_ERROR : "",
        },
        category: {
            ...newFormValues.category,
            error: !(formValues.category.value?.name),
            errorMessage: !(formValues.category.value?.name) ? EMPTY_CATEGORY_ERROR : "",
        },
    }

    return newFormValues;
};
