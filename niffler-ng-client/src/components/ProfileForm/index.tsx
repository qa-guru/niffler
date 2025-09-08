import { Grid, InputLabel, Typography, useMediaQuery, useTheme } from "@mui/material";
import { FC, FormEvent, useContext, useEffect, useState } from "react";
import LoadingButton from "@mui/lab/LoadingButton";
import { ImageUpload } from "../ImageUpload";
import { apiClient } from "../../api/apiClient.ts";
import { useSnackBar } from "../../context/SnackBarContext.tsx";
import { SessionContext } from "../../context/SessionContext.tsx";
import { convertUserToFormData, profileFormValidate, UserFormData } from "./formValidate.ts";
import { formHasErrors } from "../../utils/form.ts";
import { Input } from "../Input";
import { RegisterPasskeyPayload } from "../../types/RegisterPasskeyPayload.ts";
import { authClient } from "../../api/authClient.ts";
import { b64urlToBuf, bufToB64url } from "../../utils/passkey.ts";

export const ProfileForm: FC = () => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const { user, updateUser } = useContext(SessionContext);
    const [formData, setFormData] = useState<UserFormData>(convertUserToFormData(user));
    const [isSaveButtonLoading, setSaveButtonLoading] = useState(false);
    const [isPasskeyLoading, setPasskeyLoading] = useState(false);
    const snackbar = useSnackBar();

    useEffect(() => {
        setFormData(convertUserToFormData(user));
    }, [user]);

    const onSubmit = (e: FormEvent) => {
        e.preventDefault();
        const validatedData = profileFormValidate(formData);
        setFormData(validatedData);
        if (!formHasErrors(validatedData)) {
            setSaveButtonLoading(true);
            apiClient.updateProfile({
                id: user.id,
                username: user.username,
                fullname: formData.fullname?.value ?? "",
                photo: formData.photo?.value ?? "",
            }, {
                onSuccess: (data) => {
                    updateUser(data);
                    snackbar.showSnackBar("Profile successfully updated", "success");
                    setSaveButtonLoading(false);
                },
                onFailure: (e) => {
                    snackbar.showSnackBar(`Error while updating profile: ${e.message}`, "error");
                    setSaveButtonLoading(false);
                },
            });
        }
    };

    const registerPasskey = async () => {
        try {
            setPasskeyLoading(true);
            const csrf = (await authClient.getCsrfToken()).token;

            const pubKey = await authClient.registerPasskeyOptions(csrf, {
                onSuccess: (data) => {
                    setPasskeyLoading(false);
                    return data;
                },
                onFailure: (e) => {
                    snackbar.showSnackBar(`Error while updating profile: ${e.message}`, "error");
                    setPasskeyLoading(false);
                },
            });

            pubKey.user.id = b64urlToBuf(pubKey.user.id);
            pubKey.challenge = b64urlToBuf(pubKey.challenge);
            if (Array.isArray(pubKey.excludeCredentials)) {
                pubKey.excludeCredentials = pubKey.excludeCredentials.map((c: any) => ({
                    ...c, id: b64urlToBuf(c.id)
                }));
            }

            const cred = (await navigator.credentials.create({ publicKey: pubKey })) as PublicKeyCredential;

            const payload: RegisterPasskeyPayload = {
                publicKey: {
                    credential: {
                        id: cred.id,
                        rawId: bufToB64url(cred.rawId),
                        type: cred.type,
                        response: {
                            attestationObject: bufToB64url(
                                (cred.response as AuthenticatorAttestationResponse).attestationObject
                            ),
                            clientDataJSON: bufToB64url(cred.response.clientDataJSON),
                            transports: (cred.response as any).getTransports
                                ? (cred.response as any).getTransports()
                                : ["internal"],
                        },
                    },
                },
            };

            await authClient.registerPasskey(payload, csrf, {
                onSuccess: (data) => {
                    setPasskeyLoading(false);
                    snackbar.showSnackBar("Passkey registered", "success");
                    return data;
                },
                onFailure: (e) => {
                    snackbar.showSnackBar(`Registration failed: ${e.message}`, "error");
                    setPasskeyLoading(false);
                },
            });
        } catch (e: any) {
            snackbar.showSnackBar(e?.message ?? "Registration error", "error");
        } finally {
            setPasskeyLoading(false);
        }
    };

    return (
        <Grid
            container
            component="form"
            onSubmit={onSubmit}
            spacing={0}
            sx={{
                maxWidth: "624px",
                margin: isMobile ? "0 auto 20px" : "40px auto"
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
                        marginTop: isMobile ? 2 : 1,
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
                            loading={isSaveButtonLoading}
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
    );
};