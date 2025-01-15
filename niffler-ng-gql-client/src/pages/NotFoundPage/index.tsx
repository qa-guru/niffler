import {EmptyTableState} from "../../components/EmptyUsersState";
import {Container} from "@mui/material";
import {PrimaryButton} from "../../components/Button";
import {useNavigate} from "react-router-dom";

export const NotFoundPage = () => {
    const navigate = useNavigate();
    return (
        <Container>
            <EmptyTableState title={"Sorry we couldn't find this page"}/>
            <PrimaryButton
                type="button"
                onClick={() => navigate("/main")}
                sx={{
                    display: "block",
                    margin: "0 auto",
                }}
            >
                Go to homepage
            </PrimaryButton>
        </Container>
    );
}