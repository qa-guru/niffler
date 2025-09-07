import {FC, FormEvent, useState} from "react";
import {PrimaryButton, SecondaryButton} from "../Button";
import {Divider, Grid, InputLabel, List, ListItem, Typography, useMediaQuery, useTheme} from "@mui/material";
import {Input} from "../Input";
import {useSnackBar} from "../../context/SnackBarContext.tsx";

interface Contact {
    name: string,
    phone?: string,
}

export const ContactImporter: FC = ({}) => {
    const [contacts, setContacts] = useState<Contact[]>([]);
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const snackbar = useSnackBar();

    const [formData, setFormData] = useState<Contact>({
        name: "",
        phone: "",
    });

    const handleGetContacts = async () => {
        if ("contacts" in navigator && "select" in (navigator as any).contacts) {
            try {
                const props = ["name", "tel"];
                const opts = {multiple: true};
                const selected = await (navigator as any).contacts.select(props, opts);

                console.log(selected);

                const mapped: Contact[] = selected.map((c: any) => ({
                    name: c.name?.[0] ?? "Unknown",
                    phone: c.tel?.[0],
                }));
                setContacts([...contacts, ...mapped]);
            } catch (err) {
                console.error("Contact Picker error:", err);
            }
        }
    }

    const onSubmitContact = (e: FormEvent) => {
        e.preventDefault();
        setContacts([...contacts, formData]);
        setFormData({
            name: "",
            phone: "",
        })
    };

    return (
        <>
            {"contacts" in navigator ?
                <SecondaryButton sx={{width: "100%",
                    maxWidth: "500px",
                    margin: "0 auto",
                    display: "block"}} onClick={handleGetContacts}>Show contacts</SecondaryButton>
                : <><Grid component="form"
                          onSubmit={onSubmitContact}
                          spacing={0}
                          sx={{
                              maxWidth: "500px",
                              margin: isMobile ? "0 auto 20px" : "20px auto"
                          }}
                >
                    <Grid item xs={isMobile ? true : 10} sx={{width: "100%"}}>
                        <InputLabel
                            htmlFor={"name"}
                            sx={{
                                color: theme.palette.black.main,
                            }}>
                            Name
                        </InputLabel>
                        <Input
                            id="name"
                            name="name"
                            type="text"
                            placeholder={"Type name of your friend"}
                            value={formData.name}
                            error={false}
                            onChange={(event) => setFormData({
                                ...formData, name: event.target.value
                            })}/>
                    </Grid>
                    <Grid item xs={isMobile ? true : 10} sx={{width: "100%"}}>
                        <InputLabel
                            htmlFor={"phone"}
                            sx={{
                                color: theme.palette.black.main,
                            }}>
                            Phone
                        </InputLabel>
                        <Input
                            id="phone"
                            name="phone"
                            type="text"
                            placeholder={"Type phone number of your friend"}
                            value={formData.phone}
                            error={false}
                            onChange={(event) => setFormData({
                                ...formData, phone: event.target.value
                            })}/>
                    </Grid>
                    <Grid item xs={isMobile ? true : 4} sx={{
                        margin: "0 auto",
                        width: "100%",
                    }}>
                        <SecondaryButton
                            variant="contained"
                            type="submit"
                            size={"large"}
                            sx={{
                                width: "100%",
                                marginTop: 2.75,
                            }}
                        >
                            Add to contacts
                        </SecondaryButton>
                    </Grid>
                </Grid><Divider sx={{
                    maxWidth: "500px",
                    margin: isMobile ? "0 auto 20px" : "20px auto"
                }}/></>
            }

            <List sx={{
                width: "100%",
                display: "flex",
                flexDirection: "column",
                maxWidth: "500px",
                margin: "0 auto",
            }}>
                {
                    contacts.map((c, idx) => (
                            <ListItem key={idx} sx={{
                                display: "flex",
                                alignItems: "center",
                                justifyContent: "space-between",
                            }}>
                                <strong>{c?.name}</strong>
                                <Typography component="span"
                                            variant={"body1"}>{c?.phone}</Typography>
                            </ListItem>

                        )
                    )
                }
            </List>
            <PrimaryButton sx={{
                width: "100%",
                maxWidth: "500px",
                margin: "0 auto",
                display: "block",
            }}
                           onClick={() => {
                               setContacts([]);
                               snackbar.showSnackBar(`Sms sending to be implemented`, "info");
                           }
                           }>
                Invite to Niffler</PrimaryButton>
        </>)

};