import {FriendsTable} from "../FriendsTable";
import {PageContainer} from "../PageContainer";

export const FriendsLayout = () => {
    return (
        <PageContainer>
            <div className={"people-content"}>
                <section className="main-content__section">
                    <FriendsTable/>
                </section>
            </div>
        </PageContainer>
    );
}
