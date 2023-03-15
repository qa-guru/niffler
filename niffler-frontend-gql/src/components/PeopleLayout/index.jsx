import {PageContainer} from "../PageContainer";
import {PeopleTable} from "../PeopleTable";

export const PeopleLayout = () => {

    return (
        <PageContainer>
            <div className={"people-content"}>
                <section className="main-content__section">
                    <PeopleTable/>
                </section>
            </div>
        </PageContainer>
    );
}
