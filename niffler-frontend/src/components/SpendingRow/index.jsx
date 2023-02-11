import dayjs from "dayjs";
import {useState} from "react";
import {patchData} from "../../api/api";
import {showError, showSuccess} from "../../toaster/toaster";
import {ButtonIcon} from "../ButtonIcon";
import {Checkbox} from "../Checkbox";
import {EditableValue} from "../EditableValue";

export const SpendingRow = ({spending, isSelected, handleCheckboxClick, isGraphOutdated, setIsGraphOutdated}) => {

    const [editableSpending, setEditableSpending] = useState(spending);
    const [isEdit, setIsEdit] = useState(false);

    const handleDataSave = (evt) => {
        evt.preventDefault();
        patchData({
            path: "/editSpend",
            data: editableSpending,
            onSuccess: () => {
                showSuccess("Spending updated!");
                setIsGraphOutdated(!isGraphOutdated);
            },
            onFail: (err) => {
                showError("Spending was not updated!");
                console.log(err);
            },
        });
    };

    return (
        <tr>
                <td><Checkbox id={spending?.id} handleSingleClick={handleCheckboxClick} selected={isSelected}/></td>
                <td>{dayjs(spending?.spendDate).format('DD MMM YY')}</td>
                <td>
                    <EditableValue value={editableSpending?.amount}
                                   isEditState={isEdit}
                                   fieldName={"amount"}
                                   placeholder={null}
                                   onValueChange={(evt) => setEditableSpending({...editableSpending, amount: evt.target.value})}
                    />
                </td>
                <td>{spending?.currency}</td>
                <td>{spending?.category}</td>
                <td>
                    <EditableValue value={editableSpending?.description}
                                   isEditState={isEdit}
                                   fieldName={"description"}
                                   placeholder={null}
                                   onValueChange={(evt) => setEditableSpending({...editableSpending, description: evt.target.value})}
                    />
                </td>
                <td>
                    {isEdit ?
                        (
                            <div className={"spendings__button-group"}>
                                <ButtonIcon iconType={"submit"} onClick={(evt) => {
                                    handleDataSave(evt);
                                    setIsEdit(false);
                                }
                                }/>
                                <ButtonIcon iconType={"close"} onClick={() => {
                                    setEditableSpending(spending);
                                    setIsEdit(false)
                                }}/>
                            </div>
                        )
                        :
                        (<ButtonIcon iconType={"edit"} onClick={() => setIsEdit(true)}/>)}
                </td>
        </tr>

    );

};
