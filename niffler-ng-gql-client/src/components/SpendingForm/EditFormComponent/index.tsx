import {FormComponent} from "../FormComponent";
import {useSpendQuery} from "../../../generated/graphql.tsx";
import {FC} from "react";

interface EditFormComponentInterface {
    id: string;
}

export const EditFormComponent: FC<EditFormComponentInterface> = ({id}) => {
    const {data} = useSpendQuery({
        variables: {
            id
        }
    });

    return (
        data?.spend ?
            <FormComponent
                initialData={data.spend}
                header="Edit spending"
                submitText="Save changes"
                isEdit={true}
                successMessage="Spending is edited successfully"
            />
            :
            <></>
    )
}