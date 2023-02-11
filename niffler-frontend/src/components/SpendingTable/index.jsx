import dayjs from "dayjs";
import {useContext} from "react";
import {TableSelectionContext} from "../../contexts/TableSelectionContext";
import {Checkbox} from "../Checkbox";
import {BatchCheckbox} from "../Checkbox/BatchCheckbox";

export const SpendingTable = ({spendings}) => {

    const {selectedIds, setSelectedIds, allIds} = useContext(TableSelectionContext);

    const handleSingleClick = (id) => {
        selectedIds?.includes(id)
            ? setSelectedIds(selectedIds.filter(v => v!== id))
            : setSelectedIds([...selectedIds, id]);
    }

    const handleBulkClick = () => {
        selectedIds?.length === allIds?.length ? setSelectedIds([]) : setSelectedIds(allIds);
    }

    return (
        <>
            <table className="table spendings-table">
                <thead>
                <tr>
                    <th><BatchCheckbox handleBulkClick={handleBulkClick} selected={selectedIds?.length === allIds?.length}/></th>
                    <th>Date</th>
                    <th>Amount</th>
                    <th>Currency</th>
                    <th>Category</th>
                    <th>Description</th>
                </tr>
                </thead>

                <tbody>
                {spendings?.map((spending) => (
                    <tr key={spending?.id}>
                        <td><Checkbox id={spending?.id} handleSingleClick={handleSingleClick} selected={selectedIds.includes(spending?.id)}/></td>
                        <td>{dayjs(spending?.spendDate).format('DD MMM YY')}</td>
                        <td>{spending?.amount}</td>
                        <td>{spending?.currency}</td>
                        <td>{spending?.category}</td>
                        <td>{spending?.description}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            {spendings?.length === 0 && (<div style={{margin: 20}}>No spendings provided yet!</div>)}
        </>
    )
}
