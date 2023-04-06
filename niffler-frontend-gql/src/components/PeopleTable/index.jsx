import {useQuery} from "@apollo/client";
import {QUERY_ALL_USERS} from "../../api/graphql/queries";
import {AbstractTable} from "../AbstractTable";

export const PeopleTable = ({}) => {

    const {data: allUsersData, loading} = useQuery(QUERY_ALL_USERS);

    if (loading) {
        return (<div className="loader"></div>)
    }

    return (
        <>
            {allUsersData?.users?.length > 0 ? (
                    <AbstractTable data={allUsersData.users}
                    />
                ) :
                (<div style={{margin: 20}}>There are no other users yet!</div>)
            }
        </>
    )
}
