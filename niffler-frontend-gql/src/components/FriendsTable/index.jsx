import {useQuery} from "@apollo/client";
import {useMemo} from "react";
import {QUERY_FRIENDS} from "../../api/graphql/queries";
import {AbstractTable} from "../AbstractTable";

export const FriendsTable = () => {

    const {data: friendsData, loading} = useQuery(QUERY_FRIENDS);

    const data = useMemo(() => {
        if (loading) {
            return [];
        }
        const result = [];
        if (friendsData?.user?.friends?.length > 0) {
            result.push(...friendsData?.user?.friends);
        }
        if (friendsData?.user?.invitations?.length > 0) {
            result.push(...friendsData?.user?.invitations);
        }
        return result;
    }, [loading, friendsData]);


    if (loading) {
        return (<div className="loader"></div>)
    }

    return (
        <>
            {
                (data.length > 0 ? (
                        <AbstractTable data={data}/>
                    ) :
                    (<div style={{margin: 20}}>There are no friends yet!</div>))
            }
        </>
    )
}
