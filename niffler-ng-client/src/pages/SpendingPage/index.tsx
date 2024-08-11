import {Container} from "@mui/material";
import {SpendingForm} from "../../components/SpendingForm";
import {useParams} from "react-router-dom";

export const SpendingPage = () => {
    const params = useParams();
    return (
        <Container>
            <SpendingForm id={params.id} isEdit={Boolean(params.id)}/>
        </Container>
    )
}
