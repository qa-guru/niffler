export const Checkbox = ({id, handleSingleClick, selected}) => {
    return (
        <input type={"checkbox"}
               onChange={() => handleSingleClick(id)}
               value={id}
               checked={selected}
        />
    );
}
