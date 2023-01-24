import dayjs from "dayjs";

export const SpendingTable = ({spendings}) => {

    return (
        <>
            <table className="table spendings-table">
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Amount</th>
                    <th>Currency</th>
                    <th>Category</th>
                    <th>Description</th>
                </tr>
                </thead>

                <tbody>
                {spendings?.map((spending, index) => (
                    <tr key={`spending-${index}`}>
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
