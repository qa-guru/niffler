import {Box, Container} from "@mui/material"
import {ProfileForm} from "../../components/ProfileForm"
import {CategorySection} from "../../components/CategorySection";
import {useCurrentUserQuery} from "../../generated/graphql.tsx";
import {Loader} from "../../components/Loader";
import {CommonError} from "../../components/CommonError";

export const ProfilePage = () => {
    const {data, loading} = useCurrentUserQuery({
        errorPolicy: "none",
    });

    return (
        <Container>
            {
                loading ?
                    <Box sx={{
                        minHeight: 100,
                        width: "100%",
                        position: "relative",
                    }}>
                        <Loader/>
                    </Box>
                    :
                    data?.user ?
                        <>
                            <ProfileForm user={data.user}/>
                            <CategorySection categories={data.user.categories}/>
                        </>
                        :
                        <Box sx={{
                            minHeight: 100,
                            width: "100%",
                            position: "relative",
                        }}>
                            <CommonError/>
                        </Box>
            }
        </Container>
    )
}
