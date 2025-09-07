import {FC, RefObject, useEffect, useRef} from "react";
import {Box, Fade, Modal} from "@mui/material";
import {PrimaryButton, SecondaryButton} from "../Button";
import "./styles.css";

interface AvatarCaptureInterface {
    inputRef: RefObject<HTMLInputElement> | null,
    isOpen: boolean;
    onCapture?: (file: File) => void,
    onClose?: () => void,
}

export const AvatarCapture: FC<AvatarCaptureInterface> = ({inputRef, isOpen, onCapture, onClose}) => {
    const videoRef = useRef<HTMLVideoElement>(null);
    const canvasRef = useRef<HTMLCanvasElement>(null);
    let stream: MediaStream | null;

    const closeCamera = () => {
        if(stream) {
            stream.getTracks().forEach(track => track.stop());
            stream = null;
        }
        if(videoRef.current) {
            videoRef.current.srcObject = null;
            videoRef.current.load();
        }
    }


    useEffect(() => {
        const startCamera = async() => {
            try {
                stream = await navigator.mediaDevices.getUserMedia({
                    video: {facingMode: "user"}
                });
                if (videoRef.current){
                    videoRef.current.srcObject = stream;
                }
            } catch (err) {
                console.error("Camera error:", err);
            }
        }

        startCamera();

        return () => {
            closeCamera();
        }
    }, []);

    const handleCapture = () => {
        if(!videoRef.current || !canvasRef.current || !inputRef?.current) return;

        const video = videoRef.current;
        const canvas = canvasRef.current;

        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;

        const  ctx = canvas.getContext("2d");
        if(!ctx) return;

        ctx.drawImage(video, 0, 0 , canvas.width, canvas.height);

        canvas.toBlob(blob => {
            if(!blob) return;

            const file = new File([blob], "avatar.jpg", {type: "image/jpeg"});

            const dt = new DataTransfer();
            dt.items.add(file);

            inputRef.current!.files = dt.files;

            if(onCapture) {
                onCapture(file);
            }
         }, "image/jpeg");
    }


    return (
        <Modal
            aria-labelledby="transition-modal-title"
            aria-describedby="transition-modal-description"
            open={isOpen}
            onClose={() => {
                closeCamera();
                if(onClose) {
                    onClose();
                }
            }}
            closeAfterTransition
        >
            <Fade in={isOpen}>
                <Box
                    width={"90%"}
                    height={"90%"}
                    sx={{
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "center",
                        margin: "20px auto",
                        position: "relative",
                    }}
                    >
                    <video
                        className={"avatar-capture__video"}
                        ref={videoRef}
                        autoPlay
                        playsInline
                        width={"100%"}
                        height={"100%"}
                    />
                    <canvas ref={canvasRef} className="avatar-capture__canvas"/>
                    <Box sx={{
                        position: "absolute",
                        bottom: 0,
                        margin: 2,
                    }}>
                        <PrimaryButton sx={{marginRight: 2}} onClick={handleCapture}>Capture Avatar</PrimaryButton>
                        <SecondaryButton onClick={() => {
                            closeCamera();
                            if(onClose) {
                                onClose();
                            }
                        }}>Dismiss</SecondaryButton>
                    </Box>
                </Box>
            </Fade>
        </Modal>
    )
}