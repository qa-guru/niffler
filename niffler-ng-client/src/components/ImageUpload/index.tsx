import {Avatar, Box, useTheme} from "@mui/material"
import {ChangeEvent, FC} from "react";
import PhotoCameraRoundedIcon from '@mui/icons-material/PhotoCameraRounded';
import "./styles.css";
import {SecondaryButton} from "../Button";
import UploadIcon from "../../assets/icons/ic_upload.svg?react";


interface ImageUploadInterface {
    onFileUpload: (newFile?: string) => void;
    buttonText: string;
    error: boolean;
    file?: string;
    helperText?: string;
    isAvatar?: boolean
}

export const ImageUpload: FC<ImageUploadInterface> = ({
                                                          onFileUpload,
                                                          buttonText,
                                                          file,
                                                          error = false,
                                                          helperText,
                                                          isAvatar = false
                                                      }) => {

    const theme = useTheme();

    const handleUpload = (event: ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0];
        if (!file) {
            return;
        }
        const reader = new FileReader();
        reader.readAsDataURL(file);

        reader.onloadend = function () {
            onFileUpload(reader.result?.toString());
        };
    };

    return (
        <Box
            sx={{
                width: "100%",
                display: "flex",
                alignItems: "center",
                justifyContent: "flex-start",
                flexDirection: {xs: "column", sm: "row"}
            }}>
            <input
                accept={"image/*"}
                id="image__input"
                type="file"
                hidden
                onChange={handleUpload}
            />
            {isAvatar ? (
                <Box sx={{marginRight: 3, alignSelf: "flex-start", marginBottom: 2}}>
                    <Avatar src={file} sx={{width: 128, height: 128}}/>
                </Box>
            ) : (
                <Box sx={{
                    marginRight: 3,
                    alignSelf: "flex-start",
                    width: "128px",
                    height: "128px",
                    marginBottom: 2,
                }}>
                    {
                        file ? (
                                <img
                                    className="image-upload__image"
                                    src={file}
                                    width="100%"
                                    height="100%"
                                    alt="avatar"
                                />
                            ) :
                            <PhotoCameraRoundedIcon
                                sx={{
                                    width: "100%",
                                    height: "100%",
                                    padding: 5,
                                    color: theme.palette.primary.main,
                                    border: `2px solid ${theme.palette.primary.main}`,
                                    borderRadius: 3,
                                }}
                            />
                    }
                </Box>
            )}
            <label htmlFor="image__input" className="image__input-label">
                <SecondaryButton
                    variant="contained"
                    color="secondary"
                    component="span"
                    startIcon={<UploadIcon/>}
                    sx={{marginTop: 1, width: "100%"}}>
                    {buttonText}
                </SecondaryButton>
            </label>
            <Box sx={{
                color: theme.palette.error.main
            }}>{error && helperText}</Box>
        </Box>
    );
}
