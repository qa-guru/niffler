import {Footer} from "../Footer";
import {Header} from "../Header";
export const PageContainer = ({children}) => {
    return (
        <div className={"main-container"}>
            <Header />
            <main className={"main"}>
                {children}
            </main>
            <Footer/>
        </div>
    );
}
