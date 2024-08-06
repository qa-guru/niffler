import {Box, Button, FormControl, Grid, InputLabel, MenuItem, Modal as MuiModal,
    OutlinedInput, Select, SelectChangeEvent, TextField, Typography} from "@mui/material";
import {ChangeEvent, FormEvent, FC, useState } from "react";
import { ImageUpload } from "../ImageUpload";
import { PhotoFormProps, formHasErrors, formInitialState, formValidate } from "./formValidate";
import { useCountries } from "../../context/CountriesContext";
import { useSnackBar } from "../../context/SnackBarContext";

const style = {
    position: 'absolute' as 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 600,
    bgcolor: 'background.paper',
    boxShadow: 24,
    p: 4,
};


interface PhotoModalInterface {
    modalState: {
        isVisible: boolean,
        formData: PhotoFormProps | null,
    };
    onClose: () => void;
    isEdit: boolean;
}

export const PhotoModal: FC<PhotoModalInterface> = ({modalState, onClose, isEdit = false}) => {
    const {countries} = useCountries();
    const snackbar = useSnackBar();

    const [formValues, setFormValues] = useState<PhotoFormProps>(modalState.formData ?? formInitialState);

    const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target;
        setFormValues({
            ...formValues,
            [name]: {
                ...formValues[name],
                value,
            }
        })
    };

    const handleClose = () => {
        onClose();
        setFormValues(formInitialState);
    };

    const handleSelectValueChange = (event: SelectChangeEvent<string>) => {
        const {name, value} = event.target;
        setFormValues({
            ...formValues,
            [name]: {
                ...formValues[name],
                value,
            }
        })
    };

    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();
        const validatedData = formValidate(formValues);
        setFormValues(validatedData);
        if (!formHasErrors(validatedData)) {

            handleClose();
        } else {

        }
    };

    return (
        <MuiModal
            open={modalState.isVisible}
            onClose={onClose}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
        >
            <Box sx={style} component="form" noValidate onSubmit={handleSubmit}>
                <Typography id="modal-modal-title" variant="h5" component="h2" sx={{textAlign: "center"}}>
                    Add new photo
                </Typography>
                <Grid container spacing={2}>
                    <Grid item xs={12}>
                        {
                            isEdit ? 
                                <img
                                    width={300}
                                    height={300}
                                    src={formValues.src.value}
                                    alt={formValues.description.value ?? "Фото пользователя"}
                                />
                            :
                            <ImageUpload
                                buttonText="Upload new image"
                                file={formValues.src.value}
                                error={formValues.src.error}
                                helperText={formValues.src.error ? formValues.src.errorMessage: ""}
                                onFileUpload={(file) => {
                                    setFormValues({...formValues, src: {
                                            ...formValues.src,
                                            value: file,
                                        }})
                                }}/>
                        }
                    </Grid>
                    <Grid item xs={12}>
                        <FormControl sx={{width: "100%"}}>
                            <InputLabel id="select-country-label">Country</InputLabel>
                            <Select
                                id="country"
                                name="country"
                                labelId="select-country-label"
                                value={formValues.country.value}
                                onChange={handleSelectValueChange}
                                fullWidth
                                input={
                                    <OutlinedInput
                                        id="select-country"
                                        label="Location"
                                        multiline={false}
                                    />}
                            >
                                {countries.map((option) => (
                                    <MenuItem key={option.code} value={option.code}>
                                        <img width={20} src={option.flag} alt={option.name}/>&nbsp;{option.name}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            id="description"
                            name="description"
                            placeholder="About this photo"
                            label="Description"
                            type="text"
                            value={formValues.description.value}
                            onChange={handleChange}
                            error={formValues.description.error}
                            helperText={formValues.description.error && formValues.description.errorMessage}
                            fullWidth
                            multiline
                            maxRows={4}
                        />
                    </Grid>
                </Grid>
                <Box
                    sx={{
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "space-between",
                    }}
                >
                    <Button variant="contained" sx={{margin: 2}} type="submit">Save</Button>
                    <Button sx={{margin: 2}} onClick={handleClose}>Close</Button>
                </Box>
            </Box>
        </MuiModal>
    );
}