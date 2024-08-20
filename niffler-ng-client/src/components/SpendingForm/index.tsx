import {Button, Grid, InputLabel, Typography, useTheme} from "@mui/material";
import {ChangeEvent, FC, FormEvent, useEffect, useState} from "react";
import {CategorySelect} from "../CategorySelect";
import {CurrencySelect} from "../CurrencySelect";
import {DatePicker} from '@mui/x-date-pickers/DatePicker';
import {LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import dayjs from "dayjs";
import {CalendarIcon} from "../Icon/CalendarIcon.tsx";
import {apiClient} from "../../api/apiClient.ts";
import {useNavigate} from "react-router-dom";
import {useSnackBar} from "../../context/SnackBarContext.tsx";
import {convertSpendingToFormData, SPENDING_INITIAL_STATE, spendingFormValidate} from "./formValidate.ts";
import {formHasErrors} from "../../utils/form.ts";
import {Loader} from "../Loader";
import {isApiError} from "../../types/Error.ts";
import {Input} from "../Input";
import LoadingButton from "@mui/lab/LoadingButton";


interface SpendingFormInterface {
    id?: string,
    isEdit: boolean,
}

export const SpendingForm: FC<SpendingFormInterface> = ({id, isEdit}) => {

    const theme = useTheme();
    const navigate = useNavigate();
    const snackbar = useSnackBar();

    const [formData, setFormData] = useState(convertSpendingToFormData(SPENDING_INITIAL_STATE));
    const [isDataLoading, setIsDataLoading] = useState<boolean>(false);
    const [isSaveButtonLoading, setSaveButtonLoading] = useState(false);

    useEffect(() => {
        if (isEdit && id) {
            setIsDataLoading(true);
            apiClient.getSpend(id, {
                onSuccess: data => {
                    setFormData(convertSpendingToFormData({
                        amount: data.amount,
                        description: data.description,
                        currency: data.currency,
                        spendDate: dayjs(data.spendDate),
                        category: data.category,
                    }));
                    setIsDataLoading(false);
                },
                onFailure: (e) => {
                    snackbar.showSnackBar(e.message, "error");
                    console.error(e.message);
                    if (isApiError(e) && (e.status === 404)) {
                        navigate("/not-found");
                    } else {
                        setFormData(convertSpendingToFormData(SPENDING_INITIAL_STATE));
                        setIsDataLoading(false);
                    }
                },
            });
        } else {
            setFormData(convertSpendingToFormData(SPENDING_INITIAL_STATE));
        }
    }, [id]);

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

    const onAdd = (e: FormEvent) => {
        e.preventDefault();
        const validatedData = spendingFormValidate(formData);
        setFormData(validatedData);
        if (!formHasErrors(validatedData)) {
            setSaveButtonLoading(true);
            const data = {
                amount: formData.amount.value,
                description: formData.description.value,
                currency: formData.currency.value,
                spendDate: formData.spendDate.value.toISOString(),
                category: {
                    name: formData.category.value.name,
                }
            };
            apiClient.addSpend(data, {
                onSuccess: (_data) => {
                    snackbar.showSnackBar("New spending is successfully created", "success");
                    navigate("/main");
                    setSaveButtonLoading(false);
                },
                onFailure: (e) => {
                    console.error(e);
                    snackbar.showSnackBar(e.message, "error");
                    setSaveButtonLoading(false);
                },
            });
        }
    };

    const onEdit = (e: FormEvent) => {
        e.preventDefault();
        const validatedData = spendingFormValidate(formData);
        setFormData(validatedData);
        if (!formHasErrors(validatedData)) {
            setSaveButtonLoading(true);
            const data = {
                id,
                amount: formData.amount.value,
                description: formData.description.value,
                currency: formData.currency.value,
                spendDate: formData.spendDate.value.toISOString(),
                category: {
                    name: formData.category.value.name,
                }
            };
            apiClient.editSpend(data, {
                onSuccess: (_data) => {
                    snackbar.showSnackBar(`Spending is edited successfully`, "success");
                    setSaveButtonLoading(false);
                    navigate("/main");
                },
                onFailure: (e) => {
                    console.error(e);
                    snackbar.showSnackBar(e.message, "error");
                    setSaveButtonLoading(false);
                },
            });
        }
    };

    const onCancel = () => {
        navigate(-1);
    }

    return (
        isDataLoading
            ? <Loader/>
            : <Grid
                container
                component="form"
                onSubmit={isEdit ? onEdit : onAdd}
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
                        {isEdit ? "Edit spending" : "Add new spending"}
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
                            <CurrencySelect
                                selectedCurrency={formData.currency.value}
                                onCurrencyChange={handleChange}/>
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
                    <CategorySelect
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
                            value={formData.spendDate.value}
                            shouldDisableDate={day => day.isAfter(dayjs())}
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
                        loading={isSaveButtonLoading}
                    >
                        {isEdit ? "Save changes" : "Add"}

                    </LoadingButton>
                </Grid>
            </Grid>
    )
}
