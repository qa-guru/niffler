import {useContext} from "react";
import {PopupContext} from "../../contexts/PopupContext";
import {AbstractTable, Controls} from "../AbstractTable";
import {Button} from "../Button";

export const Popup = () => {

    const {isOpen, setIsOpen, content, setContent} = useContext(PopupContext);

    const onSubmitFriend = ({username}) => {
        setContent(content.filter(f => f.username !== username));
    };

    return (
        isOpen ? (
            <div className="popup">
                <div className="popup__content">
                    {content?.length > 0 ? (
                        <AbstractTable data={content}
                                       controls={[Controls.SUBMIT_FRIEND, Controls.DECLINE_FRIEND]}
                                       onSubmit={onSubmitFriend}
                        />
                    ) : (
                        <div className="popup__no-data">No new invitations</div>
                    )}
                    <Button buttonText="Close" onClick={() => setIsOpen(false)}/>
                </div>
            </div>
        ) : null
    );
}
