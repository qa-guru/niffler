import {Box, Container, Tab, Tabs, Typography, useMediaQuery, useTheme} from "@mui/material"
import {FC, SyntheticEvent} from "react";
import {TabPanel} from "../../components/TabPanel";
import {AllTable} from "../../components/PeopleTable/AllTable";
import {FriendsTable} from "../../components/PeopleTable/FriendsTable";
import {Link, useNavigate} from "react-router-dom";

interface PeoplePageInterface {
    activeTab: "friends" | "all",
}

export const PeoplePage: FC<PeoplePageInterface> = ({activeTab}) => {
    const navigate = useNavigate();
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

    const handleChangeTab = (_event: SyntheticEvent<Element, Event>, value: "friends" | "all") => {
        if (value === "friends") {
            navigate("/people/friends")
        } else {
            navigate("/people/all");
        }
    }

    const resolveTab = () => {
        switch (activeTab) {
            case "all":
                return (
                    isMobile
                        ? <AllTable/>
                        : <TabPanel value="all">
                            <AllTable/>
                        </TabPanel>
                );
            case "friends":
                return (
                    isMobile
                        ? <FriendsTable/>
                        : <TabPanel value="friends">
                            <FriendsTable/>
                        </TabPanel>
                );
        }
    }

    return (
        <Container
            sx={{
                padding: isMobile ? 0 : "0 16px",
                margin: "40px auto",
                width: "100%",
            }}>
            {!isMobile ? (
                    <Box sx={{maxWidth: 700, margin: "0 auto"}}>
                        <Tabs value={activeTab} onChange={handleChangeTab} aria-label="People tabs" role="navigation"
                              textColor="inherit">
                            <Tab
                                component={Link}
                                label={<Typography variant="h5" component="h2">Friends</Typography>}
                                value="friends"
                                sx={{fontSize: 36, textTransform: "initial", fontWeight: 700}}
                                to={"/people/friends"}/>
                            <Tab
                                component={Link}
                                label={<Typography variant="h5" component="h2">All people</Typography>}
                                value="all"
                                sx={{fontSize: 36, textTransform: "initial", fontWeight: 700}}
                                to={"/people/all"}/>
                        </Tabs>
                    </Box>
                ) :
                <Typography variant="h5" component="h2">
                    {activeTab === "friends" ? "Friends" : "All people"}
                </Typography>}
            {
                resolveTab()
            }
        </Container>
    )
}
