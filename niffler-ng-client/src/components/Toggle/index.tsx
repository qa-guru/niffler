import {ToggleButton, ToggleButtonGroup} from "@mui/material"
import {FC} from "react";

interface ToggleInterface {
    withMyFriends: boolean,
    setWithMyFriends: (withMyFriends: boolean) => void;
}

export const Toggle: FC<ToggleInterface> = ({withMyFriends, setWithMyFriends}) => {

    const handleChange = (
        _event: React.MouseEvent<HTMLElement>,
        newFilter: "my" | "friends",
    ) => {
        setWithMyFriends(newFilter === "friends");
    };

    return (
        <ToggleButtonGroup
            color="primary"
            size="small"
            value={withMyFriends ? "friends" : "my"}
            exclusive
            onChange={handleChange}
            aria-label="Travels filter"
        >
            <ToggleButton value="my">Only my travels</ToggleButton>
            <ToggleButton value="friends">With friends</ToggleButton>
        </ToggleButtonGroup>
    )
}
