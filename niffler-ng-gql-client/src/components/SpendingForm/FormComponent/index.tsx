import {Loader} from "../../Loader";
import {Button, Grid, InputLabel, Typography, useTheme} from "@mui/material";
import {Input} from "../../Input";
import {CurrencySelect} from "../../CurrencySelect";
import {CategorySelect} from "../../CategorySelect";
import {LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import {DatePicker} from "@mui/x-date-pickers/DatePicker";
import {CalendarIcon} from "../../Icon/CalendarIcon.tsx";
import dayjs from "dayjs";
import LoadingButton from "@mui/lab/LoadingButton";
import {ChangeEvent, FC, FormEvent, useState} from "react";
import {useNavigate} from "react-router-dom";
import {convertSpendingToFormData, spendingFormValidate} from "../formValidate.ts";
import {
    CategoriesDocument,
    Category,
    CurrencyValues,
    SpendInput,
    useCategoriesQuery,
    useCurrenciesQuery,
    useUpdateSpendMutation
} from "../../../generated/graphql.tsx";
import {formHasErrors} from "../../../utils/form.ts";
import {useSnackBar} from "../../../context/SnackBarContext.tsx";
import {SpendingData} from "../../../types/Spending.ts";
import graphqlClient from "../../../api/graphqlClient.ts";

interface FormComponentInterface {
    header: string,
    submitText: string,
    initialData: SpendingData,
    isEdit: boolean,
    successMessage: string,
    dataLoading?: boolean,
}

export const FormComponent: FC<FormComponentInterface> = ({
                                                              header,
                                                              submitText,
                                                              initialData,
                                                              successMessage,
                                                              isEdit = false,
                                                              dataLoading = false
                                                          }) => {
    const theme = useTheme();
    const navigate = useNavigate();
    const {data, loading} = useCurrenciesQuery({
        errorPolicy: "none",
    });
    const {data: userCategories, loading: loadingCategories} = useCategoriesQuery({
        errorPolicy: "none",
    });
    const categories = userCategories?.user.categories
        .filter(cat => !cat.archived)
        .map(category => category.name) ?? [];

    const snackbar = useSnackBar();

    const [updateSpend, {loading: isSubmitting}] = useUpdateSpendMutation();

    const [formData, setFormData] = useState(convertSpendingToFormData(initialData));

    const onSubmit = (e: FormEvent) => {
        e.preventDefault();
        const validatedData = spendingFormValidate(formData);
        setFormData(validatedData);
        if (!formHasErrors(validatedData)) {
            const body: SpendInput = {
                amount: Number(formData.amount.value),
                description: formData.description.value,
                currency: formData.currency.value as CurrencyValues,
                spendDate: formData.spendDate.value,
                category: {
                    name: formData.category.value.name,
                    archived: false,
                },
            }
            if (isEdit && initialData.id) {
                body.id = initialData.id
            }

            updateSpend({
                variables: {
                    input: {
                        ...body
                    }
                },
                errorPolicy: "none",
                onCompleted: (data) => {
                    snackbar.showSnackBar(successMessage, "success");
                    graphqlClient.cache.updateQuery({
                        query: CategoriesDocument
                    }, (formData) => {
                        const categories = formData?.user.categories.map((cat: Category) => cat.name);
                        const category = data.spend.category;
                        if (category && !categories?.find((cat: string) => cat === category.name)) {
                            return {
                                ...formData,
                                user: {
                                    ...formData?.user,
                                    categories: [
                                        ...formData.user.categories, category
                                    ]
                                }
                            }
                        }
                        return formData
                    });
                    navigate("/main");
                },
                onError: (err) => {
                    console.error(err);
                    snackbar.showSnackBar("Error while submitting form", "error");
                }
            })
        }
    };

    const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
        setFormData({
            ...formData,
            [event.target.name]: {
                ...formData[event.target.name],
                value: event.target.value,
                error: false,
                errorMessage: "",
            },
        });
    };

    const onCancel = () => {
        navigate(-1);
    }

    return (
        loading || dataLoading
            ? <Loader/>
            : <Grid
                container
                component="form"
                onSubmit={onSubmit}
                spacing={2}
                sx={{
                    maxWidth: "320px",
                    margin: "40px auto",
                }}
            >
                <Grid item xs={12}>
                    <Typography
                        variant="h5"
                        component="h2"
                    >
                        {header}
                    </Typography>
                </Grid>
                <Grid item xs={12}>
                    <Grid
                        container
                        spacing={3}
                    >
                        <Grid item xs={7} sx={{width: "100%"}}>
                            <InputLabel
                                htmlFor={"amount"}
                                sx={{
                                    color: theme.palette.black.main,
                                }}>
                                Amount
                            </InputLabel>
                            <Input
                                id="amount"
                                name="amount"
                                type="number"
                                value={formData.amount.value}
                                error={formData.amount.error}
                                onChange={handleChange}
                                helperText={formData.amount.errorMessage}
                            />
                        </Grid>
                        <Grid item xs={5} sx={{width: "100%"}}>
                            <InputLabel
                                htmlFor={"currency"}
                                sx={{
                                    color: theme.palette.black.main,
                                }}>
                                Currency
                            </InputLabel>
                            {data?.currencies &&
                                <CurrencySelect
                                    currencies={data.currencies}
                                    selectedCurrency={formData.currency.value}
                                    onCurrencyChange={handleChange}/>
                            }
                        </Grid>

                    </Grid>
                </Grid>
                <Grid item xs={12}>
                    <InputLabel
                        htmlFor={"date"}
                        sx={{
                            color: theme.palette.black.main,
                        }}>
                        Category
                    </InputLabel>
                    {!loadingCategories &&
                        <CategorySelect
                            categories={categories}
                            error={formData.category.error}
                            helperText={formData.category.errorMessage}
                            selectedCategory={formData.category.value.name}
                            onSelectCategory={(category) => {
                                setFormData(
                                    {
                                        ...formData,
                                        category: {
                                            ...formData.category,
                                            value: {
                                                ...formData.category.value,
                                                name: category
                                            },
                                            error: false,
                                            errorMessage: "",
                                        },
                                    }
                                );
                            }
                            }
                        />
                    }
                </Grid>
                <Grid item xs={12}>
                    <InputLabel
                        htmlFor={"date"}
                        sx={{
                            color: theme.palette.black.main,
                        }}>
                        Date
                    </InputLabel>
                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                        <DatePicker
                            sx={{
                                width: "100%",
                            }}
                            name={"date"}
                            slots={{
                                openPickerIcon: CalendarIcon,
                            }}
                            value={dayjs(formData.spendDate.value)}
                            shouldDisableDate={day => dayjs(day).isAfter(dayjs())}
                            onChange={newDate => setFormData({
                                ...formData,
                                spendDate: {...formData.spendDate, value: dayjs(newDate)}
                            })}
                        />
                    </LocalizationProvider>
                </Grid>
                <Grid item xs={12}>
                    <InputLabel
                        htmlFor={"description"}
                        sx={{
                            color: theme.palette.black.main,
                        }}>
                        Description
                    </InputLabel>
                    <Input
                        id="description"
                        name="description"
                        type="text"
                        value={formData.description.value}
                        error={formData.description.error}
                        onChange={handleChange}
                        placeholder={"Type something"}
                    />
                </Grid>
                <Grid item xs={12} sx={{
                    marginTop: 2,
                    width: "100%",
                    display: "flex",
                }}>
                    <Button type="button"
                            sx={{
                                marginRight: 3,
                                width: "100%",
                            }}
                            id="cancel"
                            color="secondary"
                            variant="contained"
                            onClick={onCancel}
                    >
                        Cancel
                    </Button>
                    <LoadingButton
                        sx={{
                            width: "100%",
                        }}
                        id="save"
                        type="submit"
                        color="primary"
                        variant="contained"
                        loading={isSubmitting}
                    >
                        {submitText}
                    </LoadingButton>
                </Grid>
            </Grid>
    )
}