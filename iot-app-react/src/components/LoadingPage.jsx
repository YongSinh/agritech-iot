import * as fly from "../assets/Animation - 1750490164126.json";
import Lottie from "react-lottie";
import "animate.css";

const LoadingPage = () => {
  const defaultOptions = {
    loop: true,
    autoplay: true,
    animationData: fly.default,
    rendererSettings: {
      preserveAspectRatio: "xMidYMid slice",
    },
  };

  return (
      <div style={{
        height: '100%',
        margin: 0,
        padding: 0,
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        overflow: 'hidden'
      }}>
        <Lottie options={defaultOptions} height={400} width={400} />
      </div>
  );
};

export default LoadingPage;