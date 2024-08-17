import {Container} from "@mui/material"
import {ProfileForm} from "../../components/ProfileForm"
import {CategorySection} from "../../components/CategorySection";

export const ProfilePage = () => {

    return (
        <Container>
            <ProfileForm/>
            <CategorySection/>
        </Container>
    )
}
