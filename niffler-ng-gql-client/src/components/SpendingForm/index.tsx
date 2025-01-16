import {FC} from "react";
import {EditFormComponent} from "./EditFormComponent";
import {AddFormComponent} from "./AddFormComponent";


interface SpendingFormInterface {
    id?: string,
    isEdit: boolean,
}

export const SpendingForm: FC<SpendingFormInterface> = ({id, isEdit}) => {

    return (
        isEdit && id ?
            <EditFormComponent id={id}/>
            : <AddFormComponent/>
    )

}
