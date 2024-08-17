import * as React from 'react';
import {createContext, FC, forwardRef, ReactElement, ReactNode, useContext, useState} from 'react';
import DialogMui from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogActions from "@mui/material/DialogActions";
import Button from "@mui/material/Button";
import {TransitionProps} from "@mui/material/transitions";
import Slide from "@mui/material/Slide";

const Transition = forwardRef(function Transition(
    props: TransitionProps & {
        children: ReactElement;
    },
    ref: React.Ref<unknown>,
) {
    return <Slide direction="up" ref={ref} {...props} />;
});

interface DialogDataInterface {
    title: string,
    description: string,
    onSubmit: () => void,
    submitTitle?: string,
    closeTitle?: string,
    submitButtonIcon?: ReactElement,
}

interface DialogContextActions {
    showDialog: (dialogData: DialogDataInterface) => void;
}

const DialogContext = createContext({} as DialogContextActions);

interface DialogContextProps {
    children: ReactNode;
}

const DialogProvider: FC<DialogContextProps> = ({children}) => {
    const [open, setOpen] = useState<boolean>(false);
    const [dialogData, setDialogData] = useState<DialogDataInterface>();

    const showDialog = (dialogData: DialogDataInterface) => {
        setDialogData(dialogData);
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleSubmit = () => {
        dialogData?.onSubmit();
        setOpen(false);
    }

    return (
        <DialogContext.Provider value={{showDialog}}>
            {children}
            <DialogMui
                open={open}
                TransitionComponent={Transition}
                keepMounted
                onClose={handleClose}
                aria-describedby="alert-dialog-slide-description"
            >
                <DialogTitle>{dialogData?.title}</DialogTitle>
                <DialogContent sx={{display: "flex", alignItems: "center"}}>
                    {/*<Box*/}
                    {/*    component="img"*/}
                    {/*    sx={{*/}
                    {/*        height: 60,*/}
                    {/*        width: 60,*/}
                    {/*        marginRight: 4,*/}
                    {/*    }}*/}
                    {/*    alt="Coin"*/}
                    {/*    src={coin}*/}
                    {/*/>*/}
                    <DialogContentText id="alert-dialog-slide-description">
                        {dialogData?.description}
                    </DialogContentText>
                </DialogContent>
                <DialogActions sx={{margin: 2}}>
                    <Button
                        onClick={handleClose}>{dialogData?.closeTitle ?? "Close"}
                    </Button>
                    <Button
                        onClick={handleSubmit}
                        color={"primary"}
                        variant={"contained"}
                        startIcon={dialogData?.submitButtonIcon}>
                        {dialogData?.submitTitle ?? "Submit"}
                    </Button>
                </DialogActions>
            </DialogMui>
        </DialogContext.Provider>
    );
};

const useDialog = (): DialogContextActions => {
    const context = useContext(DialogContext);

    if (!context) {
        throw new Error('useDialog must be used within an DialogProvider');
    }

    return context;
};

export {DialogProvider, useDialog};
