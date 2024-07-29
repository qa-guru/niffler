import {Button, Grid, InputLabel, TextField, Typography, useMediaQuery, useTheme} from "@mui/material"
import {FC, FormEvent, useContext, useEffect, useState,} from "react";
import {ImageUpload} from "../ImageUpload";
import {User} from "../../types/User.ts";
import {apiClient} from "../../api/apiClient.ts";
import {useSnackBar} from "../../context/SnackBarContext.tsx";
import {SessionContext} from "../../context/SessionContext.tsx";


export const ProfileForm: FC = () => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const {user, updateUser} = useContext(SessionContext);
    const [formData, setFormData] = useState<User>(user);
    const snackbar = useSnackBar();

    useEffect(() => {
        setFormData(user);
    }, [user]);

    const onSubmit = (e: FormEvent) => {
        e.preventDefault();
        apiClient.updateProfile(formData, {
           onSuccess: (data) => {
               updateUser(data);
               snackbar.showSnackBar("Profile successfully updated", "success");
           },
           onFailure: (e) => {
               snackbar.showSnackBar(`Error while updating profile: ${e.message}`, "error")
           },
        });
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
                    file={formData.photo}
                    error={false}
                    helperText={""}
                    isAvatar
                    onFileUpload={(file) => setFormData({...formData, photo: file ?? ""})}/>
            </Grid>
            <Grid item xs={12}>
                <Grid
                    container
                    spacing={3}
                    sx={{
                        alignItems: {xs: "center", sm: "flex-end"},
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
                            value={formData.username}
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
                            value={formData.fullname}
                            error={false}
                            helperText={""}
                            onChange={(event) => setFormData({...formData, fullname: event.target.value})}
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
                            }}
                        >Save changes</Button>
                    </Grid>
                </Grid>
            </Grid>
        </Grid>
    )
}