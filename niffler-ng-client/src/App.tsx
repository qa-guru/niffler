import "./App.css";
import {AppContent} from "./components/AppContent";
import {SnackBarProvider} from "./context/SnackBarContext";
import {DialogProvider} from "./context/DialogContext.tsx";


const App = () => {

    if("serviceWorker" in navigator){
        window.addEventListener("load", () => {
            navigator.serviceWorker
                .register("/sw.js")
                .then((reg) => console.log("SW registered", reg))
                .catch((err) => console.error("SW registration failed", err));
        });
    }

    return (
        <SnackBarProvider>
            <DialogProvider>
                <AppContent/>
            </DialogProvider>
        </SnackBarProvider>

    )
}

export default App;
