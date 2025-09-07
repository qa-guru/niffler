import {Container, Grid} from "@mui/material"
import {ProfileForm} from "../../components/ProfileForm"
import {CategorySection} from "../../components/CategorySection";
import {ContactImporter} from "../../components/ContactImporter";

export const ProfilePage = () => {

    return (
        <Container sx={{
            p: 0,
            }}>
            <ProfileForm/>
            <Grid
                container
                spacing={0}
                sx={{
                    maxWidth: "624px",
                    margin: "5px auto"
                }}
            >
                <ContactImporter/>
            </Grid>
            <CategorySection/>
        </Container>
    )
}
