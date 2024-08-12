import {Button, Grid, InputLabel, TextField, Typography, useMediaQuery, useTheme} from "@mui/material"
import {FC, FormEvent, useContext, useEffect, useState,} from "react";
import {ImageUpload} from "../ImageUpload";
import {apiClient} from "../../api/apiClient.ts";
import {useSnackBar} from "../../context/SnackBarContext.tsx";
import {SessionContext} from "../../context/SessionContext.tsx";
import {convertUserToFormData, profileFormValidate, UserFormData} from "./formValidate.ts";
import {formHasErrors} from "../../utils/form.ts";


export const ProfileForm: FC = () => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const {user, updateUser} = useContext(SessionContext);
    const [formData, setFormData] = useState<UserFormData>(convertUserToFormData(user));
    const snackbar = useSnackBar();

    useEffect(() => {
        setFormData(convertUserToFormData(user));
    }, [user]);

    const onSubmit = (e: FormEvent) => {
        e.preventDefault();
        const validatedData = profileFormValidate(formData);
        setFormData(validatedData);
        if (!formHasErrors(validatedData)) {
            apiClient.updateProfile({
                id: user.id,
                username: user.username,
                fullname: formData.fullname?.value ?? "",
                photo: formData.photo?.value ?? "",
            }, {
                onSuccess: (data) => {
                    updateUser(data);
                    snackbar.showSnackBar("Profile successfully updated", "success");
                },
                onFailure: (e) => {
                    snackbar.showSnackBar(`Error while updating profile: ${e.message}`, "error")
                },
            });
        }
    };

    return (
        <Grid
            container
            component="form"
            onSubmit={onSubmit}
            spacing={2}
            sx={{
                maxWidth: "624px",
                margin: "40px auto"
            }}
        >
            <Grid item xs={12}>
                <Typography
                    variant="h5"
                    component="h2"
                >
                    Profile
                </Typography>
            </Grid>
            <Grid item xs={8}>
                <ImageUpload
                    buttonText="Upload new picture"
                    file={formData.photo?.value ?? ""}
                    error={formData.photo?.error ?? false}
                    helperText={formData.photo?.errorMessage ?? ""}
                    isAvatar
                    onFileUpload={(file) => setFormData({...formData, photo: {
                            ...formData.photo, value: file ?? "",
                        }})}/>
            </Grid>
            <Grid item xs={12}>
                <Grid
                    container
                    spacing={3}
                    sx={{
                        alignItems: {xs: "center", sm: "flex-start"},
                        flexDirection: {xs: "column", sm: "row"}
                    }}
                >
                    <Grid item xs={ isMobile ? true : 4} sx={{width: "100%"}}>
                        <InputLabel
                            htmlFor={"username"}
                            sx={{
                                color: theme.palette.black.main,
                            }}>
                            Username
                        </InputLabel>
                        <TextField
                            sx={{
                                margin: 0,
                                padding: 0
                            }}
                            id="username"
                            name="username"
                            type="text"
                            value={user.username}
                            error={false}
                            disabled
                            fullWidth
                        />
                    </Grid>
                    <Grid item xs={ isMobile ? true : 4} sx={{width: "100%"}}>
                        <InputLabel htmlFor={"name"}
                                    sx={{color: theme.palette.black.main}}>
                            Name
                        </InputLabel>
                        <TextField
                            sx={{
                                margin: 0,
                                padding: 0
                            }}
                            id="name"
                            name="name"
                            type="text"
                            value={formData.fullname?.value ?? ""}
                            error={formData.fullname?.error ?? false}
                            helperText={formData.fullname?.errorMessage ?? ""}
                            onChange={(event) => setFormData({...formData, fullname: {
                                ...formData.fullname, value: event.target.value, error: false, errorMessage: "",
                                }})}
                            fullWidth
                        />
                    </Grid>
                    <Grid item xs={ isMobile ? true : 4} sx={{
                        margin: "0 auto",
                        width: "100%",
                    }}>
                        <Button
                            variant="contained"
                            type="submit"
                            size={"large"}
                            sx={{
                                width: "100%",
                                marginTop: 2.75,
                            }}
                        >Save changes</Button>
                    </Grid>
                </Grid>
            </Grid>
        </Grid>
    )
}