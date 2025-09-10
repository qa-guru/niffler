import {FC, useState} from "react";
import LoadingButton from "@mui/lab/LoadingButton";
import {authClient} from "../../api/authClient.ts";
import {b64urlToBuf, bufToB64url} from "../../utils/passkey.ts";
import {RegisterPasskeyPayload} from "../../types/RegisterPasskeyPayload.ts";
import {useSnackBar} from "../../context/SnackBarContext.tsx";
import {Grid, Typography, useMediaQuery, useTheme} from "@mui/material";

export const PasskeySection: FC = () => {

    const [isPasskeyLoading, setPasskeyLoading] = useState(false);
    const snackbar = useSnackBar();
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

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
            console.log(pubKey);

            const cred = (await navigator.credentials.create({publicKey: pubKey})) as PublicKeyCredential;

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
                        clientExtensionResults:
                            (cred as any).getClientExtensionResults
                                ? (cred as any).getClientExtensionResults()
                                : {},
                    },
                    label: "device-passkey",
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
