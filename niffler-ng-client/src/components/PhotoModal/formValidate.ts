export const MAX_PHOTO_DESCRIPTION_LENGTH = 255;
export const MAX_PHOTO_DESCRIPTION_ERROR = "Description length has to be not longer that 255 symbols";
export const EMPTY_SRC_ERROR = "Please upload an image";
export const EMPTY_COUNTRY_ERROR = "You have to select country";

export interface IStringIndex extends Record<string, any> {};

export type PhotoFormProps = IStringIndex & {
    description: {
        value: string,
        error: boolean,
        errorMessage: string,
    },
    country: {
        value: string,
        error: boolean,
        errorMessage: string,
    },
    src: {
        value?: string,
        error: boolean,
        errorMessage: string,
    }
}

export const formInitialState: PhotoFormProps = {
    description: {
        value: "",
        error: false,
        errorMessage: "",
    },
    country: {
        value: "ru",
        error: false,
        errorMessage: "",
    },
    src: {
        value: undefined,
        error: false,
        errorMessage: "",
    }
};


export const formValidate = (formValues: PhotoFormProps): PhotoFormProps => {
    let newFormValues = {...formValues};
    
    newFormValues = {
        ...newFormValues,
        description : {
            ...newFormValues.description,
            error: formValues.description.value?.length > MAX_PHOTO_DESCRIPTION_LENGTH ? true : false,
            errorMessage: formValues.description.value?.length > MAX_PHOTO_DESCRIPTION_LENGTH ? MAX_PHOTO_DESCRIPTION_ERROR: "",
        },
        src: {
            ...newFormValues.src,
            error: !Boolean(formValues.src.value) ? true : false,
            errorMessage: !Boolean(formValues.src.value) ? EMPTY_SRC_ERROR : "",
        },
        country: {
            ...newFormValues.country,
            error: !Boolean(formValues.country.value) ? true : false, 
            errorMessage: !Boolean(formValues.country.value) ? EMPTY_COUNTRY_ERROR : "",
        }
    }

    return newFormValues;
};

export const formHasErrors = (formValues: Record<string, any>) => {
    const keys = Object.keys(formValues);
    return keys.some((key) => formValues[key].error === true);
};