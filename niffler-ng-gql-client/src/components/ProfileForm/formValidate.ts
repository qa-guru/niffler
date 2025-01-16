import {IStringIndex} from "../../types/IStringIndex.ts";
import {User} from "../../types/User.ts";

export const MAX_FULLNAME_LENGTH = 50;
export const MAX_FULLNAME_ERROR = `Fullname length has to be not longer that ${MAX_FULLNAME_LENGTH} symbols`;


export type UserFormData = IStringIndex & {
    username: {
        value: string,
        error: boolean,
        errorMessage: string,
    },
    fullname: {
        value: string,
        error: boolean,
        errorMessage: string,
    },
    photo: {
        value: string | undefined,
        error: boolean,
        errorMessage: string,
    }
}

export const convertUserToFormData = (user: User): UserFormData => {
    return Object.keys(user).reduce((acc, key) => {
        acc[key] = {
            value: user[key as keyof User],
            error: false,
            errorMessage: "",
        };
        return acc;
    }, {} as UserFormData);
}

export const profileFormValidate = (formValues: UserFormData): UserFormData => {
    let newFormValues = {...formValues};

    newFormValues = {
        ...newFormValues,
        fullname: {
            ...newFormValues.fullname,
            error: formValues.fullname?.value?.length > MAX_FULLNAME_LENGTH,
            errorMessage: formValues.fullname?.value?.length > MAX_FULLNAME_LENGTH ? MAX_FULLNAME_ERROR : "",
        },
    }

    return newFormValues;
};
