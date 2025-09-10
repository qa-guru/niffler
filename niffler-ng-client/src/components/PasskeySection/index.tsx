import {FC, useState} from "react";
import LoadingButton from "@mui/lab/LoadingButton";
import {authClient} from "../../api/authClient.ts";
import {RegisterPasskeyPayload} from "../../types/RegisterPasskeyPayload.ts";
import {useSnackBar} from "../../context/SnackBarContext.tsx";
import {Grid, Typography, useMediaQuery, useTheme} from "@mui/material";
import {normalizePasskeyRegistrationError} from "../../utils/passkey.ts";

export const PasskeySection: FC = () => {

    const [isPasskeyLoading, setPasskeyLoading] = useState(false);
    const snackbar = useSnackBar();
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

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
            spacing={0}
            sx={{
                maxWidth: "624px",
                margin: isMobile ? "0 auto 20px" : "40px auto"
            }}
        >
            <Grid item xs={12} >
                <Typography
                    variant="h5"
                    component="h2"
                >
                    Passkeys
                </Typography>
            </Grid>
            <Grid item xs={isMobile ? 12 : 4}>
                <LoadingButton
                    onClick={registerPasskey}
                    variant="contained"
                    color="primary"
                    loading={isPasskeyLoading}
                    data-testid={"register-passkey-btn"}
                    size={"large"}
                    sx={{
                        width: "100%",
                        marginTop: 2.75,
                        whiteSpace: "nowrap",
                    }}
                >
                    Register Passkey
                </LoadingButton>
            </Grid>
        </Grid>
    )
}
