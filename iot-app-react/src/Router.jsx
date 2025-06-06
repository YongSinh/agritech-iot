import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import App from "./App";
import {
  Dashboard,
  Team,
  Invoices,
  Contacts,
  Form,
  Bar,
  Line,
  Pie,
  FAQ,
  Geography,
  Calendar,
  Stream,
  Device,
  RepeatSchedule,
  OnetimeSchedule,
  IntervalSchedule,
  Trigger,
  Control
} from "./scenes";

const AppRouter = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<App />}>
          <Route path="/" element={<Dashboard />} />
          <Route path="/team" element={<Team />} />
          <Route path="/contacts" element={<Contacts />} />
          <Route path="/invoices" element={<Invoices />} />
          <Route path="/form" element={<Form />} />
          <Route path="/calendar" element={<Calendar />} />
          <Route path="/bar" element={<Bar />} />
          <Route path="/pie" element={<Pie />} />
          <Route path="/stream" element={<Stream />} />
          <Route path="/line" element={<Line />} />
          <Route path="/faq" element={<FAQ />} />
          <Route path="/geography" element={<Geography />} />
          <Route path="/device" element={<Device />} />
          <Route path="/repeat-schedule" element={<RepeatSchedule />} />
          <Route path="/interval-schedule" element={<IntervalSchedule />} />
          <Route path="/onetime-schedule" element={<OnetimeSchedule />} />
          <Route path="/trigger" element={<Trigger />} />
          <Route path="/control" element={<Control />} />
        </Route>
      </Routes>
    </Router>
  );
};

export default AppRouter;
