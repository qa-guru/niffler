export const convertDate = (toConvert: string) => {
    return new Date(toConvert).toLocaleDateString('en-US',
        {year: 'numeric', month: 'short', day: '2-digit'});
};