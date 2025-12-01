export const formHasErrors = (formValues: Record<string, any>) => {
    const keys = Object.keys(formValues);
    return keys.some((key) => formValues[key].error === true);
};
