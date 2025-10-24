import { Button, Grid, TextField, Typography, useTheme, Alert, Box, Tabs, Tab } from "@mui/material";
import { FC, FormEvent, useState } from "react";
import { aiService, SpendingFromAI } from "../../api/aiService.ts";
import { apiClient } from "../../api/apiClient.ts";
import { useNavigate } from "react-router-dom";
import { useSnackBar } from "../../context/SnackBarContext.tsx";
import LoadingButton from "@mui/lab/LoadingButton";
import dayjs from "dayjs";
import { VoiceRecorder } from "../VoiceRecorder/index.tsx";
import KeyboardIcon from "@mui/icons-material/Keyboard";
import MicIcon from "@mui/icons-material/Mic";

export const AISpendingForm: FC = () => {
    const theme = useTheme();
    const navigate = useNavigate();
    const snackbar = useSnackBar();

    const [inputMode, setInputMode] = useState<'text' | 'voice'>('text');
    const [userInput, setUserInput] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [parsedSpending, setParsedSpending] = useState<SpendingFromAI | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [isSaveButtonLoading, setSaveButtonLoading] = useState(false);
    const [transcribedText, setTranscribedText] = useState<string | null>(null);

    const handleParse = async (e: FormEvent) => {
        e.preventDefault();
        if (!userInput.trim()) {
            setError("Please enter spending information");
            return;
        }

        setIsLoading(true);
        setError(null);
        setParsedSpending(null);

        try {
            const spending = await aiService.parseSpending(userInput);
            setParsedSpending(spending);
        } catch (err: any) {
            setError(err.message || "Failed to parse spending information");
            console.error(err);
        } finally {
            setIsLoading(false);
        }
    };

    const handleSave = async () => {
        if (!parsedSpending) return;

        setSaveButtonLoading(true);

        const data = {
            amount: parsedSpending.amount,
            description: parsedSpending.description,
            currency: parsedSpending.currency,
            spendDate: parsedSpending.spendDate,
            category: {
                name: parsedSpending.category,
            }
        };

        apiClient.addSpend(data, {
            onSuccess: () => {
                snackbar.showSnackBar("Spending successfully added via AI", "success");
                navigate("/main");
                setSaveButtonLoading(false);
            },
            onFailure: (e) => {
                console.error(e);
                snackbar.showSnackBar(e.message, "error");
                setSaveButtonLoading(false);
            },
        });
    };

    const handleCancel = () => {
        navigate(-1);
    };

    const handleReset = () => {
        setParsedSpending(null);
        setError(null);
        setTranscribedText(null);
        setUserInput("");
    };

    const handleVoiceTranscriptionComplete = async (text: string) => {
        setIsLoading(true);
        setError(null);
        setParsedSpending(null);
        setTranscribedText(text);

        try {
            // Parse spending from transcribed text
            const spending = await aiService.parseSpending(text);
            setParsedSpending(spending);
            
            snackbar.showSnackBar("Voice successfully processed!", "success");
        } catch (err: any) {
            setError(err.message || "Failed to process voice input");
            console.error(err);
        } finally {
            setIsLoading(false);
        }
    };

    const handleVoiceError = (errorMessage: string) => {
        setError(errorMessage);
    };

    return (
        <Grid
            container
            spacing={2}
            sx={{
                maxWidth: "600px",
                margin: "40px auto",
            }}
        >
            <Grid item xs={12}>
                <Typography
                    variant="h5"
                    component="h2"
                >
                    Add Spending with AI
                </Typography>
                <Typography
                    variant="body2"
                    sx={{ 
                        marginTop: 1, 
                        color: theme.palette.grey[600] 
                    }}
                >
                    Describe your spending in natural language, and AI will parse it for you.
                    Example: "Купил кофе за 300 рублей в кафе сегодня"
                </Typography>
            </Grid>

            <Grid item xs={12}>
                <Tabs 
                    value={inputMode} 
                    onChange={(_, newValue) => {
                        setInputMode(newValue);
                        setError(null);
                    }}
                    centered
                    sx={{ marginBottom: 2 }}
                >
                    <Tab 
                        icon={<KeyboardIcon />} 
                        iconPosition="start" 
                        label="Text Input" 
                        value="text" 
                        disabled={isLoading || Boolean(parsedSpending)}
                    />
                    <Tab 
                        icon={<MicIcon />} 
                        iconPosition="start" 
                        label="Voice Input" 
                        value="voice"
                        disabled={isLoading || Boolean(parsedSpending)}
                    />
                </Tabs>
            </Grid>

            <Grid item xs={12}>
                {inputMode === 'text' ? (
                    <TextField
                        fullWidth
                        multiline
                        rows={4}
                        label="Describe your spending"
                        placeholder="e.g., 'Купил продукты в магазине за 1500 рублей' or 'Spent 50 USD on dinner yesterday'"
                        value={userInput}
                        onChange={(e) => setUserInput(e.target.value)}
                        disabled={isLoading || Boolean(parsedSpending)}
                    />
                ) : (
                    <VoiceRecorder
                        onTranscriptionComplete={handleVoiceTranscriptionComplete}
                        onError={handleVoiceError}
                        disabled={isLoading || Boolean(parsedSpending)}
                    />
                )}
            </Grid>

            {transcribedText && (
                <Grid item xs={12}>
                    <Alert severity="info">
                        <Typography variant="body2">
                            <strong>Transcribed:</strong> {transcribedText}
                        </Typography>
                    </Alert>
                </Grid>
            )}

            {error && (
                <Grid item xs={12}>
                    <Alert severity="error">{error}</Alert>
                </Grid>
            )}

            {parsedSpending && (
                <Grid item xs={12}>
                    <Alert severity="success">
                        <Typography variant="h6" sx={{ marginBottom: 1 }}>
                            Parsed Spending Information:
                        </Typography>
                        <Box>
                            <Typography><strong>Amount:</strong> {parsedSpending.amount} {parsedSpending.currency}</Typography>
                            <Typography><strong>Category:</strong> {parsedSpending.category}</Typography>
                            <Typography><strong>Description:</strong> {parsedSpending.description}</Typography>
                            <Typography><strong>Date:</strong> {dayjs(parsedSpending.spendDate).format('YYYY-MM-DD')}</Typography>
                        </Box>
                    </Alert>
                </Grid>
            )}

            <Grid item xs={12} sx={{
                marginTop: 2,
                width: "100%",
                display: "flex",
                gap: 2,
            }}>
                {!parsedSpending ? (
                    <>
                        <Button
                            type="button"
                            sx={{ width: "50%" }}
                            color="secondary"
                            variant="contained"
                            onClick={handleCancel}
                            disabled={isLoading}
                        >
                            Cancel
                        </Button>
                        {inputMode === 'text' && (
                            <LoadingButton
                                sx={{ width: "50%" }}
                                type="submit"
                                color="primary"
                                variant="contained"
                                loading={isLoading}
                                onClick={handleParse}
                            >
                                Parse with AI
                            </LoadingButton>
                        )}
                    </>
                ) : (
                    <>
                        <Button
                            type="button"
                            sx={{ width: "33%" }}
                            color="secondary"
                            variant="outlined"
                            onClick={handleReset}
                            disabled={isSaveButtonLoading}
                        >
                            Try Again
                        </Button>
                        <Button
                            type="button"
                            sx={{ width: "33%" }}
                            color="secondary"
                            variant="contained"
                            onClick={handleCancel}
                            disabled={isSaveButtonLoading}
                        >
                            Cancel
                        </Button>
                        <LoadingButton
                            sx={{ width: "34%" }}
                            color="success"
                            variant="contained"
                            loading={isSaveButtonLoading}
                            onClick={handleSave}
                        >
                            Save
                        </LoadingButton>
                    </>
                )}
            </Grid>
        </Grid>
    );
};

