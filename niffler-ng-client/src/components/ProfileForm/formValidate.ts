import { IStringIndex } from "../PhotoModal/formValidate";

export const MAX_FIRST_NAME_LENGTH = 50;
export const MAX_FIRST_NAME_ERROR = `First name length has to be not longer that ${MAX_FIRST_NAME_LENGTH} symbols`;
export const MAX_SURNAME_LENGTH = 100;
export const MAX_SURNAME_ERROR = `Surname length has to be not longer that ${MAX_SURNAME_LENGTH} symbols`;

export const formInitialState: UserFormProps = {
    firstname: {
        value: "",
        error: false,
        errorMessage: "",
    },
    surname: {
        value: "",
        error: false,
        errorMessage: "",
    },
    username: {
        value: "",
        error: false,
        errorMessage: "",
    },
    location: {
        value: "ru",
        error: false,
        errorMessage: "",
    },
    avatar: {
        value: undefined,
        error: false,
        errorMessage: "",
    }
};

export type UserFormProps = IStringIndex & {
    firstname: {
        value: string,
        error: boolean,
        errorMessage: string,
    },
    surname: {
        value: string,
        error: boolean,
        errorMessage: string,
    },
    username: {
        value: string,
        error: boolean,
        errorMessage: string,
    },
    location: {
        value: string,
        error: boolean,
        errorMessage: string,
    },
    avatar: {
        value: string | undefined,
        error: boolean,
        errorMessage: string,
    }
}


export const formValidate = (formValues: UserFormProps): UserFormProps => {
    let newFormValues = {...formValues};
    
    newFormValues = {
        ...newFormValues,
        firstname : {
            ...newFormValues.firstname,
            error: formValues.firstname.value?.length > MAX_FIRST_NAME_LENGTH ? true : false,
            errorMessage: formValues.firstname.value?.length > MAX_FIRST_NAME_LENGTH  ? MAX_FIRST_NAME_ERROR : "",
        },
        surname: {
            ...newFormValues.surname,
            error: formValues.surname.value?.length > MAX_SURNAME_LENGTH ? true : false,
            errorMessage: formValues.surname.value?.length > MAX_SURNAME_LENGTH ? MAX_SURNAME_ERROR : "",
        },
    }

    return newFormValues;
};
