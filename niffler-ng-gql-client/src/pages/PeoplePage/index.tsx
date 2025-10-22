import {Box, Container, Tab, Tabs, Typography, useMediaQuery, useTheme} from "@mui/material"
import {FC, SyntheticEvent} from "react";
import {TabPanel} from "../../components/TabPanel";
import {AllTable} from "../../components/PeopleTable/AllTable";
import {FriendsTable} from "../../components/PeopleTable/FriendsTable";
import {Link, useNavigate} from "react-router-dom";
import {ContactImporter} from "../../components/ContactImporter";

interface PeoplePageInterface {
    activeTab: "friends" | "all" | "invite",
}

export const PeoplePage: FC<PeoplePageInterface> = ({activeTab}) => {
    const navigate = useNavigate();
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

    const handleChangeTab = (_event: SyntheticEvent<Element, Event>, value: "friends" | "all" | "invite") => {
        if (value === "friends") {
            navigate("/people/friends")
        } else if(value === "invite") {
            navigate("/people/invite")
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
            case "invite":
                return (
                    isMobile
                    ? <ContactImporter/>
                        : <TabPanel value="invite"><ContactImporter/></TabPanel>
                )
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
                            <Tab
                                component={Link}
                                label={<Typography variant="h5" component="h2">Invite to Niffler</Typography>}
                                value="invite"
                                sx={{fontSize: 36, textTransform: "initial", fontWeight: 700}}
                                to={"/people/invite"}/>
                        </Tabs>
                    </Box>
                ) :
                <Typography variant="h5" component="h2">
                    {activeTab === "friends" ? "Friends" : activeTab === "all" ? "All people" : "Invite to Niffler"}
                </Typography>}
            {
                resolveTab()
            }
        </Container>
    )
}
