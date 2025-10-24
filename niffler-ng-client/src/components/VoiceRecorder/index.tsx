import { FC, useState, useRef, useEffect } from "react";
import { Box, IconButton, Typography, Alert, useTheme } from "@mui/material";
import MicIcon from "@mui/icons-material/Mic";
import StopIcon from "@mui/icons-material/Stop";

interface VoiceRecorderProps {
    onTranscriptionComplete: (text: string) => void;
    onError: (error: string) => void;
    disabled?: boolean;
}

// Check if browser supports Web Speech API
const SpeechRecognition = (window as any).SpeechRecognition || (window as any).webkitSpeechRecognition;

export const VoiceRecorder: FC<VoiceRecorderProps> = ({ 
    onTranscriptionComplete, 
    onError,
    disabled = false 
}) => {
    const theme = useTheme();
    const [isRecording, setIsRecording] = useState(false);
    const [recordingTime, setRecordingTime] = useState(0);
    const [permissionError, setPermissionError] = useState<string | null>(null);
    const [interimTranscript, setInterimTranscript] = useState("");
    
    const recognitionRef = useRef<any>(null);
    const timerRef = useRef<NodeJS.Timeout | null>(null);
    const finalTranscriptRef = useRef<string>("");

    useEffect(() => {
        // Check if browser supports Speech Recognition
        if (!SpeechRecognition) {
            setPermissionError("Your browser doesn't support voice recognition. Please use Chrome, Edge, or Safari.");
            return;
        }

        // Initialize speech recognition
        const recognition = new SpeechRecognition();
        recognition.continuous = true;
        recognition.interimResults = true;
        recognition.lang = 'ru-RU'; // Russian by default, will also understand English
        
        recognition.onresult = (event: any) => {
            let interim = '';
            let final = '';
            
            for (let i = event.resultIndex; i < event.results.length; i++) {
                const transcript = event.results[i][0].transcript;
                if (event.results[i].isFinal) {
                    final += transcript + ' ';
                } else {
                    interim += transcript;
                }
            }
            
            if (final) {
                finalTranscriptRef.current += final;
            }
            setInterimTranscript(interim);
        };

        recognition.onerror = (event: any) => {
            console.error('Speech recognition error:', event.error);
            if (event.error === 'no-speech') {
                onError('No speech detected. Please try again.');
            } else if (event.error === 'not-allowed') {
                setPermissionError('Microphone access denied. Please allow microphone access.');
                onError('Microphone access denied.');
            } else {
                onError(`Speech recognition error: ${event.error}`);
            }
            setIsRecording(false);
        };

        recognition.onend = () => {
            if (isRecording) {
                // If recording is still active, restart recognition
                try {
                    recognition.start();
                } catch (e) {
                    // Already started or error
                }
            }
        };

        recognitionRef.current = recognition;

        return () => {
            if (timerRef.current) {
                clearInterval(timerRef.current);
            }
            if (recognitionRef.current) {
                recognitionRef.current.stop();
            }
        };
    }, [isRecording]);

    const startRecording = async () => {
        try {
            setPermissionError(null);
            finalTranscriptRef.current = "";
            setInterimTranscript("");
            
            if (!recognitionRef.current) {
                throw new Error("Speech recognition not initialized");
            }

            recognitionRef.current.start();
            setIsRecording(true);
            
            // Start timer
            timerRef.current = setInterval(() => {
                setRecordingTime((prev) => prev + 1);
            }, 1000);

        } catch (err: any) {
            console.error("Error starting speech recognition:", err);
            const errorMessage = "Could not start voice recognition. Please check permissions.";
            setPermissionError(errorMessage);
            onError(errorMessage);
        }
    };

    const stopRecording = () => {
        if (recognitionRef.current && isRecording) {
            recognitionRef.current.stop();
            setIsRecording(false);
            
            // Reset timer
            if (timerRef.current) {
                clearInterval(timerRef.current);
            }
            setRecordingTime(0);
            
            // Send the final transcript
            const fullTranscript = (finalTranscriptRef.current + ' ' + interimTranscript).trim();
            if (fullTranscript) {
                onTranscriptionComplete(fullTranscript);
            } else {
                onError("No speech detected. Please try again.");
            }
            
            setInterimTranscript("");
        }
    };

    const formatTime = (seconds: number): string => {
        const mins = Math.floor(seconds / 60);
        const secs = seconds % 60;
        return `${mins}:${secs.toString().padStart(2, '0')}`;
    };

    return (
        <Box
            sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                gap: 2,
                padding: 2,
                border: `2px dashed ${theme.palette.divider}`,
                borderRadius: 2,
                backgroundColor: theme.palette.background.paper,
            }}
        >
            <Typography variant="h6">
                Voice Input
            </Typography>
            
            {permissionError && (
                <Alert severity="error" sx={{ width: "100%" }}>
                    {permissionError}
                </Alert>
            )}

            <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
                {!isRecording ? (
                    <IconButton
                        onClick={startRecording}
                        disabled={disabled}
                        sx={{
                            backgroundColor: theme.palette.primary.main,
                            color: "white",
                            width: 64,
                            height: 64,
                            "&:hover": {
                                backgroundColor: theme.palette.primary.dark,
                            },
                            "&:disabled": {
                                backgroundColor: theme.palette.action.disabledBackground,
                            }
                        }}
                    >
                        <MicIcon sx={{ fontSize: 32 }} />
                    </IconButton>
                ) : (
                    <IconButton
                        onClick={stopRecording}
                        sx={{
                            backgroundColor: theme.palette.error.main,
                            color: "white",
                            width: 64,
                            height: 64,
                            animation: "pulse 1.5s infinite",
                            "&:hover": {
                                backgroundColor: theme.palette.error.dark,
                            },
                            "@keyframes pulse": {
                                "0%": {
                                    boxShadow: "0 0 0 0 rgba(244, 67, 54, 0.7)",
                                },
                                "70%": {
                                    boxShadow: "0 0 0 10px rgba(244, 67, 54, 0)",
                                },
                                "100%": {
                                    boxShadow: "0 0 0 0 rgba(244, 67, 54, 0)",
                                },
                            },
                        }}
                    >
                        <StopIcon sx={{ fontSize: 32 }} />
                    </IconButton>
                )}
            </Box>

            {isRecording && (
                <Box sx={{ textAlign: "center", width: "100%" }}>
                    <Typography variant="body1" color="error" fontWeight="bold">
                        Listening...
                    </Typography>
                    <Typography variant="h6" color="error">
                        {formatTime(recordingTime)}
                    </Typography>
                    {(finalTranscriptRef.current || interimTranscript) && (
                        <Box sx={{ 
                            mt: 2, 
                            p: 2, 
                            backgroundColor: theme.palette.grey[100], 
                            borderRadius: 1,
                            minHeight: 60
                        }}>
                            <Typography variant="body2" color="text.primary">
                                {finalTranscriptRef.current}
                                <span style={{ color: theme.palette.grey[500] }}>
                                    {interimTranscript}
                                </span>
                            </Typography>
                        </Box>
                    )}
                </Box>
            )}

            {!isRecording && (
                <Typography variant="body2" color="text.secondary" textAlign="center">
                    Click the microphone to start voice recognition
                    <br />
                    <span style={{ fontSize: '0.85em', color: theme.palette.grey[500] }}>
                        Speak in Russian or English
                    </span>
                </Typography>
            )}
        </Box>
    );
};

