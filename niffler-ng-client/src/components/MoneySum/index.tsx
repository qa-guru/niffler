import {FC, useEffect, useState} from "react";


interface MoneySumInterface {
    sum: string;
}

export const MoneySum: FC<MoneySumInterface> = ({sum}) => {
    const [visible, setVisible] = useState<boolean>(true);

    const SHAKE_THRESHOLD = 25;
    const SHAKE_COOLDOWN = 1500;
    const REQUIRED_SHAKE_COUNT = 3;

    let lastShakeTime = 0;
    let shakeCount = 0;

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

               let speed = (Math.abs(x + y + z - lastX - lastY - lastZ) / deltaTime) * 10000;

               lastX = x;
               lastY = y;
               lastZ = z;

               if(speed > SHAKE_THRESHOLD){
                   shakeCount++;
                   if(shakeCount>= REQUIRED_SHAKE_COUNT && currentTime - lastShakeTime > SHAKE_COOLDOWN) {
                       lastShakeTime = currentTime;
                       shakeCount = 0;
                       speed = 0;
                       console.log(speed);
                       setVisible((prev) => !prev);
                   }
               }


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
