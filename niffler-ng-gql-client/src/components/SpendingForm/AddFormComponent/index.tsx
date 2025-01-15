import {FormComponent} from "../FormComponent";
import {SPENDING_INITIAL_STATE} from "../formValidate.ts";


export const AddFormComponent = () => {

    return (
        <FormComponent
            initialData={SPENDING_INITIAL_STATE}
            header="Add new spending"
            successMessage="New spending is successfully created"
            submitText="Add"
            isEdit={false}
        />
    )
}