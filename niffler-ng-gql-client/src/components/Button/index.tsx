import MuiButton, {ButtonProps} from '@mui/material/Button';
import {styled} from '@mui/material/styles';
import {FC} from "react";

const Button = styled(MuiButton)<ButtonProps>(() => ({
    padding: "12px 24px",
    borderRadius: "8px",
    textTransform: "none",
    weight: 600,
    fontSize: 16,
    lineHeight: 1.3,
}));

export const PrimaryButton: FC<ButtonProps> = (props) => {
    return <Button {...props} color={"primary"} variant={"contained"}/>;
}

export const SecondaryButton: FC<ButtonProps> = (props) => {
    return <Button {...props} color={"secondary"} variant={"contained"}/>;
}
