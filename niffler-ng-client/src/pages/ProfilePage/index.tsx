import {Container} from "@mui/material"
import {ProfileForm} from "../../components/ProfileForm"
import {CategorySection} from "../../components/CategorySection";
import {PasskeySection} from "../../components/PasskeySection";

export const ProfilePage = () => {

    return (
        <Container sx={{
            p: 0,
            }}>
            <ProfileForm/>
            <PasskeySection/>
            <CategorySection/>
        </Container>
    )
}
