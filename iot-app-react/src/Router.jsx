import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import App from "./App";
import {
  Dashboard,
  Calendar,
  Device,
  RepeatSchedule,
  OnetimeSchedule,
  IntervalSchedule,
  Trigger,
  Control, 
  Mqtt
} from "./scenes";

import LoadingPage from "./components/LoadingPage";

const AppRouter = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<App />}>
          <Route path="/" element={<Dashboard />} />
          <Route path="/calendar" element={<Calendar />} />
          <Route path="/device" element={<Device />} />
          <Route path="/repeat-schedule" element={<RepeatSchedule />} />
          <Route path="/interval-schedule" element={<IntervalSchedule />} />
          <Route path="/onetime-schedule" element={<OnetimeSchedule />} />
          <Route path="/trigger" element={<Trigger />} />
          <Route path="/control" element={<Control />} />
          <Route path="/mqtt-topic" element={<Mqtt/>} />
          <Route path="/loading" element={<LoadingPage />} />
        </Route>
      </Routes>
    </Router>
  );
};

export default AppRouter;
