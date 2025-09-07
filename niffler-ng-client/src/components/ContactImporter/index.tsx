import {FC, useState} from "react";
import {SecondaryButton} from "../Button";

interface Contact {
    name: string,
    phone?: string,
    email?: string,
}

export const ContactImporter: FC = ({}) => {
    const [contacts, setContacts] = useState<Contact[]>([]);

    const handleGetContacts = async () => {
        if ("contacts" in navigator && "select" in (navigator as any).contacts) {
            try {
                const props = ["name", "email", "tel"];
                const opts = {multiple: true};
                const selected = (await navigator as any).contacts.select(props, opts);

                const mapped: Contact[] = selected.map((c: any) => ({
                    name: c.name?.[0] ?? "Unknown",
                    phone: c.tel?.[0],
                    email: c.email?.[0],
                }));

                setContacts(mapped);
            } catch (err) {
                console.error("Contact Picker error:", err);
            }
        }
    }
    return (
        <>
            {"contacts" in navigator &&  <SecondaryButton sx={{marginBottom: 2}} onClick={handleGetContacts}>Show contacts</SecondaryButton>}
            <ul>
                {
                    contacts.map((c, idx) => (
                            <li key={idx}>
                                <strong>c.name</strong>
                                <span>{c.phone}</span>
                                <span>{c.email}</span>
                            </li>
                        )
                    )
                }
            </ul>
        </>)

};