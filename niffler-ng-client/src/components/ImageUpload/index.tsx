import {Avatar, Box, useTheme} from "@mui/material"
import {ChangeEvent, FC, useRef, useState} from "react";
import PhotoCameraRoundedIcon from '@mui/icons-material/PhotoCameraRounded';
import {SecondaryButton} from "../Button";
import UploadIcon from "../../assets/icons/ic_upload.svg?react";
import {useSnackBar} from "../../context/SnackBarContext.tsx";
import {AvatarCapture} from "../AvatarCapture";
import "./styles.css";


interface ImageUploadInterface {
    onFileUpload: (newFile?: string) => void;
    buttonText: string;
    error: boolean;
    file?: string;
    helperText?: string;
    isAvatar?: boolean
}

const ALLOWED_FORMATS = ["image/jpeg", "image/jpg", "image/png", "image/bmp", "image/wbmp", "image/gif"];

export const ImageUpload: FC<ImageUploadInterface> = ({
                                                          onFileUpload,
                                                          buttonText,
                                                          file,
                                                          error = false,
                                                          helperText,
                                                          isAvatar = false
                                                      }) => {

    const theme = useTheme();
    const snackBar = useSnackBar();
    const inputRef = useRef<HTMLInputElement>(null);
    const [isAvatarCaptureVisible, setAvatarCaptureVisible] = useState<boolean>(false);

    const handleUpload = (event: ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0];
        if (!file) {
            return;
        }
        uploadFile(file);
    };

    const uploadFile = (file: File) => {
        if (!ALLOWED_FORMATS.includes(file.type)) {
            snackBar.showSnackBar(
                "Invalid file type. Please upload a valid image: 'jpeg', 'jpg', 'png', 'bmp', 'wbmp', 'gif'",
                "error"
            );
            return;
        }

        const reader = new FileReader();
        reader.readAsDataURL(file);

        reader.onloadend = function () {
            onFileUpload(reader.result?.toString());
        };
    }

    return (
        <>
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
                    ref={inputRef}
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
                <Box>
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
                    <SecondaryButton
                        variant="contained"
                        color="secondary"
                        component="span"
                        sx={{marginTop: 1, width: "100%"}}
                        onClick={() => setAvatarCaptureVisible(true)}
                    >
                        Take camera photo
                    </SecondaryButton>
                </Box>
            </Box>
            {isAvatarCaptureVisible && <AvatarCapture inputRef={inputRef}
                                                      isOpen={isAvatarCaptureVisible}
                                                      onCapture={(file) => {
                                                          uploadFile(file);
                                                          setAvatarCaptureVisible(false)
                                                      }}
                                                      onClose={() => setAvatarCaptureVisible(false)}
            />
            }
        </>

    );
}
