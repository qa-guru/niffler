import {Footer} from "../Footer";
import {Header} from "../Header";
import {Popup} from "../Popup";
export const PageContainer = ({children}) => {
    return (
        <div className={"main-container"}>
            <Popup/>
            <Header />
            <main className={"main"}>
                {children}
            </main>
            <Footer/>
        </div>
    );
}
