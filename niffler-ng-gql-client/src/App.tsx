import "./App.css";
import {AppContent} from "./components/AppContent";
import {SnackBarProvider} from "./context/SnackBarContext";
import {DialogProvider} from "./context/DialogContext.tsx";


const App = () => {

    return (
        <SnackBarProvider>
            <DialogProvider>
                <AppContent/>
            </DialogProvider>
        </SnackBarProvider>

    )
}

export default App;
