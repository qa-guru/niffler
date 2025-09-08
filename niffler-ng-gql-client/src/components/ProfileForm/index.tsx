import { Grid, InputLabel, Typography, useMediaQuery, useTheme } from "@mui/material"
import { FC, FormEvent, useEffect, useState, } from "react";
import { ImageUpload } from "../ImageUpload";
import { useSnackBar } from "../../context/SnackBarContext.tsx";
import { convertUserToFormData, profileFormValidate, UserFormData } from "./formValidate.ts";
import { formHasErrors } from "../../utils/form.ts";
import { Input } from "../Input";
import LoadingButton from "@mui/lab/LoadingButton";
import { User } from "../../types/User.ts";
import { useUpdateUserMutation } from "../../generated/graphql.tsx";
import { RegisterPasskeyPayload } from "../../types/RegisterPasskeyPayload.ts";
import { authClient } from "../../api/authClient.ts";
import { normalizePasskeyRegistrationError } from "../../utils/passkey.ts";

interface ProfileFormInterface {
    user: User;
}

export const ProfileForm: FC<ProfileFormInterface> = ({ user }) => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

    const [formData, setFormData] = useState<UserFormData>(convertUserToFormData(user));
    const [updateUser, { loading }] = useUpdateUserMutation();
    const snackbar = useSnackBar();
    const [isPasskeyLoading, setPasskeyLoading] = useState(false);

    useEffect(() => {
        setFormData(convertUserToFormData(user));
    }, [user]);

    const onSubmit = (e: FormEvent) => {
        e.preventDefault();
        const validatedData = profileFormValidate(formData);
        setFormData(validatedData);
        if (!formHasErrors(validatedData)) {
            updateUser({
                variables: {
                    input: {
                        fullname: formData.fullname?.value ?? "",
                        photo: formData.photo?.value ?? "",
                    }
                },
                errorPolicy: "none",
                onCompleted: () => snackbar.showSnackBar("Profile successfully updated", "success"),
                onError: (err) => {
                    console.error(err);
                    snackbar.showSnackBar("Error while updating profile", "error");
                }
            });
        }
    };

    const registerPasskey = async () => {
        try {
            setPasskeyLoading(true);
            const csrf = (await authClient.getCsrfToken()).token;

            const creationOptionsJSON = await authClient.registerPasskeyOptions(csrf, {
                onSuccess: (data) => data,
                onFailure: (e) => { throw new Error(e.message); },
            });

            const { parseCreationOptionsFromJSON } = (PublicKeyCredential as any);
            const publicKey: PublicKeyCredentialCreationOptions =
                parseCreationOptionsFromJSON(creationOptionsJSON);

            const cred = (await navigator.credentials.create({ publicKey })) as PublicKeyCredential | null;
            if (!cred) throw new Error('Registration was canceled');

            const payload: RegisterPasskeyPayload = {
                publicKey: {
                    credential: (cred as PublicKeyCredential).toJSON(),
                    label: 'device-passkey',
                },
            };

            await authClient.registerPasskey(payload, csrf, {
                onSuccess: () => {
                    snackbar.showSnackBar('Passkey registered', 'success');
                },
                onFailure: (e) => { throw new Error(e.message); },
            });

            return;
        } catch (e) {
            snackbar.showSnackBar(`Registration failed: ${normalizePasskeyRegistrationError(e)}`, 'error');
        } finally {
            setPasskeyLoading(false);
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
            <Grid item xs={isMobile ? 10 : 8}>
                <ImageUpload
                    buttonText="Upload new picture"
                    file={formData.photo?.value ?? ""}
                    error={formData.photo?.error ?? false}
                    helperText={formData.photo?.errorMessage ?? ""}
                    isAvatar
                    onFileUpload={(file) =>
                        setFormData({ ...formData, photo: { ...formData.photo, value: file ?? "" } })
                    }
                />
                <LoadingButton
                    onClick={registerPasskey}
                    variant="contained"
                    color="primary"
                    loading={isPasskeyLoading}
                    sx={{
                        minWidth: 220,
                        height: 40,
                        whiteSpace: "nowrap",
                    }}
                >
                    Register Passkey
                </LoadingButton>
            </Grid>
            <Grid item xs={12}>
                <Grid
                    container
                    spacing={3}
                    sx={{
                        alignItems: { xs: "center", sm: "flex-start" },
                        flexDirection: { xs: "column", sm: "row" }
                    }}
                >
                    <Grid item xs={isMobile ? true : 4} sx={{ width: "100%" }}>
                        <InputLabel
                            htmlFor={"username"}
                            sx={{
                                color: theme.palette.black.main,
                            }}>
                            Username
                        </InputLabel>
                        <Input
                            id="username"
                            name="username"
                            type="text"
                            value={user.username}
                            error={false}
                            disabled
                        />
                    </Grid>
                    <Grid item xs={isMobile ? true : 4} sx={{ width: "100%" }}>
                        <InputLabel htmlFor={"name"}
                            sx={{ color: theme.palette.black.main }}>
                            Name
                        </InputLabel>
                        <Input
                            id="name"
                            name="name"
                            type="text"
                            value={formData.fullname?.value ?? ""}
                            error={formData.fullname?.error ?? false}
                            helperText={formData.fullname?.errorMessage ?? ""}
                            onChange={(event) => setFormData({
                                ...formData, fullname: {
                                    ...formData.fullname, value: event.target.value, error: false, errorMessage: "",
                                }
                            })}
                        />
                    </Grid>
                    <Grid item xs={isMobile ? true : 4} sx={{
                        margin: "0 auto",
                        width: "100%",
                    }}>
                        <LoadingButton
                            variant="contained"
                            type="submit"
                            size={"large"}
                            loading={loading}
                            sx={{
                                width: "100%",
                                marginTop: 2.75,
                            }}
                        >
                            Save changes
                        </LoadingButton>
                    </Grid>
                </Grid>
            </Grid>
        </Grid>
    )
}