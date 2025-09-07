import {FC, useEffect, useState} from "react";


interface MoneySumInterface {
    sum: string;
}

export const MoneySum: FC<MoneySumInterface> = ({sum}) => {
    const [visible, setVisible] = useState<boolean>(true);

    const SHAKE_THRESHOLD = 15;

    useEffect(() => {
        let lastX = 0, lastY = 0, lastZ = 0;
        let lastUpdate = 0;

       const handleMotion = (event: DeviceMotionEvent) => {
           const acceleration = event.accelerationIncludingGravity;
           const currentTime = Date.now();

           if(acceleration && currentTime - lastUpdate > 100) {
               const deltaTime = currentTime - lastUpdate;
               lastUpdate = currentTime;

               const x = acceleration.x ?? 0;
               const y = acceleration.y ?? 0;
               const z = acceleration.z ?? 0;

               const speed = (Math.abs(x + y + z - lastX - lastY - lastZ) / deltaTime) * 10000;

               if(speed > SHAKE_THRESHOLD){
                   setVisible((prev) => !prev);
               }

               lastX = x;
               lastY = y;
               lastZ = z;
           }
       }

       if(window.DeviceMotionEvent) {
           window.addEventListener("devicemotion", handleMotion);
       }

       return () => {
         window.removeEventListener("devicemotion", handleMotion);
       }
    }, []);


    return (
        <span
            style={{
                transition: "opacity 0.3s",
                opacity: visible ? 1 : 0
            }}
        >
            {sum}
        </span>
    )
}
