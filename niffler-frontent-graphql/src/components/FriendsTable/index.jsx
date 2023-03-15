import {useQuery} from "@apollo/client";
import { QUERY_FRIENDS} from "../../api/graphql/queries";
import {AbstractTable} from "../AbstractTable";

export const FriendsTable = () => {

    const {data: friendsData, loading} = useQuery(QUERY_FRIENDS);

    if (loading) {
        return (<div className="loader"></div>)
    }

    return (
        <>
            {
                (friendsData?.user?.friends?.length > 0 ? (
                    <AbstractTable data={[friendsData.user.friends[0]]}/>
                    ) :
                (<div style={{margin: 20}}>There are no friends yet!</div>))
            }
        </>
    )
}
