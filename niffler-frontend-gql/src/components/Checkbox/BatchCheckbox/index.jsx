export const BatchCheckbox = ({handleBulkClick, selected}) => {
    return (
        <input type={"checkbox"} onChange={() => handleBulkClick()} checked={selected}/>
    );
}
