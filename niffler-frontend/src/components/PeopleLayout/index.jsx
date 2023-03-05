import {Footer} from "../Footer";
import {Header} from "../Header";
import {PeopleTable} from "../PeopleTable";

export const PeopleLayout = () => {

    return (
        <div className={"main-container"}>
            <Header/>
            <main className={"main"}>
                <div className={"people-content"}>
                    <section className="main-content__section">
                        <PeopleTable/>
                    </section>
                </div>
            </main>
            <Footer/>
        </div>
    );
}
