import {useContext} from "react";
import {TableSelectionContext} from "../../contexts/TableSelectionContext";
import {BatchCheckbox} from "../Checkbox/BatchCheckbox";
import {SpendingRow} from "../SpendingRow";

export const SpendingTable = ({spendings, isGraphOutdated, setIsGraphOutdated, categories}) => {

    const {selectedIds, setSelectedIds, allIds} = useContext(TableSelectionContext);

    const handleSingleClick = (id) => {
        selectedIds?.includes(id)
            ? setSelectedIds(selectedIds.filter(v => v !== id))
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
                    <th><BatchCheckbox handleBulkClick={handleBulkClick}
                                       selected={selectedIds?.length === allIds?.length}/></th>
                    <th>Date</th>
                    <th>Amount</th>
                    <th>Currency</th>
                    <th>Category</th>
                    <th>Description</th>
                    <th></th>
                </tr>
                </thead>

                <tbody>
                {spendings?.map((spending) => (
                    <SpendingRow key={spending.id}
                                 spending={spending}
                                 handleCheckboxClick={handleSingleClick}
                                 isSelected={selectedIds.includes(spending?.id)}
                                 isGraphOutdated={isGraphOutdated}
                                 setIsGraphOutdated={setIsGraphOutdated}
                                 categories={categories}
                    />
                ))}
                </tbody>
            </table>
            {spendings?.length === 0 && (<div style={{margin: 20}}>No spendings provided yet!</div>)}
        </>
    )
}
